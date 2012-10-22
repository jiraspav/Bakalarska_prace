/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans.admin;

import app.DataCreator.DataCreator;
import XMLparser.ParserController;
import view.bundle.ResourceBundleOperator;
import app.encrypt.EncryptUtil;
import app.facesMessenger.FacesMessengerUtil;
import view.auth.LoginVerifier;
import dbEntity.*;
import entityFacade.*;
import java.io.Serializable;
import java.util.*;
import javax.ejb.Asynchronous;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.component.datatable.DataTable;

/**
 *
 * @author Pavel
 */

@Named("AdminAccEditor")
@SessionScoped

public class AdminAccountEditor implements Serializable{
    
    private @Inject RezervaceMistnostiFacade rezFac;
    private @Inject MistnostFacade mistFac;
    private @Inject StrediskoFacade strFac;
    private @Inject PredmetyFacade predFac;
    private @Inject SemestrFacade semFac;
    private @Inject RozvrhyFacade rozFac;
    private @Inject UpdateRozvrhuFacade upRozFac;
    
    private @Inject DenVTydnuFacade denFac;
    
    private @Inject LoginVerifier user;
    private @Inject ParserController parsCon;
    private @Inject UzivatelFacade uzivFac;
    private @Inject GroupTableFacade groupFac;
    private @Inject EncryptUtil encrypt;
    private @Inject FacesMessengerUtil messUtil;
    private @Inject ResourceBundleOperator bundle;
    
    private DataModel items;
    private Uzivatel selectedRow;
    private Uzivatel selectedGuestRow;
    private String guestLogin,guestPassword;
    private String confDialog;
    
    /**
     * 
     */
    public AdminAccountEditor(){}
    
    /**
     * metoda zjišťující, zda je označen guest uživatel
     * pokud není označen nikdo přidává FacesMessage ERROR zprávu
     * stejně tak pokud není označen guest uživatel
     */
    public void checkGuest(){
        
        if(selectedRow != null){
            if(!selectedRow.getLogin().startsWith("guest")){
                messUtil.addFacesMsgError("Vybraný účet není guest účet.");
            }
        }
        else{
            messUtil.addFacesMsgError(bundle.getMsg("sysMsgNoSellected"));
        }
    }
    /**
     * metoda zajišťující přidáni admin role zvolenému uživateli.
     * Zároveň kontroluje, zda označený uživatel splňuje potřebná kritéria.
     * Těmi jsou : musí být někdo označen, nesmí to být guest účet(guest účty nemohou mít admin roli)
     * 
     */
    public void pridejAdminRoli(){
        if(selectedRow != null){
            if(selectedRow.getLogin().startsWith("guest")){
                messUtil.addFacesMsgError(bundle.getMsg("sysMsgGuestNoAdmin"));
            }
            else{
                GroupTable group = groupFac.getGroupTableByUziv(selectedRow);
                GrouptablePK groupPK = group.getGrouptablePK();
                
                group.setGrouptablePK(groupPK);
                
                groupFac.remove(group);
                
                groupPK.setGroupid("admin");
                
                GroupTable nova = new GroupTable(groupPK);
                nova.setLogin(group.getLogin());
                groupFac.create(group);

                messUtil.addFacesMsgInfo("Role byla úspěšně změněna.");
            }
        }
        else{
            messUtil.addFacesMsgError(bundle.getMsg("sysMsgNoSellected"));
        }
    }
    /**
     * metoda zajišťující odebrání administrátorské role označenému uživateli.
     * Zároveň provádí kontrolu, zda označený uživatel splňuje kritéria.
     * Těmi jsou : je někdo označen, označený uživatel není zároveň přihlášeným uživatelem
     */
    public void odeberAdminRoli(){
        if(selectedRow != null){
            if(selectedRow.getLogin().equals(user.getLogin())){
                messUtil.addFacesMsgError("Sám sobě nemůžete odebírat roli administrátora.");
            }
            else if(groupFac.getGroup(selectedRow).equals("admin")){
                String role;
                if(selectedRow.getLogin().startsWith("guest")){
                    role = "guest";
                }
                else{
                List<String> atributy = parsCon.getAtributes(selectedRow.getLogin(),user.getLogin(),user.getPassword());
                 role = atributy.get(2);
                }
                GroupTable group = groupFac.getGroupTableByUziv(selectedRow);
                GrouptablePK groupPK = group.getGrouptablePK();
                
                group.setGrouptablePK(groupPK);
                
                groupFac.remove(group);
                
                groupPK.setGroupid(role);
                GroupTable nova = new GroupTable(groupPK);
                nova.setLogin(group.getLogin());
                
                groupFac.create(nova);
                
                
                messUtil.addFacesMsgInfo("Role byla úspěšně změněna.");
            }
            else{
                messUtil.addFacesMsgError("Uživatel není administrátor");
            }
        }
        else{
            messUtil.addFacesMsgError(bundle.getMsg("sysMsgNoSellected"));
        }
        
        
    }
    /**
     * metoda pro vytváření nových guest účtů
     * nejdříve zjistí nejmenší možné číslo za guest jménem, které není v databázi
     * poté vygeneruje heslo a vytvoří guest účet
     */
    public void vytvorGuestUcet(){
        long idGuesta = 1;
        boolean running = true;
        while(running){
            if(uzivFac.find(new Long(idGuesta)) == null)
                running = false;
            else{
                idGuesta++;
            }
        }
        String loginGuesta = "guest"+idGuesta;
        String randomHeslo = randomString(8);
        
        Uzivatel uziv = new Uzivatel(new Long(idGuesta));
        uziv.setLogin(loginGuesta);
        uziv.setHeslo(encrypt.SHA256(randomHeslo));
        uziv.setJmeno(loginGuesta);
        
        uzivFac.create(uziv);
        
        
        GroupTable group = new GroupTable(loginGuesta, "guest");
        group.setLogin(loginGuesta);
        
        groupFac.create(group);
        
        guestLogin = loginGuesta;
        guestPassword = randomHeslo;
        
    }
    /**
     * metoda zajišťující odebrání guest účtu.
     * zároveň kotroluje, zda je někdo označen a jestli skutečně je guest 
     * 
     */
    public void zrusGuestUcet(){
        
        if(selectedRow != null){
            
            if(!selectedRow.getLogin().startsWith("guest")){
                messUtil.addFacesMsgError("Nemáte označeného uživatele s rolí guest.");
                
            }
            else{
                List<RezervaceMistnosti> rezervace = rezFac.getRezervaceByUserID(selectedRow);
                for (RezervaceMistnosti rez : rezervace) {
                    rezFac.remove(rez);
                }
                groupFac.remove(groupFac.getGroupTableByUziv(selectedRow));
                uzivFac.remove(selectedRow);
                
                
                messUtil.addFacesMsgInfo("Guest účet "+selectedRow.getLogin()+" byl úspěšně smazán.");
                
                selectedRow = null;
            }
        }
        else{
            messUtil.addFacesMsgError(bundle.getMsg("sysMsgNoSellected"));
        }
    }
    
    
    String randomString( int len ){
        
        String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random rnd = new Random();
        
        StringBuilder sb = new StringBuilder( len );
        for( int i = 0; i < len; i++ ) 
            sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
        return sb.toString();
    }
    
    
    /**
     * @return the items
     */
    public DataModel getItems() {
        
        items = new ListDataModel(uzivFac.findAll());
        
        return items;
    }

