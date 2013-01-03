/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app.facade.reservationEditor.impl;

import app.baseDataOperators.*;
import app.facade.roomFinder.RoomFinderFacade;
import app.facade.roomFinder.impl.RoomFinder;
import app.sweeper.Sweeper;
import dbEntity.*;
import java.util.ArrayList;
import java.util.Date;
import org.junit.*;
import static org.junit.Assert.*;
import org.mockito.Mockito;

/**
 *
 * @author Pavel
 */
public class ReservationEditorTest {
    
    
    private ReservationEditor instance;
    private MistnostOperator mistOper;
    private RezervaceMistnostiOperator rezOper;
    private UzivatelOperator uzivOper;
    private RozvrhyOperator rozOper;
    private SemestrOperator semOper;
    private DnyVTydnuOperator denOper;
    private Sweeper sweeper;
    private RoomFinderFacade roomFac;
    
    @Before
    public void setUp() {
        
        uzivOper = Mockito.mock(UzivatelOperator.class);
        mistOper = Mockito.mock(MistnostOperator.class);
        rezOper = Mockito.mock(RezervaceMistnostiOperator.class);
        rozOper = Mockito.mock(RozvrhyOperator.class);
        //semOper = Mockito.mock(SemestrOperator.class);
        denOper = Mockito.mock(DnyVTydnuOperator.class);
        sweeper = Mockito.mock(Sweeper.class);
        roomFac = Mockito.mock(RoomFinderFacade.class);
        
        instance = new ReservationEditor();
        
        instance.setUzivOper(uzivOper);
        instance.setDenOper(denOper);
        instance.setMistOper(mistOper);
        instance.setRezOper(rezOper);
        instance.setRozOper(rozOper);
        instance.setSweeper(sweeper);
        instance.setRoomFac(roomFac);
        //instance.setSemOper(semOper);
        
    }
    
    
    public ReservationEditorTest() {
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
    private ArrayList<RezervaceMistnosti> getTestOverwritten(){
        
        ArrayList<RezervaceMistnosti> testInterfering = new ArrayList<RezervaceMistnosti>();
        testInterfering.add(getTestRez().get(1));
        testInterfering.add(getTestRez().get(3));
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
     * Test of createAllReservations method, of class ReservationEditor.
     */
    @Test
    public void testCreateAllReservations() throws Exception {
        
        System.out.println("Test createAllReservations");
        
        Mockito.when(sweeper.getOverwrittenReservations(Mockito.any(RezervaceMistnosti.class) , Mockito.any(ArrayList.class) , Mockito.any(Uzivatel.class))).thenReturn(getTestOverwritten());
        Mockito.when(rezOper.getRezervace(Mockito.any(Date.class), Mockito.any(Mistnost.class))).thenReturn(getTestRez());
        //Mockito.when(rezOper.changeStatusToOverWritten(Mockito.any(RezervaceMistnosti.class)));
        Mockito.when(roomFac.isInterfering(Mockito.any(RezervaceMistnosti.class),Mockito.any(RezervaceMistnosti.class))).thenReturn(false);
        
        instance.createAllReservations(getTestMistnost(50), getTestRez(), getTestUzivatel());
        
        Mockito.verify(rezOper, Mockito.times(10)).changeStatusToOverWritten(Mockito.any(RezervaceMistnosti.class));
        
        System.out.println("Success.");
    }
}
