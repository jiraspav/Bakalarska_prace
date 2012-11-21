/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app.baseDataOperators;

import dbEntity.DenVTydnu;
import dbEntity.Mistnost;
import dbEntity.Predmety;
import dbEntity.Rozvrhy;
import entityFacade.RozvrhyFacade;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 *
 * @author Pavel
 */
@Stateless
public class RozvrhyOperator {
    
    private @Inject RozvrhyFacade rozFac;

    public Rozvrhy createRozvrh(Long aLong, DenVTydnu denByNazev, Mistnost mistnost, Predmety predmety,
                                Date doDate, Date odDate, boolean lichy, boolean sudy) {
        
        Rozvrhy rozvrh = new Rozvrhy(aLong, odDate, doDate, lichy, sudy);
        
        rozvrh.setIDdnu(denByNazev);
        rozvrh.setIDmistnosti(mistnost);
        rozvrh.setIDpredmetu(predmety);
        
        System.out.println(rozvrh.getIDpredmetu().getZkratka()+" "+rozvrh.getOd()+"-"+rozvrh.getDo1());
        
        rozFac.create(rozvrh);
        
        return rozvrh;
    }

    public void deleteAll() {
        rozFac.removeAll();
    }
    
    public List<Rozvrhy> getRozvrhy(Mistnost mist){
        return rozFac.findByMistnost(mist);
    }
    public List<Rozvrhy> getRozvrhy(Mistnost mist, DenVTydnu den){
        return rozFac.findByMistnostAden(mist, den);
    }
    
}
