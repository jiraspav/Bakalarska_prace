/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package view.mistnosti;

import app.facade.reservationEditor.ReservationEditorFacade;
import dbEntity.RezervaceMistnosti;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import view.SessionHolder.SessionHolderMB;
import view.bundle.ResourceBundleOperator;
import view.facesMessenger.FacesMessengerUtil;
/**
 *
 * @author Pavel
 */
@Named("mojeRezervaceBean")
@SessionScoped
public class MojeRezervaceMB implements Serializable{

    private ArrayList<RezervaceMistnosti> items;
    
    @Inject private ReservationEditorFacade resFac;
    @Inject private FacesMessengerUtil messUtil;
    @Inject private ResourceBundleOperator bundle;
    @Inject private SessionHolderMB session;
    
    private RezervaceMistnosti selectedRow;
    
    /**
     * Creates a new instance of mojeRezervaceBean
     */
    public MojeRezervaceMB() {
    }
    
 
    
    
    /**
     * metoda pro rušení rezervací
     */
    public void deleteReservation(){
        
        if(selectedRow == null){
            messUtil.addFacesMsgError(bundle.getMsg("sysMsgNoResSellected"));
        }
        else{
            resFac.deleteReservation(selectedRow);
            setSelectedRow(null);
        }
    }
    
    
    
    
    //--------------------------------------------------------------------------
    //------------------------GETTERS,SETTERS-----------------------------------
    //--------------------------------------------------------------------------
    
    /**
     * @return the selectedRow
     */
    public RezervaceMistnosti getSelectedRow() {
        return selectedRow;
    }

    /**
     * @param selectedRow the selectedRow to set
     */
    public void setSelectedRow(RezervaceMistnosti selectedRow) {
        this.selectedRow = selectedRow;
    }
    /**
     * metoda pro zjištění všech rezervací přihlášeného uživatele
     * @return všechny rezervace přihlášeného uživatele
     */
    public ArrayList<RezervaceMistnosti> getItems(){
        return new ArrayList<RezervaceMistnosti>(resFac.getReservationsOfLoggedUser(session.getLoggedUzivatel()));
    }
    /**
     * @param items the items to set
     */
    public void setItems(ArrayList<RezervaceMistnosti> items) {
        this.items = items;
    }
}
