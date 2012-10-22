/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans.admin;

import XMLparser.ParserController;
import app.SessionHolder.SessionHolder;
import app.facesMessenger.FacesMessengerUtil;
import dbEntity.*;
import entityFacade.*;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.ejb.Asynchronous;
import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import view.bundle.ResourceBundleOperator;

/**
 *
 * @author Pavel
 */
@Named(value = "adminPageBean")
@ConversationScoped
public class AdminPageBean implements Serializable{
    
    private @Inject RezervaceMistnostiFacade rezFac;
    private @Inject MistnostFacade mistFac;
    private @Inject StrediskoFacade strFac;
    private @Inject PredmetyFacade predFac;
    private @Inject SemestrFacade semFac;
    private @Inject RozvrhyFacade rozFac;
    private @Inject UpdateRozvrhuFacade upRozFac;
    private @Inject ParserController parsCon;
    private @Inject FacesMessengerUtil messUtil;
    private @Inject ResourceBundleOperator bundle;
    private @Inject SessionHolder session;
    private @Inject GroupTableFacade groupFac;
    
    private String confDialog;
    private Semestr soucasny;
    private Date posledniAktualizace;
    private boolean ready = true;
    
    /**
     * 
     */
    public AdminPageBean() {
    }

    /**
     * @return the soucasny
     */
    public Semestr getSoucasny() {
        
        List<Semestr> semestry = semFac.findAll();
        if(semestry.size() > 0)
            soucasny = (Semestr) semestry.get(semestry.size()-1);
        else
            soucasny = new Semestr();
            
        return soucasny;
    }

    /**
     * @param soucasny the soucasny to set
     */
    public void setSoucasny(Semestr soucasny) {
        this.soucasny = soucasny;
    }

    /**
     * @return the posledniAktualizace
     */
    public String getPosledniAktualizace() {
        
        List<UpdateRozvrhu> vsechnyUpdaty = upRozFac.findAll();
        if(vsechnyUpdaty.size() > 0)
            posledniAktualizace = ((UpdateRozvrhu) vsechnyUpdaty.get(vsechnyUpdaty.size()-1)).getDatumAktualizaceRozvrhu();
        else
            posledniAktualizace = new Date();
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        
        return sdf.format(posledniAktualizace);
    }

    /**
     * @param posledniAktualizace the posledniAktualizace to set
     */
    public void setPosledniAktualizace(Date posledniAktualizace) {
        this.posledniAktualizace = posledniAktualizace;
    }
    
    /**
     * metoda zajišťující aktualizaci databáze
     */
    @Asynchronous
    public void refreshDatabase(){
        if(ready){
            ready = false;
            
            
            if(session.getLoggedUzivatel().getLogin().equals("superadmin")){
                messUtil.addFacesMsgError(bundle.getMsg("sysMsgDefaultAdminUpdateDBRestricted"));
            }
            else{
                deleteDB();
                fillDB();
                messUtil.addFacesMsgInfo(bundle.getMsg("sysMsgUpdateComp"));
            }
            
            
            ready = true;
        }
    }
    /**
     * metoda naplňující databázi
     */
    @Asynchronous
    public void fillDB(){
        //System.out.println("Naplňuji databázi...");
        //if(denFac.findAll().isEmpty())
        //    fillDny();
        if(strFac.findAll().isEmpty()){
            parsCon.fillDepartments();
        }
        
        parsCon.fillCourses();
        
        if(mistFac.findAll().isEmpty()){
            parsCon.fillRooms();
        }
        
        parsCon.fillRozvrhy();
        parsCon.fillSemestr();
    }
    /**
     * metoda pro smazání databáze
     */
    @Asynchronous
    public void deleteDB(){
        ArrayList<RezervaceMistnosti> rezAll = new ArrayList(rezFac.findAll()) ;
        Iterator iter = rezAll.iterator();
        while(iter.hasNext()){
            RezervaceMistnosti delete = (RezervaceMistnosti) iter.next();
            rezFac.remove(delete);
        }
        
        ArrayList<Rozvrhy> rozvrhyAll = new ArrayList(rozFac.findAll()) ;
        iter = rozvrhyAll.iterator();
        while(iter.hasNext()){
            Rozvrhy delete = (Rozvrhy) iter.next();
            rozFac.remove(delete);
        }
        
        ArrayList<Predmety> predmetyAll = new ArrayList(predFac.findAll()) ;
        iter = predmetyAll.iterator();
        while(iter.hasNext()){
            Predmety delete = (Predmety) iter.next();
            predFac.remove(delete);
        }
        
        /*ArrayList<Mistnost> mistnostiAll = new ArrayList(mistFac.findAll()) ;
        iter = mistnostiAll.iterator();
        while(iter.hasNext()){
            Mistnost delete = (Mistnost) iter.next();
            mistFac.remove(delete);
        }
        
        ArrayList<Stredisko> strediskaAll = new ArrayList(strFac.findAll()) ;
        iter = strediskaAll.iterator();
        while(iter.hasNext()){
            Stredisko delete = (Stredisko) iter.next();
            strFac.remove(delete);
        }*/
        
        ArrayList<UpdateRozvrhu> updateRozvrhuAll = new ArrayList(upRozFac.findAll()) ;
        iter = updateRozvrhuAll.iterator();
        while(iter.hasNext()){
            UpdateRozvrhu delete = (UpdateRozvrhu) iter.next();
            upRozFac.remove(delete);
        }
        
        ArrayList<Semestr> semestrAll = new ArrayList(semFac.findAll()) ;
        iter = semestrAll.iterator();
        while(iter.hasNext()){
            Semestr delete = (Semestr) iter.next();
            semFac.remove(delete);
        }
    }
    
    
    /**
     * @return the confDialog
     */
    public String getConfDialog() {
        if(ready){
            confDialog = "Startuji aktualizaci databáze...\nTato operace trvá kolem 20 minut...";
        }
        else
            confDialog = "Právě probíhá aktualizace...";
        return confDialog;
    }

    /**
     * @param confDialog the confDialog to set
     */
    public void setConfDialog(String confDialog) {
        this.confDialog = confDialog;
    }
    
}
