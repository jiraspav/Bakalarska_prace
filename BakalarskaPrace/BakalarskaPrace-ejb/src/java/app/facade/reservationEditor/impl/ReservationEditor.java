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
import app.sessionHolder.SessionHolderEJB;
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

    private @Inject SessionHolderEJB session;
    private @Inject RezervaceMistnostiOperator rezOper;
    private @Inject RozvrhyOperator rozOper;
    private @Inject DnyVTydnuOperator denOper;
    private @Inject UzivatelOperator uzivOper;
    private @Inject MistnostOperator mistOper;
    private @Inject RoomFinderFacade roomFac;
    private @Inject Sweeper sweeper;
    
    @Override
    public void createAllReservations(Mistnost mistnost, List<RezervaceMistnosti> preparedReservations){
        //PRO KAZDOU PREPARED NAJIT REZERVACE NA STEJNY DEN A MISTNOST
        //NAJIT TY KTERE SE PREKRYVAJI
        //U TECH KAM SE PREPARED NEVEJDOU A ZAROVEN JE PREPARED VYSSI PRIORITA NASTAVIT STATUS OVERWRITEN
        for(RezervaceMistnosti rez : preparedReservations){
            rez.setIDmistnosti(mistnost);
            
            ArrayList<RezervaceMistnosti> interfering = new ArrayList<RezervaceMistnosti>();
            List<RezervaceMistnosti> rezervace = rezOper.getRezervace(rez.getDatumRezervace(), mistnost);
            
            for(RezervaceMistnosti temp : rezervace){
                System.out.println("RESERVATION: mistnost - "+temp.getIDmistnosti().getZkratka()+" datum - "+temp.getDatumRezervace()+" od - "+temp.getOd()+" do - "+temp.getDo1()+" pocet - "+temp.getPocetRezervovanychMist()+" popis - "+temp.getPopis());
                if(temp.getStatus().equals("OVERWRITTEN")){
                    System.out.println("ALERT ALERT STATUS BREACH!!!");
                }
                if(roomFac.isInterfering(rez, temp)){
                    interfering.add(temp);
                }
            }
            
            ArrayList<RezervaceMistnosti> overwritten = sweeper.getOverwrittenReservations(rez, interfering);
            //RESERVATION STATUS SWITCH HANDLER
            for(RezervaceMistnosti temp : overwritten){
                rezOper.changeStatusToOverWritten(temp);
            }
            
            
            
            this.createReservation(mistnost, rez.getDatumRezervace(), rez.getOd(), rez.getDo1(), rez.getPocetRezervovanychMist(), rez.getPopis());
            sweeper.RESET_VALUES();
        }
    }

    private ArrayList<RezervaceMistnosti> getLowerPriorityReservations(ArrayList<RezervaceMistnosti> reservations){
        
        ArrayList<RezervaceMistnosti> reservationsCache = new ArrayList<RezervaceMistnosti>(reservations);
        
        for(RezervaceMistnosti res: reservations){
            if(sweeper.isHigherPriorityThanLogged(res)){
                reservationsCache.remove(res);
            }
        }
        return reservationsCache;
    }
    @Override
    public void createReservation(Mistnost mistnost, Date startDate, Date casOd, Date casDo, int pocetMist , String popis) {
        rezOper.createRezervaceMistnosti(session.getLoggedUzivatel(), mistnost, startDate, casOd, casDo, pocetMist, popis);
    }
        
    @Override
    public void editReservation(RezervaceMistnosti selectedRow, Date editDateOfReservation, Date editFrom, Date editTo) {
        rezOper.editRezervaceMistnosti(selectedRow, null, null, editDateOfReservation, editFrom, editTo, null, null);
    }
    
    @Override
    public void deleteReservation(RezervaceMistnosti selectedRow) {
        rezOper.delete(selectedRow);
    }

    @Override
    public List<RezervaceMistnosti> getReservationsOfLoggedUser() {
        Uzivatel user = session.getLoggedUzivatel();
        return rezOper.getRezervace(user);
    }

    @Override
    public String isReservationOK(String mistnostZkratka, Date editDateOfReservation, Date editDateFrom, Date editDateTo) {
        RezervaceMistnosti rez = new RezervaceMistnosti();
        rez.setIDmistnosti(mistOper.getMistnost(mistnostZkratka));
        
        return isReservationOK(rez, editDateOfReservation, editDateFrom, editDateTo);
    }
    
    @Override
    public String isReservationOK(RezervaceMistnosti selectedRow, Date editDateOfReservation, Date editDateFrom, Date editDateTo) {
                    
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE",Locale.ENGLISH);

        DenVTydnu denRezervace = denOper.getENDen(sdf.format(editDateOfReservation));

        ArrayList<RezervaceMistnosti> rezervaceMistnosti = new ArrayList(rezOper.getRezervace(editDateOfReservation, selectedRow.getIDmistnosti()));
        ArrayList<Rozvrhy> rozvrhyMistnosti = new ArrayList(rozOper.getRozvrhy(selectedRow.getIDmistnosti(), denRezervace));


        //checks all schedules for this room

        for(Rozvrhy rez : rozvrhyMistnosti){

            if(!((editDateFrom.after(rez.getDo1())) || (editDateTo.before(rez.getOd())))){
                return "alreadyscheduled";
            }
        }


        //checks all made reservations on this room
        //DEFAULT VALUES NEEDS TO BE REWRITED
        RezervaceMistnosti curr = new RezervaceMistnosti(editDateOfReservation, editDateFrom, editDateTo, 0, "") ;

        if(!roomFac.isEnoughSpace(curr, selectedRow.getIDmistnosti())) {
            return "alreadyReserved";
        }
                
        return "ok";
        
    }




    
    
}
