/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package view.bundle;

import java.util.ResourceBundle;
import javax.ejb.Stateless;

/**
 *
 * @author Pavel
 */
@Stateless
public class ResourceBundleOperator {
    
    /**
     * Metoda pro získávání textu z bundle
     * @param bundleName název textu z bundle
     * @return text z bundle
     */
    public String getMsg(String bundleName){
        ResourceBundle bundle = ResourceBundle.getBundle("view.bundle.messages");
        return bundle.getString(bundleName);
    }
}
