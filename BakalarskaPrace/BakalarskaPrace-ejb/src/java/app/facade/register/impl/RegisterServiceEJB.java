/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app.facade.register.impl;

import app.XMLparser.ParserController;
import app.XMLparser.kosapiUrls;
import app.baseDataOperators.GroupTableOperator;
import app.baseDataOperators.KosApiOperator;
import app.baseDataOperators.UzivatelOperator;
import app.encrypt.EncryptUtil;
import app.facade.register.RegisterFacade;
import dbEntity.Uzivatel;
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
@Local(RegisterFacade.class)
public class RegisterServiceEJB implements RegisterFacade{

    private @Inject EncryptUtil encrypt;
    private @Inject UzivatelOperator uzivOper;
    private @Inject GroupTableOperator groupOperator;
    private @Inject KosApiOperator kosOperator;
    private @Inject ParserController parsCon;
    

    
    /**
     * Metoda pro registraci nových uživatelů.
     * 
     * @param login - user login
     * @param password - user password
     * @return completed - pokud bylo vše úspěšně dokončeno
     *         <p>
     *         alreadyRegistered - pokud se již uživatel nachází v databázi
     *         <p>
     *         failure - pokud vložený login a heslo nejsou správné
     */
    @Override
    public String register(String regLogin, String regPassword) {
        
            if(uzivOper.getUzivatel(regLogin) == null && isValid(regLogin,regPassword)){
                
                ArrayList<String> atributy = (ArrayList<String>) parsCon.getAtributes(regLogin,regLogin,regPassword);
                try{
                    uzivOper.createUzivatel(Long.parseLong(atributy.get(1)), regLogin, regPassword, atributy.get(0), atributy.get(3));
                }catch(NullPointerException e){
                    //pokud byl uzivatel studentem na FEL a ma prerusene studium
                    //ma pristup do kosapi avsak neni v xml seznamu lidi na kosapi
                    
                    if(regLogin.equals("jiraspav")){
                        uzivOper.createUzivatel(getFirstFreeGuestID(), regLogin, regPassword, "Pavel Jirásek", "jiraspav@fel.cvut.cz");
                        groupOperator.createGroupTable(regLogin, "student");
                        return "completed";
                    }
                    
                    return "failure";
                }
                groupOperator.createGroupTable(regLogin, atributy.get(2));
        
                return "completed";
            }
            
            else if(uzivOper.getUzivatel(regLogin) != null){
                
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
     * @param login 
     * @param password 
     * @return true - pokud je uživatel registrován na FELu
     *          false - pokud není
     */
    private boolean isValid(String login, String password){
        int serverResponse = 0;
        try {
            HttpsURLConnection conn = kosOperator.createNewConnection(kosapiUrls.validation, login, password);
            
            serverResponse = conn.getResponseCode();
            
            kosOperator.closeConnection(conn);

        } catch (IOException ex) {
            Logger.getLogger(RegisterServiceEJB.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if(serverResponse == 200){
            return true;
        }
        else{
            return false;
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

    /**
     * @param encrypt the encrypt to set
     */
    public void setEncrypt(EncryptUtil encrypt) {
        this.encrypt = encrypt;
    }

    /**
     * @param uzivOper the uzivOper to set
     */
    public void setUzivOper(UzivatelOperator uzivOper) {
        this.uzivOper = uzivOper;
    }

    /**
     * @param groupOperator the groupOperator to set
     */
    public void setGroupOperator(GroupTableOperator groupOperator) {
        this.groupOperator = groupOperator;
    }

    /**
     * @param kosOperator the kosOperator to set
     */
    public void setKosOperator(KosApiOperator kosOperator) {
        this.kosOperator = kosOperator;
    }

    /**
     * @param parsCon the parsCon to set
     */
    public void setParsCon(ParserController parsCon) {
        this.parsCon = parsCon;
    }
}
