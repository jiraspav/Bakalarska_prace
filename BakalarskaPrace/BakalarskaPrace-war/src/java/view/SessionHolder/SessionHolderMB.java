/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package view.SessionHolder;

import app.baseDataOperators.UzivatelOperator;
import app.sessionHolder.SessionHolderEJB;
import dbEntity.Uzivatel;
import entityFacade.UzivatelFacade;
import java.io.Serializable;
import javax.ejb.Remove;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Pavel
 */

@Named(value = "sessionHolder")
@SessionScoped
public class SessionHolderMB implements Serializable {
    
    private Uzivatel loggedIn;
    private String password;
    
    
    private @Inject UzivatelOperator uzivOper;
    private @Inject SessionHolderEJB session;
    
    
    
    public void saveNewSession(String login, String pass){
        session.saveNewSession(loggedIn, pass);
        
        loggedIn = (Uzivatel) uzivOper.getUzivatel(login);
        password = pass;
        
    }
    
    
    
    @Remove
    public void killSession(){
        session.killSession();
        
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();      
        password = null;
    }
    
    public String getLoggedUzivatelName(){
        return uzivOper.getUzivatelName(loggedIn);
    }
    
    public String getLoggedUzivatelRole(){
        return uzivOper.getUzivatelRole(loggedIn);
    }
    
    public String getLoggedUzivatelLogin(){
        return loggedIn.getLogin();
    }
    
    public Uzivatel getLoggedUzivatel(){
        return loggedIn;
    }
    
    public String getPassword(){
        return password;
    }
}
