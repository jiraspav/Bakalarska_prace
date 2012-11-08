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
    
    public Predmety createPredmet(Long id, String name, String shortName) {
        
        Predmety predmet = new Predmety(id, name, shortName);
        
        System.out.println("Predmet "+predmet.getNazev());
        
        predFac.create(predmet);
        
        return predmet;
    }

    public void deleteAll() {
        predFac.removeAll();
    }
    
}
