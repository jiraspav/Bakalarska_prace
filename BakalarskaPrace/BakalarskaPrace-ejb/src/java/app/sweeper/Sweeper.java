/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app.sweeper;

import app.baseDataOperators.UzivatelOperator;
import app.facade.roomFinder.RoomFinderFacade;
import dbEntity.RezervaceMistnosti;
import dbEntity.Uzivatel;
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
    
    
    /**
     * 
     */
    public Sweeper() {
        overwrittenRes = new ArrayList<RezervaceMistnosti>();
    }
    
    /**
     * Metoda pro zjištění rezervací, které je potřeba přepsat při přidání nové rezervace
     * @param curr nová rezervace
     * @param interferingRes všechny rezervací, které interferují s novou rezervací
     * @param logged přihlášený uživatel
     * @return list rezervací, které je potřeba přepsat
     */
    public ArrayList<RezervaceMistnosti> getOverwrittenReservations(RezervaceMistnosti curr, ArrayList<RezervaceMistnosti> interferingRes, Uzivatel logged){
        //System.out.println("Number of interfering reservations: "+interferingRes.size());
        ArrayList<RezervaceMistnosti> interferingReservations = new ArrayList<RezervaceMistnosti>(interferingRes);
        buildStructuresWithoutPriorityLookUp(interferingReservations);
        
        //System.out.println("X-structure size : "+xStructure.size());
        //System.out.println("Y-structure size : "+yStructure.size());
        
        boolean deletingRes = false;
        
        for (int i = 0; i < yStructure.size(); i++){
            Integer sum = yStructure.get(i);
            //System.out.println("Sum on index "+i+" is "+sum);
            //POKUD JE POCET REZERVOVANYCH MIST VETSI NEZ JE POTREBA PRO REZERVACI 
            if(sum > (curr.getIDmistnosti().getKapacita() - curr.getPocetRezervovanychMist())){
                
                ArrayList<RezervaceMistnosti> overWrittenOnIndex = setOverwrittenOnIndex(curr,i,interferingReservations,logged);
                
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
            return getOverwrittenReservations(curr,interferingReservations,logged);
        }
        else{
            return overwrittenRes;
        }
        
    }
    
    /**
     * Metoda pr ozískání maximálního počtu rezervovaných míst ze seznamu rezervací
     * 
     * @param interferingReservations list rezervací ve kterém se zjišťuje maximum rezervovaných míst
     * @param logged přihlášený uživatel
     * @return vrací maximální počet rezervovaných místností
     */
    public int getMaximumReserved(ArrayList<RezervaceMistnosti> interferingReservations, Uzivatel logged){
        
        buildStructures(interferingReservations,logged);
        
        return findMaximumReserved();
        
    }
    
    private void buildStructures(ArrayList<RezervaceMistnosti> interferingReservations, Uzivatel logged){
        xStructure = buildXstructure(interferingReservations);
        yStructure = buildYstructure(interferingReservations,logged);
    }
    private void buildStructuresWithoutPriorityLookUp(ArrayList<RezervaceMistnosti> interferingReservations){
        xStructure = buildXstructure(interferingReservations);
        yStructure = buildYstructureWithoutPriorityLookUp(interferingReservations);
    }
    
    private ArrayList<Date> buildXstructure(ArrayList<RezervaceMistnosti> interferingReservations) {
        
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
        
        //vypisList(xTemp);
        
        return xTemp;
    }

    private ArrayList<Integer> buildYstructure(ArrayList<RezervaceMistnosti> interferingReservations, Uzivatel logged) {
        
        ArrayList<Integer> temp = new ArrayList<Integer>();
        
        //SCAN LINE MOVING THROUGH X STRUCTURE
        for(Date curr : xStructure){
            
            ArrayList<RezervaceMistnosti> interferingTemp = getAllInterferingWithDate(curr, interferingReservations);
            
            temp.add(getSumOfReservedSpaceWithPriorityLookUp(interferingTemp,logged));
            
        }
        //vypisList(temp);
        
        return temp;
    }
    
    private ArrayList<Integer> buildYstructureWithoutPriorityLookUp(ArrayList<RezervaceMistnosti> interferingReservations) {
        
        
        ArrayList<Integer> temp = new ArrayList<Integer>();
        
        //SCAN LINE MOVING THROUGH X STRUCTURE
        for(Date curr : xStructure){
            
            
            
            ArrayList<RezervaceMistnosti> interferingTemp = getAllInterferingWithDate(curr, interferingReservations);
            
            temp.add(getSumOfReservedSpace(interferingTemp));
            
        }
        
        //vypisList(temp);
        
        return temp;
    }
    /*
     *  Vrací všechny rezervace, které se překrývají s date v parametru.
     * 
     */    
    private ArrayList<RezervaceMistnosti> getAllInterferingWithDate(Date date, ArrayList<RezervaceMistnosti> interferingReservations){
        
        ArrayList<RezervaceMistnosti> interferingWithDate = new ArrayList<RezervaceMistnosti>();
        
        for(RezervaceMistnosti rez : interferingReservations){
            
            if(isInterfering(rez, date)){
                interferingWithDate.add(rez);
            }
            
        }
        //System.out.println("Number of interfering with date "+date+" is "+interferingWithDate.size());
        
        return interferingWithDate;
    }
    //sečte všechny rezervace s vyšší nebo stejnou prioritou jako logged
    private int getSumOfReservedSpaceWithPriorityLookUp(ArrayList<RezervaceMistnosti> interferingReservations, Uzivatel logged){
        
        ArrayList<RezervaceMistnosti> interfering = new ArrayList<RezervaceMistnosti>();
        
        for(RezervaceMistnosti rez : interferingReservations){
            if(isHigherPriorityThanLogged(rez,logged)){
                //System.out.println("ADDING "+rez.getPocetRezervovanychMist());
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
        //System.out.println("Maximum reserved : "+max);
        return max;
    }


    private ArrayList<RezervaceMistnosti> setOverwrittenOnIndex(RezervaceMistnosti curr, int index,  ArrayList<RezervaceMistnosti> interferingReservations, Uzivatel logged){
        
        ArrayList<RezervaceMistnosti> interRezWithDate = getAllInterferingWithDate(xStructure.get(index),interferingReservations);
        
        ArrayList<RezervaceMistnosti> overWrittenOnIndex = new ArrayList<RezervaceMistnosti>();
        
        int sum = yStructure.get(index);
        
        if(sum > (curr.getIDmistnosti().getKapacita() - curr.getPocetRezervovanychMist())){
            
            overWrittenOnIndex = getOverwrittenOnIndex(curr,interRezWithDate,sum, logged);
            
            for(RezervaceMistnosti rez : overWrittenOnIndex){
                overwrittenRes.add(rez);
            }
            
        }
        
        return overWrittenOnIndex;
    }
    //VRACI REZERVACE KTERE MAJI BYT PREPSANY NA INDEXU
    private ArrayList<RezervaceMistnosti> getOverwrittenOnIndex(RezervaceMistnosti curr, ArrayList<RezervaceMistnosti> reservations, int sum, Uzivatel logged){
        //najit nejblizsi vetsi resp. mensi
        
        int potrebaUvolnit = Math.abs((curr.getIDmistnosti().getKapacita() - sum) - curr.getPocetRezervovanychMist());
        
        //System.out.println("POTREBA UVOLNIT "+potrebaUvolnit);
        
        RezervaceMistnosti closestBigger = null;
        
        Integer closestBiggerDistance = null;
            
        //NAJDU NEJBLIZSI VETSI REZERVACI
        for(RezervaceMistnosti rez : reservations){
            
            int distance = Math.abs(curr.getPocetRezervovanychMist() - rez.getPocetRezervovanychMist());
            
            if(rez.getPocetRezervovanychMist() >= curr.getPocetRezervovanychMist()){
                
                if(closestBiggerDistance == null){
                
                    closestBiggerDistance = distance;
                    closestBigger = rez;
                
                }
                else if(distance < closestBiggerDistance){
                    
                    closestBiggerDistance = distance;
                    closestBigger = rez;
                    
                }
            }
            //System.out.println("SET CLOSEST BIGGER REZ "+closestBigger.getStatus());
        }
        
        //KOLIK BUDE VOLNYCH MIST PRI UVOLNENI NEJBLIZSI VYSSI REZERVACE
        int volnychMist = curr.getIDmistnosti().getKapacita() - (sum - closestBigger.getPocetRezervovanychMist() + curr.getPocetRezervovanychMist());
        
        //VEZMI VSECHNY MENSI REZERVACE S NIZSI PRIORITOU NEZ CURR
        ArrayList<RezervaceMistnosti> mensiRezervace = new ArrayList<RezervaceMistnosti>();
        for(RezervaceMistnosti rez : reservations){
            
            if(rez.getPocetRezervovanychMist() < curr.getPocetRezervovanychMist()){
                if(!isHigherPriorityThanLogged(rez,logged)){
                    mensiRezervace.add(rez);
                }
            }
        }
        
        //A ZJISTI JESTLI MENSI REZERVACE STACI NA UVOLNENI MISTA PRO NOVOU REZERVACI
        int soucetVsechMensich = 0;
        for(RezervaceMistnosti rez : mensiRezervace){
            soucetVsechMensich = soucetVsechMensich + rez.getPocetRezervovanychMist();
            //System.out.println("SOUCET VSECH MENSICH "+soucetVsechMensich);
        }
        
        if(soucetVsechMensich >= potrebaUvolnit){
            //KDYZ MENSI REZERVACE SKUTECNE STACI NA UVOLNENI MISTA
            
            //PROJIT VSECHNY MOZNE KOMBINACE MENSICH REZERVACI A VYBRAT TAKOVOU
            //JEJIZ SOUCET BUDE NEJBLIZSI VYSSI HODNOTA K POTREBNEMU MISTU V MISTNOSTI
            //PRO TUTO KOMBINACI ZJISTIT ZDA JE LEPSI NEZ NEJBLIZSI VYSSI REZERVACE
            //(POKUD SE ODSTRANI, ZUSTANE MENE VOLNYCH MIST V MISTNOSTI NEZ PRI ODSTRANENI JEN VYSSI REZERVACE)
            
            
            Integer closestBiggest = null;
            Integer numberOfValues = null;
            String biggestBinary = null;
            
            ArrayList<RezervaceMistnosti> closestBiggerComb = new ArrayList<RezervaceMistnosti>();
            ArrayList<RezervaceMistnosti> currComb = new ArrayList<RezervaceMistnosti>();
            
            for(int i = 0;  i < Math.pow(2, mensiRezervace.size()); i++){
                String binary = Integer.toBinaryString(i);

                int pocetNul = mensiRezervace.size() - binary.length();

                for(int a = pocetNul; a > 0; a--){
                    binary = "0".concat(binary);
                }

                char binaryChar[] = binary.toCharArray();
                int sumOfComb = 0;
                int counterOfOnes = 0;
                for(int a = 0; a < binaryChar.length; a++){

                    if(binaryChar[a] == '1'){
                        currComb.add(mensiRezervace.get(a));
                        sumOfComb = sumOfComb + mensiRezervace.get(a).getPocetRezervovanychMist();
                        counterOfOnes ++;
                    }

                }
                //System.out.println(sumOfComb);

                if(sumOfComb >= potrebaUvolnit){

                    if(closestBiggest == null){
                        closestBiggerComb = currComb;
                        closestBiggest = sumOfComb;
                        numberOfValues = counterOfOnes;
                        biggestBinary = binary;
                    }
                    else if(sumOfComb < closestBiggest){
                        closestBiggerComb = currComb;
                        closestBiggest = sumOfComb;
                        numberOfValues = counterOfOnes;
                        biggestBinary = binary;
                    }
                    else if(numberOfValues > counterOfOnes){
                        closestBiggerComb = currComb;
                        closestBiggest = sumOfComb;
                        numberOfValues = counterOfOnes;
                        biggestBinary = binary;
                    }
                }
                currComb = new ArrayList<RezervaceMistnosti>();
            }
            //System.out.println("CLOSEST BIGGER "+closestBiggest);
            //System.out.println("BINARY "+biggestBinary);
            
            //POCET VOLNYCH MIST V MISTNOSTI PRI UVLONENI TETO KOMBINACE REZERVACI
            int volnychMistComb = curr.getIDmistnosti().getKapacita() - (sum - closestBiggest + curr.getPocetRezervovanychMist());
            
            if(volnychMistComb < volnychMist){
                return closestBiggerComb;
            }
            else{
                ArrayList<RezervaceMistnosti> one = new ArrayList<RezervaceMistnosti>();
                one.add(closestBigger);
                return one;
            }
            
            
        }
        else{
            ArrayList<RezervaceMistnosti> one = new ArrayList<RezervaceMistnosti>();
            one.add(closestBigger);
            return one;
        }
    }
    
    private boolean isInterfering(RezervaceMistnosti rez , Date date){
        
        //System.out.println("Is interfering rezervace "+rez.getIDrezervace()+" date "+rez.getDatumRezervace()+rez.getOd()+rez.getDo1()+" with "+date+" ? "+!((date.after(rez.getDo1())) || (date.before(rez.getOd()))));
        
        return !((date.after(rez.getDo1())) || (date.before(rez.getOd())));
    }
    /**
     * Metoda pro porovnávání priority rezervace a přihlášeného uživatele
     * 
     * @param rez rezervace pro porovnávání priority
     * @param logged přihlášený uživatel pro porovnávání priority
     * @return true - pokud priorita rezervace je vyšší než priorita uživatele
     */
    public boolean isHigherPriorityThanLogged(RezervaceMistnosti rez, Uzivatel logged){
        
        return ((uzivOper.getUzivatelRolePriority(rez.getIDuser())) >= (uzivOper.getUzivatelRolePriority(logged)));
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

    /**
     * Metoda pro nastavení defaultních hodnot proměnným
     */
    public void RESET_VALUES(){
        overwrittenRes = new ArrayList<RezervaceMistnosti>();
    }

    /**
     * @param uzivOper the uzivOper to set
     */
    public void setUzivOper(UzivatelOperator uzivOper) {
        this.uzivOper = uzivOper;
    }
}
