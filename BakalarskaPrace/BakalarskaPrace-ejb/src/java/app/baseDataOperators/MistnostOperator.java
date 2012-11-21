/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app.baseDataOperators;

import dbEntity.Mistnost;
import dbEntity.Stredisko;
import entityFacade.MistnostFacade;
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
    

    public Mistnost createMistnost(Long id, String code, Stredisko stredisko) {
        
        Mistnost mistnost = new Mistnost(id, code);
        mistnost.setIDstrediska(stredisko);
        
        System.out.println("Mistnost "+mistnost.getZkratka());
        
        misFac.create(mistnost);
        
        return mistnost;
    }

    public List<Mistnost> getAll() {
        return misFac.findAll();
    }
    public List<Mistnost> getMistnosti(Stredisko stred){
        return misFac.findMistnostiStrediska(stred);
    }   
    public Mistnost getMistnost(String zkratka){
        return misFac.findMistnostPodleZkratky(zkratka);
    }
}
