/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app.facade.register.impl;

import app.XMLparser.ParserController;
import app.baseDataOperators.*;
import app.encrypt.EncryptUtil;
import app.facade.reservationEditor.impl.ReservationEditor;
import app.facade.roomFinder.RoomFinderFacade;
import app.sweeper.Sweeper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

/**
 *
 * @author Pavel
 */
public class RegisterServiceEJBTest {
    
    
    public RegisterServiceEJBTest() {
    }

    private RegisterServiceEJB instance;
    private EncryptUtil encrypt;
    private UzivatelOperator uzivOper;
    private GroupTableOperator groupOperator;
    private KosApiOperator kosOperator;
    private ParserController parsCon;
    
    @Before
    public void setUp() {
        
        encrypt = Mockito.mock(EncryptUtil.class);
        uzivOper = Mockito.mock(UzivatelOperator.class);
        groupOperator = Mockito.mock(GroupTableOperator.class);
        kosOperator = Mockito.mock(KosApiOperator.class);
        parsCon = Mockito.mock(ParserController.class);
        
        
        instance = new RegisterServiceEJB();
        
        instance.setEncrypt(encrypt);
        instance.setUzivOper(uzivOper);
        instance.setGroupOperator(groupOperator);
        instance.setKosOperator(kosOperator);
        instance.setParsCon(parsCon);
        
        
    }
    
    /**
     * Test of register method, of class RegisterServiceEJB.
     */
    @Test
    public void testRegister() throws Exception {
        
        System.out.println("Test Register");
        
        
        
        System.out.println("Success.");
        
    }
}
