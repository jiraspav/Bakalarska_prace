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
import app.baseDataOperators.VyucujiciOperator;
import app.facade.databaseRefresh.DatabaseRefreshFacade;
import dbEntity.Uzivatel;
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
    private @Inject VyucujiciOperator vyuOper;
    
    /**
     * Metoda pro aktualizaci celé databáze. Zahrnuje kompletní smazání
     * potřebných dat a nahrání nových.
     * @param logged uživatel, který aktualizaci provádí
     */
    
    @Override
    public void refreshDatabase(Uzivatel logged) {
        deleteDB();
        fillDB(logged);
    }
    /**
     * metoda naplňující databázi
     * @param logged 
     */
    private void fillDB(Uzivatel logged){
        if(strOper.getAll().isEmpty()){
            parsCon.fillDepartments(logged);
        }
        
        parsCon.fillCourses(logged);
        
        if(mistOper.getAll().isEmpty()){
            parsCon.fillRooms(logged);
        }
        
        parsCon.fillRozvrhy(logged);
        parsCon.fillSemestr(logged);
        System.out.println("---DATABASE UPDATED---");
    }
    /**
     * metoda pro smazání databáze
     */
    private void deleteDB(){
        
        rezOper.deleteAll();
        
        rozOper.deleteAll();
        vyuOper.deleteAll();
        predOper.deleteAll();
        
        /*
         * stredOper.deleteAll();
         * mistOper.deleteAll();
         * 
         */
        
        //updateOper.deleteAll();
        
        //semOper.deleteAll();
    }
    
    /**
     * Metoda pro získání datum poslední aktualizace databáze.
     * @return textovou formu data poslední aktualizace
     */
    @Override
    public String getLatestUpdate(){
        return updateOper.getLastUpdate().getFormattedDateUpdateRozvrhu();
    }
}
