/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package view.mistnosti;

import app.facade.reservationEditor.ReservationEditorFacade;
import app.facade.roomFinder.RoomFinderFacade;
import dbEntity.Mistnost;
import dbEntity.RezervaceMistnosti;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.event.AjaxBehaviorEvent;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.event.DragDropEvent;
import org.primefaces.model.TreeNode;
import view.SessionHolder.SessionHolderMB;
import view.bundle.ResourceBundleOperator;
import view.facesMessenger.FacesMessengerUtil;

/**
 *
 * @author Pavel
 */
@Named("volneMistnostiBean")
@SessionScoped
public class VolneMistnostiMB implements Serializable{

    @Inject private RoomFinderFacade roomFac;
    @Inject private FacesMessengerUtil messUtil;
    @Inject private ResourceBundleOperator bundle;
    @Inject private ReservationEditorFacade resFac;
    @Inject private VytvoreniRezervaceMB vytMB;
    @Inject private SessionHolderMB session;
    
    private Date datumVolne,casOdVolne,casDoVolne;
    private List<Mistnost> items;
    private List<RezervaceMistnosti> draggableRes;
    private RezervaceMistnosti toBeDeleted;
    private ArrayList numbers;
    private ArrayList<Integer> numbersRep;
    private int pocetOpakovaní = 1;
    private int oldPocetOpakovaní = 1;
    private Mistnost selectedRoom;
    private String popis;
    private Integer pocetRezervovanychMist = new Integer(1);
    private boolean disabled = true;
    
    /**
     * 
     */
    public VolneMistnostiMB() {
        popis = "";
        items = new ArrayList<Mistnost>();
        draggableRes = new ArrayList<RezervaceMistnosti>();
        numbers = new ArrayList();
        numbersRep = new ArrayList<Integer>();
        fillNumbers();
    }
    
    
 
    
    
    /**
     * metoda pro vyhledávání volných mítností
     */
    public void update(){
        
        selectedRoom = null;
        switchActive();
        ArrayList<RezervaceMistnosti> tempFull = new ArrayList<RezervaceMistnosti>(draggableRes);
        
        for(int i = 1; i < pocetOpakovaní; i++){
            for(RezervaceMistnosti r : draggableRes){
                RezervaceMistnosti incremented = new RezervaceMistnosti(incrementDate(r.getDatumRezervace(), 7, i),r.getOd(),r.getDo1(),r.getPocetRezervovanychMist(),r.getPopis());
                tempFull.add(incremented);
            }
        }
        
        try{

        items = new ArrayList(roomFac.getAllFreeRooms(tempFull,session.getLoggedUzivatel()));

        messUtil.addFacesMsgInfo(bundle.getMsg("sysMsgSuccLoad"));

        }catch(Exception e){
            messUtil.addFacesMsgError(bundle.getMsg("sysMsgDBfail"));
        }
    }
    
    /**
     * Metoda pro přídání rezervace do seznamu
     */
    public void addReservationToList(){
        //System.out.println("Called add");
        //NUTNOST KONTROLY ZDA SE REZERVACE V LISTU NEPREKRYVAJI - DONE - TEST - OK
        //VYHLEDAVANI VOLNYCH MISTNOSTI PODLE LISTU - DONE - TEST - OK
        //ZAPSANI DO DB - DONE - TEST - OK
        //MOZNOST REZERVACE NA NEKOLIK TYDNU DOPREDU(OPAKOVANI STEJNEHO) - TEST - OK
        //ZAPSANI REZERVACI DO DB VCETNE OPAKOVANI - TEST - OK
        //DROPPABLE KOS
        
        if(datumVolne != null && casDoVolne != null && casOdVolne != null && casOdVolne.before(casDoVolne)){
            
            if(popis == null){
                popis = " ";
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
    /**
     * Metoda pro uložení všech rezervací v seznamu
     */
    public void saveAllPreparedReservations(){
        
        ArrayList<RezervaceMistnosti> temp = new ArrayList<RezervaceMistnosti>(draggableRes);
        
        ArrayList<RezervaceMistnosti> tempFull = new ArrayList<RezervaceMistnosti>(temp);
                
        for(int i = 1; i < pocetOpakovaní; i++){
            for(RezervaceMistnosti r : temp){
                RezervaceMistnosti incremented = new RezervaceMistnosti(incrementDate(r.getDatumRezervace(), 7, i),r.getOd(),r.getDo1(),r.getPocetRezervovanychMist(),r.getPopis());
                tempFull.add(incremented);
            }
        }
        
        
        if(!isThereRoom(selectedRoom, roomFac.getAllFreeRooms(tempFull, session.getLoggedUzivatel()))){

            messUtil.addFacesMsgError(bundle.getMsg("sysMsgRoomNotFree"));
        }
        else{
            resFac.createAllReservations(selectedRoom, tempFull, session.getLoggedUzivatel());
            draggableRes = new ArrayList<RezervaceMistnosti>();
        }
        
    }
    /**
     * Metoda pro odstraňování rezervací
     * @param toBeDeleted odstraňovaná rezervace
     */
    public void deleteSelected(RezervaceMistnosti toBeDeleted){
        
        draggableRes.remove(toBeDeleted);
        update();
    }
    
    private Date incrementDate(Date date, int days, int repetition){
        
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        
        for(int i = 0; i < repetition; i++){
            c.add(Calendar.DATE, days);
        }
        
        return c.getTime();
    }
    
    private boolean isThereRoom(Mistnost mist, List<Mistnost> listMist){
        boolean result = false;
        String name = mist.getZkratka();
        for(Mistnost curr : listMist){
            if(curr.getZkratka().equals(name)){
                result = true;
                break;
            }
        }
        return result;
    }
    
    /**
     * Přepínač disabled
     */
    public void switchActive(){
        
        if(selectedRoom == null){
            disabled = true;
        }
        else{
            disabled = false;
        }
        
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
    public Integer getPocetRezervovanychMist() {
        return pocetRezervovanychMist;
    }

    /**
     * @param pocetRezervovanychMist the pocetRezervovanychMist to set
     */
    public void setPocetRezervovanychMist(Integer pocetRezervovanychMist) {
        this.pocetRezervovanychMist = pocetRezervovanychMist;
    }
    
    
    
    private void fillNumbers() {
        for(int i = 1; i < 200; i++){
            numbers.add(i);
        }
        for(int i = 1; i < 11; i++){
            getNumbersRep().add(i);
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

    /**
     * @return the disabled
     */
    public boolean isDisabled() {
        return disabled;
    }

    /**
     * @param disabled the disabled to set
     */
    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    /**
     * @return the numbersRep
     */
    public ArrayList<Integer> getNumbersRep() {
        return numbersRep;
    }

    /**
     * @param numbersRep the numbersRep to set
     */
    public void setNumbersRep(ArrayList<Integer> numbersRep) {
        this.numbersRep = numbersRep;
    }

    /**
     * @return the pocetOpakovaní
     */
    public int getPocetOpakovaní() {
        return pocetOpakovaní;
    }

    /**
     * @param pocetOpakovaní the pocetOpakovaní to set
     */
    public void setPocetOpakovaní(int pocetOpakovaní) {
        this.pocetOpakovaní = pocetOpakovaní;
    }
}
