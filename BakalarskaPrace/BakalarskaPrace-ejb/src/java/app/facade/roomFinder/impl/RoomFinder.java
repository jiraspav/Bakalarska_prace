/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app.facade.roomFinder.impl;

import app.baseDataOperators.*;
import app.facade.roomFinder.RoomFinderFacade;
import app.sweeper.Sweeper;
import dbEntity.*;
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

    @Inject private Sweeper sweeper;
    @Inject private MistnostOperator mistOper;
    @Inject private RezervaceMistnostiOperator rezOper;
    @Inject private UzivatelOperator uzivOper;
    @Inject private RozvrhyOperator rozOper;
    @Inject private DnyVTydnuOperator denOper;
    
    
    /**
     * Metoda pro získání všech volných místností pro přípravené rezervace
     * 
     * @param preparedReservations - rezervace připravené pro uložení do databáze
     * @param logged - vlastník rezervaci
     * @return List volných místností
     */
    @Override
    public List<Mistnost> getAllFreeRooms(List<RezervaceMistnosti> preparedRezervations, Uzivatel logged) {
        
        //System.out.println("NUMBER OF REZERVATIONS:"+preparedRezervations.size());
        
        List<Mistnost> volneMistnostiCache = mistOper.getAll();
        
        //prochazim vsechny rezervace pripravene pro zapis do db
        for(RezervaceMistnosti curr: preparedRezervations){
            //pro kazdou rezervaci najdu volne mistnosti
            List<Mistnost> freeRooms = getAllFreeRoomsOnOneReservation(curr,logged);
            //System.out.println("POCET VOLNYCH : "+freeRooms.size());
            //a ty mistnosti, ktere se nachazeji v cache i v volnych mistnostech necham v cache, ostatni smazu
            List<Mistnost> temp = new ArrayList<Mistnost>();
            for(Mistnost mist : freeRooms){
                
                if(volneMistnostiCache.contains(mist)){
                    temp.add(mist);
                }
                /*else{
                    System.out.println("Mistnost NOT free: "+mist.getZkratka());
                }*/
            }
            volneMistnostiCache = temp;
            
        }
        
        return volneMistnostiCache;
    }
    
    //VRACI VOLNE MISTNOSTI PRO JEDNU ZADANOU REZERVACI
    /**
     * 
     * @param curr
     * @param logged
     * @return
     */
    public List<Mistnost> getAllFreeRoomsOnOneReservation(RezervaceMistnosti curr, Uzivatel logged) {
        
        Date day = curr.getDatumRezervace();
        Date odCas = curr.getOd();
        Date doCas = curr.getDo1();
        
        ArrayList<Mistnost> mistnosti = new ArrayList<Mistnost>(mistOper.getAll());
        
        //System.out.println("MISTNOSTI:"+mistnosti.size());
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE",Locale.ENGLISH);

        DenVTydnu denRezervace = denOper.getENDen(sdf.format(day));
        
        
        ArrayList<Mistnost> mistnostiCache = new ArrayList<Mistnost> (mistnosti);
                
        
        for(Mistnost mist : mistnosti){
            
            ArrayList<Rozvrhy> rozvrhyMistnosti = new ArrayList(rozOper.getRozvrhy(mist, denRezervace));
            //System.out.println("Pocet rozvrhu pro "+mist.getZkratka()+" je "+rozvrhyMistnosti.size());
            //checks all schedules for this room

            for(Rozvrhy roz : rozvrhyMistnosti){
                
                
                if(isInterfering(roz, odCas, doCas)){
                    
                    mistnostiCache.remove(mist);
                }
            }
        }
        
        
        mistnosti = new ArrayList<Mistnost>(mistnostiCache);
        
        //Check all reservations which interfere with mine and nejsou v mistnosti s rozvrhem, ktery by prekryval moji, remove occupied rooms
        for(Mistnost mist : mistnosti){
            
            if(!isEnoughSpace(curr, mist, logged)){
                //System.out.println("Not enough space "+mist.getZkratka());
                mistnostiCache.remove(mist);
            }
        }
        
        return mistnostiCache ;
    }

    
    /**
     * Metoda pro zjištění zda rezervcae překrývá některý z rozvrhů
     * @param rez
     * @param rozvrhy
     * @return true - pokud překrývá
     */
    @Override
    public boolean isInterfering(RezervaceMistnosti rez, ArrayList<Rozvrhy> rozvrhy){
        
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        
        for(Rozvrhy curr: rozvrhy){
            
            if(isInterfering(curr, rez.getOd(),rez.getDo1()) && sdf.format(rez.getDatumRezervace()).equals(curr.getIDdnu().getNazev())){
                return true;
            }
        }
        return false;
    }
    
    
    /**
     * Metoda pro zjištění rezervací, které novou překrývají
     * @param rez nová rezervace
     * @param preparedReservations list rezervací pro které zjišťuji zda je nová překrývá
     * @return true - pokud překrývá
     */
    @Override
    public boolean isInterfering(RezervaceMistnosti rez, List<RezervaceMistnosti> preparedReservations){
        
        for(RezervaceMistnosti curr: preparedReservations){
            if(isInterfering(curr, rez)){
                return true;
            }
        }
        return false;
    }
    
    /**
     * Metoda pro zjištění zda se dvě rezervace překrývají
     * @param rez
     * @param rez2
     * @return true - pokud se překrývají
     */
    @Override
    public boolean isInterfering(RezervaceMistnosti curr , RezervaceMistnosti rez){
        if(curr.getDatumRezervace().equals(rez.getDatumRezervace())){
            return !((rez.getOd().after(curr.getDo1())) || (rez.getDo1().before(curr.getOd())));
        }
        else{
            return false;
        }
    }
    
    private boolean isInterfering(Rozvrhy roz1, Rozvrhy roz2){
        if(roz1.getIDmistnosti().equals(roz2.getIDmistnosti()) && roz1.getIDdnu().equals(roz2.getIDdnu())){
            return isInterfering(roz1, roz2.getOd(), roz2.getDo1());
        }else{
            return false;
        }
    }
    
    
    
    private boolean isInterfering(Rozvrhy rez , Date odCas, Date doCas){
        return !((odCas.after(rez.getDo1())) || (doCas.before(rez.getOd())));
    }
    
    private boolean isInterfering(RezervaceMistnosti rez , Date odCas, Date doCas){
        return !((odCas.after(rez.getDo1())) || (doCas.before(rez.getOd())));
    }
    

    
    /**
     * Metoda zjišťující zda je v místnosti dostatek místa pro vložení nové rezervace.
     * 
     * @param rez vkládaná rezervace
     * @param mist místnost do které je rezervace vkládána
     * @param logged vlastník rezervace
     * @return - true - pokud je dostatek místa v místnosti
     *          <p>
     *         - false - pokud není dostatek místa
     */
    @Override
    public boolean isEnoughSpace(RezervaceMistnosti rez, Mistnost mist, Uzivatel logged){
        
        //System.out.println("Rezervace mist "+rez.getPocetRezervovanychMist()+" Mistnost mist "+mist.getKapacita());
        boolean response = true;
        if(mist.getKapacita() < rez.getPocetRezervovanychMist()){
            response = false;
        }
        else if(rez.getPocetRezervovanychMist() > (mist.getKapacita() - getPocetRezervovanychMist(rez,mist,logged))){
            response = false;
        }
        //System.out.println("Response: "+response);
        
        return response;
        
    }
    
    private int getPocetRezervovanychMist(RezervaceMistnosti rez, Mistnost mist, Uzivatel logged) {
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
        

        return sweeper.getMaximumReserved(interferingReservations,logged);
    }

    /**
     * @param sweeper the sweeper to set
     */
    public void setSweeper(Sweeper sweeper) {
        this.sweeper = sweeper;
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
     * @param denOper the denOper to set
     */
    public void setDenOper(DnyVTydnuOperator denOper) {
        this.denOper = denOper;
    }
}
