/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app.facade.RoomTreeCreator.impl;

import app.baseDataOperators.MistnostOperator;
import app.baseDataOperators.StrediskoOperator;
import app.facade.reservationEditor.impl.ReservationEditor;
import dbEntity.Mistnost;
import dbEntity.Stredisko;
import java.util.ArrayList;
import javax.inject.Inject;
import org.junit.*;
import static org.junit.Assert.*;
import org.mockito.Mockito;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

/**
 *
 * @author Pavel
 */
public class RoomTreeCreatorTest {
    
    public RoomTreeCreatorTest() {
    }

    private RoomTreeCreator instance;
    private StrediskoOperator stredOper;
    private MistnostOperator mistOper;
    
    @Before
    public void setUp() {
        
        mistOper = Mockito.mock(MistnostOperator.class);
        stredOper = Mockito.mock(StrediskoOperator.class);
        
        instance = new RoomTreeCreator();
        
        instance.setMistOper(mistOper);
        instance.setStredOper(stredOper);
        
    }
    

    private ArrayList<Stredisko> getTestStrediska(){
        ArrayList<Stredisko> test = new ArrayList<Stredisko>();
        Stredisko tt = new Stredisko(1L, 1111L, "testStredisko1");        
        Stredisko tt2 = new Stredisko(1L, 1111L, "testStredisko2");
        Stredisko tt3 = new Stredisko(1L, 1111L, "testStredisko3");
        test.add(tt);
        test.add(tt2);
        test.add(tt3);
        
        return test;
    }
    private ArrayList<Mistnost> getTestMistnosti(){
        ArrayList<Mistnost> test = new ArrayList<Mistnost>();
        Mistnost tt = new Mistnost(1L, "testMistnost1");
        Mistnost tt2 = new Mistnost(1L, "testMistnost2");
        Mistnost tt3 = new Mistnost(1L, "testMistnost3");
        test.add(tt);
        test.add(tt2);
        test.add(tt3);
        
        return test;
    }
    /**
     * Test of createRoomTree method, of class RoomTreeCreator.
     */
    @Test
    public void testCreateRoomTree() throws Exception {
        
        System.out.println("Test createRoomTree");
        
        Mockito.when(stredOper.getAll()).thenReturn(getTestStrediska());
        Mockito.when(mistOper.getMistnosti(Mockito.any(Stredisko.class))).thenReturn(getTestMistnosti());
        
        TreeNode createRoomTree = instance.createRoomTree(new DefaultTreeNode());
        
        assertEquals(createRoomTree.getChildren().get(0).getChildCount(), 3);
        assertEquals(createRoomTree.getChildren().get(0).getChildren().get(0).getChildCount(), 3);
        
        System.out.println("Success.");
        
    }
}