    /**
     * @param items the items to set
     */
    public void setItems(DataModel items) {
        this.items = items;
    }

    /**
     * @return the selectedRow
     */
    public Uzivatel getSelectedRow() {
        return selectedRow;
    }

    /**
     * @param selectedRow the selectedRow to set
     */
    public void setSelectedRow(Uzivatel selectedRow) {
        this.selectedRow = selectedRow;
    }

    /**
     * @return the selectedGuestRow
     */
    public Uzivatel getSelectedGuestRow() {
        return selectedGuestRow;
    }

    /**
     * @param selectedGuestRow the selectedGuestRow to set
     */
    public void setSelectedGuestRow(Uzivatel selectedGuestRow) {
        this.selectedGuestRow = selectedGuestRow;
    }

    /**
     * @return the guestLogin
     */
    public String getGuestLogin() {
        return guestLogin;
    }

    /**
     * @param guestLogin the guestLogin to set
     */
    public void setGuestLogin(String guestLogin) {
        this.guestLogin = guestLogin;
    }

    /**
     * @return the guestPassword
     */
    public String getGuestPassword() {
        return guestPassword;
    }

    /**
     * @param guestPassword the guestPassword to set
     */
    public void setGuestPassword(String guestPassword) {
        this.guestPassword = guestPassword;
    }

    /**
     * metoda vracející zprávu, která se zobrazí uživateli, na základě kritérií
     * kterými jsou : je někdo označen, je to guest
     * @return the confDialog
     */
    public String getConfDialog() {
        if(selectedRow != null){
            confDialog = "Skutečně smazat? Budou zrušeny všechny rezervace vázané na tento účet.";
            if(!selectedRow.getLogin().startsWith("guest")){
                confDialog = "Označený uživatel "+selectedRow.getLogin()+" není guest účet.";
            }
        }
        else{
            confDialog = "Nemáte označeného žadné uživatele.";
        }
        return confDialog;
    }

    /**
     * @param confDialog the confDialog to set
     */
    public void setConfDialog(String confDialog) {
        this.confDialog = confDialog;
    }
    
}
