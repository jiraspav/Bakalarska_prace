/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entityFacade;

import dbEntity.DenVTydnu;
import dbEntity.Mistnost;
import dbEntity.Rozvrhy;
import dbEntity.Vyucujici;
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
public class RozvrhyFacade extends AbstractFacade<Rozvrhy> {
    @PersistenceContext(unitName = "BakalarskaPracePU")
    private EntityManager em;

    
    
    /**
     * Getter pro EntityManager
     * @return EntityManager pro RozvrhyFacade
     */
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    /**
     * Konstruktor
     */
    public RozvrhyFacade() {
        super(Rozvrhy.class);
    }

    /**
     * Metoda pro vyhledávání dat v databázi podle zadaných parametrů
     * Vyhledává rozvrh na zadanou místnost
     * 
     * @param mistnost místnost, pro kterou se vyhledávají rozvrhy
     * @return list rozvrhů pro tuto místnost
     */
    public List<Rozvrhy> findByMistnost(Mistnost mistnost) {
        Query qr = em.createNamedQuery(Rozvrhy.FIND_ROZVRH_BY_MISTNOST);
        qr.setParameter("iDmistnosti", mistnost);
        
        return qr.getResultList();
    }

    /**
     * Metoda pro vyhledávání dat v databázi podle zadaných parametrů
     * Vyhledává rozvrh pro zadanou místnost a zadaný den
     * 
     * @param mistnost místnost, pro kterou se vyhledávají rozvrhy
     * @param denVTydnu den, pro který se vyhledávají rozvrhy
     * @return list rovrhů pro zadanou místnost na zadaný den
     */
    public List<Rozvrhy> findByMistnostAden(Mistnost mistnost, DenVTydnu denVTydnu) {
        Query qr = em.createNamedQuery(Rozvrhy.FIND_ROZVRH_BY_MISTNOST_DEN);
        qr.setParameter("iDmistnosti", mistnost);
        qr.setParameter("iDdnu", denVTydnu);
        
        return qr.getResultList();
    }
    
    /**
     * Metoda pro odstranění všech rozvrhů z databáze
     */
    public void removeAll(){
        
        ArrayList<Rozvrhy> rozvrhyAll = new ArrayList(this.findAll()) ;
        Iterator iter = rozvrhyAll.iterator();
        while(iter.hasNext()){
            Rozvrhy delete = (Rozvrhy) iter.next();
            
            ArrayList<Vyucujici> vyucujiciCollection = new ArrayList(delete.getVyucujiciCollection());
            
            for(Vyucujici v : vyucujiciCollection){
                delete.getVyucujiciCollection().remove(v);
                v.getRozvrhyCollection().remove(delete);
                
               this.edit(delete);
            }
            
            this.remove(delete);
            System.out.println("REMOVED ROZVRH "+delete.getIDrozvrhu());
        }
    }
    
}
