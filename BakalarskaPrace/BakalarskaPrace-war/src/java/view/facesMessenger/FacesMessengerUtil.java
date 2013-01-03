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
    
    /**
     * Metoda pro přidání FacesMessage zprávy závažnost INFO
     * @param message zpráva, která se zobrazí
     */
    public void addFacesMsgInfo(String message){
        FacesMessage fMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "" ,message) ;
        addMessage(fMessage);
    }
    /**
     * Metoda pro přidání FacesMessage zprávy závažnost ERROR
     * @param message zpráva, která se zobrazí
     */
    public void addFacesMsgError(String message){
        FacesMessage fMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "" ,message) ;
        addMessage(fMessage);
    }
    /**
     * Metoda pro přidání FacesMessage zprávy závažnost FATAL
     * @param message zpráva, která se zobrazí
     */
    public void addFacesMsgFatal(String message){
        FacesMessage fMessage = new FacesMessage(FacesMessage.SEVERITY_FATAL, "" ,message) ;
        addMessage(fMessage);
    }
    
    private void addMessage(FacesMessage message) {
        FacesContext.getCurrentInstance().addMessage("sysMsg", message);
    }
}
