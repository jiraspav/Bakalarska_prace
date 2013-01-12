/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package view.mistnosti;

import app.baseDataOperators.DnyVTydnuOperator;
import app.baseDataOperators.MistnostOperator;
import app.baseDataOperators.RozvrhyOperator;
import app.facade.RoomTreeCreator.RoomTreeCreatorFacade;
import app.facade.reservationEditor.ReservationEditorFacade;
import app.facade.roomFinder.RoomFinderFacade;
import dbEntity.Mistnost;
import dbEntity.RezervaceMistnosti;
import dbEntity.Rozvrhy;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import view.SessionHolder.SessionHolderMB;
import view.bundle.ResourceBundleOperator;
import view.facesMessenger.FacesMessengerUtil;

/**
 *
 * @author Pavel
 */

@Named("vytvoreniRezervaceBean")
@SessionScoped
public class VytvoreniRezervaceMB implements Serializable{
    
    @Inject private RoomTreeCreatorFacade roomTreeFac;
    @Inject private ReservationEditorFacade resFac;
    @Inject private DnyVTydnuOperator denOper;
    @Inject private MistnostOperator mistOper;
    @Inject private RozvrhyOperator rozoper;
    @Inject private RoomFinderFacade roomFac;
    @Inject private FacesMessengerUtil messUtil;
    @Inject private ResourceBundleOperator bundle;
    @Inject private SessionHolderMB session;
    
    private TreeNode selectedNode;  
    private TreeNode root;  
    
    private int odHodiny = 0,odMinuty = 0,doHodiny = 0,doMinuty = 0;
    
    private Date odCas,doCas;
    private Date datumRezervace;
    private int pocetRezervovanychMist = 1;
    private String popis = " ";
    private ArrayList<Integer> numbers; //pocet rezervovanych mist
    private ArrayList<Integer> numbersRep; // pocet opakovani zadanych rezervaci
    private int pocetOpakovaní = 1;
    private int oldPocetOpakovaní = 1;
    
    private List<RezervaceMistnosti> draggableRes;

    private boolean disabled = true;
    
 
    
    
    /**
     * Konstruktor
     */
    public VytvoreniRezervaceMB() {
        draggableRes = new ArrayList<RezervaceMistnosti>();
        numbers = new ArrayList<Integer>();
        numbersRep = new ArrayList<Integer>();
        fillNumbers();
        
        root = new DefaultTreeNode("root", null);
    }

    
    /**
     * Metoda pro uložení všeh rezervací ze seznamu
     */
    public void saveAllPreparedReservations(){
        
        ArrayList<RezervaceMistnosti> toBeSaved = new ArrayList<RezervaceMistnosti>(draggableRes);
        
        for(int i = 1; i < pocetOpakovaní; i++){
            for(RezervaceMistnosti r : draggableRes){
                RezervaceMistnosti incremented = new RezervaceMistnosti(incrementDate(r.getDatumRezervace(), 7, i),r.getOd(),r.getDo1(),r.getPocetRezervovanychMist(),r.getPopis());
                toBeSaved.add(incremented);
            }
        }
        
        resFac.createAllReservations(mistOper.getMistnost(selectedNode.getData().toString()), toBeSaved, session.getLoggedUzivatel());
        draggableRes = new ArrayList<RezervaceMistnosti>();
    }
    
    
    /**
     * přepínač rendered
     */
    public void switchActive(){
        
        if(selectedNode == null){
            disabled = true;
        }
        else{
            disabled = false;
        }
        //System.out.println("SWITCHED TO "+disabled);
    }
    
