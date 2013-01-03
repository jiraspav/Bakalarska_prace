/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app.facade.databaseRefresh;

import dbEntity.Uzivatel;

/**
 *
 * @author Pavel
 */
public interface DatabaseRefreshFacade {
    
    /**
     * Metoda pro aktualizaci celé databáze. Zahrnuje kompletní smazání
     * potřebných dat a nahrání nových.
     * @param logged uživatel, který aktualizaci provádí
     */
    public void refreshDatabase(Uzivatel logged);
    /**
     * Metoda pro získání datum poslední aktualizace databáze.
     * @return textovou formu data poslední aktualizace
     */
    public String getLatestUpdate();
    
}
