/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app.encrypt;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;

/**
 *
 * @author Pavel
 */
@Stateless
public class EncryptUtil {
    /**
     * Hashovací metoda SHA-256 konvertuje obyčejný String na hash
     * @param password - String, který je potřeba zahashovat
     * @return String zahashovaný metodou SHA-256
     */
    public String SHA256(String password){
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(password.getBytes());
     
            byte byteData[] = md.digest();
     
            //convert the byte to hex format method 1
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < byteData.length; i++) {
             sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }
            
            return sb.toString();
            
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(EncryptUtil.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    
    public String randomString( int len ){
        
        String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random rnd = new Random();
        
        StringBuilder sb = new StringBuilder( len );
        for( int i = 0; i < len; i++ ) 
            sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
        return sb.toString();
    }
}
