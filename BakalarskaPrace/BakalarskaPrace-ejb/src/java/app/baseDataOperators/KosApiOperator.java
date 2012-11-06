/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app.baseDataOperators;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.net.ssl.HttpsURLConnection;

/**
 *
 * @author Pavel
 */
@Stateful
public class KosApiOperator {
    
    private HttpsURLConnection currConn;
    
    public void createNewConnection(String address, String login, String pass){
        
        try {
            URL url = new URL(address);
            currConn = (HttpsURLConnection) url.openConnection();

            String userPassword = login+":"+pass;  
            //zakodovani dat do BASE64
            String encoding = new sun.misc.BASE64Encoder().encode(userPassword.getBytes());
            currConn.setRequestProperty("Authorization", "Basic "+encoding);
        
        
        } catch (IOException ex) {
            Logger.getLogger(KosApiOperator.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public HttpsURLConnection getConnection(){
        return currConn;
    }
    
    @Remove
    public void closeConnection(){
        currConn.disconnect();
        currConn = null;
    }
}
