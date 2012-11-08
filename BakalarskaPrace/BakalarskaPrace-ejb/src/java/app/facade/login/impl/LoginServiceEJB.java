/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app.facade.login.impl;

import app.XMLparser.ParserController;
import app.XMLparser.kosapiUrls;
import app.baseDataOperators.GroupTableOperator;
import app.baseDataOperators.KosApiOperator;
import app.baseDataOperators.UzivatelOperator;
import app.encrypt.EncryptUtil;
import app.facade.login.LoginFacade;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.net.ssl.HttpsURLConnection;

/**
 *
 * @author Pavel
 */

@Stateless
@Local(LoginFacade.class)
public class LoginServiceEJB implements LoginFacade{

    private @Inject EncryptUtil encrypt;
    private @Inject UzivatelOperator uzivOperator;
    private @Inject GroupTableOperator groupOperator;
    private @Inject KosApiOperator kosOperator;
    private @Inject ParserController parsCon;
    

    @Override
    public String register(String regLogin, String regPassword) {
        
            if(uzivOperator.getUzivatel(regLogin) == null && isValid(regLogin,regPassword)){
                
                ArrayList<String> atributy = (ArrayList<String>) parsCon.getAtributes(regLogin,regLogin,regPassword);
                
                uzivOperator.createUzivatel(Long.parseLong(atributy.get(1)), regLogin, encrypt.SHA256(regPassword), atributy.get(0));
                
                groupOperator.createGroupTable(regLogin, atributy.get(2));
        
                return "completed";
            }
            
            else if(uzivOperator.getUzivatel(regLogin) != null){
                
                return "alreadyRegistered";
                
            }
            else{
                
                return "failure";
                
            }
    }

    
    /**
     * metoda zjišťující správnost loginu a hesla co se týče jejich registrace na FELu
     * Za validní se považují pouze ty osoby, které mají přístup do KOSu, tedy pro externisty
     * a navštěvy vrací false
     * 
     * @return true - pokud je uživatel registrován na FELu
     *          false - pokud není
     */
    public boolean isValid(String login, String password){
        int serverResponse = 0;
        try {
            HttpsURLConnection conn = kosOperator.createNewConnection(kosapiUrls.validation, login, password);
            
            serverResponse = conn.getResponseCode();
            
            kosOperator.closeConnection(conn);

        } catch (IOException ex) {
            Logger.getLogger(LoginServiceEJB.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if(serverResponse == 200){
            return true;
        }
        else{
            return false;
        }
    }
}
