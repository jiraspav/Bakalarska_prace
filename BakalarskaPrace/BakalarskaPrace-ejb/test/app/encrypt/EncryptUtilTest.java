/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app.encrypt;

import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author Pavel
 */
public class EncryptUtilTest {
    
    public EncryptUtilTest() {
    }
    
    private EncryptUtil instance;
    
    @Before
    public void setUp() {
        instance = new EncryptUtil();
    }
    
    /**
     * Test of encode method, of class EncryptUtil.
     */
    @Test
    public void testEncode() throws Exception {
        System.out.println("Test encode & decode");
        
        String test = "test";
        
        String encodedTest = instance.encode(test);
        
        assertEquals(instance.decode(encodedTest), test);
        
        System.out.println("Success.");
    }
}
