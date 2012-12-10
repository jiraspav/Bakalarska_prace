/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app.baseDataOperators;

import dbEntity.TypMistnosti;
import entityFacade.TypMistnostiFacade;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 *
 * @author Pavel
 */
@Stateless
public class TypMistnostiOperator {

    @Inject TypMistnostiFacade typFac;
    
    public TypMistnostiOperator() {
    }

    public TypMistnosti getTyp(String nazev){
        return typFac.getTypMistnostiByNazev(nazev);
    }

    public void buildDefaultTypyMistnosti() {
        TypMistnosti typ = new TypMistnosti(1, "Přednášková místnost");
        typFac.create(typ);
        
        typ = new TypMistnosti(2, "Laboratoř");
        typFac.create(typ);
        
        typ = new TypMistnosti(3, "Učebna");
        typFac.create(typ);
    }
    
}
