/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app.XMLparser;


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
     * @see Parser
     */
    public void fillDepartments() {
        try {
            getParser().setDepartments();
        } catch (IOException ex) {
            //Logger.getLogger(ParserController.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Couldnt connect.");
        }
    }

    
    /**
     * vola metodu Parser.setRooms()
     * @see Parser
     */
    public void fillRooms() {
        try {
            getParser().setRooms();
        } catch (IOException ex) {
            //Logger.getLogger(ParserController.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Couldnt connect.");
        }
    }
    
    
    /**
     * vola metodu Parser.setCourses()
     * @see Parser 
     */
    public void fillCourses() {
        try {
            getParser().setCourses();
        } catch (IOException ex) {
            //Logger.getLogger(ParserController.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Couldnt connect.");
        }
    }
    /**
     * vola metodu Parser.setRozvrhy()
     * @see Parser 
     */
    public void fillRozvrhy() {
        try {
            getParser().setRozvrhy();
        } catch (IOException ex) {
            //Logger.getLogger(ParserController.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Couldnt connect.");
        }
        
    }
    
    /**
     * vola metodu Parser.setSemestr()
     * @see Parser 
     */
    public void fillSemestr() {
        try {
            getParser().setSemestr();
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
