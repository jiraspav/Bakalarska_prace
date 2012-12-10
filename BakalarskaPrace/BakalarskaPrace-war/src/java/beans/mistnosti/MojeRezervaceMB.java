/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans.mistnosti;

import app.facade.reservationEditor.ReservationEditorFacade;
import dbEntity.RezervaceMistnosti;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.faces.model.DataModel;
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
    
    @Inject ReservationEditorFacade resFac;
    @Inject FacesMessengerUtil messUtil;
    @Inject ResourceBundleOperator bundle;
    
    private RezervaceMistnosti selectedRow;
    private Date editDateOfReservation,editDateFrom,editDateTo;
    
    /**
     * Creates a new instance of mojeRezervaceBean
     */
    public MojeRezervaceMB() {
    }
    


    /**
     * metoda pro úpravu rezervací
     */
    public void editReservation(){
        
        if(selectedRow == null){
            messUtil.addFacesMsgError(bundle.getMsg("sysMsgNoResSellected"));
        }
        
        else {
            if(!editDateFrom.before(editDateTo)) {
                messUtil.addFacesMsgError(bundle.getMsg("sysMsgTimeError"));
            }
            else{
                
                String response = resFac.isReservationOK(selectedRow, editDateOfReservation, editDateFrom, editDateTo);
            
                if(response.equalsIgnoreCase("ok")){

                    try {

                        resFac.editReservation(selectedRow, editDateOfReservation, editDateFrom, editDateTo);

                        messUtil.addFacesMsgInfo(bundle.getMsg("sysMsgSuccReserved"));

                    } catch (Exception e) {
                        messUtil.addFacesMsgFatal(bundle.getMsg("sysMsgDBunreachable"));
                    }

                }
                else if(response.equalsIgnoreCase("alreadyscheduled")){
                    messUtil.addFacesMsgError(bundle.getMsg("sysMsgAlreadyScheduled"));
                }
                else{
                    messUtil.addFacesMsgError(bundle.getMsg("sysMsgAlreadyReserved"));
                }
            }
        }   
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
        if(selectedRow != null){
            setEditDateOfReservation(selectedRow.getDatumRezervace());
            setEditDateFrom(selectedRow.getOd());
            setEditDateTo(selectedRow.getDo1());
        }
    }
    /**
     * metoda pro zjištění všech rezervací přihlášeného uživatele
     * @return všechny rezervace přihlášeného uživatele
     */
    public ArrayList<RezervaceMistnosti> getItems(){
        return new ArrayList<RezervaceMistnosti>(resFac.getReservationsOfLoggedUser());
    }
    /**
     * @param items the items to set
     */
    public void setItems(ArrayList<RezervaceMistnosti> items) {
        this.items = items;
    }

    /**
     * @return the editDateOfReservation
     */
    public Date getEditDateOfReservation() {
        return editDateOfReservation;
    }

    /**
     * @param editDateOfReservation the editDateOfReservation to set
     */
    public void setEditDateOfReservation(Date editDateOfReservation) {
        this.editDateOfReservation = editDateOfReservation;
    }

    /**
     * @return the editDateFrom
     */
    public Date getEditDateFrom() {
        return editDateFrom;
    }

    /**
     * @param editDateFrom the editDateFrom to set
     */
    public void setEditDateFrom(Date editDateFrom) {
        this.editDateFrom = editDateFrom;
    }

    /**
     * @return the editDateTo
     */
    public Date getEditDateTo() {
        return editDateTo;
    }

    /**
     * @param editDateTo the editDateTo to set
     */
    public void setEditDateTo(Date editDateTo) {
        this.editDateTo = editDateTo;
    }
}
