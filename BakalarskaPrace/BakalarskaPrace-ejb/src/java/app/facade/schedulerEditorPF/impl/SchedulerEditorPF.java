/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app.facade.schedulerEditorPF.impl;

import app.baseDataOperators.MistnostOperator;
import app.baseDataOperators.RezervaceMistnostiOperator;
import app.baseDataOperators.UzivatelOperator;
import app.facade.schedulerEditorPF.SchedulerEditorPFFacade;
import dbEntity.Mistnost;
import dbEntity.RezervaceMistnosti;
import dbEntity.Uzivatel;
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
        
        ArrayList<RezervaceMistnosti> rezervace = new ArrayList(rezOper.getRezervace(mistnost));
        
        for(RezervaceMistnosti rezer : rezervace){
            
            Date datumOd = putTogetherTimeDate(rezer.getDatumRezervace(), rezer.getOd());
            Date datumDo = putTogetherTimeDate(rezer.getDatumRezervace(), rezer.getDo1());
            
            if(rezer.getIDmistnosti().equals(mistnost)){
                
                Uzivatel uziv = rezer.getIDuser();
                DefaultScheduleEvent event = new DefaultScheduleEvent(uzivOper.getUzivatelRole(uziv)+":"+uziv.getLogin()+":"+rezer.getPopis(), 
                                        datumOd, datumDo);
                
                eventModel.addEvent(event);
            }
        }
        
        return eventModel;
    }

}
