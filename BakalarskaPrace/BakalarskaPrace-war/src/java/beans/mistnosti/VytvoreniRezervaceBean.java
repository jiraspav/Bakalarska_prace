/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans.mistnosti;

import app.facade.ScheduleCreator.impl.RozvrhNaDen;
import app.facade.RoomTreeCreator.RoomTreeCreatorFacade;
import app.facade.ScheduleCreator.ScheduleCreatorFacade;
import app.facade.reservationEditor.ReservationEditorFacade;
import dbEntity.*;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.SessionScoped;
import javax.faces.event.ActionEvent;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.model.TreeNode;

/**
 *
 * @author Pavel
 */

@Named("vytvoreniRezervaceBean")
@SessionScoped

public class VytvoreniRezervaceBean implements Serializable{
    
    @Inject private RoomTreeCreatorFacade roomTreeFac;
    @Inject private ScheduleCreatorFacade scheduleCreatorFac;
    @Inject private ReservationEditorFacade resFac;
    
    private TreeNode selectedNode;  
    private TreeNode root;  
    
    private int odHodiny = 0,odMinuty = 0,doHodiny = 0,doMinuty = 0;
    private String odCas,doCas;
    private String popis = " ";
    private Mistnost rezervovanaMistnost;
    private DataModel rozvrhyMistnosti;
    private ArrayList dny,mesice,roky;
    private Date datumRezervace;
    private Date maxDate,minDate;
    
    
    
    /**
     * 
     */
    public VytvoreniRezervaceBean() {
    }

    
    
    /**
     * metoda, zajišťující vytváření rozvrhů pro předměty
     * @return seznam hotových rozvrhů
     */
    public DataModel getRozvrhyMistnosti(){
        
        return new ListDataModel(scheduleCreatorFac.createSchedule(selectedNode));
        
    }


    /**
     * 
     */
    public void smazRoot(){
        root = null;
    }
    
    /**
     * metoda pro tvorbu stromové struktury ze středisek a místností PrimeFaces komponenty p:tree
     * @return hotovou stromovou strukturu
     */
    public TreeNode getRoot() { 
        
        return roomTreeFac.createRoomTree(root);
        
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
        this.selectedNode = selectedNode;  
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
    public String getOdCas() {
        return odCas;
    }

    /**
     * @param odCas the odCas to set
     */
    public void setOdCas(String odCas) {
        this.odCas = odCas;
    }

    /**
     * @return the doCas
     */
    public String getDoCas() {
        return doCas;
    }

    /**
     * @param doCas the doCas to set
     */
    public void setDoCas(String doCas) {
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
}

