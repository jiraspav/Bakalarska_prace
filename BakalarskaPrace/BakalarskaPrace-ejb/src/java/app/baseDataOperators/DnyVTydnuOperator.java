/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app.baseDataOperators;

import dbEntity.DenVTydnu;
import entityFacade.DenVTydnuFacade;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 *
 * @author Pavel
 */
@Stateless
public class DnyVTydnuOperator {
    
    private @Inject DenVTydnuFacade denFac;
    
    /**
     * Metoda pro vytvoření defaultních hodnot v databázi. V tomto případě dnů v týdnu.
     */
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
    
    /**
     * Hledá den v databázi podle jeho českého názvu.
     * @param nazev název dnu
     * @return instanci DenVTydnu se stejným jménem jako parametr nazev nebo speciální 
     *         instanci DenVTydnu s jménem Neznámý v případě, že žádný ze dnů nesedí.
     *          
     */
    public DenVTydnu getCZDen(String nazev){
        return denFac.getDenByCeskyNazev(nazev);
    }
    /**
     * Hledá den v databázi podle jeho anglického názvu. Vzhledem k tomu, že jsou
     * dny v databázi uloženy v českém jazyce, je k přeložení použit speciální
     * převodník.
     * @param nazev název dnu
     * @return instanci DenVTydnu se stejným jménem jako přeložený parametr nazev nebo speciální 
     *         instanci DenVTydnu s jménem Neznámý v případě, že žádný ze dnů nesedí.
     */
    public DenVTydnu getENDen(String nazev){
        return denFac.getDenByNazev(nazev);
    }
}
