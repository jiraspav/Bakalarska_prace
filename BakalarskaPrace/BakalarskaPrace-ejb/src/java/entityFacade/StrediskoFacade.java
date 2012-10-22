/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entityFacade;

import dbEntity.Mistnost;
import dbEntity.Stredisko;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Pavel
 */
@Stateless
public class StrediskoFacade extends AbstractFacade<Stredisko> {
    @PersistenceContext(unitName = "BakalarskaPracePU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public StrediskoFacade() {
        super(Stredisko.class);
    }
    public Stredisko findStrediskoPodleNazvu(String nazev){
        Query query = em.createNamedQuery("Stredisko.findByNazev");
        query.setParameter("nazev", nazev);
        
        return (Stredisko) query.getSingleResult();
        
    }
    
    public void removeAll(){
        ArrayList<Stredisko> strediskaAll = new ArrayList(this.findAll()) ;
        Iterator iter = strediskaAll.iterator();
        while(iter.hasNext()){
            Stredisko delete = (Stredisko) iter.next();
            this.remove(delete);
        }
    }
        
}
