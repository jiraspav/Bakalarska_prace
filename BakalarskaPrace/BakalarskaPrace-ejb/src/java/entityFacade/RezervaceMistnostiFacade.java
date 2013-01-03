/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entityFacade;

import dbEntity.Mistnost;
import dbEntity.RezervaceMistnosti;
import dbEntity.Uzivatel;
import java.util.ArrayList;
import java.util.Date;
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
public class RezervaceMistnostiFacade extends AbstractFacade<RezervaceMistnosti> {
    @PersistenceContext(unitName = "BakalarskaPracePU")
    private EntityManager em;

    /**
     * Getter pro EntityManager
     * @return EntityManager pro RezervaceMistnostiFacade
     */
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    /**
     * Konstruktor
     */
    public RezervaceMistnostiFacade() {
        super(RezervaceMistnosti.class);
    }
    
    /**
     * Metoda pro vyhledávání dat v databázi
     * Vyhledává všechny rezervace příslušející danému uživateli
     * 
     * @param user
     * @return list rezervací daného uživatele
     */
    public List<RezervaceMistnosti> getRezervaceByUserID(Uzivatel user) {
        Query qr = em.createNamedQuery("RezervaceMistnosti.findByIDuser");
        qr.setParameter("iDuser", user);
        qr.setParameter("status", "ACTIVE");
        
        return qr.getResultList();
    }
    
    /**
     * Metoda pro vyhledávání dat v databázi
     * Vyhledává všechny rezervace příslušející dané místnosti 
     * 
     * @param mistnost
     * @return list rezervací dané místnosti
     */
    public List<RezervaceMistnosti> getRezervaceByMistnostID(Mistnost mistnost) {
        Query qr = em.createNamedQuery("RezervaceMistnosti.findByIDmistnosti");
        qr.setParameter("iDmistnosti", mistnost);
        qr.setParameter("status", "ACTIVE");
        return qr.getResultList();
    }
    
    /**
     * Metoda pro vyhledávání dat v databázi
     * Vyhledává všechny rezervace na dané datum 
     * 
     * @param datum
     * @return list rezervací na dané datum
     */
    public List<RezervaceMistnosti> getRezervaceByDatum(Date datum) {
        Query qr = em.createNamedQuery("RezervaceMistnosti.findByDatumRezervace");
        qr.setParameter("datumRezervace", datum);
        qr.setParameter("status", "ACTIVE");
        return qr.getResultList();
    }
    
    /**
     * Metoda pro vyhledávání dat v databázi
     * Vyhledává všechny rezervace na dané datum a danou místnost
     * 
     * @param datum
     * @param mistnost
     * @return list rezervací
     */
    public List<RezervaceMistnosti> getRezervaceByMistnostIDandDatum(Date datum, Mistnost mistnost) {
        Query qr = em.createNamedQuery(RezervaceMistnosti.FIND_BY_MISTNOST_A_DEN);
        qr.setParameter("datumRezervace", datum);
        qr.setParameter("iDmistnosti", mistnost);
        qr.setParameter("status", "ACTIVE");
        return qr.getResultList();
    }
    
    /**
     * Metoda pro odstranění všech rezervací v databázi
     */
    public void removeAll(){
        ArrayList<RezervaceMistnosti> rezAll = new ArrayList(this.findAll()) ;
        Iterator iter = rezAll.iterator();
        while(iter.hasNext()){
            RezervaceMistnosti delete = (RezervaceMistnosti) iter.next();
            this.remove(delete);
        }
    }
}

