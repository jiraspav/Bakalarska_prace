/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entityFacade;

import dbEntity.DenVTydnu;
import dbEntity.Mistnost;
import dbEntity.Rozvrhy;
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
public class RozvrhyFacade extends AbstractFacade<Rozvrhy> {
    @PersistenceContext(unitName = "BakalarskaPracePU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public RozvrhyFacade() {
        super(Rozvrhy.class);
    }

    public void findByMistnost(Mistnost mistnost) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public List<Rozvrhy> findByMistnostAden(Mistnost mistnost, DenVTydnu denVTydnu) {
        Query qr = em.createNamedQuery("Rozvrhy.findByIDmistnostiAdnu");
        qr.setParameter("iDmistnosti", mistnost);
        qr.setParameter("iDdnu", denVTydnu);
        
        return qr.getResultList();
    }
    
}
