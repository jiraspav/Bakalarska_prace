/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package view.mistnosti;

import app.facade.accountEditor.AccountEditorFacade;
import dbEntity.Uzivatel;
import java.io.Serializable;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import view.SessionHolder.SessionHolderMB;

/**
 *
 * @author Pavel
 */

@Named("editorMB")
@SessionScoped
public class EditorUctuMB implements Serializable{
    
    private @Inject AccountEditorFacade accEdit;
    private @Inject SessionHolderMB session;
    //private @Inject Conversation conversation;
    
    private String login,name,contacts,role;
    private boolean disabled = true;
    
    
    /**
     * Přepínač rendered
     */
    public void switchDisabled(){
        
        if(disabled){
            setDisabled(!isDisabled());
            
                    }
        else{
            setDisabled(!isDisabled());
            
        }
    }
    
    /**
     * Metoda používaná pro ukládání nových dat uživatele
     */
    public void saveContacts(){
        
        Uzivatel edit = session.getLoggedUzivatel();
        edit.setJmeno(name);
        edit.setKontakt(contacts);
        System.out.println("JMENO "+name);
        accEdit.editAccount(edit);
        
        setDisabled(!isDisabled());
        //endConversation();
    }
    
    /**
     * @return the login
     */
    public String getLogin() {
        return session.getLoggedUzivatelLogin();
    }

    /**
     * @param login the login to set
     */
    public void setLogin(String login) {
        this.login = login;
    }

    /**
     * @return the name
     */
    public String getName() {
        return session.getLoggedUzivatelName();
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the contacts
     */
    public String getContacts() {
        return session.getLoggedUzivatel().getKontakt();
    }

    /**
     * @param contacts the contacts to set
     */
    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    /**
     * @return the role
     */
    public String getRole() {
        return session.getLoggedUzivatelRole();
    }

    /**
     * @param role the role to set
     */
    public void setRole(String role) {
        this.role = role;
    }

    /**
     * @return the disabled
     */
    public boolean isDisabled() {
        return disabled;
    }

    /**
     * @param disabled the disabled to set
     */
    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }
    
}
