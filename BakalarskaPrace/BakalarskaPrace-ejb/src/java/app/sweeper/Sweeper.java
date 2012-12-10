/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app.sweeper;

import app.baseDataOperators.UzivatelOperator;
import app.facade.roomFinder.RoomFinderFacade;
import app.sessionHolder.SessionHolderEJB;
import dbEntity.RezervaceMistnosti;
import java.util.ArrayList;
import java.util.Collections;

import java.util.Date;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 *
 * @author Pavel
 */
//ZAMETACI METODA
@Stateless
public class Sweeper {
    
    
    private ArrayList<Date> xStructure;
    private ArrayList<Integer> yStructure;
    private ArrayList<RezervaceMistnosti> overwrittenRes;
    
    private @Inject UzivatelOperator uzivOper;
    private @Inject SessionHolderEJB session;
    
    public Sweeper() {
        overwrittenRes = new ArrayList<RezervaceMistnosti>();
    }
    
    public ArrayList<RezervaceMistnosti> getOverwrittenReservations(RezervaceMistnosti curr, ArrayList<RezervaceMistnosti> interferingRes){
        
        ArrayList<RezervaceMistnosti> interferingReservations = new ArrayList<RezervaceMistnosti>(interferingRes);
        buildStructuresWithoutPriorityLookUp(interferingReservations);
        
        boolean deletingRes = false;
        
        for (int i = 0; i < yStructure.size(); i++){
            Integer sum = yStructure.get(i);
            //POKUD JE POCET REZERVOVANYCH MIST VETSI NEZ JE POTREBA PRO REZERVACI 
            if(sum > (curr.getIDmistnosti().getKapacita() - curr.getPocetRezervovanychMist())){
                
                ArrayList<RezervaceMistnosti> overWrittenOnIndex = getOverwrittenOnIndex(curr,i,interferingReservations);
                
                //////////////
                //najdi rezervace ktere budou pretypovany
                //odeber je a pridej do Listu, ktery se bude vracet 
                //////////////
                
                interferingReservations = removeAllFromList(interferingReservations, overWrittenOnIndex);
                
                deletingRes = true;
                break;
                
            }
        }
        
        if(deletingRes){
            return getOverwrittenReservations(curr,interferingReservations);
        }
        else{
            System.out.println("NUMBER OF OVERWRITTEN IS "+overwrittenRes.size());
            vypisList(overwrittenRes);
            return overwrittenRes;
        }
        
    }
    
    public int getMaximumReserved(ArrayList<RezervaceMistnosti> interferingReservations){
        
        buildStructures(interferingReservations);
        
        return findMaximumReserved();
        
    }
    
    private void buildStructures(ArrayList<RezervaceMistnosti> interferingReservations){
        xStructure = buildXstructure(interferingReservations);
        yStructure = buildYstructure(interferingReservations);
    }
    private void buildStructuresWithoutPriorityLookUp(ArrayList<RezervaceMistnosti> interferingReservations){
        xStructure = buildXstructure(interferingReservations);
        yStructure = buildYstructureWithoutPriorityLookUp(interferingReservations);
    }
    
    private ArrayList<Date> buildXstructure(ArrayList<RezervaceMistnosti> interferingReservations) {
        
        System.out.println("BUILD X-STRUCTURE");
        
        ArrayList<Date> xTemp = new ArrayList<Date>();
        
        for(RezervaceMistnosti rez : interferingReservations){
            if(!xTemp.contains(rez.getOd())){
                xTemp.add(rez.getOd());
            }
            if(!xTemp.contains(rez.getDo1())){
                xTemp.add(rez.getDo1());
            }
        }
        
        Collections.sort(xTemp);
        
        vypisList(xTemp);
        
        return xTemp;
    }

    private ArrayList<Integer> buildYstructure(ArrayList<RezervaceMistnosti> interferingReservations) {
        
        System.out.println("BUILD Y-STRUCTURE");
        
        ArrayList<Integer> temp = new ArrayList<Integer>();
        
        //SCAN LINE MOVING THROUGH X STRUCTURE
        for(Date curr : xStructure){
            
            ArrayList<RezervaceMistnosti> interferingTemp = getAllInterferingWithDate(curr, interferingReservations);
            
            temp.add(getSumOfReservedSpaceWithPriorityLookUp(interferingTemp));
            
        }
        vypisList(temp);
        
        return temp;
    }
    
