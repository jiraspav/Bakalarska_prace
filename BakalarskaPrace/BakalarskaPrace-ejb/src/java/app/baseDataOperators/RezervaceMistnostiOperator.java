/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app.baseDataOperators;

import dbEntity.Mistnost;
import dbEntity.RezervaceMistnosti;
import dbEntity.Uzivatel;
import entityFacade.RezervaceMistnostiFacade;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 *
 * @author Pavel
 */
@Stateless
public class RezervaceMistnostiOperator {
    
    private @Inject RezervaceMistnostiFacade rezFac;

    
    
    
    
    public List<RezervaceMistnosti> getRezervace(Uzivatel uziv){
        return rezFac.getRezervaceByUserID(uziv);
    }
    
    public List<RezervaceMistnosti> getRezervace(Mistnost mist){
        return rezFac.getRezervaceByMistnostID(mist);
    }
    
    public List<RezervaceMistnosti> getRezervace(Date date){
        return rezFac.getRezervaceByDatum(date);
    }
    
    public void delete(RezervaceMistnosti rez){
        rezFac.remove(rez);
    }
    
    public void deleteAll() {
        rezFac.removeAll();
    }
    
}
