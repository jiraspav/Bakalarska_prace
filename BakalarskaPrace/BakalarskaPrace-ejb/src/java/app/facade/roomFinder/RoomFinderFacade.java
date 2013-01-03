/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app.facade.roomFinder;

import dbEntity.Mistnost;
import dbEntity.RezervaceMistnosti;
import dbEntity.Rozvrhy;
import dbEntity.Uzivatel;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Pavel
 */
public interface RoomFinderFacade {
    
    /**
     * Metoda pro získání všech volných místností pro přípravené rezervace
     * 
     * @param preparedReservations - rezervace připravené pro uložení do databáze
     * @param logged - vlastník rezervaci
     * @return List volných místností
     */
    public List<Mistnost> getAllFreeRooms(List<RezervaceMistnosti> preparedReservations, Uzivatel logged);
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
    public boolean isEnoughSpace(RezervaceMistnosti rez, Mistnost mist, Uzivatel logged);
    /**
     * Metoda pro zjištění rezervací, které novou překrývají
     * @param rez nová rezervace
     * @param preparedReservations list rezervací pro které zjišťuji zda je nová překrývá
     * @return true - pokud překrývá
     */
    public boolean isInterfering(RezervaceMistnosti rez, List<RezervaceMistnosti> preparedReservations);
    /**
     * Metoda pro zjištění zda se dvě rezervace překrývají
     * @param rez
     * @param rez2
     * @return true - pokud se překrývají
     */
    public boolean isInterfering(RezervaceMistnosti rez, RezervaceMistnosti rez2);
    /**
     * Metoda pro zjištění zda rezervcae překrývá některý z rozvrhů
     * @param rez
     * @param rozvrhy
     * @return true - pokud překrývá
     */
    public boolean isInterfering(RezervaceMistnosti rez, ArrayList<Rozvrhy> rozvrhy);
}
