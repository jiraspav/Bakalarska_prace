/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app.encrypt;

import java.io.IOException;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;
import javax.ejb.Stateless;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 *
 * @author Pavel
 */
@Stateless
public class EncryptUtil {
    
    /**
     * 
     */
    public static final Charset CHARSET  = Charset.forName("UTF-8");
    private static final String ENCRYPTOR = "a5eg7w9ax14a99xv1g4h6j79a4x4y2";
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
    
    /**
     * Metoda pro zašifrování daného Stringu
     * @param string String určený pro zašifrování
     * @return zašifrovaný String
     */
    public String encode(String string){
        String encryptedPwd = null;
        try {
            DESKeySpec keySpec = new DESKeySpec(ENCRYPTOR.getBytes(CHARSET));
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey key = keyFactory.generateSecret(keySpec);
            sun.misc.BASE64Encoder base64encoder = new BASE64Encoder();

            byte[] cleartext = string.getBytes(CHARSET);      

            Cipher cipher = Cipher.getInstance("DES"); 
            cipher.init(Cipher.ENCRYPT_MODE, key);
            encryptedPwd = base64encoder.encode(cipher.doFinal(cleartext));
            
            
        } catch (IllegalBlockSizeException ex) {
            Logger.getLogger(EncryptUtil.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BadPaddingException ex) {
            Logger.getLogger(EncryptUtil.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchPaddingException ex) {
            Logger.getLogger(EncryptUtil.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeySpecException ex) {
            Logger.getLogger(EncryptUtil.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(EncryptUtil.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeyException ex) {
            Logger.getLogger(EncryptUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return encryptedPwd;
    }
    /**
     * Metoda pro dešifrování daného zašifrovaného Stringu
     * @param string dešifrovaný String
     * @return dešifrovaný String
     */
    public String decode(String string){
        String decoded = null;
        try {
            DESKeySpec keySpec = new DESKeySpec(ENCRYPTOR.getBytes(CHARSET));
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey key = keyFactory.generateSecret(keySpec);
            
            sun.misc.BASE64Decoder base64decoder = new BASE64Decoder();
            
            byte[] encrypedPwdBytes = base64decoder.decodeBuffer(string);

            Cipher cipher2 = Cipher.getInstance("DES");// cipher is not thread safe
            cipher2.init(Cipher.DECRYPT_MODE, key);
            byte[] plainTextPwdBytes = (cipher2.doFinal(encrypedPwdBytes));
            
            decoded = new String(plainTextPwdBytes);
            
            
        } catch (IOException ex) {
            Logger.getLogger(EncryptUtil.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalBlockSizeException ex) {
            Logger.getLogger(EncryptUtil.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BadPaddingException ex) {
            Logger.getLogger(EncryptUtil.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchPaddingException ex) {
            Logger.getLogger(EncryptUtil.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeySpecException ex) {
            Logger.getLogger(EncryptUtil.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(EncryptUtil.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeyException ex) {
            Logger.getLogger(EncryptUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return decoded;
    }
    
    
    /**
     * Metoda pro získávání random textových řetězců
     * @param len délka textového řetězce
     * @return náhodný textový řetězec
     */
    public String randomString( int len ){
        
        String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random rnd = new Random();
        
        StringBuilder sb = new StringBuilder( len );
        for( int i = 0; i < len; i++ ) 
            sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
        return sb.toString();
    }
}
