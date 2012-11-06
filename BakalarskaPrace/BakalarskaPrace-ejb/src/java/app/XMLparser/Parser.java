/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app.XMLparser;


import app.baseDataOperators.*;
import app.sessionHolder.SessionHolderEJB;
import dbEntity.*;
import entityFacade.*;
import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.net.ssl.HttpsURLConnection;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author Pavel
 */
@Stateless
public class Parser implements Serializable{

    private @Inject DenVTydnuFacade denFac;
    private @Inject SessionHolderEJB session;
    
    private @Inject KosApiOperator kosOper;
    private @Inject RozvrhyOperator rozOper;
    private @Inject SemestrOperator semOper;
    private @Inject UpdateRozvrhuOperator upRozOper;
    private @Inject MistnostOperator misOper;
    private @Inject StrediskoOperator stredOper;
    private @Inject PredmetyOperator predOper;
    private @Inject DocumentOperator documentOper;
    
    static String[] converter = {"7:30","9:00","9:15","10:45","11:00","12:30",
                                "12:45","14:15","14:30","16:00","16:15","17:45","18:00","19:30","20:30"};
    
    
    /**
     * 
     */
    
    public Parser() {}
    
    
    /**
     * Načítá data vsech stredisek z KosAPI a plní s nimi databázi.
     * @throws IOException 
     * @see Stredisko
     */
    public void setDepartments() throws IOException {
        
        System.out.println("|---------------------------------|");
        System.out.println("|            Střediska            |");
        System.out.println("|---------------------------------|");
        
            kosOper.createNewConnection(kosapiUrls.departments, session.getLoggedUzivatel().getLogin(), session.getPassword());
            
            NodeList nodeList = null;

                    Document doc = documentOper.getNewDocument(kosOper.getConnection().getInputStream());

                    nodeList = doc.getElementsByTagName("department");
                        
                    boolean utvs = false;
                    for(int i = 0; i<nodeList.getLength(); i++){
                        
                        Element current = (Element) nodeList.item(i);
                        
                        Long id = Long.parseLong(current.getAttribute("id"));
                        
                        
                        Long code = null;
                        String department = null;
                        
                        NodeList childs = current.getChildNodes();
                        
                        for(int b = 0; b < childs.getLength(); b++){
                            
                            Node node = childs.item(b);
                            
                            if(node.getNodeName().equals("code")){
                                String kod = node.getTextContent();
                                if(!(id.intValue() == 1000006 || id.intValue() == 475)){
                                    code = Long.parseLong(kod);
                                }
                                else
                                    utvs = true;
                            }
                            
                            if(node.getNodeName().equals("nameCz"))
                                department = node.getTextContent();
                        }
                        if(!utvs){
                            
                            stredOper.createStredisko(id,code,department);

                        }
                        utvs = false;
                    }
                
            kosOper.closeConnection();
    }
    
    /**
     * Načítá data vsech mistnosti z KosAPI a plní s nimi databázi.
     * @throws IOException 
     * @see Mistnost
     */
    
    public void setRooms() throws IOException {
            
        System.out.println("|---------------------------------|");
        System.out.println("|            Místnosti            |");
        System.out.println("|---------------------------------|");
        
            kosOper.createNewConnection(kosapiUrls.rooms, session.getLoggedUzivatel().getLogin(), session.getPassword());
        
                    Document doc = documentOper.getNewDocument(kosOper.getConnection().getInputStream());
                    
                    NodeList nodeList = doc.getElementsByTagName("room");
                
                
                for(int i = 0; i<nodeList.getLength(); i++){
                    
                    Element current = (Element) nodeList.item(i);
                    
                    String uri = current.getAttribute("uri");
                    
                    Long id = Long.parseLong(current.getAttribute("id"));
                    
                    String code = null;
                    Long department = null;
                    
                    NodeList childs = current.getChildNodes();
                    for(int b = 0; b < childs.getLength(); b++){
                        Node node = childs.item(b);
                        if(node.getNodeName().equals("code"))
                            code = node.getTextContent();
                        
                        if(node.getNodeName().equals("department"))
                            department = (Long.parseLong(((Element)node).getAttribute("id")));
                        
                    }
                    
                    misOper.createMistnost(id,code,new Stredisko(department));
                    
                }
                
           kosOper.closeConnection();
    }
    
