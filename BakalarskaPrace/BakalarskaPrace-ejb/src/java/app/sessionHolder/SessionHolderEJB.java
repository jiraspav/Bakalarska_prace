/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app.sessionHolder;

import dbEntity.Uzivatel;
import entityFacade.UzivatelFacade;
import java.io.Serializable;
import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Pavel
 */

@Stateful
public class SessionHolderEJB implements Serializable {
    
    private Uzivatel loggedIn;
    private String password;
    
    private @Inject UzivatelFacade uzivFac;
    
    public void saveNewSession(Uzivatel uziv, String pass){
        loggedIn = uziv;
        password = pass;
    }
    
    @Remove
    public void killSession(){
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();  
        password = null;
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