    /**
     * Metoda pro přidávání rezervací do seznamu a zároveň kontrola zda se nový seznam vejde do místnosti
     */
    public void addReservationToList(){
        //System.out.println("addReservationToList");
        if(datumRezervace != null && doCas != null && odCas != null && odCas.before(doCas)){
            
            if(popis == null){
                popis = " ";
            }
            
            RezervaceMistnosti res = new RezervaceMistnosti(datumRezervace, odCas , doCas, pocetRezervovanychMist, popis);
            ArrayList<Rozvrhy> rozvrhyMistnosti = new ArrayList<Rozvrhy>(rozoper.getRozvrhy(mistOper.getMistnost(selectedNode.getData().toString())));
            //KONTROLA PREKRYVANI V SEZNAMU
            
            
            if(roomFac.isInterfering(res, rozvrhyMistnosti)){
                messUtil.addFacesMsgError(bundle.getMsg("sysMsgAlreadyScheduled"));
            }
            else if(!roomFac.isInterfering(res, draggableRes)){
                
                //KONTROLA ZDA PRI PRIDANI REZERVACE SE ZVOLENA MISTNOST NACHAZI MEZI VOLNYMI
                ArrayList<RezervaceMistnosti> temp = new ArrayList<RezervaceMistnosti>(draggableRes);
                temp.add(res);
                
                ArrayList<RezervaceMistnosti> tempFull = new ArrayList<RezervaceMistnosti>(temp);
                
                for(int i = 0; i < pocetOpakovaní; i++){
                    for(RezervaceMistnosti r : temp){
                        RezervaceMistnosti incremented = new RezervaceMistnosti(incrementDate(r.getDatumRezervace(), 7, i),r.getOd(),r.getDo1(),r.getPocetRezervovanychMist(),r.getPopis());
                        tempFull.add(incremented);
                    }
                }
                
                
                if(isThereRoom(selectedNode, roomFac.getAllFreeRooms(tempFull, session.getLoggedUzivatel()))){
                    
                    draggableRes.add(res);
                }
                else{
                    
                    messUtil.addFacesMsgError(bundle.getMsg("sysMsgRoomNotFree"));
                }
            }
            else{
                messUtil.addFacesMsgError(bundle.getMsg("sysMsgReservationsInterfering"));
            }
        }
        else if(odCas.after(doCas) || odCas.equals(doCas)){
            messUtil.addFacesMsgError(bundle.getMsg("sysMsgWrongTimes"));
        }
        else{
            messUtil.addFacesMsgError(bundle.getMsg("sysMsgWrongInput"));
        }
    }
    
    /**
     * Listener pro změnu hodnoty p:selectOneMenu
     */
    public void valueChangeListener(AjaxBehaviorEvent e){
    
        
        ArrayList<RezervaceMistnosti> temp = new ArrayList<RezervaceMistnosti>(draggableRes);
        
        
        ArrayList<RezervaceMistnosti> tempFull = new ArrayList<RezervaceMistnosti>(temp);
                
        for(int i = 1; i < pocetOpakovaní; i++){
            for(RezervaceMistnosti r : temp){
                RezervaceMistnosti incremented = new RezervaceMistnosti(incrementDate(r.getDatumRezervace(), 7, i),r.getOd(),r.getDo1(),r.getPocetRezervovanychMist(),r.getPopis());
                tempFull.add(incremented);
            }
        }


        if(!isThereRoom(selectedNode, roomFac.getAllFreeRooms(tempFull, session.getLoggedUzivatel()))){

            pocetOpakovaní = oldPocetOpakovaní;
            messUtil.addFacesMsgError(bundle.getMsg("sysMsgRoomNotFree"));
        }
    }
    
    /**
     * Metoda pro odstraňování rezervace
     * @param toBeDeleted
     */
    public void deleteSelected(RezervaceMistnosti toBeDeleted){
        
        draggableRes.remove(toBeDeleted);
    }
    
    private boolean isThereRoom(TreeNode mist, List<Mistnost> listMist){
        boolean result = false;
        String name = (String) mist.getData();
        for(Mistnost curr : listMist){
            if(curr.getZkratka().equals(name)){
                result = true;
                break;
            }
        }
        return result;
    }
    
    /**
     * 
     */
    public void smazRoot(){
        root = new DefaultTreeNode("root", null);
    }
    