    /**
     * Načítá data vsech predmetu z KosAPI a plní s nimi databázi.
     * @throws IOException 
     * @see Predmety
     */
    public void setCourses() throws IOException{
    
        System.out.println("|---------------------------------|");
        System.out.println("|            Předměty             |");
        System.out.println("|---------------------------------|");
        
        kosOper.createNewConnection(kosapiUrls.courses, session.getLoggedUzivatel().getLogin(), session.getPassword());
            
                    Document doc = documentOper.getNewDocument(kosOper.getConnection().getInputStream());
                    
                    NodeList nodeList = doc.getElementsByTagName("course");
                
                
                for(int i = 0; i<nodeList.getLength(); i++){
                    
                    Element current = (Element) nodeList.item(i);
                    
                    
                    Long id = Long.parseLong(current.getAttribute("id"));
                    
                    String shortName = null;
                    String name = null;
                    
                    NodeList childs = current.getChildNodes();
                    for(int b = 0; b < childs.getLength(); b++){
                        Node node = childs.item(b);
                        if(node.getNodeName().equals("code"))
                            shortName = node.getTextContent();
                        
                        if(node.getNodeName().equals("nameCz"))
                            name = node.getTextContent();
                        
                    }
                    
                    predOper.createPredmet(id,name,shortName);
                    
                }
                
        kosOper.closeConnection();
    }

