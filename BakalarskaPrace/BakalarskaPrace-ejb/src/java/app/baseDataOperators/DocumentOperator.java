/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app.baseDataOperators;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 *
 * @author asus
 */
@Stateless
public class DocumentOperator {
    
    
    /**
     * Metoda pro získání nového objektu typu Document ze streamu.
     * @param input stream ze kterého se načítají data
     * @return nový objekt typu Document
     */
    public Document getNewDocument(InputStream input){
            Document doc = null;
        try {
            
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            doc = db.parse(input);
            doc.getDocumentElement().normalize();
            
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(DocumentOperator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(DocumentOperator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DocumentOperator.class.getName()).log(Level.SEVERE, null, ex);
        }
        return doc;
    
    }
    
}
