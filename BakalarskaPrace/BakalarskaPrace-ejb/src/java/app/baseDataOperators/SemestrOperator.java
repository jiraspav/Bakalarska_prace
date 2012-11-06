/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app.baseDataOperators;

import dbEntity.Semestr;
import entityFacade.SemestrFacade;
import java.util.Date;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 *
 * @author Pavel
 */
@Stateless
public class SemestrOperator {
    
    private @Inject SemestrFacade semFac;
    
    public Semestr createSemestr(Integer idSemestru, String code, Date zacatek, Date konec) {
        
        Semestr semestr = new Semestr(idSemestru, code, zacatek, konec);
        
        System.out.println("Semestr "+semestr.toString());
        
        semFac.create(semestr);
        
        return semestr;
                
    }
    
}
