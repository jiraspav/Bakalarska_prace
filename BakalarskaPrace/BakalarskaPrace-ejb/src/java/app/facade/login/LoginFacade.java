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
    
    public String login(String login, String password);
    public String register(String login, String password);
    public String logout();
    
}
