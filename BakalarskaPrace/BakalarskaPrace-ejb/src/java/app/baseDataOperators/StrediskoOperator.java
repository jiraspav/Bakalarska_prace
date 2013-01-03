/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app.baseDataOperators;

import dbEntity.Stredisko;
import entityFacade.StrediskoFacade;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 *
 * @author Pavel
 */
@Stateless
public class StrediskoOperator {
    
    private @Inject StrediskoFacade stredFac;
    
    /**
     * Metoda určená pro uložení nového střediska do databáze
     * @param id ID střediska
     * @param code kód střediska
     * @param nazev textové pojmenování střediska
     * @return uložené středisko
     */
    public Stredisko createStredisko(Long id, Long code, String nazev) {
        Stredisko stredisko = new Stredisko(id, code, nazev);
        
        System.out.println("Stredisko id:"+stredisko.getIDstredisko()+" "+stredisko.getNazev());
        
        stredFac.create(stredisko);
        
        return stredisko;
    }

    /**
     * Metoda pro získání veškerých středisek z databáze
     * @return List všech středisek z databáze
     */
    public List getAll() {
        return stredFac.findAll();
    }
    /**
     * Metoda pro odstranění všech středisek z databáze
     */
    public void deleteAll(){
        stredFac.removeAll();
    }
    
}

