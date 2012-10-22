/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans.mistnosti;

import dbEntity.Mistnost;
import dbEntity.RezervaceMistnosti;
import entityFacade.MistnostFacade;
import entityFacade.RezervaceMistnostiFacade;
import java.io.Serializable;
import java.util.*;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Pavel
 */
@Named("volneMistnostiBean")
@SessionScoped
public class volneMistnostiBean implements Serializable{

    private int counter = 0;
    private int den,mesic,rok;
    private int odHodiny,odMinuty,doHodiny,doMinuty;
    private Date datumVolne,casOdVolne,casDoVolne;
    private ArrayList dny,mesice,roky;
    private DataModel items = new ListDataModel();

    @Inject MistnostFacade mistnostCon;
    @Inject RezervaceMistnostiFacade rezCon;
    
    /**
     * 
     */
    public volneMistnostiBean() {
    }
    
    
    
    
    /**
     * metoda pro vyhledávání volných mítností
     */
    public void update(){
        
        
            if(datumVolne != null && casDoVolne != null && casOdVolne != null && casOdVolne.before(casDoVolne)){
                try{
                List<Mistnost> mistnosti = mistnostCon.findAll();
                List<RezervaceMistnosti> rezervace = rezCon.getRezervaceByDatum(datumVolne);
                Iterator iter = rezervace.iterator();

                while(iter.hasNext()){
                    RezervaceMistnosti rez = (RezervaceMistnosti) iter.next();
                    if(!((casOdVolne.after(rez.getDo1())) || (casDoVolne.before(rez.getOd())))){
                        if(rez.getNaCelouMistnost()){
                            Mistnost mistnost = rez.getIDmistnosti(); 
                            mistnosti.remove(mistnost);
                            counter++;
                        }
                    }
                }

                items = new ListDataModel(mistnosti);
                
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Successfully loaded");
                addMessage(message);
                
                }catch(Exception e){
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Database fail", "");
                addMessage(message);
                }
            }
            else if(casOdVolne.after(casDoVolne) || casOdVolne.equals(casDoVolne)){
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Time error", "Zadali jste špatně časy.");
                addMessage(message);
            }
            else{
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Informations fail", "Wrong input values.");
                addMessage(message);
            }
            
    }
    
    /**
     * 
     * @param dm
     */
    public void setItems(DataModel dm){
        this.items = dm;
    }
    /**
     * 
     * @return vrací všechny místnosti
     */
    public DataModel getItems(){
        return items;
    }

    private void addMessage(FacesMessage message) {
        FacesContext.getCurrentInstance().addMessage(null, message);
        
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
     * @return the den
     */
    public int getDen() {
        if(this.den == 0){
            Calendar cal = Calendar.getInstance();
            return cal.get(Calendar.DAY_OF_MONTH);
        }
        return den;
    }

    /**
     * @param den the den to set
     */
    public void setDen(int den) {
        this.den = den;
    }

    /**
     * @return the mesic
     */
    public int getMesic() {
        if(this.mesic == 0){
            Calendar cal = Calendar.getInstance();
            return cal.get(Calendar.MONTH);
        }
        return mesic;
    }

    /**
     * @param mesic the mesic to set
     */
    public void setMesic(int mesic) {
        this.mesic = mesic;
    }

    /**
     * @return the rok
     */
    public int getRok() {
        if(this.rok == 0){
            Calendar cal = Calendar.getInstance();
            return cal.get(Calendar.YEAR);
        }
        return rok;
    }

    /**
     * @param rok the rok to set
     */
    public void setRok(int rok) {
        this.rok = rok;
    }

    /**
     * @return the counter
     */
    public int getCounter() {
        return counter;
    }

    /**
     * @param counter the counter to set
     */
    public void setCounter(int counter) {
        this.counter = counter;
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
    
    
    
    
}
