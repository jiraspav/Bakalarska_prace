/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package view.auth;

import app.encrypt.EncryptUtil;
import app.facade.login.LoginFacade;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import view.SessionHolder.SessionHolderMB;
import view.bundle.ResourceBundleOperator;
import view.facesMessenger.FacesMessengerUtil;

/**
 *
 * @author Pavel
 */
@Named(value = "LoginVerifier")
@RequestScoped
public class LoginVerifier implements Serializable{
    
    private @Inject FacesMessengerUtil faceUtil;
    private @Inject ResourceBundleOperator bundle;
    //private @Inject LoginServiceEJB loginService;
    private @Inject LoginFacade loginFac;
    private @Inject SessionHolderMB session;
    private @Inject NavigationBean naviBean;
    private @Inject EncryptUtil encrypt;
    
    
    private String login,password;
    private String regLogin,regPassword;
    
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
        
        session.saveNewSession(login, password);
        
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
        
        session.killSession();
        
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) ec.getRequest();
        try {
            
            request.logout();
            ec.invalidateSession();
            
        } catch (ServletException ex) {
            Logger.getLogger(LoginVerifier.class.getName()).log(Level.SEVERE, null, ex);
            
        }
        return naviBean.toIndex();
        
    }
   
    /**
     * Metoda zajišťující registraci a kontrolu nového uživatele, zda je registrován na FELu
     * a zda má přístup do KOSu
     */
    public void register(){
            
            String response = loginFac.register(regLogin,regPassword);
        
            if(response.equalsIgnoreCase("completed")){
                
                faceUtil.addFacesMsgInfo(bundle.getMsg("sysMsgRegSucc"));
                
            }
            else if(response.equalsIgnoreCase("alreadyRegistered")){
                
                faceUtil.addFacesMsgError(bundle.getMsg("sysMsgAlreadyReg"));
                
            }
            else{
                
                faceUtil.addFacesMsgError(bundle.getMsg("sysMsgWrongPass"));
                
            }
    }
    
    //--------------------------NUTNO ZMENIT-----------------------
    
    public boolean isAdmin(){
        return true;
    }
    
    
    
    //--------------------------------------------------------------------------
    //-------------------------GETTERS, SETTERS---------------------------------
    //--------------------------------------------------------------------------
    
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
