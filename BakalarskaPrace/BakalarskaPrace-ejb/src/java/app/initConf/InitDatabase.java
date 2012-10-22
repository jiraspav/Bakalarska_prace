/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app.initConf;

import app.DataCreator.DataCreator;
import javax.annotation.PostConstruct;
import javax.ejb.DependsOn;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

/**
 *
 * @author Pavel
 */
@Singleton
@Startup
public class InitDatabase {
    
    @Inject private DataCreator dataCreator;
    
    @PostConstruct
    private void initData(){
        dataCreator.initDefaultConf();
    }
}
