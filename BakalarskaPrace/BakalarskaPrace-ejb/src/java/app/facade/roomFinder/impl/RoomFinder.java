/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app.facade.roomFinder.impl;

import app.baseDataOperators.*;
import app.facade.roomFinder.RoomFinderFacade;
import app.sessionHolder.SessionHolderEJB;
import app.sweeper.Sweeper;
import dbEntity.DenVTydnu;
import dbEntity.Mistnost;
import dbEntity.RezervaceMistnosti;
import dbEntity.Rozvrhy;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 *
 * @author Pavel
 */
@Stateless
@Local(RoomFinderFacade.class)
public class RoomFinder implements RoomFinderFacade{

    @Inject Sweeper sweeper;
    @Inject MistnostOperator mistOper;
    @Inject RezervaceMistnostiOperator rezOper;
    @Inject UzivatelOperator uzivOper;
    @Inject SessionHolderEJB session;
    @Inject RozvrhyOperator rozOper;
    @Inject DnyVTydnuOperator denOper;
    
    //VRACI VOLNE MISTNOSTI PRO VSECHNY ZADANE REZERVACE
    @Override
    public List<Mistnost> getAllFreeRooms(List<RezervaceMistnosti> preparedRezervations) {
        
        List<Mistnost> volneMistnostiCache = mistOper.getAll();
        
        //prochazim vsechny rezervace pripravene pro zapis do db
        for(RezervaceMistnosti curr: preparedRezervations){
            //pro kazdou rezervaci najdu volne mistnosti
            List<Mistnost> freeRooms = getAllFreeRoomsOnOneReservation(curr);
            
            //a ty mistnosti, ktere se nachazeji v cache i v volnych mistnostech necham v cache, ostatni smazu
            List<Mistnost> temp = new ArrayList<Mistnost>();
            for(Mistnost mist : freeRooms){
                
                if(volneMistnostiCache.contains(mist)){
                    temp.add(mist);
                }
                else{
                    System.out.println("Mistnost NOT free: "+mist.getZkratka());
                }
            }
            volneMistnostiCache = temp;
            
        }
        
        return volneMistnostiCache;
    }
    
    //VRACI VOLNE MISTNOSTI PRO JEDNU ZADANOU REZERVACI
    public List<Mistnost> getAllFreeRoomsOnOneReservation(RezervaceMistnosti curr) {
        
        Date day = curr.getDatumRezervace();
        Date odCas = curr.getOd();
        Date doCas = curr.getDo1();
        
        ArrayList<Mistnost> mistnosti = new ArrayList<Mistnost>(mistOper.getAll());
        

        SimpleDateFormat sdf = new SimpleDateFormat("EEEE",Locale.ENGLISH);

        System.out.println("DEN REZERVACE "+sdf.format(day));
        DenVTydnu denRezervace = denOper.getENDen(sdf.format(day));
        
        
        ArrayList<Mistnost> mistnostiCache = new ArrayList<Mistnost> (mistnosti);
                
        
        for(Mistnost mist : mistnosti){
            
            ArrayList<Rozvrhy> rozvrhyMistnosti = new ArrayList(rozOper.getRozvrhy(mist, denRezervace));
            //System.out.println("Pocet rozvrhu pro "+mist.getZkratka()+" je "+rozvrhyMistnosti.size());
            //checks all schedules for this room

            for(Rozvrhy roz : rozvrhyMistnosti){
                
                
                if(isInterfering(roz, odCas, doCas)){
                    System.out.println("INTERFERING Rozvrh na "+roz.getIDdnu().getNazev()+" od "+roz.getOd()+" do "+roz.getDo1()+" interfering? "+isInterfering(roz, odCas, doCas));
                    mistnostiCache.remove(mist);
                }
            }
        }
        
        
        mistnosti = new ArrayList<Mistnost>(mistnostiCache);
        
        
        
        //Check all reservations which interfere with mine and nejsou v mistnosti s rozvrhem, ktery by prekryval moji, remove occupied rooms
        for(Mistnost mist : mistnosti){
            
            if(!isEnoughSpace(curr, mist)){
                //System.out.println("Not enough space "+mist.getZkratka());
                mistnostiCache.remove(mist);
            }
        }
        

        
        return mistnostiCache ;
    }

    
    //JE TO NA SPRAVNEM MISTE???
    
    
    
    @Override
    public boolean isInterfering(RezervaceMistnosti rez, List<RezervaceMistnosti> preparedReservations){
        
        for(RezervaceMistnosti curr: preparedReservations){
            if(isInterfering(curr, rez)){
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean isInterfering(RezervaceMistnosti curr , RezervaceMistnosti rez){
        if(curr.getDatumRezervace().equals(rez.getDatumRezervace())){
            return !((rez.getOd().after(curr.getDo1())) || (rez.getDo1().before(curr.getOd())));
        }
        else{
            return false;
        }
    }
    
    private boolean isInterfering(Rozvrhy rez , Date odCas, Date doCas){
        return !((odCas.after(rez.getDo1())) || (doCas.before(rez.getOd())));
    }
    
    private boolean isInterfering(RezervaceMistnosti rez , Date odCas, Date doCas){
        return !((odCas.after(rez.getDo1())) || (doCas.before(rez.getOd())));
    }
    

    
    @Override
    public boolean isEnoughSpace(RezervaceMistnosti rez, Mistnost mist){
        
        //System.out.println("Rezervace mist "+rez.getPocetRezervovanychMist()+" Mistnost mist "+mist.getKapacita());
        boolean response = true;
        if(mist.getKapacita() < rez.getPocetRezervovanychMist()){
            response = false;
        }
        else if(rez.getPocetRezervovanychMist() > (mist.getKapacita() - getPocetRezervovanychMist(rez,mist))){
            response = false;
        }
        //System.out.println("Response: "+response);
        return response;
        
    }
    
    private int getPocetRezervovanychMist(RezervaceMistnosti rez, Mistnost mist) {
        //VSECHNY REZERVACE NA TUTO MISTNOSTI + NA DATUM TETO REZERVACE
        //VSECHNY REZERVACE PROJIT A NAJIT TY CO SE MLATI S REZ
        //Z NICH NAJIT MINIMUM Z (kapacita mistnosti - rezervace.pocetMist) <- SPATNE JE NUTNE POUZIT ZAMETACI METODU
        //KTERA NAJDE MISTA S NEJMENSI MOZNOU KAPACITOU V ZAVISLOSTI NA PRIORITACH REZERVACI
        List<RezervaceMistnosti> rezervace = rezOper.getRezervace(rez.getDatumRezervace(), mist);
        
        ArrayList<RezervaceMistnosti> interferingReservations = new ArrayList<RezervaceMistnosti>();
        
        for(RezervaceMistnosti curr: rezervace){
            if(isInterfering(curr, rez)){
                interferingReservations.add(curr);
            }
        }
        

        return sweeper.getMaximumReserved(interferingReservations);
    }
}
