/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app.facade.reservationEditor.impl;

import app.baseDataOperators.DnyVTydnuOperator;
import app.baseDataOperators.MistnostOperator;
import app.baseDataOperators.RezervaceMistnostiOperator;
import app.baseDataOperators.RozvrhyOperator;
import app.baseDataOperators.UzivatelOperator;
import app.facade.reservationEditor.ReservationEditorFacade;
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
@Local(ReservationEditorFacade.class)
public class ReservationEditor implements ReservationEditorFacade{

    private @Inject RezervaceMistnostiOperator rezOper;
    private @Inject RozvrhyOperator rozOper;
    private @Inject DnyVTydnuOperator denOper;
    private @Inject UzivatelOperator uzivOper;
    private @Inject MistnostOperator mistOper;
    private @Inject RoomFinderFacade roomFac;
    private @Inject Sweeper sweeper;
    
    /**
     * Metoda pro uložení rezervací v Listu. Je nutné zkontrolovat ostatní rezervace v místnosti
     * případně je nutné vybrat rezervace, které je nutné přepsat.
     * 
     * @param mistnost - mstnost na kterou jsou rezervace ukládány
     * @param preparedReservations - List ukládaných rezervací
     * @param logged - uživatel, který tyto rezervace vytváří
     */
    @Override
    public void createAllReservations(Mistnost mistnost, List<RezervaceMistnosti> preparedReservations, Uzivatel logged){
        //PRO KAZDOU PREPARED NAJIT REZERVACE NA STEJNY DEN A MISTNOST
        //NAJIT TY KTERE SE PREKRYVAJI
        //U TECH KAM SE PREPARED NEVEJDOU A ZAROVEN JE PREPARED VYSSI PRIORITA NASTAVIT STATUS OVERWRITEN
        for(RezervaceMistnosti rez : preparedReservations){
            rez.setIDmistnosti(mistnost);
            
            ArrayList<RezervaceMistnosti> interfering = new ArrayList<RezervaceMistnosti>();
            List<RezervaceMistnosti> rezervace = rezOper.getRezervace(rez.getDatumRezervace(), mistnost);
            
            for(RezervaceMistnosti temp : rezervace){
                //System.out.println("RESERVATION: mistnost - "+temp.getIDmistnosti().getZkratka()+" datum - "+temp.getDatumRezervace()+" od - "+temp.getOd()+" do - "+temp.getDo1()+" pocet - "+temp.getPocetRezervovanychMist()+" popis - "+temp.getPopis());
                if(temp.getStatus().equals("OVERWRITTEN")){
                    System.out.println("ALERT ALERT STATUS BREACH!!!");
                }
                if(roomFac.isInterfering(rez, temp)){
                    interfering.add(temp);
                }
            }
            
            ArrayList<RezervaceMistnosti> overwritten = sweeper.getOverwrittenReservations(rez, interfering, logged);
            //System.out.println("Number of overwritten:"+overwritten.size());
            //RESERVATION STATUS SWITCH HANDLER
            for(RezervaceMistnosti temp : overwritten){
                //System.out.println("prepis");
                rezOper.changeStatusToOverWritten(temp);
            }
            
            
            
            this.createReservation(mistnost, rez.getDatumRezervace(), rez.getOd(), rez.getDo1(), rez.getPocetRezervovanychMist(), rez.getPopis(), logged);
            sweeper.RESET_VALUES();
        }
    }

    private ArrayList<RezervaceMistnosti> getLowerPriorityReservations(ArrayList<RezervaceMistnosti> reservations, Uzivatel logged){
        
        ArrayList<RezervaceMistnosti> reservationsCache = new ArrayList<RezervaceMistnosti>(reservations);
        
        for(RezervaceMistnosti res: reservations){
            if(sweeper.isHigherPriorityThanLogged(res,logged)){
                reservationsCache.remove(res);
            }
        }
        return reservationsCache;
    }
    /**
     * Metoda pro uložení jedné rezervace.
     * 
     * @param mistnost místnost na kterou je tato rezervace
     * @param startDate datum rezervace
     * @param casOd čas začátku rezervace
     * @param casDo čas konce rezervace
     * @param pocetMist počet rezervovaných míst
     * @param popis popis rezervace
     * @param logged uživatel vytvářející tuto rezervaci
     */
    @Override
    public void createReservation(Mistnost mistnost, Date startDate, Date casOd, Date casDo, int pocetMist , String popis, Uzivatel logged) {
        rezOper.createRezervaceMistnosti(logged, mistnost, startDate, casOd, casDo, pocetMist, popis);
    }
        
    
    /**
     * Metoda pro mazání rezervace z databáze
     * 
     * @param selectedRow - označená rezervace určená pro odstranění
     */
    @Override
    public void deleteReservation(RezervaceMistnosti selectedRow) {
        rezOper.delete(selectedRow);
    }

    /**
     * Metoda pro získání rezervací uživatele
     * @param logged
     * @return List rezervací jejichž vlastníkem je uživatel v parametru
     */
    @Override
    public List<RezervaceMistnosti> getReservationsOfLoggedUser(Uzivatel logged) {
        
        return rezOper.getRezervace(logged);
    }

    /**
     * @param rezOper the rezOper to set
     */
    public void setRezOper(RezervaceMistnostiOperator rezOper) {
        this.rezOper = rezOper;
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

    /**
     * @param uzivOper the uzivOper to set
     */
    public void setUzivOper(UzivatelOperator uzivOper) {
        this.uzivOper = uzivOper;
    }

    /**
     * @param mistOper the mistOper to set
     */
    public void setMistOper(MistnostOperator mistOper) {
        this.mistOper = mistOper;
    }

    /**
     * @param roomFac the roomFac to set
     */
    public void setRoomFac(RoomFinderFacade roomFac) {
        this.roomFac = roomFac;
    }

    /**
     * @param sweeper the sweeper to set
     */
    public void setSweeper(Sweeper sweeper) {
        this.sweeper = sweeper;
    }
}
