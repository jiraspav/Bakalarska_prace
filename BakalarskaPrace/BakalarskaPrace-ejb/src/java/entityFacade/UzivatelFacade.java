/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entityFacade;

import dbEntity.Uzivatel;
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
public class UzivatelFacade extends AbstractFacade<Uzivatel> {
    @PersistenceContext(unitName = "BakalarskaPracePU")
    private EntityManager em;

    /**
     * Getter pro EntityManager
     * @return EntityManager pro UzivatelFacade
     */
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    /**
     * Konstruktor
     */
    public UzivatelFacade() {
        super(Uzivatel.class);
    }
    /**
     * Metoda pro vyhledávání dat v databázi
     * Vyhledává uživatele podle loginu
     * 
     * @param login login, pro který se vyhledává uživatel
     * @return nalezeného živatele
     *          <p>
     *         null - pokud žádný takový uživatel není v databázi
     */
    public Uzivatel getUserByLogin(String login) {
        Query qr = em.createNamedQuery(Uzivatel.FIND_BY_LOGIN);
        qr.setParameter("login", login);
        Uzivatel uziv;
        try{
            uziv = (Uzivatel) qr.getSingleResult();
        }catch(NoResultException e){
            uziv = null;
        }
        
        return uziv;
    }
    
}
