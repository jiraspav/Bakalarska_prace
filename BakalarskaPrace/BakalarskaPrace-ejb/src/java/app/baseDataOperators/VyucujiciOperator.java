/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app.baseDataOperators;

import dbEntity.Rozvrhy;
import dbEntity.Vyucujici;
import entityFacade.VyucujiciFacade;
import java.util.Collection;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 *
 * @author Pavel
 */
@Stateless
public class VyucujiciOperator {
    
    private @Inject VyucujiciFacade vyuFac;
    
    
    /**
     * Metoda pro ukládání nových vyučujícívh do databíze.
     * @param iDvyucujici ID vyucujícího
     * @param jmeno celé jméno vyučujícího
     * @param kontakt kontakt na vyučujícího
     * @param rozvrhyCollection rozvrhy, ve kterých vyučující učí
     * @return uloženého vyučujícího
     */
    public Vyucujici createVyucujici(Long iDvyucujici, String jmeno, String kontakt, Collection<Rozvrhy> rozvrhyCollection){
        
        Vyucujici vyucujici = getObject(iDvyucujici, jmeno, kontakt, rozvrhyCollection);
        vyuFac.create(vyucujici);
        
        System.out.println("CREATE VYUCUJICI "+iDvyucujici+" "+jmeno+" "+kontakt);
        
        return vyucujici;
    }
    
    
    /**
     * Metoda pro získání nového objektu typu Vyučující
     * @param iDvyucujici ID vyučujícího
     * @param jmeno celé jméno vyučujícího
     * @param kontakt kontakt na vyučujícího
     * @param rozvrhyCollection rozvrhy, ve kterých vyučující učí
     * @return nový objekt Vyucujici
     */
    public Vyucujici getObject(Long iDvyucujici, String jmeno, String kontakt, Collection<Rozvrhy> rozvrhyCollection){
        
        return new Vyucujici(iDvyucujici, jmeno, kontakt, rozvrhyCollection);
        
    }
    
    /**
     * Metoda určená pro aktualizaci dat vyučujícího v databázi
     * @param v objekt Vyucujici s novými daty pro aktualizaci
     */
    public void update(Vyucujici v){
        vyuFac.edit(v);
    }
    
    /**
     * Metoda pro získání všech vyučujících v databázi.
     * @return List všech vyučujících
     */
    public List<Vyucujici> getAll(){
        return vyuFac.findAll();
    }
    /**
     * Metoda pro vyhledání konkrétnícho vyučujícího v databázi podle jeho ID
     * @param id objekt Vyucujici s nastaveným ID
     * @return nalezeného vyučujícího z databáze
     *         null - pokud se v databázi nenachází žádný vyučující s tímto ID
     */
    public Vyucujici getVyucujici(Vyucujici id){
        return vyuFac.find(id.getiDvyucujici());
    }
    /**
     * Metoda pro získání vyučujících jejichž jméno obsahuje zadaný parametr
     * @param name část jména pro vyhledávání
     * @return List vyučujících jejichž jméno obsahuje zadaný parametr
     */
    public List<Vyucujici> getVyucujici(String name){
        return vyuFac.findByName(name);
    }
    /**
     * Metoda pro smazání veškerých vyučujících z databáze
     */
    public void deleteAll(){
        vyuFac.removeAll();
    }
}

