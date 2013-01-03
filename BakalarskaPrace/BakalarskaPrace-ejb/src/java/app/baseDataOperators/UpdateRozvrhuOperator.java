/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app.baseDataOperators;

import dbEntity.Semestr;
import dbEntity.UpdateRozvrhu;
import entityFacade.UpdateRozvrhuFacade;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 *
 * @author Pavel
 */
@Stateless
public class UpdateRozvrhuOperator {
    
    private @Inject UpdateRozvrhuFacade updateFac;
    
    
    /**
     * Metoda pro vytvoření a uložení nového objektu UpdateRozvrhu, který reprezentuje
     * datum poslední aktualizace
     * @param integer ID aktualizace
     * @param current semestr ke kterému se aktualizace vztahuje
     * @param date datum aktualizace
     * @return vrací uloženou aktualizaci
     */
    public UpdateRozvrhu createUpdateRozvrhu(Integer integer, Semestr current, Date date) {
        
        UpdateRozvrhu update = new UpdateRozvrhu(integer, date);
        update.setIDsemestru(current);
        
        System.out.println("UpdateRozvrhu "+update.toString());
        
        updateFac.create(update);
        
        return update;
        
    }
    
    /**
     * Metoda pro získání poslední aktualizace.
     * @return poslední aktualizaci
     */
    public UpdateRozvrhu getLastUpdate() {
        
        UpdateRozvrhu last;
            
        List<UpdateRozvrhu> vsechnyUpdaty = updateFac.findAll();
        if(vsechnyUpdaty.size() > 0){
            last = vsechnyUpdaty.get(vsechnyUpdaty.size()-1);
        }
        else{
            last = new UpdateRozvrhu(1, new Date());
        }
        return last;
    }

    /**
     * Metoda pro odstranění celé historia aktualizací
     */
    public void deleteAll() {
        updateFac.removeAll();
    }
    
}
