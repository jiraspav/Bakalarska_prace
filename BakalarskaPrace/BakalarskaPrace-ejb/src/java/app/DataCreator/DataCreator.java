/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app.DataCreator;

import app.baseDataOperators.DnyVTydnuOperator;
import app.baseDataOperators.DnyVTydnuOperator;
import app.baseDataOperators.DnyVTydnuOperator;
import app.baseDataOperators.UzivatelOperator;
import app.baseDataOperators.UzivatelOperator;
import app.baseDataOperators.UzivatelOperator;
import entityFacade.DenVTydnuFacade;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 *
 * @author Pavel
 */
@Stateless
public class DataCreator {
    
    private @Inject DenVTydnuFacade denFac;
    private @Inject DnyVTydnuOperator dnyOperator;
    private @Inject UzivatelOperator uzivOperator;
    
    public void initDefaultConf(){
        if(!isDefaultConf()){
            buildDny();
            buildDefaultAdmin();
        }
    }
    
    private boolean isDefaultConf(){
        if(denFac.findAll().isEmpty()){
            return false;
        }
        return true;
    }
    
    private void buildDny(){
        dnyOperator.buildDefault();
    }

    private void buildDefaultAdmin() {
        uzivOperator.buildDefault();
    }
    
    
}
