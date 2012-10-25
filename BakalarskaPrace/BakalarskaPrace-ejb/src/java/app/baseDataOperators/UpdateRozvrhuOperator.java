/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app.baseDataOperators;

import dbEntity.Semestr;
import dbEntity.UpdateRozvrhu;
import entityFacade.UpdateRozvrhuFacade;
import java.util.Date;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 *
 * @author Pavel
 */
@Stateless
public class UpdateRozvrhuOperator {
    
    private @Inject UpdateRozvrhuFacade updateFac;
    
    
    public UpdateRozvrhu createUpdateRozvrhu(Integer integer, Semestr current, Date date) {
        
        UpdateRozvrhu update = new UpdateRozvrhu(integer, date);
        update.setIDsemestru(current);
        
        updateFac.create(update);
        
        System.out.println("UpdateRozvrhu "+update.toString());
        
        return update;
        
    }
    
}