    /**
     * Načítá data vsech rozvrhu z KosAPI a plní s nimi databázi.
     * @throws IOException 
     * @see Rozvrhy
     */
    public void setRozvrhy() throws IOException {
        System.out.println("|---------------------------------|");
        System.out.println("|            Rozvrhy              |");
        System.out.println("|---------------------------------|");
             
             kosOper.createNewConnection(kosapiUrls.roomsNolevel, session.getLoggedUzivatel().getLogin(), session.getPassword());
            
                    Document doc = documentOper.getNewDocument(kosOper.getConnection().getInputStream());
                    
                    NodeList nodeList = doc.getElementsByTagName("room");
                    
             kosOper.closeConnection();   
                
                for(int i = 0; i<nodeList.getLength(); i++){
                    
                    Element current = (Element) nodeList.item(i);
                    
                    
                    String uri = current.getAttribute("uri");
                    Long idMistnosti = Long.parseLong(current.getAttribute("id"));
                    
                    
                    //pripojeni ke kosapi pro jednu konkretni mistnost a stazeni vsech paralelek pro danou mistnost
                    kosOper.createNewConnection(uri+"parallels?paging=false&level=1", session.getLoggedUzivatel().getLogin(), session.getPassword());
                    
                        Document doc2 = documentOper.getNewDocument(kosOper.getConnection().getInputStream());
                    
                    kosOper.closeConnection();
                    
                    NodeList nodeList2 = doc2.getElementsByTagName("parallel");
                    //System.out.println("| Adresa = "+uri+"parallels?paging=false&level=1");
                    //System.out.println("| Pocet paralelek = "+nodeList2.getLength());
                    //System.out.println("---------------------------------------------");
                    
                    
                    //informace o jednotlivych paralelkach
                    String den = null,od = null,do1 = null,parita = null,urlPredmetu = null;
                    if(nodeList2.getLength() > 0){
                        for(int a = 0; a<nodeList2.getLength(); a++){


                            Node current2 = nodeList2.item(a);

                            Element currParalelka = (Element) current2;

                            urlPredmetu = currParalelka.getAttribute("uri");

                            String split[] = urlPredmetu.split("instances");

                            urlPredmetu = split[0];

                            NodeList childs = current2.getChildNodes();

                            for(int c = 0; c<childs.getLength(); c++){
                                Node node = childs.item(c);
                                if(node.getNodeName().equals("day"))
                                    den = node.getTextContent();
                                if(node.getNodeName().equals("firstHour"))
                                    od = node.getTextContent();
                                if(node.getNodeName().equals("lastHour"))
                                    do1 = node.getTextContent();
                                if(node.getNodeName().equals("parity"))
                                    parita = node.getTextContent();

                            }
                        

                            //zjisteni id predmetu
                            
                            kosOper.createNewConnection(urlPredmetu, session.getLoggedUzivatel().getLogin(), session.getPassword());

                                Document doc3 = documentOper.getNewDocument(kosOper.getConnection().getInputStream());
                            
                            kosOper.closeConnection();

                            NodeList nodeList3 = doc3.getElementsByTagName("course");
                            Long idPredmetu = Long.parseLong(((Element)nodeList3.item(0)).getAttribute("id"));

                            od = converter[Integer.parseInt(od)-1];
                            do1 = converter[Integer.parseInt(do1)-1];

                            Date odDate = null,doDate = null;
                            try {
                                SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
                                odDate = sdf.parse(od);
                                doDate = sdf.parse(do1);
                            } catch (ParseException ex) {
                                Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
                            }

                            boolean lichy,sudy;
                            
                            if(parita.equals("BOTH")){
                                lichy = true;
                                sudy = true;
                            }
                            else if(parita.equals("ODD")){
                                lichy = true;
                                sudy = false;
                            }
                            else {
                                lichy = false;
                                sudy = true;
                            }
                            
                            rozOper.createRozvrh(new Long(1),denFac.getDenByNazev(den),new Mistnost(idMistnosti),
                                            new Predmety(idPredmetu),doDate,odDate,lichy,sudy);
                            
                        }
                    
                    //System.out.println("---------------------------------------------");
                    }
                }
    }

    
    /**
     * Načte data o aktuálním rozvrhu z KosAPI a plní s nimi databázi.
     * @throws IOException 
     * @see Semestr
     */
    public void setSemestr() throws IOException{
    
            kosOper.createNewConnection(kosapiUrls.currentSemestr, session.getLoggedUzivatel().getLogin(), session.getPassword());

                Document doc = documentOper.getNewDocument(kosOper.getConnection().getInputStream());
            
            kosOper.closeConnection();
            
            NodeList semestr = doc.getElementsByTagName("semester");
            Integer idSemestru = Integer.parseInt(((Element)semestr.item(0)).getAttribute("id"));
            
            NodeList attributes = semestr.item(0).getChildNodes();
                    String zacina = null,konci = null,code = null;
                    
            for(int i = 0; i<attributes.getLength(); i++){

                Node node = attributes.item(i);

                if(node.getNodeName().equals("code"))
                    code = node.getTextContent();
                if(node.getNodeName().equals("endDate"))
                    konci = node.getTextContent();
                if(node.getNodeName().equals("startDate"))
                    zacina = node.getTextContent();
            }
            
            
            Date zacatek = null,konec = null;
            try {
                String[] data = zacina.split("T");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                zacatek = sdf.parse(data[0]);
                data = konci.split("T");
                konec = sdf.parse(data[0]);
            } catch (ParseException ex) {
                Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            Semestr current = semOper.createSemestr(idSemestru,code,zacatek,konec);
            
            upRozOper.createUpdateRozvrhu(new Integer(1),current,new Date());
            
    }
    
    
    /**
     * Zjistí informace o uživateli z KosAPI.
     * 
     * 
     * 
     * @param login login uzivatele, o kterem chci zjistit informace
     * @param mujLogin muj login pouzivany k autorizaci na KosAPI
     * @param mojeHeslo me heslo k autorizaci na KosAPI
     * @return list atributu hledaneho uzivatele, pokud existuje
     *          (0) - cele jmeno ,
     *          (1) - id uzivatele,
     *          (2) - role uzivatele
     */
    public List<String> getAtributes(String login, String mujLogin, String mojeHeslo) throws IOException {
        ArrayList<String> seznamAtributu = new ArrayList<String>();
        
            kosOper.createNewConnection(kosapiUrls.people+"/"+login, mujLogin, mojeHeslo);
            
                Document doc = documentOper.getNewDocument(kosOper.getConnection().getInputStream());
            
            kosOper.closeConnection();
            
            NodeList person = doc.getElementsByTagName("person");
            NodeList teacher = doc.getElementsByTagName("teacher");
            NodeList attributes = person.item(0).getChildNodes();
                    String jmeno = null,prijmeni = null;
                    
            for(int i = 0; i<attributes.getLength(); i++){

                Node node = attributes.item(i);

                if(node.getNodeName().equals("firstName"))
                    jmeno = node.getTextContent();
                if(node.getNodeName().equals("surname"))
                    prijmeni = node.getTextContent();
            }
            
            String fullName = jmeno+" "+prijmeni;
            String idUzivatele = ((Element)person.item(0)).getAttribute("id");
            String role;
            if(teacher.getLength() > 0){
                role = "ucitel";
            }
            else{
                role = "student";
            }
            
            seznamAtributu.add(fullName);
            seznamAtributu.add(idUzivatele);
            seznamAtributu.add(role);
            
        return seznamAtributu;
    }
}

    
