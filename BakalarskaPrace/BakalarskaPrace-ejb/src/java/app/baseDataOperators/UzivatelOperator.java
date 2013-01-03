/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app.baseDataOperators;

import app.encrypt.EncryptUtil;
import dbEntity.GroupTable;
import dbEntity.Uzivatel;
import entityFacade.DenVTydnuFacade;
import entityFacade.GroupTableFacade;
import entityFacade.UzivatelFacade;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Pavel
 */
@Stateless
public class UzivatelOperator {
    
    private @Inject UzivatelFacade uzivFac;
    private @Inject GroupTableFacade groupFac;
    private @Inject EncryptUtil crypt;
    
    /**
     * Metoda pro uložení nové entity do databáze.
     * @param id ID nového uživatele
     * @param login login nového uživatele
     * @param password heslo nového uživatele
     * @param name jméno nového uživatele
     * @return uloženou entitu
     */
    public Uzivatel createUzivatel(long id, String login, String password, String name, String kontakt){
        
        Uzivatel uziv = new Uzivatel(id, login, name,kontakt);
        uziv.setHeslo(crypt.encode(password));
        
        System.out.println("CREATE Uzivatel "+uziv.getJmeno());
        
        uzivFac.create(uziv);

        return uziv;
    }
    
    /**
     * Metoda vytvářející defaultní uživatele v databázi při spuštění aplikace.
     */
    public void buildDefaultUzivatel(){
        
        Uzivatel uziv = createUzivatel(1, "superadmin", "admin", "Default administrátor", "Nemá uveden.");

        GroupTable groupTab = new GroupTable("superadmin","admin");

        groupFac.create(groupTab);
        
        uziv = createUzivatel(2, "testucitel", "admin", "Default učitel", "Nemá uveden.");

        groupTab = new GroupTable("testucitel","ucitel");

        groupFac.create(groupTab);
        
        uziv = createUzivatel(3, "teststudent", "admin", "Default student", "Nemá uveden.");

        groupTab = new GroupTable("teststudent","student");

        groupFac.create(groupTab);
    }
    
    
    
    /**
     * Metoda pro určení, zda je daným uživatel admin či nikoliv.
     * @param uziv zkoumaný uživatel
     * @return true - pokud je daný uživatel adminem
     *         false - pokud není daný uživatel adminem
     */        
    public boolean isAdmin(Uzivatel uziv){
        if(groupFac.getGroup(uziv).equals("admin")){
            return true;
        }
        else{
            return false;
        }
    }
    
    /**
     * Metoda pro získání veškerých uživatelů v databázi
     * @return List všech uložených uživatelů
     */
    public List<Uzivatel> getAll(){
        return uzivFac.findAll();
    }
    
    /**
     * Metoda pro získání uživatelů z databáze podle daných parametrů
     * Přes ID parametru uziv se vyhledává uživatel v databázi.
     * @param uziv daný objekt typu Uživatel
     * @return nalezenou entitu Uzivatel se stejným ID
     *         null - pokud taková entita není
     */
    public Uzivatel getUzivatel(Uzivatel uziv){
        return uzivFac.find(uziv.getIDuser());
    }
    
    /**
     * Metoda pro získání uživatelů z databáze podle daných parametrů
     * @param login login hledaného uživatele
     * @return nalezenou entitu Uzivatel se stejným loginem
     *         null - pokud taková entita není
     */
    public Uzivatel getUzivatel(String login){
        return uzivFac.getUserByLogin(login);
    }

    /**
     * Metoda pro získání jména daného uživatele
     * @param uziv 
     * @return jméno daného uživatele
     */
    public String getUzivatelName(Uzivatel uziv) {
        return uziv.getJmeno();
    }
    /**
     * Metoda pro získání role daného uživatele
     * @param uziv
     * @return roli daného uživatele
     */
    public String getUzivatelRole(Uzivatel uziv){
        return groupFac.getGroup(uziv);
    }
    /**
     * Metoda pro získání loginu daného uživatele
     * @param uziv
     * @return login daného uživatele
     */
    public String getUzivatelLogin(Uzivatel uziv){
        return uziv.getLogin();
    }
    /**
     * Metoda určená pro aktualizaci dat v databázi daného uživatele
     * @param uziv
     */
    public void edit(Uzivatel uziv){
        System.out.println("EDIT USER");
        uzivFac.edit(uziv);
    }
    /**
     * Metoda určená k odstranění daného uživatele z databáze
     * @param uziv
     */
    public void delete(Uzivatel uziv){
        System.out.println("DELETE USER");
        uzivFac.remove(uziv);
    }
    /**
     * Metoda pro získání priority podle role daného uživatele
     * @param uziv
     * @return 3 - pokud je uživatel admin
     *         2 - pokud je uživatel učitel nebo guest
     *         1 - pokud je uživatel student
     */     
    public int getUzivatelRolePriority(Uzivatel uziv){
        String role = getUzivatelRole(uziv);
        if(role.equals("admin")){
            return 3;
        }
        else if(role.equals("ucitel") || role.equals("guest")){
            return 2;
        }
        else if(role.equals("student")){
            return 1;
        }else{
            try {
                throw new Exception("Unknown role : "+role);
            } catch (Exception ex) {
                Logger.getLogger(UzivatelOperator.class.getName()).log(Level.SEVERE, null, ex);
                return -1;
            }
        }
    }

    /**
     * @param uzivFac the uzivFac to set
     */
    public void setUzivFac(UzivatelFacade uzivFac) {
        this.uzivFac = uzivFac;
    }

    /**
     * @param groupFac the groupFac to set
     */
    public void setGroupFac(GroupTableFacade groupFac) {
        this.groupFac = groupFac;
    }

    /**
     * @param crypt the crypt to set
     */
    public void setCrypt(EncryptUtil crypt) {
        this.crypt = crypt;
    }

}
