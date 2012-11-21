/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app.facade.reservationEditor;

import dbEntity.Mistnost;
import dbEntity.RezervaceMistnosti;
import dbEntity.Uzivatel;
import java.util.Date;
import javax.faces.model.DataModel;

/**
 *
 * @author Pavel
 */
public interface ReservationEditorFacade {
    
    public void createReservation(Uzivatel uziv, Mistnost mistnost, Date startDate, Date casOd, Date casDo, boolean naCelouMistnost, String popis);
    public void editReservation(RezervaceMistnosti selectedRow, Date editDateOfReservation, Date editFrom, Date editTo);
    public void deleteReservation(RezervaceMistnosti selectedRow);
    public String isReservationOK(RezervaceMistnosti selectedRow, Date editDateOfReservation, Date editFrom, Date editTo);
    public String isReservationOK(String mistnostZkratka, Date editDateOfReservation, Date editFrom, Date editTo);
    public DataModel getReservationsOfLoggedUser();



    

    
}
