/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app.facade.roomFinder;

import dbEntity.Mistnost;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Pavel
 */
public interface RoomFinderFacade {
    
    public List<Mistnost> getAllFreeRooms(Date from, Date odCas, Date doCas);
    
}
