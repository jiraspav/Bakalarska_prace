/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app.baseDataOperators;

import dbEntity.Predmety;
import entityFacade.PredmetyFacade;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 *
 * @author Pavel
 */
@Stateless
public class PredmetyOperator {
    
    private @Inject PredmetyFacade predFac;
    
    /**
     * Metoda pro uložení nového předmětu do databáze.
     * @param id ID nového předmětu
     * @param name celé jméno předmětu
     * @param shortName zkratka jména tohoto předmětu
     * @return uložený objekt typu Predmet
     */
    public Predmety createPredmet(Long id, String name, String shortName) {
        
        Predmety predmet = new Predmety(id, name, shortName);
        
        System.out.println("Predmet "+predmet.getNazev());
        
        predFac.create(predmet);
        
        return predmet;
    }

    /**
     * Metoda pro smazání veškerých předmětů z databáze.
     */
    public void deleteAll() {
        predFac.removeAll();
    }
    
}
