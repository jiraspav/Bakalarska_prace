/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package view.facesMessenger;

import javax.ejb.Stateless;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 *
 * @author Pavel
 */
@Stateless
public class FacesMessengerUtil {
    
    public void addFacesMsgInfo(String message){
        FacesMessage fMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "" ,message) ;
        addMessage(fMessage);
    }
    public void addFacesMsgError(String message){
        FacesMessage fMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "" ,message) ;
        addMessage(fMessage);
    }
    
    private void addMessage(FacesMessage message) {
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
}
