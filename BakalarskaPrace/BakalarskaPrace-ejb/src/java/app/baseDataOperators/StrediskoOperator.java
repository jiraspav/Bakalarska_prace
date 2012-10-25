/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app.baseDataOperators;

import dbEntity.Stredisko;
import entityFacade.StrediskoFacade;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 *
 * @author Pavel
 */
@Stateless
public class StrediskoOperator {
    
    private @Inject StrediskoFacade stredFac;
    
    public Stredisko createStredisko(Long id, Long code, String department) {
        Stredisko stredisko = new Stredisko(id, code, department);
        
        stredFac.create(stredisko);
        
        System.out.println("Stredisko "+stredisko.getNazev());
        
        return stredisko;
    }
}

