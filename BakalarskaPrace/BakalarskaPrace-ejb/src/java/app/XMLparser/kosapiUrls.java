/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package XMLparser;

/**
 *
 * @author Pavel
 */
public class kosapiUrls {
    
    /**
     * adresa, na které se nachází všechny střediska
     */
    public static String departments = "https://kosapi.feld.cvut.cz/api/2/departments?paging=false&level=2";    
    /**
     * adresa, na které se nachází všechny místnosti i s podrobnostmi
     */
    public static String rooms = "https://kosapi.feld.cvut.cz/api/2/rooms?paging=false&level=1";
    /**
     * adresa, na které se nachází všechny místnosti bez podrobností
     */
    public static String roomsNolevel = "https://kosapi.feld.cvut.cz/api/2/rooms?paging=false";
    /**
     * adresa, na které se nachází všechny předmety
     */
    public static String courses = "https://kosapi.feld.cvut.cz/api/2/courses?level=1&paging=false";
    /**
     * adresa, na které se nachází všichni uživatelé registrovaní na FELu
     */
    public static String people = "https://kosapi.feld.cvut.cz/api/2/people";
    /**
     * adresa, na které se nachází * adresa, na které se nachází informace o aktuálním semestru
     */
    public static String currentSemestr = "https://kosapi.feld.cvut.cz/api/2/semesters/current";
    /**
     * adresa, na které se nachází všichni uživatelé registrovaní na FELu
     */
    public static String validation = "https://kosapi.feld.cvut.cz/api/2/people";
    
}
