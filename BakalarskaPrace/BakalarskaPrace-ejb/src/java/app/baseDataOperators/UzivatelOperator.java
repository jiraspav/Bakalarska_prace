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
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 *
 * @author Pavel
 */
@Stateless
public class UzivatelOperator {
    
    private @Inject UzivatelFacade uzivFac;
    private @Inject GroupTableFacade groupFac;
    private @Inject EncryptUtil crypt;
    
    public Uzivatel createUzivatel(long id, String login, String password, String name){
        
        Uzivatel uziv = new Uzivatel(id, login, name);
        uziv.setHeslo(crypt.SHA256(password));
        
        System.out.println("Uzivatel "+uziv.getJmeno());
        
        uzivFac.create(uziv);

        return uziv;
    }
    
    public void buildDefaultUzivatel(){
        
        createUzivatel(1, "superadmin", crypt.SHA256("admin"), "Default administrátor");

        
        GroupTable groupTab = new GroupTable("superadmin","admin");
        groupTab.setLogin("superadmin");

        groupFac.create(groupTab);
    }
}
