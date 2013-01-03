/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans.mistnosti;

import view.mistnosti.VytvoreniRezervaceMB;
import app.XMLparser.ParserController;
import app.baseDataOperators.MistnostOperator;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.embeddable.EJBContainer;
import javax.inject.Inject;
import javax.naming.Context;
import javax.naming.NamingException;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author Pavel
 */
public class VytvoreniRezervaceMBTest {
    
    private static EJBContainer container;
    private VytvoreniRezervaceMB instance;
    private static Context ctx;
    @Inject MistnostOperator mistOper;
    
    public VytvoreniRezervaceMBTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        System.out.println("Creating EJBContainer");
        Map<String, Object> props = new HashMap<String, Object>();
        props.put("org.glassfish.ejb.embedded.glassfish.configuration.file", "glassfish-install/domain.xml");
        container = EJBContainer.createEJBContainer(props);
        ctx = container.getContext();
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        container.close();
    }
    
    @Before
    public void setUp() {
        try {
            instance = (VytvoreniRezervaceMB) container.getContext().lookup("java:global/classes/VytvoreniRezervaceMB");
        } catch (NamingException ex) {
            Logger.getLogger(VytvoreniRezervaceMB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of saveAllPreparedReservations method, of class VytvoreniRezervaceMB.
     */
    @Test
    public void testSaveAllPreparedReservations() {
    }

    /**
     * Test of switchActive method, of class VytvoreniRezervaceMB.
     */
    @Test
    public void testSwitchActive() {
    }

    /**
     * Test of addReservationToList method, of class VytvoreniRezervaceMB.
     */
    @Test
    public void testAddReservationToList() {
    }

    /**
     * Test of deleteSelected method, of class VytvoreniRezervaceMB.
     */
    @Test
    public void testDeleteSelected() {
    }

    /**
     * Test of smazRoot method, of class VytvoreniRezervaceMB.
     */
    @Test
    public void testSmazRoot() {
    }

    /**
     * Test of getRoot method, of class VytvoreniRezervaceMB.
     */
    @Test
    public void testGetRoot() {
    }

    /**
     * Test of setRoot method, of class VytvoreniRezervaceMB.
     */
    @Test
    public void testSetRoot() {
    }

    /**
     * Test of getSelectedNode method, of class VytvoreniRezervaceMB.
     */
    @Test
    public void testGetSelectedNode() {
    }

    /**
     * Test of setSelectedNode method, of class VytvoreniRezervaceMB.
     */
    @Test
    public void testSetSelectedNode() {
    }

    /**
     * Test of toolTip method, of class VytvoreniRezervaceMB.
     */
    @Test
    public void testToolTip() {
        
        String roomName = "";
        
        String result = instance.toolTip(roomName);
        
        assertNull(result);
        
        
    }
}
