/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app.sessionHolder;

import app.baseDataOperators.UzivatelOperator;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import dbEntity.Uzivatel;
import entityFacade.UzivatelFacade;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.LocalBean;
import javax.ejb.Remove;
import javax.ejb.SessionContext;
import javax.ejb.Stateful;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 *
 * @author Pavel
 */

@Stateless
public class SessionHolderEJB {
    
    private @Inject UzivatelOperator uzivOper;
    
    
    
    public String getLoggedUzivatelLogin(){
        return getLoggedName();
    }
    public Uzivatel getLoggedUzivatel(){
         
        System.out.println("Named : "+getLoggedName());
        
        return uzivOper.getUzivatel(getLoggedName());
    }
    public String getPassword(){
        //System.out.println("Password : "+getLoggedPassword());
        return getLoggedPassword();
    }
    
    private String getLoggedPassword(){
        String pass = getLoggedUzivatel().getHeslo();
        return new String(Base64.decode(pass));
    }
    
    private String getLoggedName(){
        return getSessionContext().getCallerPrincipal().getName();
    }
    
    private SessionContext getSessionContext(){
        SessionContext sctxLookup = null;
        try {
            InitialContext ic = new InitialContext();
            sctxLookup = (SessionContext) ic.lookup("java:comp/EJBContext");
        } catch (NamingException ex) {
            Logger.getLogger(SessionHolderEJB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return sctxLookup;
    }
}
