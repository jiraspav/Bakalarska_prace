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

    /**
     * Getter pro EntityManager
     * @return EntityManager pro MistnostFacade
     */
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    /**
     * Konstruktor
     */
    public MistnostFacade() {
        super(Mistnost.class);
    }
    
    /**
     * Metoda pro vyhledávání dat v databázi, vyhledává místnosti podle střediska
     * @param str středisko pod které patří místnosti
     * @return list místnosti, které patří pod dané středisko
     */
    public List<Mistnost> findMistnostiStrediska(Stredisko str){
        Query query = em.createNamedQuery("Mistnost.findByIDstrediska");
        query.setParameter("iDstrediska", str);
        
        return query.getResultList();
        
    }
    
    /**
     * Metoda pro vyhledávání dat v databázi, vyhledává místnosti podle zkratky
     * @param str zkratka mistnosti
     * @return místnost, která má danou zkratku
     *         <p>
     *         null - pokud žádná taková místnost není
     */
    public Mistnost findMistnostPodleZkratky(String str){
        Query query = em.createNamedQuery("Mistnost.findByZkratka");
        query.setParameter("zkratka", str);
        try{
            return (Mistnost) query.getSingleResult();
        }catch(NoResultException e){
            return null;
        }
    }
    
    /**
     * Metoda pro odstranění všech místností z databáze
     */
    public void removeAll(){
        ArrayList<Mistnost> mistnostiAll = new ArrayList(this.findAll()) ;
        Iterator iter = mistnostiAll.iterator();
        while(iter.hasNext()){
            Mistnost delete = (Mistnost) iter.next();
            this.remove(delete);
        }
    }
}
