/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package view.auth;

import XMLparser.ParserController;
import app.XMLparser.kosapiUrls;
import app.baseDataOperators.KosApiOperator;
import app.baseDataOperators.UzivatelOperator;
import dbEntity.GroupTable;
import dbEntity.Uzivatel;
import app.encrypt.EncryptUtil;
import app.facesMessenger.FacesMessengerUtil;
import entityFacade.DenVTydnuFacade;
import entityFacade.GroupTableFacade;
import entityFacade.UzivatelFacade;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.net.ssl.HttpsURLConnection;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import view.bundle.ResourceBundleOperator;

/**
 *
 * @author Pavel
 */
@Named(value = "LoginVerifier")
@SessionScoped
public class LoginVerifier implements Serializable{
    
    private @Inject NavigationBean naviBean;
    private @Inject ParserController parsCon;
    private @Inject GroupTableFacade groupFac;
    private @Inject DenVTydnuFacade denFac;
    private @Inject EncryptUtil encrypt;
    private @Inject UzivatelOperator uzivOperator;
    private @Inject UzivatelFacade uzivFac;
    private @Inject FacesMessengerUtil faceUtil;
    private @Inject ResourceBundleOperator bundle;
    private @Inject KosApiOperator kosOper;
    
    private String login,password,role;
    private String regLogin,regPassword;
    private String name;
    
    /**
     * 
     */
    public LoginVerifier() {
    }

    /**
     * metoda zajišťující autentizaci proti aplikačnímu serveru
     * @return String pomocí něhož se rozhodne na kterou stránku se má pokračovat
     */
    public String login(){
        
        FacesContext ctx = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) ctx.getExternalContext().getRequest();
        
        if(request.getUserPrincipal() == null){
            try {
                request.login(login, encrypt.SHA256(password));

            } catch (ServletException ex) {

                return naviBean.validationFail();
            }
        }
        return naviBean.success();
    }
    /**
     * metoda zajišťující odhlášení
     * Nastavuje login, password na null a aktuální session znehodnotí
     * @return vrací string pro navigaci
     */
    public String logout(){
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) ec.getRequest();
        try {
            
            request.logout();
            request.getSession().invalidate();
            setLogin(null);
            setPassword(null);
            
        } catch (ServletException ex) {
            Logger.getLogger(LoginVerifier.class.getName()).log(Level.SEVERE, null, ex);
            
        }
        return naviBean.toIndex();
    }
   
    /**
     * metoda zjišťující správnost loginu a hesla co se týče jejich registrace na FELu
     * Za validní se považují pouze ty osoby, které mají přístup do KOSu, tedy pro externisty
     * a navštěvy vrací false
     * 
     * @return true - pokud je uživatel registrován na FELu
     *          false - pokud není
     */
    public boolean isValid(){
        int serverResponse = 0;
        try {
            kosOper.createNewConnection(kosapiUrls.validation, regLogin, regPassword);
            
            serverResponse = kosOper.getConnection().getResponseCode();
            
            kosOper.closeConnection();
        } catch (IOException ex) {
            Logger.getLogger(LoginVerifier.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(serverResponse == 200){
            return true;
        }
        else{
            return false;
        }
    }
    
    /**
     * Metoda zajišťující registraci a kontrolu nového uživatele, zda je registrován na FELu
     * a zda má přístup do KOSu
     */
    public void register(){
        Uzivatel uziv = uzivFac.getUserByLogin(regLogin);
        
            if(uziv == null && isValid()){
                ArrayList<String> atributy = (ArrayList<String>) parsCon.getAtributes(regLogin,regLogin,regPassword);
                
                uzivOperator.createUzivatel(Long.parseLong(atributy.get(1)), regLogin, encrypt.SHA256(regPassword), atributy.get(0));

                GroupTable groupTab;
                
                groupTab = new GroupTable(regLogin,atributy.get(2));
                groupTab.setLogin(regLogin);
                
                
                groupFac.create(groupTab);
               
                faceUtil.addFacesMsgInfo(bundle.getMsg("sysMsgRegSucc"));
            }
            else if(uziv != null){
                faceUtil.addFacesMsgError(bundle.getMsg("sysMsgAlreadyReg"));
            }
            else{
                faceUtil.addFacesMsgError(bundle.getMsg("sysMsgWrongPass"));
            }
    }
    
    
    
    
    
    /**
     * 
     * @return zda je admin či ne
     */
    public boolean isAdmin(){
        if(getRole().equals("admin"))
            return true;
        return false;
    }
    
    /**
     * @return the login
     */
    public String getLogin() {
        
        return login;
    }

    /**
     * @param login the login to set
     */
    public void setLogin(String login) {
        
        this.login = login;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the role
     */
    public String getRole() {
        Uzivatel uziv;
        if(role == null){
            uziv = uzivFac.getUserByLogin(login);
            role = groupFac.getGroup(uziv);
            //role = uziv.getIDrole().getNazev();
        }
        return role;
    }
    /**
     * metoda zjišťující roli uzivatele
     * @param uziv uzivatel, pro kterého hledáme roli
     * @return roli uživatele
     * @see GroupTable
     */
    public String getRole(Uzivatel uziv) {
        
        return groupFac.getGroup(uziv);
    }

    /**
     * @param role the role to set
     */
    public void setRole(String role) {
        this.role = role;
    }

    /**
     * @return the name
     */
    public String getName(){
        if(name == null){
            name = uzivFac.getUserByLogin(login).getJmeno();
        }
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
    

    /**
     * @return the regLogin
     */
    public String getRegLogin() {
        return regLogin;
    }

    /**
     * @param regLogin the regLogin to set
     */
    public void setRegLogin(String regLogin) {
        this.regLogin = regLogin;
    }

    /**
     * @return the regPassword
     */
    public String getRegPassword() {
        return regPassword;
    }

    /**
     * @param regPassword the regPassword to set
     */
    public void setRegPassword(String regPassword) {
        this.regPassword = regPassword;
    }
    /**
     * metoda zjišťující prioritu zadané role
     * defaultní hodnoty jsou: student,ucitel,guest,admin
     * pro jiné hodnoty vrací 0
     * @param role role pro kterou hledáme prioritu
     * @return prioritu zadané role
     */
    public int getRolePriority(String role){
        if(role.equals("admin"))
            return 3;
        else if(role.equals("guest")|| role.equals("ucitel"))
            return 2;
        else if(role.equals("student"))
            return 1;
        else
            return 0;
    }
}
