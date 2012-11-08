/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app.baseDataOperators;

import dbEntity.Semestr;
import entityFacade.SemestrFacade;
import java.util.Date;
import java.util.List;
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

    public Semestr getLatestSemestr(){
        
        Semestr soucasny;
        List<Semestr> semestry = semFac.findAll();
        if(semestry.size() > 0)
            soucasny = (Semestr) semestry.get(semestry.size()-1);
        else
            soucasny = new Semestr();
        
        return soucasny;
    }
    
    public void deleteAll() {
        semFac.removeAll();
    }
    
}
