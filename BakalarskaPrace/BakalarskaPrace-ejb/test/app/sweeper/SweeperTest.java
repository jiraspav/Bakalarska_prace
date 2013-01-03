/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app.sweeper;

import app.baseDataOperators.UzivatelOperator;
import dbEntity.Mistnost;
import dbEntity.RezervaceMistnosti;
import dbEntity.Uzivatel;
import java.util.ArrayList;
import java.util.Date;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

/**
 *
 * @author asus
 */
public class SweeperTest {
    
    private Sweeper instance;
    private UzivatelOperator uzivOper;
    
    public SweeperTest() {
    }

    @Before
    public void setUp() {
        
        uzivOper = Mockito.mock(UzivatelOperator.class);
        
        instance = new Sweeper();
        
        instance.setUzivOper(uzivOper);
    }
    
    private Uzivatel getTestUzivatel(){
    
        return new Uzivatel(1L, "login", "jmeno", "kontakt");
        
    }
    
    private Uzivatel getTestAdminUzivatel(){
    
        return new Uzivatel(2L, "admin", "admin", "admin");
        
    }
    
    private Mistnost getTestMistnost(){
        
        Mistnost test = new Mistnost(1L, "zkratka");
        test.setKapacita(50);
        
        return test;
        
    }

    /**
     * Test of getOverwrittenReservations method, of class Sweeper.
     */
    @Test
    public void testGetOverwrittenReservations() throws Exception {
        System.out.println("Test getOverwrittenReservations");
        
        
        Mockito.when(uzivOper.getUzivatelRolePriority(getTestAdminUzivatel())).thenReturn(3);
        Mockito.when(uzivOper.getUzivatelRolePriority(getTestUzivatel())).thenReturn(1);
        
        RezervaceMistnosti testMojeRezervace = new RezervaceMistnosti(6,getTestUzivatel(), getTestMistnost(), new Date(0), new Date(60000), new Date(0), 10 , "test", "ACTIVE");
        
        RezervaceMistnosti testInterfering1 = new RezervaceMistnosti(1,getTestUzivatel(), getTestMistnost(), new Date(0),  new Date(0),new Date(10000), 1 , "test", "ACTIVE");
        RezervaceMistnosti testInterfering2 = new RezervaceMistnosti(2,getTestUzivatel(), getTestMistnost(), new Date(0), new Date(0),new Date(10000),  4 , "test", "ACTIVE");
        RezervaceMistnosti testInterfering3 = new RezervaceMistnosti(3,getTestUzivatel(), getTestMistnost(), new Date(0), new Date(0), new Date(10000), 5 , "test", "ACTIVE");
        RezervaceMistnosti testInterfering4 = new RezervaceMistnosti(4,getTestUzivatel(), getTestMistnost(), new Date(0), new Date(0), new Date(10000), 6 , "test", "ACTIVE");
        RezervaceMistnosti testInterfering5 = new RezervaceMistnosti(5,getTestUzivatel(), getTestMistnost(), new Date(0), new Date(0), new Date(10000), 34 , "test", "ACTIVE");
        
        ArrayList<RezervaceMistnosti> testInterfering = new ArrayList<RezervaceMistnosti>();
        testInterfering.add(testInterfering1);
        testInterfering.add(testInterfering2);
        testInterfering.add(testInterfering3);
        testInterfering.add(testInterfering4);
        testInterfering.add(testInterfering5);
        
        ArrayList<RezervaceMistnosti> resultExp = new ArrayList<RezervaceMistnosti>();
        resultExp.add(testInterfering2);
        resultExp.add(testInterfering4);
        
        ArrayList<RezervaceMistnosti> result = instance.getOverwrittenReservations(testMojeRezervace, testInterfering, getTestAdminUzivatel());
        
        //Mockito.verify(uzivOper, Mockito.times(10)).getUzivatelRolePriority(Mockito.any(Uzivatel.class));
        
        assertEquals(resultExp, result);
        
        System.out.println("Success.");
    }

    /**
     * Test of getMaximumReserved method, of class Sweeper.
     */
    @Test
    public void testGetMaximumReserved() throws Exception {
        
        System.out.println("Testing getMaximumReserved");
        
        Mockito.when(uzivOper.getUzivatelRolePriority(Mockito.any(Uzivatel.class))).thenReturn(1).thenReturn(3).thenReturn(1).thenReturn(3)
                                                                                    .thenReturn(1).thenReturn(3).thenReturn(1).thenReturn(3)
                                                                                    .thenReturn(1).thenReturn(3).thenReturn(1).thenReturn(3)
                                                                                    .thenReturn(1).thenReturn(3).thenReturn(1).thenReturn(3);
        
        
        RezervaceMistnosti testInterfering1 = new RezervaceMistnosti(1,getTestUzivatel(), getTestMistnost(), new Date(0),  new Date(0),new Date(10000), 1 , "test", "ACTIVE");
        RezervaceMistnosti testInterfering2 = new RezervaceMistnosti(2,getTestUzivatel(), getTestMistnost(), new Date(0), new Date(0),new Date(10000),  4 , "test", "ACTIVE");
        RezervaceMistnosti testInterfering3 = new RezervaceMistnosti(3,getTestUzivatel(), getTestMistnost(), new Date(0), new Date(0), new Date(10000), 5 , "test", "ACTIVE");
        RezervaceMistnosti testInterfering4 = new RezervaceMistnosti(4,getTestUzivatel(), getTestMistnost(), new Date(0), new Date(0), new Date(10000), 6 , "test", "ACTIVE");
        RezervaceMistnosti testInterfering5 = new RezervaceMistnosti(5,getTestUzivatel(), getTestMistnost(), new Date(0), new Date(0), new Date(10000), 34 , "test", "ACTIVE");
        
        ArrayList<RezervaceMistnosti> testInterfering = new ArrayList<RezervaceMistnosti>();
        testInterfering.add(testInterfering1);
        testInterfering.add(testInterfering2);
        testInterfering.add(testInterfering3);
        testInterfering.add(testInterfering4);
        testInterfering.add(testInterfering5);
        
        int resultExp = 40;
        int result = instance.getMaximumReserved(testInterfering, new Uzivatel());
        
        assertEquals(resultExp, result);
        
        //Mockito.verify(uzivOper, Mockito.times(20)).getUzivatelRolePriority(Mockito.any(Uzivatel.class));
        
        System.out.println("Success");
    }

    /**
     * Test of isHigherPriorityThanLogged method, of class Sweeper.
     */
    @Test
    public void testIsHigherPriorityThanLogged() throws Exception {
        
        System.out.println("Test isHigherPriorityThanLogged");
        
        Mockito.when(uzivOper.getUzivatelRolePriority(Mockito.any(Uzivatel.class))).thenReturn(1).thenReturn(3);
        
        assertFalse(instance.isHigherPriorityThanLogged(new RezervaceMistnosti(), new Uzivatel()));
            
        Mockito.when(uzivOper.getUzivatelRolePriority(Mockito.any(Uzivatel.class))).thenReturn(3).thenReturn(1);
        
        assertTrue(instance.isHigherPriorityThanLogged(new RezervaceMistnosti(), new Uzivatel()));
        
        System.out.println("Success");
        
    }
}
