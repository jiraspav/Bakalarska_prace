/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app.facade.accountEditor;

import dbEntity.Uzivatel;

/**
 *
 * @author Pavel
 */
public interface AccountEditorFacade {
    public void addAdminRights(Uzivatel uziv);
    public void removeAdminRights(Uzivatel uziv);
    public String[] createGuestAccount();
    public void deleteAccount(Uzivatel uziv);
    public boolean isLastAdmin(Uzivatel uziv);
}
