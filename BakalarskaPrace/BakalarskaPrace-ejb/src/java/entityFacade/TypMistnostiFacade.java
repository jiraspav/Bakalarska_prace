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

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TypMistnostiFacade() {
        super(TypMistnosti.class);
    }

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
