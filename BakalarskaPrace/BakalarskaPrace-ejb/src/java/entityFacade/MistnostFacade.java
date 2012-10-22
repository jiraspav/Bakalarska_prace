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
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Pavel
 */
@Stateless
public class MistnostFacade extends AbstractFacade<Mistnost> {
    @PersistenceContext(unitName = "BakalarskaPracePU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public MistnostFacade() {
        super(Mistnost.class);
    }
    
    public List<Mistnost> findMistnostiStrediska(Stredisko str){
        Query query = em.createNamedQuery("Mistnost.findByIDstrediska");
        query.setParameter("iDstrediska", str);
        
        return query.getResultList();
        
    }
    
    public Mistnost findMistnostPodleZkratky(String str){
        Query query = em.createNamedQuery("Mistnost.findByZkratka");
        query.setParameter("zkratka", str);
        try{
            return (Mistnost) query.getSingleResult();
        }catch(NoResultException e){
            return null;
        }
    }
    
    public void removeAll(){
        ArrayList<Mistnost> mistnostiAll = new ArrayList(this.findAll()) ;
        Iterator iter = mistnostiAll.iterator();
        while(iter.hasNext()){
            Mistnost delete = (Mistnost) iter.next();
            this.remove(delete);
        }
    }
}
