/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app.facade.roomFinder;

import dbEntity.Mistnost;
import dbEntity.RezervaceMistnosti;
import java.util.List;

/**
 *
 * @author Pavel
 */
public interface RoomFinderFacade {
    
    public List<Mistnost> getAllFreeRooms(List<RezervaceMistnosti> preparedReservations);
    public boolean isEnoughSpace(RezervaceMistnosti rez, Mistnost mist);
    public boolean isInterfering(RezervaceMistnosti rez, List<RezervaceMistnosti> preparedReservations);
    public boolean isInterfering(RezervaceMistnosti rez, RezervaceMistnosti rez2);
    
}
