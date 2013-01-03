/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app.XMLparser;


import dbEntity.Uzivatel;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Pavel
 */

@Stateless
public class ParserController {
    
    
    private @Inject Parser parser;

    /**
     * 
     */
    public ParserController() {
    }
    
    /**
     * vola metodu Parser.setDepartments()
     * @param logged právě přihlášený uživatel
     * @see Parser
     */
    public void fillDepartments(Uzivatel logged) {
        try {
            getParser().setDepartments(logged);
        } catch (IOException ex) {
            //Logger.getLogger(ParserController.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Couldnt connect.");
        }
    }

    
    /**
     * vola metodu Parser.setRooms()
     * @param logged právě přihlášený uživatel
     * @see Parser
     */
    public void fillRooms(Uzivatel logged) {
        try {
            getParser().setRooms(logged);
        } catch (IOException ex) {
            //Logger.getLogger(ParserController.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Couldnt connect.");
        }
    }
    
    
    /**
     * vola metodu Parser.setCourses()
     * @param logged právě přihlášený uživatel
     * @see Parser 
     */
    public void fillCourses(Uzivatel logged) {
        try {
            getParser().setCourses(logged);
        } catch (IOException ex) {
            //Logger.getLogger(ParserController.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Couldnt connect.");
        }
    }
    /**
     * vola metodu Parser.setRozvrhy()
     * @param logged právě přihlášený uživatel
     * @see Parser 
     */
    public void fillRozvrhy(Uzivatel logged) {
        try {
            getParser().setRozvrhy(logged);
        } catch (IOException ex) {
            //Logger.getLogger(ParserController.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Couldnt connect.");
        }
        
    }
    
    /**
     * vola metodu Parser.setSemestr()
     * @param logged právě přihlášený uživatel
     * @see Parser 
     */
    public void fillSemestr(Uzivatel logged) {
        try {
            getParser().setSemestr(logged);
        } catch (IOException ex) {
            //Logger.getLogger(ParserController.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Couldnt connect.");
        }
    }
    
    /**
     * vola metodu Parser.getAtributes()
     * @see Parser 
     * @param login login uzivatele, o kterem chci zjistit informace
     * @param mujLogin muj login pouzivany pro autorizaci na KosAPI
     * @param mojeHeslo heslo pouzivane pro autorizaci na KosAPI
     * @return vrací list atributů
     */
    public List<String> getAtributes(String login, String mujLogin, String mojeHeslo) {
        List<String> attributes = null;
        try {
            attributes = getParser().getAtributes(login,mujLogin,mojeHeslo);
        } catch (IOException ex) {
            //Logger.getLogger(ParserController.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Couldnt connect.");
        }
        return attributes;
    }

    /**
     * @return the parser
     */
    public Parser getParser() {
        return parser;
    }

    /**
     * @param parser the parser to set
     */
    public void setParser(Parser parser) {
        this.parser = parser;
    }

    
}
