/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans.mistnosti;

import dbEntity.Predmety;

/**
 *
 * @author Pavel
 */
public class RozvrhNaDen {

    private String[] rozvrh = {"", "", "", "", "", "", "", "", "", "", "", "", "", "", ""};
    private String den = "";

    /**
     * 
     */
    public RozvrhNaDen() {
    }

    /**
     * 
     * @param den
     */
    public RozvrhNaDen(String den) {
        this.den = den;
    }

    /**
     * metoda pro přidání konkrétního předmětu na konkrétní hodinu
     * @param predmet předmět, který vkládáme do rozvrhu
     * @param hodina hodina, kdy je předmět vyučován
     */
    public void addHodinu(Predmety predmet, int hodina) {
        rozvrh[hodina] = predmet.getZkratka();
    }

    /**
     * 
     * @param pozice
     * @return hodinu na pozici pozice
     */
    public String getHodinu(int pozice) {
        return rozvrh[pozice];
    }

    /**
     * 
     * @return den v tydnu
     */
    public String getDen() {
        return den;
    }
}
