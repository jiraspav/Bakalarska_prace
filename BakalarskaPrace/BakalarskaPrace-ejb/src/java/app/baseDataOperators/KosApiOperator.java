/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app.baseDataOperators;

import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.net.ssl.HttpsURLConnection;

/**
 *
 * @author Pavel
 */
@Stateless
public class KosApiOperator {
    
    /**
     * Metoda pro vytvoření nového připojení na zadanou adresu.
     * @param address URL adresa připojení
     * @param login login určený pro autorizaci
     * @param pass hrslo určené pro autorizaci
     * @return nové připojení
     */
    public HttpsURLConnection createNewConnection(String address, String login, String pass){
        
        HttpsURLConnection currConn = null;
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
        return currConn;
    }
    
    /**
     * Metoda uzavírající dané připojení
     * @param currConn HttpsURLConnection určené k uzavření
     */
    public void closeConnection(HttpsURLConnection currConn){
        currConn.disconnect();
    }
}
