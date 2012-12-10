/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package view.admin;

import app.baseDataOperators.SemestrOperator;
import app.facade.databaseRefresh.DatabaseRefreshFacade;
import dbEntity.Semestr;
import java.io.Serializable;
import javax.ejb.Asynchronous;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import view.SessionHolder.SessionHolderMB;
import view.bundle.ResourceBundleOperator;
import view.facesMessenger.FacesMessengerUtil;

/**
 *
 * @author Pavel
 */
@Named(value = "adminPageMB")
@RequestScoped
public class AdminPageMB implements Serializable{
    
    private @Inject SemestrOperator semOper;
    private @Inject DatabaseRefreshFacade dbFac;
    private @Inject FacesMessengerUtil messUtil;
    private @Inject ResourceBundleOperator bundle;
    private @Inject SessionHolderMB session;
    
    private String confDialog;
    private boolean ready = true;
    
    /**
     * 
     */
    public AdminPageMB() {
    }

    /**
     * @return the soucasny
     */
    public Semestr getSoucasny() {
        return semOper.getLatestSemestr();
    }

    /**
     * @return the posledniAktualizace
     */
    public String getLastUpdate() {
        return dbFac.getLatestUpdate();
    }
    
    /**
     * metoda zajišťující aktualizaci databáze
     */
    @Asynchronous
    public void refreshDatabase(){
        if(ready){
            ready = false;
            
            if(session.getLoggedUzivatelLogin().equals("superadmi")){
                messUtil.addFacesMsgError(bundle.getMsg("sysMsgDefaultAdminUpdateDBRestricted"));
            }
            else{
                dbFac.refreshDatabase();
                messUtil.addFacesMsgInfo(bundle.getMsg("sysMsgUpdateComp"));
            }
            
            ready = true;
        }
    }
    
    /**
     * @return the confDialog
     */
    public String getConfDialog() {
        if(ready){
            confDialog = bundle.getMsg("adminConfDialogStarting");
        }
        else{
            confDialog = bundle.getMsg("adminConfDialogInProgress");
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
