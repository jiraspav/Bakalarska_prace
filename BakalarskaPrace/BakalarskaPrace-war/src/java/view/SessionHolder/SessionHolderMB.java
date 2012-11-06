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
    
    
    
    public void saveNewSession(String pass){
        FacesContext ctx = FacesContext.getCurrentInstance();
        loggedIn = (Uzivatel) uzivOper.getUzivatel(ctx.getExternalContext().getUserPrincipal().getName());
        password = pass;
    }
    
    @Remove
    public void killSession(){
        session.killSession();
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
