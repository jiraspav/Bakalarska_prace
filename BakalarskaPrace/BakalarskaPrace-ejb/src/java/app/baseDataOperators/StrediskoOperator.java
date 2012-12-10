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
    
    public Stredisko createStredisko(Long id, Long code, String department) {
        Stredisko stredisko = new Stredisko(id, code, department);
        
        System.out.println("Stredisko id:"+stredisko.getIDstredisko()+" "+stredisko.getNazev());
        
        stredFac.create(stredisko);
        
        return stredisko;
    }

    public List getAll() {
        return stredFac.findAll();
    }
    public void deleteAll(){
        stredFac.removeAll();
    }
    /*public List<Stredisko> getStrediska(){
        
    }*/
    
}

