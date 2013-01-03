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
    /**
     * Metoda pro přidání administrátorských práv danému uživateli
     * @param uziv uživatel, kterému mají být přidány administrátorská práva
     */
    public void addAdminRights(Uzivatel uziv);
    /**
     * Metoda pro odebrání administrátorských práv uživateli
     * @param uziv uživatel, kterému mají být odebrány práva
     * @param logged uživatel, který tyto práva odstraňuje (kvůli připojení do kosapi)
     */
    public void removeAdminRights(Uzivatel uziv, Uzivatel logged);
    /**
     * Metoda pro vytváření nových guest účtů
     * @return pole atributů nového účtu
     *         pozice 0 - login účtu
     *         pozice 1 - heslo účtu
     */
    public String[] createGuestAccount();
    /**
     * Metoda pro úpravu uživatelů. Je však možné upravovat pouze takové atributy,
     * které se neobjevují jinde jako cizí klíče nebo jimi sami nejsou
     * @param uziv uzivatel s novými atributy
     */
    public void editAccount(Uzivatel uziv);
    /**
     * Metoda pro odstranění uživatelského účtu. Zahrnuje odstranění všech rezervací
     * vázaných na tento účet.
     * @param uziv uživatel jehož účet se má rušit
     */
    public void deleteAccount(Uzivatel uziv);
    /**
     * Metoda pro zjištění, zda se v databázi nachází poslední admin.
     * @param uziv uživatel, pro kterého se zjišťuje, zda je posledním administrátorem
     * @return true - pokud je uživatel posledním administrátorem
     *         false - pokud ne
     */
    public boolean isLastAdmin(Uzivatel uziv);
}
