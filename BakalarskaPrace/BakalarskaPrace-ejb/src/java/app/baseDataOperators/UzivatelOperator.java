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
        
        Uzivatel uziv = createUzivatel(1, "superadmin", crypt.SHA256("admin"), "Default administrátor");

        GroupTable groupTab = new GroupTable("superadmin","admin");

        groupFac.create(groupTab);
    }
    
    
    
    public boolean isAdmin(Uzivatel uziv){
        if(groupFac.getGroup(uziv).equals("admin")){
            return true;
        }
        else{
            return false;
        }
    }
    
    public List<Uzivatel> getAll(){
        return uzivFac.findAll();
    }
    
    public Uzivatel getUzivatel(Uzivatel uziv){
        return uzivFac.find(uziv.getIDuser());
    }
    
    public Uzivatel getUzivatel(String name){
        return uzivFac.getUserByLogin(name);
    }

    public String getUzivatelName(Uzivatel uziv) {
        return uziv.getJmeno();
    }
    public String getUzivatelRole(Uzivatel uziv){
        return groupFac.getGroup(uziv);
    }
    public String getUzivatelLogin(Uzivatel uziv){
        return uziv.getLogin();
    }
    public void delete(Uzivatel uziv){
        uzivFac.remove(uziv);
    }
    public int getUzivatelRolePriority(Uzivatel uziv){
        String role = getUzivatelRole(uziv);
        if(role.equals("admin")){
            return 3;
        }
        else if(role.equals("ucitel") || role.equals("guest")){
            return 2;
        }
        else{
            return 1;
        }
    }

}
