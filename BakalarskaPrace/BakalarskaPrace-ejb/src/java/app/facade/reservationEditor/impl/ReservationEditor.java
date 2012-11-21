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
import app.sessionHolder.SessionHolderEJB;
import dbEntity.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
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

    @Override
    public void createReservation(Uzivatel uziv, Mistnost mistnost, Date startDate, Date casOd, Date casDo, boolean naCelouMistnost, String popis) {
        rezOper.createRezervaceMistnosti(uziv, mistnost, startDate, casOd, casDo, naCelouMistnost, popis);
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
    public DataModel getReservationsOfLoggedUser() {
        Uzivatel user = session.getLoggedUzivatel();
        return new ListDataModel(rezOper.getRezervace(user)) ;
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

        DenVTydnu denRezervace = denOper.getCZDen(sdf.format(editDateOfReservation));

        ArrayList<RezervaceMistnosti> rezervaceMistnosti = new ArrayList(rezOper.getRezervace(selectedRow.getIDmistnosti()));
        ArrayList<Rozvrhy> rozvrhyMistnosti = new ArrayList(rozOper.getRozvrhy(selectedRow.getIDmistnosti(), denRezervace));


        //checks all schedules for this room

        for(Rozvrhy rez : rozvrhyMistnosti){

            if(!((editDateFrom.after(rez.getDo1())) || (editDateTo.before(rez.getOd())))){
                return "alreadyscheduled";
            }
        }


        //checks all made reservations on this room

        for(RezervaceMistnosti rez : rezervaceMistnosti){

            //are reservations of same date?
            if(editDateOfReservation.equals(rez.getDatumRezervace())){
                //are they interfering with each other?
                if(!((editDateFrom.after(rez.getDo1())) || (editDateTo.before(rez.getOd())))){
                    //is role of user higher than user of older reservation?
                    if(rez.getNaCelouMistnost() && (uzivOper.getUzivatelRolePriority(rez.getIDuser())) >= (uzivOper.getUzivatelRolePriority(session.getLoggedUzivatel()))) {
                        return "alreadyReserved";
                    }
                }
            }
        }
        return "ok";
        
    }




    
    
}
