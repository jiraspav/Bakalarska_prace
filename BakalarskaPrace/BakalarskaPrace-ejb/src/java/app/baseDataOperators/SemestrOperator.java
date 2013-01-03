/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app.baseDataOperators;

import dbEntity.Semestr;
import entityFacade.SemestrFacade;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 *
 * @author Pavel
 */
@Stateless
public class SemestrOperator {
    
    private @Inject SemestrFacade semFac;
    
    /**
     * Metoda pro uložení nového semestru do databáze
     * @param idSemestru ID nového semestru
     * @param code kód nového semestru (lze z něj určit rok a další informace)
     * @param zacatek datum začátku nového semestru
     * @param konec datum konce nového semestru
     * @return uložený semestr
     */
    public Semestr createSemestr(Integer idSemestru, String code, Date zacatek, Date konec) {
        
        Semestr semestr = new Semestr(idSemestru, code, zacatek, konec);
        
        
        if(semFac.find(idSemestru) == null){
            System.out.println("Semestr "+semestr.toString());
            semFac.create(semestr);
        }
        return semestr;
                
    }

    /**
     * Metoda pro získání nejnovějšího semestru
     * @return nejnovější semestr z databáze
     *         prázdný semestr - pokud se žádný v databázi nenachází
     */
    public Semestr getLatestSemestr(){
        
        Semestr soucasny;
        List<Semestr> semestry = semFac.findAll();
        if(semestry.size() > 0)
            soucasny = (Semestr) semestry.get(semestry.size()-1);
        else
            soucasny = new Semestr();
        
        return soucasny;
    }
    
    /**
     * Metoda pro odstranění všech semestrů z databáze
     */
    public void deleteAll() {
        semFac.removeAll();
    }
    
}
