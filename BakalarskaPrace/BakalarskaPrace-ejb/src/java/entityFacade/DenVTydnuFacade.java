/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entityFacade;

import dbEntity.DenVTydnu;
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
public class DenVTydnuFacade extends AbstractFacade<DenVTydnu> {
    
    static String[] translator = {"MONDAY","Pondělí","TUESDAY","Úterý",
                                "WEDNESDAY","Středa","THURSDAY","Čtvrtek",
                                "FRIDAY","Pátek","SATURDAY","Sobota","SUNDAY","Neděle"};
    
    @PersistenceContext(unitName = "BakalarskaPracePU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public DenVTydnuFacade() {
        super(DenVTydnu.class);
    }
    public DenVTydnu getDenByNazev(String nazev){
        String prelozeno = null;
        for(int i = 0;i<translator.length; i++){
            if(translator[i].equalsIgnoreCase(nazev)){
                prelozeno = translator[i+1];
                break;
            }
        }
        try{
            Query qr = em.createNamedQuery("DenVTydnu.findByNazev");
            qr.setParameter("nazev", prelozeno);
            return (DenVTydnu) qr.getSingleResult();
        }catch(NoResultException e){
            return getDenByCeskyNazev("Neznámý");
        }
    }
    public DenVTydnu getDenByCeskyNazev(String nazev){
        try{
            Query qr = em.createNamedQuery("DenVTydnu.findByNazev");
            qr.setParameter("nazev", nazev);
            return (DenVTydnu) qr.getSingleResult();
        }catch(NoResultException e){
            return getDenByCeskyNazev("Neznámý");
        }
    }
    
}
