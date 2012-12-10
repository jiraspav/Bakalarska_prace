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
import app.sessionHolder.SessionHolderEJB;
import dbEntity.*;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.inject.Inject;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleModel;
import org.primefaces.model.TreeNode;

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
    @Inject private SessionHolderEJB session;
    @Inject private RozvrhyOperator rozOper;
    @Inject private SemestrOperator semOper;
    @Inject private DnyVTydnuOperator denOper;
    
    @Override
    public ScheduleModel createNewModel(TreeNode mistnostNode) {
        if(mistnostNode != null){
            String zkratka = mistnostNode.getData().toString();
            Mistnost mistnost = mistOper.getMistnost(zkratka);
            
            return setSchedule(mistnost);
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
    private ScheduleModel setSchedule(Mistnost mistnost){

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
                if(uziv.equals(session.getLoggedUzivatel())){
                    event = new DefaultScheduleEvent(uzivOper.getUzivatelRole(uziv)+":"+uziv.getLogin()+":"+rezer.getPopis(), 
                                        datumOd, datumDo, "moje-rezervace-event");
                }
                else{
                    event = new DefaultScheduleEvent(uzivOper.getUzivatelRole(uziv)+":"+uziv.getLogin()+":"+rezer.getPopis(), 
                                        datumOd, datumDo, "rezervace-event");
                }
                eventModel.addEvent(event);
            }
        }
        ////////////////////////////////ROZVRHY/////////////////////////////////
        
        Date currentDate = starts;
        System.out.println("CREATING ROZVRHY");
        while(currentDate.before(ends)){
            
            System.out.println("DATE: "+currentDate);
            
            List<Rozvrhy> rozvrhy = rozOper.getRozvrhy(mistnost, denOper.getENDen(getNameOfDay(currentDate)));
            
            for(Rozvrhy roz : rozvrhy){
                
                DefaultScheduleEvent event = new DefaultScheduleEvent("Rozvrh", 
                            concateDateAndTime(currentDate, roz.getOd()), concateDateAndTime(currentDate, roz.getDo1()), "rozvrh-event");
                eventModel.addEvent(event);
                System.out.println("ADDED ROZVRH: "+roz.getIDrozvrhu()+" "+roz.getIDdnu().getNazev()+" PREDMET: "+roz.getIDpredmetu().getNazev());
            }
            
            System.out.println("");    
            
            
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
}
