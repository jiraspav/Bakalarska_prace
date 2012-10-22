/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entityFacade;

import dbEntity.UpdateRozvrhu;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Pavel
 */
@Stateless
public class UpdateRozvrhuFacade extends AbstractFacade<UpdateRozvrhu> {
    @PersistenceContext(unitName = "BakalarskaPracePU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UpdateRozvrhuFacade() {
        super(UpdateRozvrhu.class);
    }
    
}
