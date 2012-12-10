/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package view.SessionHolder;

import app.DataCreator.DataCreator;
import app.baseDataOperators.UzivatelOperator;
import dbEntity.Uzivatel;
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
    private @Inject DataCreator creator;
    
    
    public void saveNewSession(String login, String pass){
        
        
        loggedIn = uzivOper.getUzivatel(login);
        password = pass;
        
        System.out.println(""+loggedIn.getLogin()+" : "+password);
        
       /* SessionHolderEJB session = null;
        try {
            InitialContext ic = new InitialContext();
            session = (SessionHolderEJB) ic.lookup("SessionHolderEJB");
            
        } catch (NamingException ex) {
            Logger.getLogger(SessionHolderMB.class.getName()).log(Level.SEVERE, null, ex);
        }*/
             
        
        //session.saveNewSession(loggedIn, pass);
        //creator.initSession(login, password);
    }
    
    
    
    @Remove
    public void killSession(){
        
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();      
        password = null;
    }
    
    
    public boolean isLoggedUserAdmin(){
        return uzivOper.isAdmin(loggedIn);
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
