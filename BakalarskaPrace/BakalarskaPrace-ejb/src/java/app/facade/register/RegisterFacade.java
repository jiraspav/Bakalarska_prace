/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app.facade.register;

/**
 *
 * @author Pavel
 */


public interface RegisterFacade {
    
    
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
    public String register(String login, String password);
    
}
