/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entityFacade;

import java.util.List;
import javax.persistence.EntityManager;

/**
 *  Abstraktní EntityManager
 * @param <T> konkrétní třída
 * @author Pavel
 */
public abstract class AbstractFacade<T> {
    private Class<T> entityClass;

    /**
     * 
     * @param entityClass
     */
    public AbstractFacade(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    /**
     * 
     * @return
     */
    protected abstract EntityManager getEntityManager();

    /**
     * Abstraktní metoda pro ukládání entit do databáze
     * 
     * @param entity
     */
    public void create(T entity) {
        getEntityManager().persist(entity);
    }

    /**
     * Abstraktní metoda pro aktualizaci entity v databázi
     * @param entity
     */
    public void edit(T entity) {
        getEntityManager().merge(entity);
    }

    /**
     * Abstraktní metoda pro odstraňování entit z databáze
     * @param entity
     */
    public void remove(T entity) {
        getEntityManager().remove(getEntityManager().merge(entity));
    }

    /**
     * Abstraktní metoda pro vyhledávání entit v databázi
     * @param id
     * @return
     */
    public T find(Object id) {
        return getEntityManager().find(entityClass, id);
    }

    /**
     * Abstraktní metoda pro vyhledání všech entit v databázi
     * @return
     */
    public List<T> findAll() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        return getEntityManager().createQuery(cq).getResultList();
    }

    /**
     * 
     * @param range
     * @return
     */
    public List<T> findRange(int[] range) {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    /**
     * Abstraktní metoda pro spočítání entit v databázi
     * @return
     */
    public int count() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        javax.persistence.criteria.Root<T> rt = cq.from(entityClass);
        cq.select(getEntityManager().getCriteriaBuilder().count(rt));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }
    
}
