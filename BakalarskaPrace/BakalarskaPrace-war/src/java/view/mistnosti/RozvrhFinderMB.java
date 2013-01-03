/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package view.mistnosti;

import app.baseDataOperators.RozvrhyOperator;
import dbEntity.Rozvrhy;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import view.SessionHolder.SessionHolderMB;
import view.bundle.ResourceBundleOperator;
import view.facesMessenger.FacesMessengerUtil;

/**
 *
 * @author Pavel
 */
@Named("RozvrhFinderMB")
@SessionScoped
public class RozvrhFinderMB implements Serializable{
    
    private String rozvrhFindID;
    private Rozvrhy result;
    
    private boolean isThereResult = false; //skryte ci ne
    
    @Inject private RozvrhyOperator rozOper;
    @Inject private FacesMessengerUtil messUtil;
    @Inject private ResourceBundleOperator bundle;
    @Inject private SessionHolderMB session;
    
    
    /**
     * Metoda pro vyhledávání rozvrhů
     */
    public void findRozvrh(){
        
        Long id = null;
        try{
            id = Long.parseLong(rozvrhFindID);
        }
        catch(NumberFormatException e){
            messUtil.addFacesMsgError(bundle.getMsg("sysMsgNotNumber"));
        }
        
        
        setResult(rozOper.getRozvrh(new Rozvrhy(id)));
        
        if(getResult() != null){
            setIsThereResult(true);
            //update detaily rozvrhu
        }
        else{
            setIsThereResult(false);
        }
    }

    /**
     * @return the rozvrhFindID
     */
    public String getRozvrhFindID() {
        return rozvrhFindID;
    }

    /**
     * @param rozvrhFindID the rozvrhFindID to set
     */
    public void setRozvrhFindID(String rozvrhFindID) {
        this.rozvrhFindID = rozvrhFindID;
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
    public Rozvrhy getResult() {
        return result;
    }

    /**
     * @param result the result to set
     */
    public void setResult(Rozvrhy result) {
        this.result = result;
    }
    
}
