/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entityFacade;

import dbEntity.TypMistnosti;
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
public class TypMistnostiFacade extends AbstractFacade<TypMistnosti> {
    
    @PersistenceContext(unitName = "BakalarskaPracePU")
    private EntityManager em;

    /**
     * Getter pro EntityManager
     * @return EntityManager pro TypMistnostiFacade
     */
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    /**
     * Konstruktor
     */
    public TypMistnostiFacade() {
        super(TypMistnosti.class);
    }

    /**
     * Metoda pro vyhledávání dat v databázi
     * Vyhledává typ místnosti podle zadaného nazvu
     * 
     * @param nazev název typu místnosti
     * @return nalezený typ místnosti
     *          <p>
     *         null - pokud takový typ místnosti není v databázi
     */
    public TypMistnosti getTypMistnostiByNazev(String nazev){
        try{
            Query qr = em.createNamedQuery(TypMistnosti.FIND_BY_NAME);
            qr.setParameter("nazev", nazev);
            return (TypMistnosti) qr.getSingleResult();
        }catch(NoResultException e){
            return null;
        }
    }
}
