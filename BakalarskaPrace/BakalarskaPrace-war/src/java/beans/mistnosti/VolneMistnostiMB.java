/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans.mistnosti;

import app.facade.reservationEditor.ReservationEditorFacade;
import app.facade.roomFinder.RoomFinderFacade;
import dbEntity.Mistnost;
import dbEntity.RezervaceMistnosti;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.event.DragDropEvent;
import view.bundle.ResourceBundleOperator;
import view.facesMessenger.FacesMessengerUtil;

/**
 *
 * @author Pavel
 */
@Named("volneMistnostiBean")
@SessionScoped
public class VolneMistnostiMB implements Serializable{

    @Inject RoomFinderFacade roomFac;
    @Inject FacesMessengerUtil messUtil;
    @Inject ResourceBundleOperator bundle;
    @Inject ReservationEditorFacade resFac;
    
    
    private Date datumVolne,casOdVolne,casDoVolne;
    private List<Mistnost> items;
    private List<RezervaceMistnosti> draggableRes;
    private RezervaceMistnosti toBeDeleted;
    private ArrayList numbers;
    private Mistnost selectedRoom;
    private String popis;
    private int pocetRezervovanychMist = 1;
    
    
    /**
     * 
     */
    public VolneMistnostiMB() {
        popis = "";
        items = new ArrayList<Mistnost>();
        draggableRes = new ArrayList<RezervaceMistnosti>();
        numbers = new ArrayList();
        fillNumbers();
    }
    
    
    
    
    /**
     * metoda pro vyhledávání volných mítností
     */
    public void update(){
            
        try{

        items = new ArrayList(roomFac.getAllFreeRooms(draggableRes));

        messUtil.addFacesMsgInfo(bundle.getMsg("sysMsgSuccLoad"));

        }catch(Exception e){
            messUtil.addFacesMsgError(bundle.getMsg("sysMsgDBfail"));
        }
    }
    
    public void addReservationToList(){
        //System.out.println("Called add");
        //NUTNOST KONTROLY ZDA SE REZERVACE V LISTU NEPREKRYVAJI - DONE - TEST - OK
        //VYHLEDAVANI VOLNYCH MISTNOSTI PODLE LISTU - DONE - TEST - OK
        //ZAPSANI DO DB - DONE - TEST - OK
        //MOZNOST REZERVACE NA NEKOLIK TYDNU DOPREDU(OPAKOVANI STEJNEHO)
        //ZAPSANI REZERVACI DO DB VCETNE OPAKOVANI
        //DROPPABLE KOS
        
        if(datumVolne != null && casDoVolne != null && casOdVolne != null && casOdVolne.before(casDoVolne)){
            
            if(popis == null){
                popis = "";
            }
            
            RezervaceMistnosti res = new RezervaceMistnosti(datumVolne, casOdVolne , casDoVolne, pocetRezervovanychMist, popis);
            
            
            if(!roomFac.isInterfering(res, draggableRes)){
                draggableRes.add(res);
            }
            else{
                messUtil.addFacesMsgError(bundle.getMsg("sysMsgReservationsInterfering"));
            }
        }
        else if(casOdVolne.after(casDoVolne) || casOdVolne.equals(casDoVolne)){
            messUtil.addFacesMsgError(bundle.getMsg("sysMsgWrongTimes"));
        }
        else{
            messUtil.addFacesMsgError(bundle.getMsg("sysMsgWrongInput"));
        }
    }
    public void saveAllPreparedReservations(){
        resFac.createAllReservations(selectedRoom, draggableRes);
    }
    public void deleteSelected(RezervaceMistnosti toBeDeleted){
        draggableRes.remove(toBeDeleted);
    }


    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    public void onResDrop(DragDropEvent ddEvent) {  
        RezervaceMistnosti res = ((RezervaceMistnosti) ddEvent.getData());  
  
        draggableRes.remove(res);  
    }  
    
    /**
     * 
     * @param dm
     */
    public void setItems(List<Mistnost> dm){
        this.items = dm;
    }
    /**
     * 
     * @return vrací všechny místnosti
     */
    public List<Mistnost> getItems(){
        return items;
    }

    /**
     * @return the datumVolne
     */
    public Date getDatumVolne() {
        return datumVolne;
    }

    /**
     * @param datumVolne the datumVolne to set
     */
    public void setDatumVolne(Date datumVolne) {
        this.datumVolne = datumVolne;
    }

    /**
     * @return the casDoVolne
     */
    public Date getCasDoVolne() {
        return casDoVolne;
    }

    /**
     * @param casDoVolne the casDoVolne to set
     */
    public void setCasDoVolne(Date casDoVolne) {
        this.casDoVolne = casDoVolne;
    }

    /**
     * @return the casOdVolne
     */
    public Date getCasOdVolne() {
        return casOdVolne;
    }

    /**
     * @param casOdVolne the casOdVolne to set
     */
    public void setCasOdVolne(Date casOdVolne) {
        this.casOdVolne = casOdVolne;
    }

    /**
     * @return the draggableRes
     */
    public List<RezervaceMistnosti> getDraggableRes() {
        return draggableRes;
    }

    /**
     * @param draggableRes the draggableRes to set
     */
    public void setDraggableRes(List<RezervaceMistnosti> draggableRes) {
        this.draggableRes = draggableRes;
    }

    /**
     * @return the selectedRoom
     */
    public Mistnost getSelectedRoom() {
        return selectedRoom;
    }

    /**
     * @param selectedRoom the selectedRoom to set
     */
    public void setSelectedRoom(Mistnost selectedRoom) {
        this.selectedRoom = selectedRoom;
    }

    /**
     * @return the popis
     */
    public String getPopis() {
        return popis;
    }

    /**
     * @param popis the popis to set
     */
    public void setPopis(String popis) {
        this.popis = popis;
    }

    /**
     * @return the pocetRezervovanychMist
     */
    public int getPocetRezervovanychMist() {
        return pocetRezervovanychMist;
    }

    /**
     * @param pocetRezervovanychMist the pocetRezervovanychMist to set
     */
    public void setPocetRezervovanychMist(int pocetRezervovanychMist) {
        this.pocetRezervovanychMist = pocetRezervovanychMist;
    }
    
    
    
    private void fillNumbers() {
        for(int i = 1; i < 200; i++){
            numbers.add(i);
        }
    }

    /**
     * @return the numbers
     */
    public ArrayList getNumbers() {
        return numbers;
    }

    /**
     * @param numbers the numbers to set
     */
    public void setNumbers(ArrayList numbers) {
        this.numbers = numbers;
    }

    /**
     * @return the toBeDeleted
     */
    public RezervaceMistnosti getToBeDeleted() {
        return toBeDeleted;
    }

    /**
     * @param toBeDeleted the toBeDeleted to set
     */
    public void setToBeDeleted(RezervaceMistnosti toBeDeleted) {
        this.toBeDeleted = toBeDeleted;
    }
}