    /**
     * metoda pro tvorbu stromové struktury ze středisek a místností PrimeFaces komponenty p:tree
     * @return hotovou stromovou strukturu
     */
    public TreeNode getRoot() { 
        
        if(root.getChildCount() == 0){
            root = roomTreeFac.createRoomTree(root);
        }
        
        return root;
        
    }
    
    
    private Date incrementDate(Date date, int days, int repetition){
        
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        
        for(int i = 0; i < repetition; i++){
            c.add(Calendar.DATE, days);
        }
        
        return c.getTime();
    }
    
    
    
    /**
     * 
     * @param root
     */
    public void setRoot(TreeNode root) { 
        this.root = root;
    }
    
    private void fillNumbers() {
        
        for(int i = 1; i < 11; i++){
            numbersRep.add(i);
        }
    }
    
    /**
     * 
     * @return vrací označenou node
     */
    public TreeNode getSelectedNode() {  
        return selectedNode;  
    }  
  
    /**
     * 
     * @param selectedNode
     */
    public void setSelectedNode(TreeNode selectedNode) {
        
        if(isThereRoom(selectedNode, roomFac.getAllFreeRooms(draggableRes, session.getLoggedUzivatel()))){
            this.selectedNode = selectedNode;
        }
        else{
            messUtil.addFacesMsgError(bundle.getMsg("sysMsgRoomNotFree"));
        }
    }  
    
        
    /**
     * Metoda vytvářející popisky k místnostem v p:tree
     * @param roomName zkratka místnosti
     * @return nový popisek s detaily místnosti
     */
    public String toolTip(String roomName){
        Mistnost curr = mistOper.getMistnost(roomName);
        String result;
        try{
            result = "Název: "+curr.getZkratka()+"   Kapacita: "+curr.getKapacita()+"   Typ místnosti: "+curr.getiDtyp().getNazev()+"   Lokalita: "+curr.getLokalita();
        }catch(NullPointerException e){
            return null;
        }
        return result;
    }
    
    /**
     * @return the odHodiny
     */
    public int getOdHodiny() {
        return odHodiny;
    }

    /**
     * @param odHodiny the odHodiny to set
     */
    public void setOdHodiny(int odHodiny) {
        this.odHodiny = odHodiny;
    }

    /**
     * @return the odMinuty
     */
    public int getOdMinuty() {
        return odMinuty;
    }

    /**
     * @param odMinuty the odMinuty to set
     */
    public void setOdMinuty(int odMinuty) {
        this.odMinuty = odMinuty;
    }

    /**
     * @return the doHodiny
     */
    public int getDoHodiny() {
        return doHodiny;
    }

    /**
     * @param doHodiny the doHodiny to set
     */
    public void setDoHodiny(int doHodiny) {
        this.doHodiny = doHodiny;
    }

    /**
     * @return the doMinuty
     */
    public int getDoMinuty() {
        return doMinuty;
    }

    /**
     * @param doMinuty the doMinuty to set
     */
    public void setDoMinuty(int doMinuty) {
        this.doMinuty = doMinuty;
    }


    /**
     * @return the odCas
     */
    public Date getOdCas() {
        return odCas;
    }

    /**
     * @param odCas the odCas to set
     */
    public void setOdCas(Date odCas) {
        this.odCas = odCas;
    }

    /**
     * @return the doCas
     */
    public Date getDoCas() {
        return doCas;
    }

    /**
     * @param doCas the doCas to set
     */
    public void setDoCas(Date doCas) {
        this.doCas = doCas;
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
     * @return the datumRezervace
     */
    public Date getDatumRezervace() {
        return datumRezervace;
    }

    /**
     * @param datumRezervace the datumRezervace to set
     */
    public void setDatumRezervace(Date datumRezervace) {
        this.datumRezervace = datumRezervace;
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

    /**
     * @return the numbers
     */
    public ArrayList<Integer> getNumbers() {
        
        numbers = new ArrayList<Integer>();
        
        if(selectedNode != null){
            int pocetMist = mistOper.getMistnost(selectedNode.getData().toString()).getKapacita();
            for(int i = 2; i <= pocetMist; i++){
                numbers.add(i);
            }
        }
        
        return numbers;
    }

    /**
     * @param numbers the numbers to set
     */
    public void setNumbers(ArrayList<Integer> numbers) {
        this.numbers = numbers;
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

