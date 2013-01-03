/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package view.SessionHolder;

import app.DataCreator.DataCreator;
import app.baseDataOperators.UzivatelOperator;
import app.encrypt.EncryptUtil;
import app.qualifiers.Login;
import app.qualifiers.Logout;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import dbEntity.Uzivatel;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Remove;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Observes;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Pavel
 */

@Named(value = "sessionHolder")
@SessionScoped
public class SessionHolderMB implements Serializable {
    
    private Uzivatel loggedIn;
    private String password;
    
    private @Inject EncryptUtil crypt;
    private @Inject UzivatelOperator uzivOper;
    private @Inject DataCreator creator;
    
    
    /**
     * Metoda pro nastavení nové session
     * @param auth
     */
    public void saveNewSession(@Observes @Login String auth){
        
        loggedIn = uzivOper.getUzivatel(auth);
        
        password = crypt.decode(loggedIn.getHeslo());
        
    }
    
    
    
    /**
     * Metoda rušení session
     * @param temp
     */
    @Remove
    public void killSession(@Observes @Logout String temp){
        
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();      
        
    }
    
    private HttpServletRequest getHttpServletRequest(){
        return (HttpServletRequest) getExternalContext().getRequest();
    }
    private ExternalContext getExternalContext(){
        return FacesContext.getCurrentInstance().getExternalContext();
    }
    
    /**
     * 
     * @return
     */
    public boolean isLoggedUserAdmin(){
        return uzivOper.isAdmin(loggedIn);
    }
    
    /**
     * 
     * @return
     */
    public String getLoggedUzivatelName(){
        return uzivOper.getUzivatelName(loggedIn);
    }
    
    /**
     * 
     * @return
     */
    public String getLoggedUzivatelRole(){
        return uzivOper.getUzivatelRole(loggedIn);
    }
    
    /**
     * 
     * @return
     */
    public String getLoggedUzivatelLogin(){
        return loggedIn.getLogin();
    }
    
    /**
     * 
     * @return
     */
    public Uzivatel getLoggedUzivatel(){
        return loggedIn;
    }
    
    /**
     * 
     * @return
     */
    public String getPassword(){
        return password;
    }
}
