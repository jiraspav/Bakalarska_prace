/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app.baseDataOperators;

import dbEntity.GroupTable;
import entityFacade.GroupTableFacade;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 *
 * @author Pavel
 */
@Stateless
public class GroupTableOperator {
    
    private @Inject GroupTableFacade groupFac;
    
    public GroupTable createGroupTable(String login, String role){
        
        GroupTable groupTable = new GroupTable(login, role);
        
        groupFac.create(groupTable);
        
        return groupTable;
    }
    
}
