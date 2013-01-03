/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app.baseDataOperators;

import dbEntity.Mistnost;
import dbEntity.RezervaceMistnosti;
import dbEntity.Stredisko;
import entityFacade.MistnostFacade;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 *
 * @author Pavel
 */
@Stateless
public class MistnostOperator {
    
    private @Inject MistnostFacade misFac;
    private @Inject TypMistnostiOperator typOper;
    private @Inject RezervaceMistnostiOperator rezOper;

    /**
     * Metoda určená k uložení nové entity do databáze
     * @param id Long reprezentace ID entity
     * @param code String reprezentace kódu místnosti
     * @param stredisko stredisko, ke kterému vytvářená místnost patří 
     * @return uloženou entitu
     */
    public Mistnost createMistnost(Long id, String code, Stredisko stredisko) {
        
        Mistnost mistnost = new Mistnost(id, code);
        mistnost.setIDstrediska(stredisko);
        
        //DEFAULT VALUES 
        
        mistnost.setKapacita(50);
        mistnost.setiDtyp(typOper.getTyp("Učebna"));
        mistnost.setLokalita("Dejvice");
        
        //
        System.out.println("Mistnost "+mistnost.getZkratka()+" stredisko id: "+stredisko.getIDstredisko()+" "+stredisko.getNazev());
        
        misFac.create(mistnost);
        
        return mistnost;
    }

    /**
     * Metoda pro získání všech místností v databázi
     * @return List všech místnotí v databázi
     */
    public List<Mistnost> getAll() {
        return misFac.findAll();
    }
    /**
     * Metoda pro získání místností z databáze podle zadaných parametrů
     * @param stred středisko, podle kterého se vyhledávájí místnosti
     * @return List místností, které se nacházejí v daném středisku
     */
    public List<Mistnost> getMistnosti(Stredisko stred){
        return misFac.findMistnostiStrediska(stred);
    }   
    /**
     * Metoda pro získání místností podle zadaných parametrů
     * @param zkratka kód místnosti pro vyhledávání
     * @return místnost se zadaným kódem
     *         null - pokud se místnost s tímto kódem v databázi nenachází
     */
    public Mistnost getMistnost(String zkratka){
        return misFac.findMistnostPodleZkratky(zkratka);
    }


}
