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
    
    
    public UpdateRozvrhu createUpdateRozvrhu(Integer integer, Semestr current, Date date) {
        
        UpdateRozvrhu update = new UpdateRozvrhu(integer, date);
        update.setIDsemestru(current);
        
        System.out.println("UpdateRozvrhu "+update.toString());
        
        updateFac.create(update);
        
        return update;
        
    }
    
    public UpdateRozvrhu getLastUpdate() {
        
        UpdateRozvrhu last;
            
        List<UpdateRozvrhu> vsechnyUpdaty = updateFac.findAll();
        if(vsechnyUpdaty.size() > 0)
            last = vsechnyUpdaty.get(vsechnyUpdaty.size()-1);
        else
            last = new UpdateRozvrhu(1, new Date());
        
        return last;
    }

    public void deleteAll() {
        updateFac.removeAll();
    }
    
}
