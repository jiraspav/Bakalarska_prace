/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package view.admin;

import app.baseDataOperators.UzivatelOperator;
import app.facade.accountEditor.AccountEditorFacade;
import dbEntity.Uzivatel;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.inject.Inject;
import javax.inject.Named;
import view.SessionHolder.SessionHolderMB;
import view.bundle.ResourceBundleOperator;
import view.facesMessenger.FacesMessengerUtil;

/**
 *
 * @author Pavel
 */

@Named("AdminAccEditor")
@SessionScoped

public class AdminAccountEditorMB implements Serializable{
    
    private @Inject AccountEditorFacade accFac;
    private @Inject SessionHolderMB session;
    private @Inject FacesMessengerUtil messUtil;
    private @Inject ResourceBundleOperator bundle;
    private @Inject UzivatelOperator uzivOper;
    
    private DataModel items;
    private Uzivatel selectedRow;
    private String guestLogin,guestPassword;
    private String confDialog;
    
    /**
     * 
     */
    public AdminAccountEditorMB(){}
    
    /**
     * metoda zjišťující, zda je označen guest uživatel
     * pokud není označen nikdo přidává FacesMessage ERROR zprávu
     * stejně tak pokud není označen guest uživatel
     */
    public void checkGuest(){
        
        if(selectedRow != null){
            if(!selectedRow.getLogin().startsWith("guest")){
                messUtil.addFacesMsgError(bundle.getMsg("sysMsgNotGuest"));
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
    public void addAdminRights(){
        if(selectedRow != null){
            if(selectedRow.getLogin().startsWith("guest")){
                messUtil.addFacesMsgError(bundle.getMsg("sysMsgGuestNoAdmin"));
            }
            else{
                accFac.addAdminRights(selectedRow);

                messUtil.addFacesMsgInfo(bundle.getMsg("sysMsgRoleChanged"));
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
    public void removeAdminRights(){
        if(selectedRow != null){
            
            if(selectedRow.getLogin().equals(session.getLoggedUzivatelLogin())){
                messUtil.addFacesMsgError(bundle.getMsg("sysMsgSelfDeadmin"));
            }
            else if(uzivOper.isAdmin(selectedRow)){
                accFac.removeAdminRights(selectedRow);
                
                messUtil.addFacesMsgInfo(bundle.getMsg("sysMsgRoleChanged"));
            }
            else{
                messUtil.addFacesMsgError(bundle.getMsg("sysMsgNotAdmin"));
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
        
        String[] atributes = accFac.createGuestAccount();
        
        guestLogin = atributes[0];
        guestPassword = atributes[1];
        
    }
    /**
     * metoda zajišťující odebrání guest účtu.
     * zároveň kotroluje, zda je někdo označen a jestli skutečně je guest 
     * 
     */
    public void deleteAccount(){
        
        if(selectedRow != null){
            
            if(accFac.isLastAdmin(selectedRow)){
                messUtil.addFacesMsgError(bundle.getMsg("sysMsgNoGuestSellected"));
            }
            else{
                accFac.deleteAccount(selectedRow);
                
                messUtil.addFacesMsgInfo(bundle.getMsg("sysMsgAccountDeletedA") +" "+selectedRow.getLogin()+" "+bundle.getMsg("sysMsgAccountDeletedB"));
                
                selectedRow = null;
            }
            
        }
        else{
            messUtil.addFacesMsgError(bundle.getMsg("sysMsgNoSellected"));
        }
    }
    
    

    public String getRole(Uzivatel uziv){
        return uzivOper.getUzivatelRole(uziv);
    }
    
    /**
     * @return the items
     */
    public DataModel getItems() {
        
        items = new ListDataModel(uzivOper.getAll());
        
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
            confDialog = bundle.getMsg("adminAccountEditorLabelConfDialog");

        }
        else{
            confDialog = bundle.getMsg("sysMsgNoSellected");
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
