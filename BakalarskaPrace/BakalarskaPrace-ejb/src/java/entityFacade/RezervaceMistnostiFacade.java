/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entityFacade;

import dbEntity.Mistnost;
import dbEntity.RezervaceMistnosti;
import dbEntity.Uzivatel;
import java.util.Date;
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
public class RezervaceMistnostiFacade extends AbstractFacade<RezervaceMistnosti> {
    @PersistenceContext(unitName = "BakalarskaPracePU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public RezervaceMistnostiFacade() {
        super(RezervaceMistnosti.class);
    }
    
    public List<RezervaceMistnosti> getRezervaceByUserID(Uzivatel user) {
        Query qr = em.createNamedQuery("RezervaceMistnosti.findByIDuser");
        qr.setParameter("iDuser", user);
        
        return qr.getResultList();
    }
    public List<RezervaceMistnosti> getRezervaceByMistnostID(Mistnost mistnost) {
        Query qr = em.createNamedQuery("RezervaceMistnosti.findByIDmistnosti");
        qr.setParameter("iDmistnosti", mistnost);
        return qr.getResultList();
    }
    public List<RezervaceMistnosti> getRezervaceByDatum(Date datum) {
        Query qr = em.createNamedQuery("RezervaceMistnosti.findByDatumRezervace");
        qr.setParameter("datumRezervace", datum);
        return qr.getResultList();
    }
}

