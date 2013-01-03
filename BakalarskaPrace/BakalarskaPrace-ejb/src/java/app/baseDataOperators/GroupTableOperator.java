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
    
    /**
     * Metoda pro vytvoření role uživatele s daným loginem a jeho uložení do databáze.
     * @param login login uživatele v databázi
     * @param role role uživatele, možné vstupy jsou admin,student,guest,ucitel
     * @return uloženou roli
     */
    public GroupTable createGroupTable(String login, String role){
        
        GroupTable groupTable = new GroupTable(login, role);
        
        groupFac.create(groupTable);
        
        return groupTable;
    }
    
    
    /**
     * Metoda pro získání všech rolí z databáze podle zadaných parametrů
     * @param role název role
     * @return List všech rolí se stejným názvem
     */
    public List<GroupTable> getAllWithOneRole(String role){
        return groupFac.getGroupTableByRole(role);
    }
    /**
     * Metoda pro získání všech rolí z databáze podle zadaných parametrů
     * @param uziv zadaný uživatel pro kterého hledám roli
     * @return roli uživatele
     *         null - pokud takový uživatel neexistuje
     */
    public GroupTable getGroupTable(Uzivatel uziv){
        return groupFac.getGroupTableByUziv(uziv);
    }
    /**
     * Metoda pro získání všech rolí z databáze podle zadaných parametrů
     * @param login
     * @return roli uzivatele se stejným loginem
     *         null - pokud uživatel s takovým loginem neexistuje
     */
    public GroupTable getGroupTable(String login){
        return groupFac.getGroupTableByUziv(login);
    }
    /**
     * Metoda pro získání všech rolí z databáze podle zadaných parametrů
     * @param gTable
     * @return roli z databáze
     */
    public GroupTable getGroupTable(GroupTable gTable){
        return groupFac.find(gTable);
    }
    
    /**
     * Metoda pro odstranění role z databáze
     * @param gTable role určená pro odstranění
     */
    public void delete(GroupTable gTable){
        groupFac.remove(gTable);
    }
    
}
