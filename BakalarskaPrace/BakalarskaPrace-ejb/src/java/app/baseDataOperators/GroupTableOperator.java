/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app.baseDataOperators;

import dbEntity.GroupTable;
import dbEntity.Uzivatel;
import entityFacade.GroupTableFacade;
import java.util.List;
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
    
    
    public List<GroupTable> getAllWithOneRole(String role){
        return groupFac.getGroupTableByRole(role);
    }
    public GroupTable getGroupTable(Uzivatel uziv){
        return groupFac.getGroupTableByUziv(uziv);
    }
    public GroupTable getGroupTable(String login){
        return groupFac.getGroupTableByUziv(login);
    }
    public GroupTable getGroupTable(GroupTable gTable){
        return groupFac.find(gTable);
    }
    
    public void delete(GroupTable gTable){
        groupFac.remove(gTable);
    }
    
}
