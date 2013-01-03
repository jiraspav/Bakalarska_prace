/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app.facade.RoomTreeCreator.impl;

import dbEntity.Stredisko;
import java.util.Comparator;

/**
 *
 * @author Pavel
 */
public class StrediskoComparator implements Comparator {
    
    /**
     * Comparator pro dvě střediska
     * @param jedna
     * @param dva
     * @return 1 - pokud stejné
     *         jinak jsou odlišné
     */
    @Override
    public int compare(Object jedna, Object dva){    
 
        String str1name = ((Stredisko)jedna).getNazev();        
        String str2name = ((Stredisko)dva).getNazev();
       
        return str1name.compareTo(str2name);
   
    }
}
