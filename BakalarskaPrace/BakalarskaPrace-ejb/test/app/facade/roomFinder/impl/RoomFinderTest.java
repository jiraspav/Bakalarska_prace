/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app.facade.roomFinder.impl;

import app.baseDataOperators.*;
import app.facade.schedulerEditorPF.impl.SchedulerEditorPF;
import app.sweeper.Sweeper;
import dbEntity.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.junit.*;
import static org.junit.Assert.*;
import org.mockito.Mockito;

/**
 *
 * @author Pavel
 */
public class RoomFinderTest {
    
    private RoomFinder instance;
    private MistnostOperator mistOper;
    private RezervaceMistnostiOperator rezOper;
    private UzivatelOperator uzivOper;
    private RozvrhyOperator rozOper;
    private SemestrOperator semOper;
    private DnyVTydnuOperator denOper;
    private Sweeper sweeper;
    
    public RoomFinderTest() {
    }

    @Before
    public void setUp() {
        
        uzivOper = Mockito.mock(UzivatelOperator.class);
        mistOper = Mockito.mock(MistnostOperator.class);
        rezOper = Mockito.mock(RezervaceMistnostiOperator.class);
        rozOper = Mockito.mock(RozvrhyOperator.class);
        //semOper = Mockito.mock(SemestrOperator.class);
        denOper = Mockito.mock(DnyVTydnuOperator.class);
        sweeper = Mockito.mock(Sweeper.class);
        
        instance = new RoomFinder();
        
        instance.setUzivOper(uzivOper);
        instance.setDenOper(denOper);
        instance.setMistOper(mistOper);
        instance.setRezOper(rezOper);
        instance.setRozOper(rozOper);
        instance.setSweeper(sweeper);
        //instance.setSemOper(semOper);
        
    }
    
    private RezervaceMistnosti getTestRezervace(int pocetRezervovanych){
        RezervaceMistnosti rez = new RezervaceMistnosti(1);
        rez.setPocetRezervovanychMist(pocetRezervovanych);;
        return rez;
    }
    
    private Mistnost getTestMistnost(int kapacita){
        Mistnost test = new Mistnost(1l, "test");
        test.setKapacita(kapacita);
           
        return test;
    }
    private Uzivatel getTestUzivatel(){
        Uzivatel uziv = new Uzivatel(1L, "test", "test", "test");
        return uziv;
    }
    private DenVTydnu getTestDen(){
        DenVTydnu test = new DenVTydnu(1, "test");
        return test;
    }
    private ArrayList<RezervaceMistnosti> getTestRez(){
        
        RezervaceMistnosti testInterfering1 = new RezervaceMistnosti(1,getTestUzivatel(), getTestMistnost(50), new Date(0),  new Date(0),new Date(10000), 1 , "test", "ACTIVE");
        RezervaceMistnosti testInterfering2 = new RezervaceMistnosti(2,getTestUzivatel(), getTestMistnost(50), new Date(0), new Date(1000),new Date(10000),  4 , "test", "ACTIVE");
        RezervaceMistnosti testInterfering3 = new RezervaceMistnosti(3,getTestUzivatel(), getTestMistnost(50), new Date(0), new Date(1000), new Date(90000), 5 , "test", "ACTIVE");
        RezervaceMistnosti testInterfering4 = new RezervaceMistnosti(4,getTestUzivatel(), getTestMistnost(50), new Date(0), new Date(0), new Date(10000), 6 , "test", "ACTIVE");
        RezervaceMistnosti testInterfering5 = new RezervaceMistnosti(5,getTestUzivatel(), getTestMistnost(50), new Date(0), new Date(0), new Date(10000), 34 , "test", "ACTIVE");
        
        ArrayList<RezervaceMistnosti> testInterfering = new ArrayList<RezervaceMistnosti>();
        testInterfering.add(testInterfering1);
        testInterfering.add(testInterfering2);
        testInterfering.add(testInterfering3);
        testInterfering.add(testInterfering4);
        testInterfering.add(testInterfering5);
        
        return testInterfering;
    }
    private Semestr getTestSemestr(){
        Semestr test = new Semestr(1, "TEST", new Date(0), new Date(1000000));
        return test;
    }
    private ArrayList<Mistnost> getTestMistnosti(int kapacita){
        ArrayList<Mistnost> test = new ArrayList<Mistnost>();
        test.add(getTestMistnost(kapacita));
        
        return test;
    }
    private ArrayList<Rozvrhy> getTestRozvhry(){
        ArrayList<Rozvrhy> test = new ArrayList<Rozvrhy>();
        test.add(new Rozvrhy(1L, new Date(0), new Date(999), true, true, getTestMistnost(50), getTestDen(), new Predmety(), new ArrayList<Vyucujici>()));
        return test;
    
    }
    /**
     * Test of getAllFreeRooms method, of class RoomFinder.
     */
    @Test
    public void testGetAllFreeRooms() throws Exception {
        
        System.out.println("Test GetAllFreeRooms");
        
        Mockito.when(mistOper.getAll()).thenReturn(getTestMistnosti(50));
        Mockito.when(denOper.getENDen(Mockito.anyString())).thenReturn(getTestDen());
        Mockito.when(rozOper.getRozvrhy(getTestMistnost(50), getTestDen())).thenReturn(getTestRozvhry());
        Mockito.when(sweeper.getMaximumReserved(getTestRez(), null)).thenReturn(20);
        
        List<Mistnost> allFreeRooms = instance.getAllFreeRooms(getTestRez(), null);
        
        assertEquals(allFreeRooms.size(), 0);
        
        System.out.println("Success.");
        
    }

    /**
     * Test of getAllFreeRoomsOnOneReservation method, of class RoomFinder.
     */
    @Test
    public void testGetAllFreeRoomsOnOneReservation() throws Exception {
        
        System.out.println("Test GetAllFreeRoomsOnOneReservation");
        
        Mockito.when(mistOper.getAll()).thenReturn(getTestMistnosti(50));
        Mockito.when(rozOper.getRozvrhy(Mockito.any(Mistnost.class), Mockito.any(DenVTydnu.class))).thenReturn(getTestRozvhry());
        
        List<Mistnost> result = instance.getAllFreeRoomsOnOneReservation(getTestRez().get(1), getTestUzivatel());
        
        assertEquals(result.size(), 1);
        
        result = instance.getAllFreeRoomsOnOneReservation(getTestRez().get(0), getTestUzivatel());
        
        assertEquals(result.size(), 0);
        
        System.out.println("Success.");
    }

    /**
     * Test of isEnoughSpace method, of class RoomFinder.
     */
    @Test
    public void testIsEnoughSpace() throws Exception {
        
        System.out.println("Test isEnoughSpace");
        
        Mockito.when(sweeper.getMaximumReserved(Mockito.any(ArrayList.class), Mockito.any(Uzivatel.class))).thenReturn(20);
        Mockito.when(rezOper.getRezervace(new Date(),getTestMistnost(50))).thenReturn(getTestRez());
        boolean enoughSpace = instance.isEnoughSpace(getTestRezervace(10), getTestMistnost(30), getTestUzivatel());
        
        assertTrue(enoughSpace);
        
        enoughSpace = instance.isEnoughSpace(getTestRezervace(20), getTestMistnost(30), getTestUzivatel());
        
        assertFalse(enoughSpace);
        
        System.out.println("Success.");
    }
}
