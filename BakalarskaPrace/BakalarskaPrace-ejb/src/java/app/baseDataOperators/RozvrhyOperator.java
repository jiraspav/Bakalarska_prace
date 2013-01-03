/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app.baseDataOperators;

import dbEntity.*;
import entityFacade.RozvrhyFacade;
import java.util.ArrayList;
import java.util.Collection;
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

    /**
     * Metoda zajišťující uložení objektu do databáze.
     * @param rozvrh entita Rozvrh, která má být uložena do databáze
     * @return vrací uloženou entitu
     */
    public Rozvrhy createRozvrh(Rozvrhy rozvrh){
        
        System.out.println("CREATE Rozvrh "+rozvrh.getIDrozvrhu()+" "+rozvrh.getOd()+"-"+rozvrh.getDo1());
        
        rozFac.create(rozvrh);
        
        return rozvrh;
    }
    
    /**
     * Metoda zajišťující uložení objektu do databáze. Parametry této metody jsou
     * potřebné k vytvoření nové entity.
     * @param aLong id ukládané entity
     * @param denByNazev objekt reprezentující den v týdnu
     * @param mistnost objekt reprezentující místnost z databáze, ve které se rozvrh nachází
     * @param predmety objekt reprezentující předmět z databáze, který je v rozvrhu vyučován
     * @param odDate časová reprezentace začátku rozvrhu
     * @param doDate časová reprezentace konce rozvrhu
     * @param lichy zda se rozvrh vyučuje v lichém týdnu
     * @param sudy zda se rozvrh vyučuje v sudém týdnu
     * @param vyucujiciCollection seznam učitelů vyučujících v tomto rozvrhu
     * @return vrací uloženou entitu
     */
    public Rozvrhy createRozvrh(Long aLong, DenVTydnu denByNazev, Mistnost mistnost, Predmety predmety,
                                Date odDate, Date doDate, boolean lichy, boolean sudy, Collection<Vyucujici> vyucujiciCollection) {
        
        Rozvrhy rozvrh = getObject(aLong, denByNazev, mistnost, predmety, odDate, doDate, lichy, sudy, vyucujiciCollection);
        
        System.out.println("CREATE Rozvrh "+aLong+" "+rozvrh.getOd()+"-"+rozvrh.getDo1());
        
        rozFac.create(rozvrh);
        
        return rozvrh;
    }

    /**
     * Metoda pro získávání nového objektu typu Rozvrhy.
     * @param aLong id ukládané entity
     * @param denByNazev objekt reprezentující den v týdnu
     * @param mistnost objekt reprezentující místnost z databáze, ve které se rozvrh nachází
     * @param predmety objekt reprezentující předmět z databáze, který je v rozvrhu vyučován
     * @param odDate časová reprezentace začátku rozvrhu
     * @param doDate časová reprezentace konce rozvrhu
     * @param lichy zda se rozvrh vyučuje v lichém týdnu
     * @param sudy zda se rozvrh vyučuje v sudém týdnu
     * @param vyucujiciCollection seznam učitelů vyučujících v tomto rozvrhu
     * @return nový objekt typu Rozvrhy
     */
    public Rozvrhy getObject(Long aLong, DenVTydnu denByNazev, Mistnost mistnost, Predmety predmety,
                                Date odDate, Date doDate, boolean lichy, boolean sudy, Collection<Vyucujici> vyucujiciCollection){
        
        return new Rozvrhy(aLong, odDate, doDate, lichy, sudy, mistnost, denByNazev, predmety, vyucujiciCollection);
    }
    
    /**
     * Metoda umožňující aktualizaci entity v databázi.
     * @param r aktualizovaná entita
     */
    public void update(Rozvrhy r){
        rozFac.edit(r);
    }
    
    /**
     * Metoda pro odstranění veškerých dat v tabulce Rozvrhy.
     */
    public void deleteAll() {
        rozFac.removeAll();
    }
    /**
     * Metoda pro vyhledávání rozvrhu v databázi.
     * @param rozvrhy objekt typu Rozvrhy, vyhledává se pomocí jeho ID
     * @return nalezenou entitu
     *         null - pokud entita s daným ID neexistuje
     */
    public Rozvrhy getRozvrh(Rozvrhy rozvrhy) {
        return rozFac.find(rozvrhy.getIDrozvrhu());
    }
    /**
     * Metoda pro vyhledávání rozvrhu v databázi.
     * @param mist objekt typu Mistnost
     * @return List rozvrhů pro danou místnost
     */
    public List<Rozvrhy> getRozvrhy(Mistnost mist){
        return rozFac.findByMistnost(mist);
    }
    /**
     * Metoda pro vyhledávání rozvrhu v databázi.
     * @param mist objekt typu Mistnost
     * @param den objekt typu DenVTydnu
     * @return List rozvrhů pro danou místnost probíhajících v daném dni
     */
    public List<Rozvrhy> getRozvrhy(Mistnost mist, DenVTydnu den){
        return rozFac.findByMistnostAden(mist, den);
    }
    /**
     * Metoda pro porovnávání dvou objektů typu Rozvrhy
     * @param roz1
     * @param roz2
     * @return výsledek porovnávání(neporovnává se jejich ID pouze jejich obsah)
     */
    public boolean isSame(Rozvrhy roz1, Rozvrhy roz2){
        
        //System.out.println("DNY STEJNE: "+roz1.getIDdnu().equals(roz2.getIDdnu())+" ZACATEK STEJNY: "+roz1.getOd().equals(roz2.getOd())+" KONEC STEJNY: "+roz1.getDo1().equals(roz2.getDo1())+" MISTNOSTI STEJNE: "+roz1.getIDmistnosti().equals(roz2.getIDmistnosti())+" PREDMETY STEJNE: "+roz1.getIDpredmetu().equals(roz2.getIDpredmetu()));
        
        if(roz1.getIDdnu().equals(roz2.getIDdnu()) &&
            roz1.getOd().equals(roz2.getOd()) &&
            roz1.getDo1().equals(roz2.getDo1()) &&
            roz1.getIDmistnosti().equals(roz2.getIDmistnosti()) &&
            roz1.getIDpredmetu().equals(roz2.getIDpredmetu()))
        {
            return true;
        }
        else{
            return false;
        }
        
    }

    
}
