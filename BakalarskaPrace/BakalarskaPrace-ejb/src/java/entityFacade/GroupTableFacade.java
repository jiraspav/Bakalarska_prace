/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entityFacade;

import dbEntity.GroupTable;
import dbEntity.Uzivatel;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
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

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public GroupTableFacade() {
        super(GroupTable.class);
    }
    public String getGroup(Uzivatel uziv){
        Query qr = em.createNamedQuery(GroupTable.FIND_GROUPTABLE_BY_LOGIN);
        qr.setParameter("login", uziv.getLogin());
        GroupTable table = (GroupTable) qr.getSingleResult();
        return table.getGrouptablePK().getGroupid();
    }
    public GroupTable getGroupTableByUziv(Uzivatel uziv){
        Query qr = em.createNamedQuery(GroupTable.FIND_GROUPTABLE_BY_LOGIN);
        qr.setParameter("login", uziv.getLogin());
        GroupTable table = (GroupTable) qr.getSingleResult();
        return table;
    }
}
