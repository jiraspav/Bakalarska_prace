/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package view.auth;

import app.facade.login.impl.LoginServiceEJB;
import java.io.Serializable;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.jms.Session;
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
    private @Inject LoginServiceEJB loginService;
    private @Inject SessionHolderMB session;
    
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
        session.saveNewSession(password);
        return loginService.login(login,password);
    }
    
    /**
     * metoda zajišťující odhlášení
     * Nastavuje login, password na null a aktuální session znehodnotí
     * @return vrací string pro navigaci
     */
    public String logout(){
        return loginService.logout();
    }
   
    /**
     * Metoda zajišťující registraci a kontrolu nového uživatele, zda je registrován na FELu
     * a zda má přístup do KOSu
     */
    public void register(){
            
            String response = loginService.register(regLogin,regPassword);
        
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
    
    //--------------------------------------------------------------------------
    //-------------------------GETTERS, SETTERS---------------------------------
    //--------------------------------------------------------------------------
    
    /**
     * @param login the login to set
     */
    public void setLogin(String login) {
        
        this.login = login;
    }


    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @param regLogin the regLogin to set
     */
    public void setRegLogin(String regLogin) {
        this.regLogin = regLogin;
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
