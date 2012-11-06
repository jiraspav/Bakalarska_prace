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
import app.facade.NavigationBean;
import app.facade.login.LoginFacade;
import app.sessionHolder.SessionHolderEJB;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Pavel
 */

@Stateless
@Remote(LoginFacade.class)
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class LoginServiceEJB implements LoginFacade{

    private @Inject NavigationBean naviBean;
    private @Inject EncryptUtil encrypt;
    private @Inject UzivatelOperator uzivOperator;
    private @Inject GroupTableOperator groupOperator;
    private @Inject KosApiOperator kosOperator;
    private @Inject ParserController parsCon;
    
    @Override
    public String login(String login, String password) {
        
        FacesContext ctx = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) ctx.getExternalContext().getRequest();
        
        if(request.getUserPrincipal() == null){
            try {
                request.login(login, encrypt.SHA256(password));
                
                
            } catch (ServletException ex) {

                return naviBean.validationFail();
            }
        }
        
        return naviBean.success();
        
    }

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

    @Override
    public String logout() {
        
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) ec.getRequest();
        try {
            
            request.logout();
            ec.invalidateSession();
            
        } catch (ServletException ex) {
            Logger.getLogger(LoginServiceEJB.class.getName()).log(Level.SEVERE, null, ex);
            
        }
        return naviBean.toIndex();    
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
            kosOperator.createNewConnection(kosapiUrls.validation, login, password);
            
            serverResponse = kosOperator.getConnection().getResponseCode();
            
            kosOperator.closeConnection();

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
