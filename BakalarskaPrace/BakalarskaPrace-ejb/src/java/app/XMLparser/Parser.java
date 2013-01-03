/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app.XMLparser;


import app.baseDataOperators.*;
import app.encrypt.EncryptUtil;
import dbEntity.*;
import entityFacade.*;
import java.io.IOException;
import java.io.Serializable;
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
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author Pavel
 */
@Stateless
public class Parser implements Serializable{

    private @Inject DnyVTydnuOperator denOper;
    
    private @Inject EncryptUtil encrypt;
    private @Inject KosApiOperator kosOper;
    private @Inject RozvrhyOperator rozOper;
    private @Inject SemestrOperator semOper;
    private @Inject UpdateRozvrhuOperator upRozOper;
    private @Inject MistnostOperator misOper;
    private @Inject StrediskoOperator stredOper;
    private @Inject PredmetyOperator predOper;
    private @Inject DocumentOperator documentOper;
    private @Inject VyucujiciOperator vyuOper;
    
    static String[] converter = {"7:30","9:00","9:15","10:45","11:00","12:30",
                                "12:45","14:15","14:30","16:00","16:15","17:45","18:00","19:30","20:30"};
    
    
    /**
     * 
     */
    
    public Parser() {}
    
    
    /**
     * Načítá data vsech stredisek z KosAPI a plní s nimi databázi.
     * @param logged právě přihlášený uživatel
     * @throws IOException 
     * @see Stredisko
     */
    public void setDepartments(Uzivatel logged) throws IOException {
        
        System.out.println("|---------------------------------|");
        System.out.println("|            Střediska            |");
        System.out.println("|---------------------------------|");
        
            HttpsURLConnection conn = kosOper.createNewConnection(kosapiUrls.departments, logged.getLogin(), encrypt.decode(logged.getHeslo()));
            
            NodeList nodeList = null;

                    Document doc = documentOper.getNewDocument(conn.getInputStream());

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
                
            kosOper.closeConnection(conn);
    }
    
    /**
     * Načítá data vsech mistnosti z KosAPI a plní s nimi databázi.
     * @param logged právě přihlášený uživatel
     * @throws IOException 
     * @see Mistnost
     */
    
    public void setRooms(Uzivatel logged) throws IOException {
            
        System.out.println("|---------------------------------|");
        System.out.println("|            Místnosti            |");
        System.out.println("|---------------------------------|");
        
            HttpsURLConnection conn = kosOper.createNewConnection(kosapiUrls.rooms, logged.getLogin(), encrypt.decode(logged.getHeslo()));
        
                    Document doc = documentOper.getNewDocument(conn.getInputStream());
                    
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
                        if(node.getNodeName().equals("code")){
                            code = node.getTextContent();
                        }
                        
                        if(node.getNodeName().equals("department")){
                            department = (Long.parseLong(((Element)node).getAttribute("id")));
                        }
                        
                    }
                    
