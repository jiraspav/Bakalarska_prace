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
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Pavel
 */
@Stateless
public class VyucujiciFacade extends AbstractFacade<Vyucujici> {
    @PersistenceContext(unitName = "BakalarskaPracePU")
    private EntityManager em;

    
    
    /**
     * Getter pro EntityManager
     * @return EntityManager pro VyucujiciFacade
     */
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    /**
     * Konstruktor
     */
    public VyucujiciFacade() {
        super(Vyucujici.class);
    }

    /**
     * Metoda pro vyhledávání dat v databázi, vyhledává vyučující jejichž jméno obsahuje
     * textový parametr
     * 
     * @param name část jména
     * @return list vyučujících, jejichž jméno obsahuje zadaný řetězec
     */
    public List<Vyucujici> findByName(String name){
        Query qr = em.createNamedQuery(Vyucujici.FIND_BY_JMENO);
        qr.setParameter("jmeno", "%"+name+"%");
        
        return qr.getResultList();
    }
    
    /**
     * Metoda pro odstranění všech vyučujících z databáze
     */
    public void removeAll(){
        
        ArrayList<Vyucujici> vyucujiciAll = new ArrayList(this.findAll()) ;
        Iterator iter = vyucujiciAll.iterator();
        while(iter.hasNext()){
            Vyucujici delete = (Vyucujici) iter.next();
            
            ArrayList<Rozvrhy> rozvrhyCollection = new ArrayList<Rozvrhy>(delete.getRozvrhyCollection());
            
            for(Rozvrhy r : rozvrhyCollection){
                delete.getRozvrhyCollection().remove(r);
                r.getVyucujiciCollection().remove(delete);
                
                this.edit(delete);
            }
            
            this.remove(delete);
            System.out.println("REMOVED VYUCUJICI "+delete.getiDvyucujici());
        }
    }
    
}
