/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app.facade.ScheduleCreator.impl;

import app.baseDataOperators.DnyVTydnuOperator;
import app.baseDataOperators.MistnostOperator;
import app.baseDataOperators.RozvrhyOperator;
import app.facade.ScheduleCreator.ScheduleCreatorFacade;
import dbEntity.DenVTydnu;
import dbEntity.Mistnost;
import dbEntity.Rozvrhy;
import dbEntity.Stredisko;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.faces.model.ListDataModel;
import javax.inject.Inject;
import org.primefaces.model.TreeNode;

/**
 *
 * @author Pavel
 */
@Stateless
@Local(ScheduleCreatorFacade.class)
public class ScheduleCreator implements ScheduleCreatorFacade{

    @Inject private MistnostOperator mistOper;
    @Inject private RozvrhyOperator rozOper;
    @Inject private DnyVTydnuOperator denOper;
    
    private static String[] converter = {"7:30","9:00","9:15","10:45","11:00","12:30",
                            "12:45","14:15","14:30","16:00","16:15","17:45","18:00","19:30","20:30"};

    
    @Override
    public ArrayList<RozvrhNaDen> createSchedule(TreeNode selectedNode) {
        
        ArrayList<RozvrhNaDen> novy = buildEmptySchedule();
        
        if(selectedNode != null)
        if(selectedNode.isLeaf()){
            
            novy.clear();
            
            String zkratka = (String) selectedNode.getData();
            Mistnost mistnost = mistOper.getMistnost(zkratka);
            
            
            
            ArrayList<Rozvrhy> pondeli = new ArrayList(rozOper.getRozvrhy(mistnost, denOper.getCZDen("Pondělí")));
            RozvrhNaDen pondelLich = createRozvrhNaDen(pondeli,"Pondělí lichý",true);
            RozvrhNaDen pondelSud = createRozvrhNaDen(pondeli,"Pondělí sudý",false);
            novy.add(pondelLich);
            novy.add(pondelSud);
            
            ArrayList<Rozvrhy> utery = new ArrayList(rozOper.getRozvrhy(mistnost, denOper.getCZDen("Úterý")));
            RozvrhNaDen uterLich = createRozvrhNaDen(utery,"Úterý lichý",true);
            RozvrhNaDen uterSud = createRozvrhNaDen(utery,"Úterý sudý",false);
            novy.add(uterLich);
            novy.add(uterSud);
            
            ArrayList<Rozvrhy> streda = new ArrayList(rozOper.getRozvrhy(mistnost, denOper.getCZDen("Středa")));
            RozvrhNaDen stredLich = createRozvrhNaDen(streda,"Středa lichý",true);
            RozvrhNaDen stredSud = createRozvrhNaDen(streda,"Středa sudý",false);
            novy.add(stredLich);
            novy.add(stredSud);
            
            ArrayList<Rozvrhy> ctvrtek = new ArrayList(rozOper.getRozvrhy(mistnost, denOper.getCZDen("Čtvrtek")));
            RozvrhNaDen ctvrteLich = createRozvrhNaDen(ctvrtek,"Čtvrtek lichý",true);
            RozvrhNaDen ctvrteSud = createRozvrhNaDen(ctvrtek,"Čtvrtek sudý",false);
            novy.add(ctvrteLich);
            novy.add(ctvrteSud);
            
            ArrayList<Rozvrhy> patek = new ArrayList(rozOper.getRozvrhy(mistnost, denOper.getCZDen("Pátek")));
            RozvrhNaDen pateLich = createRozvrhNaDen(patek,"Pátek lichý",true);
            RozvrhNaDen pateSud = createRozvrhNaDen(patek,"Pátek sudý",false);
            novy.add(pateLich);
            novy.add(pateSud);
            
        }
        
        return novy;
    }
    private RozvrhNaDen createRozvrhNaDen(ArrayList<Rozvrhy> rozvrhy,String naJakyDen,boolean lichy) {

        RozvrhNaDen novy = new RozvrhNaDen(naJakyDen);
        
        for(Rozvrhy curr : rozvrhy){
            
            SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
            String odKdy = sdf.format(curr.getOd());
            int poziceOd = getHodinuInt(odKdy);
            String doKdy = sdf.format(curr.getDo1());
            int poziceDo = getHodinuInt(doKdy);
            
            
                for(int i = poziceOd; i <= poziceDo; i++){
                    
                    if(lichy){
                        if(curr.getLichyTyden())
                            novy.addHodinu(curr.getIDpredmetu(), i);
                    }
                    
                    else{
                        if(curr.getSudyTyden())
                            novy.addHodinu(curr.getIDpredmetu(), i);
                    }
                }
                          
        }
        
        return novy;
    }
    private int getHodinuInt(String hodina){
        int pozice = 0;
        
        for(int i = 0; i<converter.length ; i++){
            if(converter[i].equals(hodina)){
                pozice = i;
                break;
            }
        }
        return pozice;
    }
    
    private ArrayList<RozvrhNaDen> buildEmptySchedule(){
        RozvrhNaDen empty = new RozvrhNaDen();
        
        ArrayList<RozvrhNaDen> novy = new ArrayList<RozvrhNaDen>();
        novy.add(empty);
        novy.add(empty);
        novy.add(empty);
        novy.add(empty);
        novy.add(empty);
        
        return novy;
    }
    
}