/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app.facade.accountEditor.impl;

import app.XMLparser.ParserController;
import app.baseDataOperators.GroupTableOperator;
import app.baseDataOperators.RezervaceMistnostiOperator;
import app.baseDataOperators.UzivatelOperator;
import app.encrypt.EncryptUtil;
import app.facade.accountEditor.AccountEditorFacade;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import dbEntity.GroupTable;
import dbEntity.GrouptablePK;
import dbEntity.RezervaceMistnosti;
import dbEntity.Uzivatel;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 *
 * @author Pavel
 */
@Stateless
@Local(AccountEditorFacade.class)
public class AccountEditorEJB implements AccountEditorFacade{

    private @Inject ParserController parsCon;
    private @Inject GroupTableOperator groupOper;
    private @Inject UzivatelOperator uzivOper;
    private @Inject RezervaceMistnostiOperator rezOper;
    private @Inject EncryptUtil encrypt;
    
    /**
     * Metoda pro přidání administrátorských práv danému uživateli
     * @param uziv uživatel, kterému mají být přidány administrátorská práva
     */
    @Override
    public void addAdminRights(Uzivatel uziv) {
        groupOper.delete(groupOper.getGroupTable(uziv));
        
        groupOper.createGroupTable(uzivOper.getUzivatelLogin(uziv), "admin");
    }

    /**
     * Metoda pro odebrání administrátorských práv uživateli
     * @param uziv uživatel, kterému mají být odebrány práva
     * @param logged uživatel, který tyto práva odstraňuje (kvůli připojení do kosapi)
     */
    @Override
    public void removeAdminRights(Uzivatel uziv, Uzivatel logged) {
        List<String> atributy = parsCon.getAtributes(uziv.getLogin(),logged.getLogin(),encrypt.decode(logged.getHeslo()));
        String role;
        try{
            
            role = atributy.get(2);
        
        }catch(NullPointerException e){
            role = "student";
            System.out.println("UZIVATEL IS NOT IN KOSAPI DATABASE!");
        }
        groupOper.delete(groupOper.getGroupTable(uziv));

        groupOper.createGroupTable(uzivOper.getUzivatelLogin(uziv), role);
    }

    /**
     * Metoda pro vytváření nových guest účtů
     * @return pole atributů nového účtu
     *         pozice 0 - login účtu
     *         pozice 1 - heslo účtu
     */
    @Override
    public String[] createGuestAccount() {
        
        long idGuesta = getFirstFreeGuestID();

        String loginGuesta = "guest"+idGuesta;
        String randomHeslo = encrypt.randomString(8);
        
        uzivOper.createUzivatel(idGuesta, loginGuesta, encrypt.SHA256(randomHeslo), loginGuesta, "Nemá uveden.");
        
        groupOper.createGroupTable(loginGuesta, "guest");
        
        return new String[]{loginGuesta, randomHeslo};
    }

    /**
     * Metoda pro úpravu uživatelů. Je však možné upravovat pouze takové atributy,
     * které se neobjevují jinde jako cizí klíče nebo jimi sami nejsou
     * @param uziv uzivatel s novými atributy
     */
    @Override
    public void editAccount(Uzivatel uziv){
        uzivOper.edit(uziv);
    }
    
    /**
     * Metoda pro odstranění uživatelského účtu. Zahrnuje odstranění všech rezervací
     * vázaných na tento účet.
     * @param uziv uživatel jehož účet se má rušit
     */
    @Override
    public void deleteAccount(Uzivatel uziv) {
        
        List<RezervaceMistnosti> rezervace = rezOper.getRezervace(uziv);
         
        //remove reservations of this user
        for (RezervaceMistnosti rez : rezervace) {
            rezOper.delete(rez);
        }
        //delete grouptable
        groupOper.delete(groupOper.getGroupTable(uziv));
        //delete user
        uzivOper.delete(uziv);
    }
    
    /**
     * Metoda pro zjištění, zda se v databázi nachází poslední admin.
     * @param uziv uživatel, pro kterého se zjišťuje, zda je posledním administrátorem
     * @return true - pokud je uživatel posledním administrátorem
     *         false - pokud ne
     */
    @Override
    public boolean isLastAdmin(Uzivatel uziv) {
        
        if(!uzivOper.getUzivatelRole(uziv).equals("admin")){
            return false;
        }
        else{
            if(groupOper.getAllWithOneRole("admin").size() == 1){
                return true;
            }
            else {return false;}
        }
        
        
    }
    
    private long getFirstFreeGuestID(){
        long idGuesta = 1;
        boolean running = true;
        
        while(running){
            if(uzivOper.getUzivatel(new Uzivatel(idGuesta)) == null){
                running = false;
            }
            else{
                idGuesta++;
            }
        }
        
        return idGuesta;
    }
    
}
