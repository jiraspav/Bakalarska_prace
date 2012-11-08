/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app.facade.login;

/**
 *
 * @author Pavel
 */


public interface LoginFacade {
    
    
    /**
     * Method for registration and controlling of new user, if he's registered on FEL
     * and if he has access to KOS
     * 
     * @param login - user login
     * @param password - user password
     * @return completed - if everything was successful
     *         <p>
     *         alreadyRegistered - if user is already registered
     *         <p>
     *         failure - if insterted values arent correct
     */
    public String register(String login, String password);
    
}
