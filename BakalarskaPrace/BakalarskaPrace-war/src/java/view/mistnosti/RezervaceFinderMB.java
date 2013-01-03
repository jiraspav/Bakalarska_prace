/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package view.mistnosti;

import app.baseDataOperators.RezervaceMistnostiOperator;
import app.baseDataOperators.UzivatelOperator;
import dbEntity.RezervaceMistnosti;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import view.SessionHolder.SessionHolderMB;
import view.bundle.ResourceBundleOperator;
import view.facesMessenger.FacesMessengerUtil;

/**
 *
 * @author asus
 */
@Named("ReservationsFinderMB")
@SessionScoped
public class RezervaceFinderMB implements Serializable{
    
    private String rezervaceFindID;
    private RezervaceMistnosti result;
    private String roleVlastnika;
    
    private boolean isThereResult = false; //skryte ci ne
    
    @Inject private UzivatelOperator uzivOper;
    @Inject private RezervaceMistnostiOperator rezOper;
    @Inject private FacesMessengerUtil messUtil;
    @Inject private ResourceBundleOperator bundle;
    @Inject private SessionHolderMB session;
    
    
    /**
     * Vyhledávač rezervací
     */
    public void findRezervaci(){
        
        Integer id = null;
        try{
            id = Integer.parseInt(getRezervaceFindID());
        }
        catch(NumberFormatException e){
            messUtil.addFacesMsgError(bundle.getMsg("sysMsgNotNumber"));
        }
        
        
        setResult(rezOper.getRezervace(new RezervaceMistnosti(id)));
        
        if(getResult() != null){
            setIsThereResult(true);
            roleVlastnika = uzivOper.getUzivatelRole(result.getIDuser());
        }
        else{
            setIsThereResult(false);
            roleVlastnika = "";
        }
    }



    /**
     * @return the isThereResult
     */
    public boolean isIsThereResult() {
        return isThereResult;
    }

    /**
     * @param isThereResult the isThereResult to set
     */
    public void setIsThereResult(boolean isThereResult) {
        this.isThereResult = isThereResult;
    }

    /**
     * @return the result
     */
    public RezervaceMistnosti getResult() {
        return result;
    }

    /**
     * @param result the result to set
     */
    public void setResult(RezervaceMistnosti result) {
        this.result = result;
    }

    /**
     * @return the rezervaceFindID
     */
    public String getRezervaceFindID() {
        return rezervaceFindID;
    }

    /**
     * @param rezervaceFindID the rezervaceFindID to set
     */
    public void setRezervaceFindID(String rezervaceFindID) {
        this.rezervaceFindID = rezervaceFindID;
    }

    /**
     * @return the roleVlastnika
     */
    public String getRoleVlastnika() {
        return roleVlastnika;
    }

    /**
     * @param roleVlastnika the roleVlastnika to set
     */
    public void setRoleVlastnika(String roleVlastnika) {
        this.roleVlastnika = roleVlastnika;
    }
    
    
}
