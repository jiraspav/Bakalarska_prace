/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app.facade.reservationEditor;

import dbEntity.Mistnost;
import dbEntity.RezervaceMistnosti;
import dbEntity.Uzivatel;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Pavel
 */
public interface ReservationEditorFacade {
    
    /**
     * Metoda pro uložení rezervací v Listu. Je nutné zkontrolovat ostatní rezervace v místnosti
     * případně je nutné vybrat rezervace, které je nutné přepsat.
     * 
     * @param mistnost - mstnost na kterou jsou rezervace ukládány
     * @param preparedReservations - List ukládaných rezervací
     * @param logged - uživatel, který tyto rezervace vytváří
     */
    public void createAllReservations(Mistnost mistnost, List<RezervaceMistnosti> preparedReservations, Uzivatel logged);
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
    public void createReservation(Mistnost mistnost, Date startDate, Date casOd, Date casDo, int pocetMist, String popis, Uzivatel logged);
    
    /**
     * Metoda pro mazání rezervace z databáze
     * 
     * @param selectedRow - označená rezervace určená pro odstranění
     */
    public void deleteReservation(RezervaceMistnosti selectedRow);
    
    /**
     * Metoda pro získání rezervací uživatele
     * 
     * @param logged
     * @return List rezervací jejichž vlastníkem je uživatel v parametru
     */
    public List<RezervaceMistnosti> getReservationsOfLoggedUser(Uzivatel logged);



    

    
}
