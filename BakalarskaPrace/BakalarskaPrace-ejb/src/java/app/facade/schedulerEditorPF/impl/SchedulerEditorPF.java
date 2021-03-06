/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app.facade.schedulerEditorPF.impl;

import app.baseDataOperators.DnyVTydnuOperator;
import app.baseDataOperators.MistnostOperator;
import app.baseDataOperators.RezervaceMistnostiOperator;
import app.baseDataOperators.RozvrhyOperator;
import app.baseDataOperators.SemestrOperator;
import app.baseDataOperators.UzivatelOperator;
import app.facade.schedulerEditorPF.SchedulerEditorPFFacade;
import dbEntity.*;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.inject.Inject;
import org.primefaces.model.*;

/**
 *
 * @author Pavel
 */
@Stateless
@Local(SchedulerEditorPFFacade.class)
public class SchedulerEditorPF implements SchedulerEditorPFFacade{
    
    @Inject private MistnostOperator mistOper;
    @Inject private RezervaceMistnostiOperator rezOper;
    @Inject private UzivatelOperator uzivOper;
    @Inject private RozvrhyOperator rozOper;
    @Inject private SemestrOperator semOper;
    @Inject private DnyVTydnuOperator denOper;
    
    
    /**
     * Metoda pro vytváření nového ScheduleModelu
     * 
     * @param mistnost zkratka místnosti pro kterou se model vytváří
     * @param logged přihlášený uživatel
     * @return nový ScheduleModel vytvořený z rozvrhů a rezervací
     */
    @Override
    public ScheduleModel createNewModel(TreeNode mistnostNode, Uzivatel logged) {
        if(mistnostNode != null){
            String zkratka = mistnostNode.getData().toString();
            Mistnost mistnost = mistOper.getMistnost(zkratka);
            
            return setSchedule(mistnost,logged);
        }
        else{
            
            return new DefaultScheduleModel();
        }
    }
    
    private Date putTogetherTimeDate(Date date,Date time){
        Calendar datum = GregorianCalendar.getInstance();
        datum.setTime(date);
        Calendar cas = GregorianCalendar.getInstance();
        cas.setTime(time);

        Calendar celkem = GregorianCalendar.getInstance();
        celkem.set(datum.get(Calendar.YEAR), datum.get(Calendar.MONTH), datum.get(Calendar.DAY_OF_MONTH),
                    cas.get(Calendar.HOUR_OF_DAY), cas.get(Calendar.MINUTE), cas.get(Calendar.SECOND));
        Date nove = celkem.getTime();
        return nove;
    }
    /**
     * metoda, vytvářející nový model pro PrimeFaces komponentu p:schedule
     * @param mistnost
     * @return hotový model
     */
    private ScheduleModel setSchedule(Mistnost mistnost,Uzivatel logged){

        ScheduleModel eventModel = new DefaultScheduleModel();
        
        Semestr curr = semOper.getLatestSemestr();
        
        Date starts = curr.getZacina();
        Date ends = curr.getKonci();
        
        //////////////////////////////REZERVACE/////////////////////////////////
        
        ArrayList<RezervaceMistnosti> rezervace = new ArrayList(rezOper.getRezervace(mistnost));
        
        for(RezervaceMistnosti rezer : rezervace){
            
            Date datumOd = putTogetherTimeDate(rezer.getDatumRezervace(), rezer.getOd());
            Date datumDo = putTogetherTimeDate(rezer.getDatumRezervace(), rezer.getDo1());
            
            if(rezer.getIDmistnosti().equals(mistnost)){
                
                Uzivatel uziv = rezer.getIDuser();
                DefaultScheduleEvent event;
                if(uziv.equals(logged)){
                    event = new DefaultScheduleEvent("Rezervace "+rezer.getIDrezervace()+"\n"+uziv.getLogin(), 
                                        datumOd, datumDo, "moje-rezervace-event");
                }
                else{
                    event = new DefaultScheduleEvent("Rezervace "+rezer.getIDrezervace()+"\n"+uziv.getLogin(), 
                                        datumOd, datumDo, "rezervace-event");
                }
                eventModel.addEvent(event);
            }
        }
        ////////////////////////////////ROZVRHY/////////////////////////////////
        
        Date currentDate = starts;
        //System.out.println("CREATING ROZVRHY");
        while(currentDate.before(ends)){
            
            //System.out.println("DATE: "+currentDate);
            
            List<Rozvrhy> rozvrhy = rozOper.getRozvrhy(mistnost, denOper.getENDen(getNameOfDay(currentDate)));
            
            for(Rozvrhy roz : rozvrhy){
                
                DefaultScheduleEvent event = new DefaultScheduleEvent("Rozvrh "+roz.getIDrozvrhu()+"\n"+roz.getIDpredmetu().getNazev(), 
                            concateDateAndTime(currentDate, roz.getOd()), concateDateAndTime(currentDate, roz.getDo1()), "rozvrh-event");
                eventModel.addEvent(event);
                //System.out.println("ADDED ROZVRH: "+roz.getIDrozvrhu()+" "+roz.getIDdnu().getNazev()+" PREDMET: "+roz.getIDpredmetu().getNazev());
            }
            
            //System.out.println("");    
            
            
            currentDate = incrementDate(currentDate);
        }
        
        return eventModel;
    }
    
    private Date incrementDate(Date date){
        
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, 1);
        
        return c.getTime();
    }
    private String getNameOfDay(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE",Locale.ENGLISH);
        return sdf.format(date);
    }
    private Date concateDateAndTime(Date d, Date t){
        
        Calendar date = Calendar.getInstance();
        date.setTime(d);
        Calendar time = Calendar.getInstance();
        time.setTime(t);
        Calendar sum = Calendar.getInstance();
        sum.set(date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH), time.get(Calendar.HOUR_OF_DAY), time.get(Calendar.MINUTE));
        
        return sum.getTime();
    }

    /**
     * @param mistOper the mistOper to set
     */
    public void setMistOper(MistnostOperator mistOper) {
        this.mistOper = mistOper;
    }

    /**
     * @param rezOper the rezOper to set
     */
    public void setRezOper(RezervaceMistnostiOperator rezOper) {
        this.rezOper = rezOper;
    }

    /**
     * @param uzivOper the uzivOper to set
     */
    public void setUzivOper(UzivatelOperator uzivOper) {
        this.uzivOper = uzivOper;
    }

    /**
     * @param rozOper the rozOper to set
     */
    public void setRozOper(RozvrhyOperator rozOper) {
        this.rozOper = rozOper;
    }

    /**
     * @param semOper the semOper to set
     */
    public void setSemOper(SemestrOperator semOper) {
        this.semOper = semOper;
    }

    /**
     * @param denOper the denOper to set
     */
    public void setDenOper(DnyVTydnuOperator denOper) {
        this.denOper = denOper;
    }
}