    private ArrayList<Integer> buildYstructureWithoutPriorityLookUp(ArrayList<RezervaceMistnosti> interferingReservations) {
        
        System.out.println("BUILD Y-STRUCTURE WITHOUT PRIORITY LOOK UP");
        
        ArrayList<Integer> temp = new ArrayList<Integer>();
        
        //SCAN LINE MOVING THROUGH X STRUCTURE
        for(Date curr : xStructure){
            
            ArrayList<RezervaceMistnosti> interferingTemp = getAllInterferingWithDate(curr, interferingReservations);
            
            temp.add(getSumOfReservedSpace(interferingTemp));
            
        }
        
        vypisList(temp);
        
        return temp;
    }
        
    private ArrayList<RezervaceMistnosti> getAllInterferingWithDate(Date date, ArrayList<RezervaceMistnosti> interferingReservations){
        
        ArrayList<RezervaceMistnosti> interferingWithDate = new ArrayList<RezervaceMistnosti>();
        
        for(RezervaceMistnosti rez : interferingReservations){
            
            if(isInterfering(rez, date)){
                interferingWithDate.add(rez);
            }
            
        }
        return interferingWithDate;
    }
    private int getSumOfReservedSpaceWithPriorityLookUp(ArrayList<RezervaceMistnosti> interferingReservations){
        
        ArrayList<RezervaceMistnosti> interfering = new ArrayList<RezervaceMistnosti>();
        
        for(RezervaceMistnosti rez : interferingReservations){
            if(isHigherPriorityThanLogged(rez)){
                interfering.add(rez);
            }
        }
        return getSumOfReservedSpace(interfering);
    }
    private int getSumOfReservedSpace(ArrayList<RezervaceMistnosti> interferingReservations){
        
        int sum = 0;
        
        for(RezervaceMistnosti rez : interferingReservations){
            sum = sum + rez.getPocetRezervovanychMist();
        }
        return sum;
    }
    
    private int findMaximumReserved() {
        
        int max = 0;
        for(int sumCurr : yStructure){
            if(sumCurr > max){
                    max = sumCurr;
            }
        }
        System.out.println("MAXIMUM RESERVED: "+max);
        return max;
    }


    private ArrayList<RezervaceMistnosti> getOverwrittenOnIndex(RezervaceMistnosti curr, int index,  ArrayList<RezervaceMistnosti> interferingReservations){
        
        ArrayList<RezervaceMistnosti> interRezWithDate = getAllInterferingWithDate(xStructure.get(index),interferingReservations);
        
        ArrayList<RezervaceMistnosti> overWrittenOnIndex = new ArrayList<RezervaceMistnosti>();
        
        int sum = yStructure.get(index);
        
        while(sum > (curr.getIDmistnosti().getKapacita() - curr.getPocetRezervovanychMist())){
            RezervaceMistnosti max = findBiggestReservation(interRezWithDate);
            
            if(isHigherPriorityThanLogged(max)){
                interRezWithDate.remove(max);
            }
            else{
                
                interRezWithDate.remove(max);
                overwrittenRes.add(max);
                overWrittenOnIndex.add(max);
                sum = sum - max.getPocetRezervovanychMist();
            }
            
        }
        
        return overWrittenOnIndex;
    }
    
    private RezervaceMistnosti findBiggestReservation(ArrayList<RezervaceMistnosti> reservations){
        
        RezervaceMistnosti max = reservations.get(0);
        
        for(RezervaceMistnosti rez : reservations){
            if(rez.getPocetRezervovanychMist() > max.getPocetRezervovanychMist()){
                max = rez;
            }
        }
        
        return max;
    }
    
    private boolean isInterfering(RezervaceMistnosti rez , Date date){
        return !((date.after(rez.getDo1())) || (date.before(rez.getOd())));
    }
    public boolean isHigherPriorityThanLogged(RezervaceMistnosti rez){
        System.out.println("");
        return ((uzivOper.getUzivatelRolePriority(rez.getIDuser())) >= (uzivOper.getUzivatelRolePriority(session.getLoggedUzivatel())));
    }

    private ArrayList<RezervaceMistnosti> removeAllFromList(ArrayList<RezervaceMistnosti> memory, ArrayList<RezervaceMistnosti> toBeDeleted) {
        
        ArrayList<RezervaceMistnosti> memoryCache = new ArrayList<RezervaceMistnosti>(memory);
        
        for(RezervaceMistnosti rez : memory){
            if(toBeDeleted.contains(rez)){
                memoryCache.remove(rez);
            }
        }
        
        return memoryCache;
    }

    private void vypisList(ArrayList xTemp) {
        for(Object o : xTemp){
            System.out.print(o.toString()+" || ");
        }
        System.out.println("");
    }

    public void RESET_VALUES(){
        overwrittenRes = new ArrayList<RezervaceMistnosti>();
    }
}
