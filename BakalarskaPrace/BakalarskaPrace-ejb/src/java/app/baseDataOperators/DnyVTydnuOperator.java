/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app.baseDataOperators;

import app.encrypt.EncryptUtil;
import dbEntity.DenVTydnu;
import entityFacade.DenVTydnuFacade;
import entityFacade.GroupTableFacade;
import entityFacade.UzivatelFacade;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 *
 * @author Pavel
 */
@Stateless
public class DnyVTydnuOperator {
    
    private @Inject DenVTydnuFacade denFac;
    
    public void buildDefaultDny(){
        DenVTydnu den = new DenVTydnu(new Integer(1), "Pondělí");
        denFac.create(den);
            den = new DenVTydnu(new Integer(2), "Úterý");
        denFac.create(den);
            den = new DenVTydnu(new Integer(3), "Středa");
        denFac.create(den);
            den = new DenVTydnu(new Integer(4), "Čtvrtek");
        denFac.create(den);
            den = new DenVTydnu(new Integer(5), "Pátek");
        denFac.create(den);
            den = new DenVTydnu(new Integer(6), "Sobota");
        denFac.create(den);
            den = new DenVTydnu(new Integer(7), "Neděle");
        denFac.create(den);
            den = new DenVTydnu(new Integer(8), "Neznámý");
        denFac.create(den);
    }
    
    public DenVTydnu getCZDen(String nazev){
        return denFac.getDenByCeskyNazev(nazev);
    }
    public DenVTydnu getENDen(String nazev){
        return denFac.getDenByNazev(nazev);
    }
}
