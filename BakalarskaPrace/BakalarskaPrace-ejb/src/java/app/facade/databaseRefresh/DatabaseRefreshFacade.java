/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app.facade.databaseRefresh;

/**
 *
 * @author Pavel
 */
public interface DatabaseRefreshFacade {
    
    public void refreshDatabase();
    public String getLatestUpdate();
    
}
