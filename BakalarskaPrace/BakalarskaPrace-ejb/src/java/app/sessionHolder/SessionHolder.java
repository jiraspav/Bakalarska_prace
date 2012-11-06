/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app.sessionHolder;

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
public class SessionHolder implements Serializable {
    
    private Uzivatel loggedIn;
    private String password;
    
    private @Inject UzivatelFacade uzivFac;
    
    public void saveNewSession(String pass){
        FacesContext ctx = FacesContext.getCurrentInstance();
        loggedIn = (Uzivatel) uzivFac.getUserByLogin(ctx.getExternalContext().getUserPrincipal().getName());
        password = pass;
    }
    
    @Remove
    public void killSession(){
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();  
        password = null;
    }
    
    public Uzivatel getLoggedUzivatel(){
        return loggedIn;
    }
    public String getPassword(){
        return password;
    }
}