/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app.facade.schedulerEditorPF.impl;

import app.baseDataOperators.*;
import dbEntity.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.ScheduleModel;
/**
 *
 * @author Pavel
 */
public class SchedulerEditorPFTest {
    
    private SchedulerEditorPF instance;
    private MistnostOperator mistOper;
    private RezervaceMistnostiOperator rezOper;
    private UzivatelOperator uzivOper;
    private RozvrhyOperator rozOper;
    private SemestrOperator semOper;
    private DnyVTydnuOperator denOper;
    
    
    
    public SchedulerEditorPFTest() {
    }

   
    @Before
    public void setUp() {
        
        uzivOper = Mockito.mock(UzivatelOperator.class);
        mistOper = Mockito.mock(MistnostOperator.class);
        rezOper = Mockito.mock(RezervaceMistnostiOperator.class);
        rozOper = Mockito.mock(RozvrhyOperator.class);
        semOper = Mockito.mock(SemestrOperator.class);
        denOper = Mockito.mock(DnyVTydnuOperator.class);
        
        instance = new SchedulerEditorPF();
        
        instance.setUzivOper(uzivOper);
        instance.setDenOper(denOper);
        instance.setMistOper(mistOper);
        instance.setRezOper(rezOper);
        instance.setRozOper(rozOper);
        instance.setSemOper(semOper);
        
    }

    
    private Mistnost getTestMistnost(){
        return new Mistnost();
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
        
        return testInterfering;
    }
    private Semestr getTestSemestr(){
        Semestr test = new Semestr(1, "TEST", new Date(0), new Date(1000000));
        return test;
    }
    private ArrayList<Rozvrhy> getTestRozvhry(){
        
        return new ArrayList<Rozvrhy>();
    
    }
    /**
     * Test of createNewModel method, of class SchedulerEditorPF.
     */
    @Test
    public void testCreateNewModel() throws Exception {
        
        ScheduleModel result = instance.createNewModel(null, new Uzivatel());
        
        
        
        
        assertEquals((new DefaultScheduleModel()).getEventCount(), result.getEventCount());
        
        /*Mockito.when(mistOper.getMistnost(Mockito.anyString())).thenReturn(getTestMistnost());
        Mockito.when(denOper.getENDen(Mockito.anyString())).thenReturn(getTestDen());
        Mockito.when(rezOper.getRezervace(getTestMistnost())).thenReturn(getTestRez());
        Mockito.when(semOper.getLatestSemestr()).thenReturn(getTestSemestr());
        Mockito.when(rozOper.getRozvrhy(getTestMistnost(), getTestDen())).thenReturn(getTestRozvhry());*/
                
    }
    
    private Date putTogetherTimeDate(Date date,Date time){
        Calendar datum = GregorianCalendar.getInstance();
        datum.setTime(date);
        Calendar cas = GregorianCalendar.getInstance();
        cas.setTime(time);

        Calendar celkem = GregorianCalendar.getInstance();
        celkem.set(datum.get(Calendar.YEAR), datum.get(Calendar.MONTH), datum.get(Calendar.DAY_OF_MONTH),
                    cas.get(Calendar.HOUR_OF_DAY), cas.get(Calendar.MINUTE), cas.get(Calendar.SECOND));
        Date nove = celkem.getTime();
        return nove;
    }
    
}
