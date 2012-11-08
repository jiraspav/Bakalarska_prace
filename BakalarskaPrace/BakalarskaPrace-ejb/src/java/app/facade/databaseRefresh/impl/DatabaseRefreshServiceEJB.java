/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app.facade.databaseRefresh.impl;

import app.XMLparser.ParserController;
import app.baseDataOperators.MistnostOperator;
import app.baseDataOperators.PredmetyOperator;
import app.baseDataOperators.RezervaceMistnostiOperator;
import app.baseDataOperators.RozvrhyOperator;
import app.baseDataOperators.SemestrOperator;
import app.baseDataOperators.StrediskoOperator;
import app.baseDataOperators.UpdateRozvrhuOperator;
import app.facade.databaseRefresh.DatabaseRefreshFacade;
import dbEntity.RezervaceMistnosti;
import dbEntity.UpdateRozvrhu;
import java.text.SimpleDateFormat;
import javax.ejb.Asynchronous;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 *
 * @author Pavel
 */
@Stateless
@Local(DatabaseRefreshFacade.class)
public class DatabaseRefreshServiceEJB implements DatabaseRefreshFacade{

    private @Inject SemestrOperator semOper;
    private @Inject RezervaceMistnostiOperator rezOper;
    private @Inject RozvrhyOperator rozOper;
    private @Inject PredmetyOperator predOper;
    private @Inject StrediskoOperator strOper;
    private @Inject MistnostOperator mistOper;
    private @Inject UpdateRozvrhuOperator updateOper;
    private @Inject ParserController parsCon;
    
    @Asynchronous
    @Override
    public void refreshDatabase() {
        deleteDB();
        fillDB();
    }
    /**
     * metoda naplňující databázi
     */
    public void fillDB(){
        if(strOper.getAll().isEmpty()){
            parsCon.fillDepartments();
        }
        
        parsCon.fillCourses();
        
        if(mistOper.getAll().isEmpty()){
            parsCon.fillRooms();
        }
        
        parsCon.fillRozvrhy();
        parsCon.fillSemestr();
    }
    /**
     * metoda pro smazání databáze
     */
    public void deleteDB(){
        
        rezOper.deleteAll();
        
        rozOper.deleteAll();
        
        predOper.deleteAll();
        
        /*
         * stredOper.deleteAll();
         * mistOper.deleteAll();
         * 
         */
        
        updateOper.deleteAll();
        
        semOper.deleteAll();
    }
    
    @Override
    public String getLatestUpdate(){
        return updateOper.getLastUpdate().getFormattedDateUpdateRozvrhu();
    }
}
