/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entityFacade;

import dbEntity.Predmety;
import java.util.ArrayList;
import java.util.Iterator;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Pavel
 */
@Stateless
public class PredmetyFacade extends AbstractFacade<Predmety> {
    @PersistenceContext(unitName = "BakalarskaPracePU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PredmetyFacade() {
        super(Predmety.class);
    }
    
    public void removeAll(){
        
        ArrayList<Predmety> predmetyAll = new ArrayList(this.findAll()) ;
        Iterator iter = predmetyAll.iterator();
        while(iter.hasNext()){
            Predmety delete = (Predmety) iter.next();
            this.remove(delete);
        }
    }
}
