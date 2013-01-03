/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package view.auth;

import app.qualifiers.Logout;
import app.qualifiers.Login;
import app.encrypt.EncryptUtil;
import app.facade.register.RegisterFacade;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Event;
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
    private @Inject RegisterFacade registerFac;
    private @Inject @Login Event<String> loginEvent;
    private @Inject @Logout Event<String> logoutEvent;
    private @Inject SessionHolderMB session;
    private @Inject NavigationBean naviBean;
    private @Inject EncryptUtil crypt;
    
    
    private String login,password;
    private String regLogin,regPassword;
    
    /**
     * 
     */
    public LoginVerifier() {
    }

    
    /**
     * Metoda pro přesměrování v případě, že je nějaký uživatel na prohlížeči přihlášen
     */
    public void initRedirect(){
    
        if(getExternalContext().getUserPrincipal() != null){
            try {
                    getExternalContext().redirect("../BakalarskaPrace-war/welcome.faces");
            } catch (IOException ex) {
                Logger.getLogger(LoginVerifier.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
    
    
    /**
     * metoda zajišťující autentizaci proti aplikačnímu serveru
     * @return String pomocí něhož se rozhodne na kterou stránku se má pokračovat
     */
    public String login(){
        
        
        if(getExternalContext().getUserPrincipal() == null){
            try {
                    getHttpServletRequest().login(login, crypt.encode(password));
                    loginEvent.fire(getExternalContext().getUserPrincipal().getName());
                
            } catch (ServletException ex) {
                
                try {
                    getHttpServletRequest().getSession(false).invalidate();
                } catch (NullPointerException nulEx) {
                    return null;
                }
                
                return naviBean.validationFail();
            }
            
            return naviBean.success();
        }
        return naviBean.success();
        
            
        
        
    }
    
    /**
     * metoda zajišťující odhlášení
     * Nastavuje login, password na null a aktuální session znehodnotí
     * @return vrací string pro navigaci
     */
    public String logout(){
        
        try {
            
            getHttpServletRequest().logout();
            logoutEvent.fire(session.getLoggedUzivatelLogin());
            
            
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
            
            String response = registerFac.register(regLogin,regPassword);
        
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
    
    
    private HttpServletRequest getHttpServletRequest(){
        return (HttpServletRequest) getExternalContext().getRequest();
    }
    private ExternalContext getExternalContext(){
        return FacesContext.getCurrentInstance().getExternalContext();
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
}
