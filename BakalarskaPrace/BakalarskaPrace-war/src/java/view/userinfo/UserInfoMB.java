/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package view.userinfo;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import view.SessionHolder.SessionHolderMB;

/**
 *
 * @author Pavel
 */
@Named(value="UserInfoMB")
@RequestScoped
public class UserInfoMB {
    
    private @Inject SessionHolderMB session;
    
    private String name;
    

    //--------------------------------------------------------------------------
    //-------------------------GETTERS, SETTERS---------------------------------
    //--------------------------------------------------------------------------
    
  

    /**
     * @return the name
     */
    public String getName() {
        return session.getLoggedUzivatelName();
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

}
