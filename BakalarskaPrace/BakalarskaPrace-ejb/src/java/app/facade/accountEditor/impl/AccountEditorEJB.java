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
import app.sessionHolder.SessionHolderEJB;
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
    private @Inject SessionHolderEJB session;
    private @Inject GroupTableOperator groupOper;
    private @Inject UzivatelOperator uzivOper;
    private @Inject RezervaceMistnostiOperator rezOper;
    private @Inject EncryptUtil encrypt;
    
    @Override
    public void addAdminRights(Uzivatel uziv) {
        groupOper.delete(groupOper.getGroupTable(uziv));
        
        groupOper.createGroupTable(uzivOper.getUzivatelLogin(uziv), "admin");
    }

    @Override
    public void removeAdminRights(Uzivatel uziv) {
        List<String> atributy = parsCon.getAtributes(uziv.getLogin(),session.getLoggedUzivatel().getLogin(),session.getPassword());
        String role = atributy.get(2);
        
        groupOper.delete(groupOper.getGroupTable(uziv));
        
        groupOper.createGroupTable(uzivOper.getUzivatelLogin(uziv), role);

    }

    @Override
    public String[] createGuestAccount() {
        
        long idGuesta = getFirstFreeGuestID();

        String loginGuesta = "guest"+idGuesta;
        String randomHeslo = encrypt.randomString(8);
        
        uzivOper.createUzivatel(idGuesta, loginGuesta, encrypt.SHA256(randomHeslo), loginGuesta);
        
        groupOper.createGroupTable(loginGuesta, "guest");
        
        return new String[]{loginGuesta, randomHeslo};
    }

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
