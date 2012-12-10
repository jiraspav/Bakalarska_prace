/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app.DataCreator;

import app.baseDataOperators.DnyVTydnuOperator;
import app.baseDataOperators.TypMistnostiOperator;
import app.baseDataOperators.UzivatelOperator;
import app.sessionHolder.SessionHolderEJB;
import dbEntity.TypMistnosti;
import dbEntity.Uzivatel;
import entityFacade.DenVTydnuFacade;
import javax.ejb.Stateful;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 *
 * @author Pavel
 */
@Stateless
public class DataCreator {
    
    private @Inject TypMistnostiOperator typOper;
    private @Inject DenVTydnuFacade denFac;
    private @Inject DnyVTydnuOperator dnyOperator;
    private @Inject UzivatelOperator uzivOperator;
    public @Inject SessionHolderEJB session;
    

    
    public void initDefaultConf(){
        if(!isDefaultConf()){
            buildDny();
            buildTypyMistnosti();
            buildDefaultUsers();
        }
    }
    
    private boolean isDefaultConf(){
        if(denFac.findAll().isEmpty()){
            return false;
        }
        return true;
    }
    
    private void buildDny(){
        dnyOperator.buildDefaultDny();
    }

    private void buildTypyMistnosti(){
        typOper.buildDefaultTypyMistnosti();
    }
    
    private void buildDefaultUsers() {
        uzivOperator.buildDefaultUzivatel();
    }

    
}
