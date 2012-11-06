/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package XMLparser;


import view.auth.LoginVerifier;
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
    private @Inject MistnostFacade mistFac;
    private @Inject StrediskoFacade strFac;
    private @Inject PredmetyFacade predFac;
    private @Inject SemestrFacade semFac;
    private @Inject RozvrhyFacade rozFac;
    private @Inject DenVTydnuFacade denFac;
    private @Inject UpdateRozvrhuFacade upRozFac;
    private @Inject LoginVerifier user;
    
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
        
            URL url = new URL(kosapiUrls.departments);
            HttpsURLConnection http = (HttpsURLConnection) url.openConnection();
            
            String userPassword = user.getLogin()+":"+user.getPassword();  
            //zakodovani dat do BASE64
            String encoding = new sun.misc.BASE64Encoder().encode(userPassword.getBytes());
            http.setRequestProperty("Authorization", "Basic "+encoding);

            NodeList nodeList = null;
            try {
            
                        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                        DocumentBuilder db = dbf.newDocumentBuilder();
                        Document doc = db.parse(http.getInputStream());
                        doc.getDocumentElement().normalize();
                        //System.out.println("Root element " + doc.getDocumentElement().getNodeName());
                        
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
                            Stredisko stredisko = new Stredisko();
                            
                            stredisko.setIDstredisko(id);
                            stredisko.setIdentCislo(code);
                            stredisko.setNazev(department);
                            
                            persistDepartment(stredisko);
                        }
                        utvs = false;
                    }
                
        } catch (SAXException ex) {
            Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
        }
            http.disconnect();
    }
    
    private void persistDepartment(Stredisko stredisko){
        System.out.println(stredisko.getNazev());
        strFac.create(stredisko);
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
            URL url = new URL(kosapiUrls.rooms);
            HttpsURLConnection http = (HttpsURLConnection) url.openConnection();
            
            String userPassword = user.getLogin()+":"+user.getPassword();  
            //zakodovani dat do BASE64
            String encoding = new sun.misc.BASE64Encoder().encode(userPassword.getBytes());
            http.setRequestProperty("Authorization", "Basic "+encoding);
            
           try {     
                    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                    DocumentBuilder db = dbf.newDocumentBuilder();
                    Document doc = db.parse(http.getInputStream());
                    doc.getDocumentElement().normalize();
                    
                    NodeList nodeList = doc.getElementsByTagName("room");
                
                
                for(int i = 0; i<nodeList.getLength(); i++){
                    
                    Element current = (Element) nodeList.item(i);
                    
                    String uri = current.getAttribute("uri");
                    
                    Long id = Long.parseLong(current.getAttribute("id"));
                    Long lucky = new Long(777);
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
                    
                    Mistnost mistnost = new Mistnost();
                    
                    mistnost.setIDmistnosti(id);
                    mistnost.setZkratka(code);
                    mistnost.setIDstrediska(new Stredisko(department));
                    
                    this.persistRoom(mistnost);
                }
                
        } catch (SAXException ex) {
            Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
        }
           http.disconnect();
    }
    
    private void persistRoom(Mistnost mistnost){
        System.out.println(mistnost.getZkratka());
        mistFac.create(mistnost);
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
            URL url = new URL(kosapiUrls.courses);
            HttpsURLConnection http = (HttpsURLConnection) url.openConnection();
            
            String userPassword = user.getLogin()+":"+user.getPassword();  
            //zakodovani dat do BASE64
            String encoding = new sun.misc.BASE64Encoder().encode(userPassword.getBytes());
            http.setRequestProperty("Authorization", "Basic "+encoding);
            
           try {     
                    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                    DocumentBuilder db = dbf.newDocumentBuilder();
                    Document doc = db.parse(http.getInputStream());
                    doc.getDocumentElement().normalize();
                    
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
                    
                    Predmety predmet = new Predmety();
                    
                    predmet.setIDpredmetu(id);
                    predmet.setNazev(name);
                    predmet.setZkratka(shortName);
                    
                    persistCourse(predmet);
                }
                
        } catch (SAXException ex) {
            Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
        }
        http.disconnect();
    }

    private void persistCourse(Predmety predmet) {
        System.out.println(predmet.getNazev());
            predFac.create(predmet);
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
         try {
            URL url = new URL(kosapiUrls.roomsNolevel);
            HttpsURLConnection http = (HttpsURLConnection) url.openConnection();
            
            String userPassword = user.getLogin()+":"+user.getPassword();  
            //zakodovani dat do BASE64
            String encoding = new sun.misc.BASE64Encoder().encode(userPassword.getBytes());
            http.setRequestProperty("Authorization", "Basic "+encoding);
            
                    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                    DocumentBuilder db = dbf.newDocumentBuilder();
                    Document doc = db.parse(http.getInputStream());
                    doc.getDocumentElement().normalize();
                    
                    NodeList nodeList = doc.getElementsByTagName("room");
             http.disconnect();   
                
                for(int i = 0; i<nodeList.getLength(); i++){
                    
                    Element current = (Element) nodeList.item(i);
                    
                    
                    String uri = current.getAttribute("uri");
                    Long idMistnosti = Long.parseLong(current.getAttribute("id"));
                    //System.out.println("---------------------------------------------");
                    //System.out.println("| Mistnost uri = "+uri+" id = "+id);
                    
                    
                    
                    //pripojeni ke kosapi pro jednu konkretni mistnost a stazeni vsech paralelek pro danou mistnost
                    URL adresaParalelky = new URL(uri+"parallels?paging=false&level=1");
                    HttpsURLConnection httpParalelky = (HttpsURLConnection) adresaParalelky.openConnection();

                    //zakodovani dat do BASE64
                    httpParalelky.setRequestProperty("Authorization", "Basic "+encoding);
            
                    DocumentBuilderFactory dbf2 = DocumentBuilderFactory.newInstance();
                    DocumentBuilder db2 = dbf2.newDocumentBuilder();
                    Document doc2 = db2.parse(httpParalelky.getInputStream());
                    doc2.getDocumentElement().normalize();
                    httpParalelky.disconnect();
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
                        //  System.out.println("| Vyuka predmetu "+idParalelky+" v "+den+" od "+od+" do "+do1+" parity "+parita);








                            //zjisteni id predmetu
                            URL adresaPredmetu = new URL(urlPredmetu);
                            HttpsURLConnection httpPredmetu = (HttpsURLConnection) adresaPredmetu.openConnection();

                            //zakodovani dat do BASE64
                            httpPredmetu.setRequestProperty("Authorization", "Basic "+encoding);

                            DocumentBuilderFactory dbf3 = DocumentBuilderFactory.newInstance();
                            DocumentBuilder db3 = dbf3.newDocumentBuilder();
                            Document doc3 = db3.parse(httpPredmetu.getInputStream());
                            doc3.getDocumentElement().normalize();
                            httpPredmetu.disconnect();

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


                            Rozvrhy rozvrh = new Rozvrhy();
                            rozvrh.setIDrozvrhu(new Long(1));
                            rozvrh.setIDdnu(denFac.getDenByNazev(den));
                            rozvrh.setIDmistnosti(new Mistnost(idMistnosti));
                            rozvrh.setIDpredmetu(new Predmety(idPredmetu));
                            rozvrh.setDo1(doDate);
                            rozvrh.setOd(odDate);

                            if(parita.equals("BOTH")){
                                rozvrh.setLichyTyden(true);
                                rozvrh.setSudyTyden(true);
                            }
                            else if(parita.equals("ODD")){
                                rozvrh.setLichyTyden(true);
                                rozvrh.setSudyTyden(false);
                            }
                            else {
                                rozvrh.setLichyTyden(false);
                                rozvrh.setSudyTyden(true);
                            }

                            persistRozvrh(rozvrh);




                        }
                    
                    
                    
                    
                    //System.out.println("---------------------------------------------");
                    }
                }
        } catch (MalformedURLException ex) {
            Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
        }
         
    }

    private void persistRozvrh(Rozvrhy rozvrh) {
        System.out.println(rozvrh.getIDpredmetu().getZkratka()+" "+rozvrh.getOd()+"-"+rozvrh.getDo1());
        this.rozFac.create(rozvrh);
    }
    
    /**
     * Načte data o aktuálním rozvrhu z KosAPI a plní s nimi databázi.
     * @throws IOException 
     * @see Semestr
     */
    public void setSemestr() throws IOException{
    
        try {
            URL url = new URL(kosapiUrls.currentSemestr);
            HttpsURLConnection http = (HttpsURLConnection) url.openConnection();

            String userPassword = user.getLogin()+":"+user.getPassword();  
            //zakodovani dat do BASE64
            String encoding = new sun.misc.BASE64Encoder().encode(userPassword.getBytes());
            http.setRequestProperty("Authorization", "Basic "+encoding);

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(http.getInputStream());
            doc.getDocumentElement().normalize();
            http.disconnect();
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
            
            Semestr current = new Semestr();
            current.setIDsemestru(idSemestru);
            current.setKod(code);
            current.setZacina(zacatek);
            current.setKonci(konec);
            
            this.persistSemestr(current);
            
            UpdateRozvrhu update = new UpdateRozvrhu();
            update.setIDupdaterozvrhu(new Integer(1));
            update.setIDsemestru(current);
            update.setDatumAktualizaceRozvrhu(new Date());
            
            this.persistUpdateRozvrhu(update);
        } catch (SAXException ex) {
            Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            //Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Couldnt connect.");
        }
        
        
    }
    private void persistSemestr(Semestr semestr){
        this.semFac.create(semestr);
    }
    private void persistUpdateRozvrhu(UpdateRozvrhu update){
        this.upRozFac.create(update);
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
    public List<String> getAtributes(String login, String mujLogin, String mojeHeslo) {
        ArrayList<String> seznamAtributu = new ArrayList<String>();
        try {
            URL url = new URL(kosapiUrls.people+"/"+login);
            HttpsURLConnection http = (HttpsURLConnection) url.openConnection();
            //System.out.println(mujLogin+":"+mojeHeslo);
            String userPassword = mujLogin+":"+mojeHeslo;  
            //zakodovani dat do BASE64
            String encoding = new sun.misc.BASE64Encoder().encode(userPassword.getBytes());
            http.setRequestProperty("Authorization", "Basic "+encoding);

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(http.getInputStream());
            doc.getDocumentElement().normalize();
            http.disconnect();
            
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
            if(teacher.getLength() > 0)
                role = "ucitel";
            else
                role = "student";
            
            
            
            
            
            seznamAtributu.add(fullName);
            seznamAtributu.add(idUzivatele);
            seznamAtributu.add(role);
            
        } catch (SAXException ex) {
            Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            //Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Couldnt connect.");
        }
        return seznamAtributu;
    }

    
}

    
