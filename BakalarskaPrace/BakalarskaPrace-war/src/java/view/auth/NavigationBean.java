/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package view.auth;

import java.io.Serializable;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

/**
 *
 * @author asus
 */
@Named(value = "navigationBean")
@RequestScoped
public class NavigationBean implements Serializable{

    
    /**
     * 
     */
    public NavigationBean() {
    }
    
    /**
     * metoda používaná pro navigace napříč stránkami.
     * konkrétně na adminAccountEditor.xhtml
     * @return vrací string pro navigaci
     */
    public String toAdminAccountEditor(){
            return "adminAccountEditor";
    }
    
    /**
     * metoda používaná pro navigace napříč stránkami.
     * konkrétně na adminIndex.xhtml
     * @return vrací string pro navigaci
     */
    public String toAdminPage(){
        return "adminPage";
    }
    
    /**
     * metoda používaná pro navigace napříč stránkami.
     * konkrétně na mojeRezervace.xhtml
     * @return vrací string pro navigaci
     */
    public String toMojeRezervacePage(){
        return "mojeRezervace";
    }
    
    /**
     * metoda používaná pro navigace napříč stránkami.
     * konkrétně na volneMistnosti.xhtml
     * @return
     */
    public String toVolneMistnostiPage(){
        return "volneMistnosti";
    }
    /**
     * metoda používaná pro navigace napříč stránkami.
     * konkrétně na vytvoreniRezervace.xhtml
     * @return vrací string pro navigaci
     */
    public String toVytvoreniRezervacePage(){
        return "vytvoreniRezervace";
    }
    
    
    /**
     * metoda používaná pro navigace napříč stránkami.
     * konkrétně na loginError.xhtml
     * @return vrací string pro navigaci
     */
    public String validationFail(){
        return "validationFail";
    }
    /**
     * metoda používaná pro navigace napříč stránkami.
     * konkrétně na welcome.xhtml
     * @return vrací string pro navigaci
     */
    public String success(){
        return "success";
    }
    
    /**
     * metoda používaná pro navigace napříč stránkami.
     * @param checker
     * @return vrací string pro navigaci
     */
    public String validationTest(boolean checker){
        if(checker)
            return success();
        else
            return validationFail();
    }
    
    /**
     * metoda používaná pro navigace napříč stránkami.
     * konkrétně na index.xhtml
     * @return vrací string pro navigaci
     */
    public String toIndex(){
        return "logout";
    }
}
