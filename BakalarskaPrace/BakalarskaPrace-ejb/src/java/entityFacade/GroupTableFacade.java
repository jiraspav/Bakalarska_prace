/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entityFacade;

import dbEntity.GroupTable;
import dbEntity.Uzivatel;
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
public class GroupTableFacade extends AbstractFacade<GroupTable> {
    
    @PersistenceContext(unitName = "BakalarskaPracePU")
    private EntityManager em;

    /**
     * Getter pro EntityManager
     * @return EntityManager pro GroupTableFacade
     */
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    /**
     * Konstruktor
     */
    public GroupTableFacade() {
        super(GroupTable.class);
    }
    /**
     * Metoda pro zjištění role uživatele
     * 
     * @param uziv
     * @return role uživatele
     */
    public String getGroup(Uzivatel uziv){
        Query qr = em.createNamedQuery(GroupTable.FIND_GROUPTABLE_BY_LOGIN);
        qr.setParameter("login", uziv.getLogin());
        GroupTable table = (GroupTable) qr.getSingleResult();
        return table.getGrouptablePK().getGroupid();
    }
    /**
     * Metoda pro získání GroupTable z databáze podle uživatele
     * 
     * @param uziv
     * @return
     */
    public GroupTable getGroupTableByUziv(Uzivatel uziv){
        Query qr = em.createNamedQuery(GroupTable.FIND_GROUPTABLE_BY_LOGIN);
        qr.setParameter("login", uziv.getLogin());
        
        try{
            return (GroupTable) qr.getSingleResult();
        }catch(NoResultException r){
            return null;
        }
    }
    /**
     * Metoda pro získání GroupTable podle loginu
     * 
     * @param login
     * @return
     */
    public GroupTable getGroupTableByUziv(String login){
        Query qr = em.createNamedQuery(GroupTable.FIND_GROUPTABLE_BY_LOGIN);
        qr.setParameter("login", login);
        
        try{
            return (GroupTable) qr.getSingleResult();
        }catch(NoResultException r){
            return null;
        }
    }
    /**
     * Metoda pro vyhledávání dat v databázi
     * Vyhledává všechny GroupTable jedné role
     * 
     * @param role
     * @return
     */
    public List<GroupTable> getGroupTableByRole(String role){
        Query qr = em.createNamedQuery(GroupTable.FIND_GROUPTABLE_BY_GROUPID);
        qr.setParameter("group", role);
        
        return qr.getResultList();
    }
}
