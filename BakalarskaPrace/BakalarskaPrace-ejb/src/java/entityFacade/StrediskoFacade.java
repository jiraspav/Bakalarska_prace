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
public class StrediskoFacade extends AbstractFacade<Stredisko> {
    @PersistenceContext(unitName = "BakalarskaPracePU")
    private EntityManager em;

    /**
     * Getter pro EntityManager
     * @return entityManager pro StrediskoFacade
     */
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    /**
     * Konstruktor
     */
    public StrediskoFacade() {
        super(Stredisko.class);
    }
    /**
     * Metoda pro vyhledávání v databázi, vyhledává středisko podle názvu
     * @param nazev název střediska
     * @return nalezené středisko
     *         <p>
     *         null - pokud takové středisko v databázi není
     */
    public Stredisko findStrediskoPodleNazvu(String nazev){
        Query query = em.createNamedQuery("Stredisko.findByNazev");
        query.setParameter("nazev", nazev);
        try{
            return (Stredisko) query.getSingleResult();
        }catch(NoResultException e){
            return null;
        }
    }
    
    /**
     * Metoda pro odstraňování dat z databáze, odstraňuje veškerá střediska
     */
    public void removeAll(){
        ArrayList<Stredisko> strediskaAll = new ArrayList(this.findAll()) ;
        Iterator iter = strediskaAll.iterator();
        while(iter.hasNext()){
            Stredisko delete = (Stredisko) iter.next();
            this.remove(delete);
        }
    }
        
}