                    //T2:C4-156 doesnt have department
                    if(!code.equals("T2:C4-156")){
                        misOper.createMistnost(id,code,new Stredisko(department));
                    }
                    
                }
                
           kosOper.closeConnection(conn);
    }
    
    /**
     * Načítá data vsech predmetu z KosAPI a plní s nimi databázi.
     * @param logged právě přihlášený uživatel
     * @throws IOException 
     * @see Predmety
     */
    public void setCourses(Uzivatel logged) throws IOException{
    
        System.out.println("|---------------------------------|");
        System.out.println("|            Předměty             |");
        System.out.println("|---------------------------------|");
        
        HttpsURLConnection conn = kosOper.createNewConnection(kosapiUrls.courses, logged.getLogin(), encrypt.decode(logged.getHeslo()));
            
                    Document doc = documentOper.getNewDocument(conn.getInputStream());
                    
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
                
        kosOper.closeConnection(conn);
    }

    /**
     * Načítá data vsech rozvrhu z KosAPI a plní s nimi databázi. Zároveň s rozvrhy
     * se do databáze ukládají s rozvrhy spojení vyučující.
     * @param logged právě přihlášený uživatel
     * @throws IOException 
     * @see Rozvrhy
     */
    public void setRozvrhy(Uzivatel logged) throws IOException {
        System.out.println("|---------------------------------|");
        System.out.println("|            Rozvrhy              |");
        System.out.println("|---------------------------------|");
             
             HttpsURLConnection conn = kosOper.createNewConnection(kosapiUrls.roomsNolevel, logged.getLogin(), encrypt.decode(logged.getHeslo()));
            
                    Document doc = documentOper.getNewDocument(conn.getInputStream());
                    
                    NodeList nodeList = doc.getElementsByTagName("room");
                    
             kosOper.closeConnection(conn);   
                
                for(int i = 0; i<nodeList.getLength(); i++){
                    
                    Element current = (Element) nodeList.item(i);
                    
                    
                    String uri = current.getAttribute("uri");
                    Long idMistnosti = Long.parseLong(current.getAttribute("id"));
                    
                    
                    //pripojeni ke kosapi pro jednu konkretni mistnost a stazeni vsech paralelek pro danou mistnost
                    conn = kosOper.createNewConnection(uri+"parallels?paging=false&level=1", logged.getLogin(), encrypt.decode(logged.getHeslo()));
                    
                        Document doc2 = documentOper.getNewDocument(conn.getInputStream());
                    
                    kosOper.closeConnection(conn);
                    
                    NodeList nodeList2 = doc2.getElementsByTagName("parallel");
                    
                    //informace o jednotlivych paralelkach
                    
                    if(nodeList2.getLength() > 0){
                        for(int a = 0; a<nodeList2.getLength(); a++){
                            
                            String den = null,od = null,do1 = null,parita = null,urlPredmetu = null;
                            Long idRozvrhu = null;
                            NodeList seznamUcitelu = null;

                            Node current2 = nodeList2.item(a);

                            Element currParalelka = (Element) current2;
                            
                            idRozvrhu = Long.parseLong(currParalelka.getAttribute("id"));
                            
                            urlPredmetu = currParalelka.getAttribute("uri");

                            String split[] = urlPredmetu.split("instances");

                            urlPredmetu = split[0];

                            NodeList childs = current2.getChildNodes();

                            //DEN V TYDNU, ZACINA, KONCI, LICHY/SUDY TYDEN, UCITELE
                            for(int c = 0; c<childs.getLength(); c++){
                                Node node = childs.item(c);
                                if(node.getNodeName().equals("day")){
                                    den = node.getTextContent();
                                }
                                if(node.getNodeName().equals("firstHour")){
                                    od = node.getTextContent();
                                }
                                if(node.getNodeName().equals("lastHour")){
                                    do1 = node.getTextContent();
                                }
                                if(node.getNodeName().equals("parity")){
                                    parita = node.getTextContent();
                                }
                                if(node.getNodeName().equals("teachers")){
                                    seznamUcitelu = node.getChildNodes();
                                }
                            }
                        
                            
                            
                            
                           
                            
                            
                            
                            
                            //zjisteni id predmetu
                            
                            conn = kosOper.createNewConnection(urlPredmetu, logged.getLogin(), encrypt.decode(logged.getHeslo()));

                                Document doc3 = documentOper.getNewDocument(conn.getInputStream());
                            
                            kosOper.closeConnection(conn);

                            NodeList nodeList3 = doc3.getElementsByTagName("course");
                            Long idPredmetu = Long.parseLong(((Element)nodeList3.item(0)).getAttribute("id"));

                            
                            //zmena ciselneho urceni hodiny na konkretni cas (napr. 1 -> 7:30)
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
                            
                            
                            
                            
                            
                            
                            
                            
                            
                            
                             //zjisteni informaci o ucitelech vyucujicich tuto paralelku
                            
                            ArrayList<Vyucujici> uciteleParalelky = new ArrayList<Vyucujici>();
                            
                            for(int b = 0; b<seznamUcitelu.getLength(); b++){
                                
                                String ucitelJmeno = "", ucitelKontakt = "Nemá uveden.";
                                
                                Element ucitel = (Element) seznamUcitelu.item(b);
                                String xmlUcitele = ucitel.getAttribute("uri");
                                Long idUcitele = Long.parseLong(ucitel.getAttribute("id"));
                                
                                conn = kosOper.createNewConnection(xmlUcitele, logged.getLogin(), encrypt.decode(logged.getHeslo()));

                                    Document doc4 = documentOper.getNewDocument(conn.getInputStream());

                                kosOper.closeConnection(conn);
                                
                                
                                String jmeno = "", prijmeni = "", titulPre = "", titulPost = "", email = "";
                                NodeList ucitelDetail = doc4.getElementsByTagName("person");
                                Node person = ucitelDetail.item(0);
                                for(int c = 0; c<person.getChildNodes().getLength(); c++){
                                    Node node = person.getChildNodes().item(c);
                                    if(node.getNodeName().equals("titlePre")){
                                        titulPre = node.getTextContent();
                                    }
                                    if(node.getNodeName().equals("titlePost")){
                                        titulPost = node.getTextContent();
                                    }
                                    if(node.getNodeName().equals("firstName")){
                                        jmeno = node.getTextContent();
                                    }
                                    if(node.getNodeName().equals("surname")){
                                        prijmeni = node.getTextContent();
                                    }
                                    if(node.getNodeName().equals("email")){
                                        email = node.getTextContent();
                                    }
                                }
                                String jmenoCele = titulPre.concat(" "+jmeno.concat(" "+prijmeni)).concat(" "+titulPost);
                                //System.out.println("FULL NAME: "+jmenoCele+" EMAIL: "+email);
                                
                                ucitelJmeno = jmenoCele;
                                
                                if(!email.equals("")){
                                    ucitelKontakt = email;
                                }
                                
                                Vyucujici vyucujici;
                                
                                if(vyuOper.getVyucujici(new Vyucujici(idUcitele)) != null){
                                    vyucujici = vyuOper.getVyucujici(new Vyucujici(idUcitele));
                                }
                                else{
                                    vyucujici = vyuOper.createVyucujici(idUcitele, ucitelJmeno, ucitelKontakt, new ArrayList<Rozvrhy>());
                                }
                                
                                
                                
                                uciteleParalelky.add(vyucujici);
                                
                            }
                            
                            
                            //System.out.println("ROZVRH "+idRozvrhu+" DEN "+den);
                            Rozvrhy newRoz = rozOper.getObject(idRozvrhu,denOper.getENDen(den),new Mistnost(idMistnosti),
                                            new Predmety(idPredmetu),odDate,doDate,lichy,sudy, new ArrayList<Vyucujici>()/*uciteleParalelky*/);
                            
                            
                            
                            
                            
                            boolean alreadySaved = false;
                            for(Rozvrhy roz : rozOper.getRozvrhy(new Mistnost(idMistnosti),denOper.getENDen(den))){
                                if(rozOper.isSame(newRoz, roz)){
                                    alreadySaved = true;
                                    break;
                                }
                            }
                            
                            if(!alreadySaved){
                                rozOper.createRozvrh(newRoz);
                                
                                for(Vyucujici v : uciteleParalelky){
                                    v.getRozvrhyCollection().add(newRoz);
                                    newRoz.getVyucujiciCollection().add(v);
                                }
                                for(Vyucujici v : uciteleParalelky){
                                    vyuOper.update(v);
                                }
                                rozOper.update(newRoz);
                            }
                            else{
                                System.out.println("ROZVRH JE JIŽ V DATABAZI...");
                            }
                        }
                    
                    //System.out.println("---------------------------------------------");
                    }
                }
    }

    
    /**
     * Načte data o aktuálním semestru z KosAPI a naplní s nimi databázi.
     * @param logged právě přihlášený uživatel
     * @throws IOException 
     * @see Semestr
     */
    public void setSemestr(Uzivatel logged) throws IOException{
    
            HttpsURLConnection conn = kosOper.createNewConnection(kosapiUrls.currentSemestr, logged.getLogin(), encrypt.decode(logged.getHeslo()));

                Document doc = documentOper.getNewDocument(conn.getInputStream());
            
            kosOper.closeConnection(conn);
            
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
     *          (3) - kontakt uzivatele
     * @throws IOException  
     */
    public List<String> getAtributes(String login, String mujLogin, String mojeHeslo) throws IOException {
        ArrayList<String> seznamAtributu = new ArrayList<String>();
        
            HttpsURLConnection conn = kosOper.createNewConnection(kosapiUrls.people+"/"+login, mujLogin, mojeHeslo);
            
                Document doc = documentOper.getNewDocument(conn.getInputStream());
            
            kosOper.closeConnection(conn);
             NodeList person = null;
             NodeList teacher = null;
            try{
            person = doc.getElementsByTagName("person");
            teacher = doc.getElementsByTagName("teacher");
            }catch(NullPointerException e){
                System.out.println("FUXK");
            }
            
            String uzivKontakt = "Nemá uveden.";
            String jmeno = "", prijmeni = "", titulPre = "", titulPost = "", email = "";
            String idUzivatele = "", role = "", fullName="";
            if(person != null){
                Node personDet = person.item(0);
                for(int c = 0; c<personDet.getChildNodes().getLength(); c++){
                    Node node = personDet.getChildNodes().item(c);
                    if(node.getNodeName().equals("titlePre")){
                        titulPre = node.getTextContent();
                    }
                    if(node.getNodeName().equals("titlePost")){
                        titulPost = node.getTextContent();
                    }
                    if(node.getNodeName().equals("firstName")){
                        jmeno = node.getTextContent();
                    }
                    if(node.getNodeName().equals("surname")){
                        prijmeni = node.getTextContent();
                    }
                    if(node.getNodeName().equals("email")){
                        email = node.getTextContent();
                    }
                }
            
            fullName = titulPre.concat(" "+jmeno.concat(" "+prijmeni)).concat(" "+titulPost);
            
            if(!email.equals("")){
                uzivKontakt = email;
            }
            
            idUzivatele = ((Element)person.item(0)).getAttribute("id");
            
            if(teacher.getLength() > 0){
                role = "ucitel";
            }
            else{
                role = "student";
            }
            }
            seznamAtributu.add(fullName);
            seznamAtributu.add(idUzivatele);
            seznamAtributu.add(role);
            seznamAtributu.add(uzivKontakt);
            
        return seznamAtributu;
    }
}

    
