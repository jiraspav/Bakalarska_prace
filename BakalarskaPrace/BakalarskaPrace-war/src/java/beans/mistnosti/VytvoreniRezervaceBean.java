/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans.mistnosti;

import view.auth.LoginVerifier;
import dbEntity.*;
import entityFacade.MistnostFacade;
import entityFacade.RezervaceMistnostiFacade;
import entityFacade.RozvrhyFacade;
import entityFacade.SemestrFacade;
import entityFacade.StrediskoFacade;
import entityFacade.UzivatelFacade;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import view.SessionHolder.SessionHolderMB;

/**
 *
 * @author Pavel
 */

@Named("vytvoreniRezervaceBean")
@SessionScoped

public class VytvoreniRezervaceBean implements Serializable{
    
    @Inject SemestrFacade semFac;
    @Inject RozvrhyFacade rozFac;
    @Inject StrediskoFacade stredFac;
    @Inject MistnostFacade mistFac;
    @Inject RezervaceMistnostiFacade rezFac;
    @Inject UzivatelFacade userFac;
    @Inject SessionHolderMB session;
    
    private TreeNode selectedNode;  
    private TreeNode root;  
    
    private int odHodiny = 0,odMinuty = 0,doHodiny = 0,doMinuty = 0;
    private String odCas,doCas;
    private String popis = " ";
    private Mistnost rezervovanaMistnost;
    private DataModel rozvrhyMistnosti;
    private ArrayList dny,mesice,roky;
    private Date datumRezervace;
    private Date maxDate,minDate;
    
    private static String[] converter = {"7:30","9:00","9:15","10:45","11:00","12:30",
                                "12:45","14:15","14:30","16:00","16:15","17:45","18:00","19:30","20:30"};
    
    /**
     * 
     */
    public VytvoreniRezervaceBean() {
    }
    /**
     * 
     * @return vrací uživatelské jméno
     */
    public String getUzivatelName(){
        return session.getLoggedUzivatel().getLogin();
    }
    
    
    /**
     * metoda, zajišťující vytváření rozvrhů pro předměty
     * @return seznam hotových rozvrhů
     */
    public DataModel getRozvrhyMistnosti(){
        
        RozvrhNaDen empty = new RozvrhNaDen();
        
        ArrayList<RozvrhNaDen> novy = new ArrayList<RozvrhNaDen>();
        novy.add(empty);
        novy.add(empty);
        novy.add(empty);
        novy.add(empty);
        novy.add(empty);
        
        if(getSelectedNode() != null)
        if(getSelectedNode().isLeaf()){
            
            novy.clear();
            
            String zkratka = (String) getSelectedNode().getData();
            Mistnost mistnost = mistFac.findMistnostPodleZkratky(zkratka);
            
            
            ArrayList<Rozvrhy> pondeli = new ArrayList(rozFac.findByMistnostAden(mistnost,new DenVTydnu(new Integer(1))));
            RozvrhNaDen pondelLich = createRozvrhNaDen(pondeli,"Pondělí lichý",true);
            RozvrhNaDen pondelSud = createRozvrhNaDen(pondeli,"Pondělí sudý",false);
            novy.add(pondelLich);
            novy.add(pondelSud);
            
            ArrayList<Rozvrhy> utery = new ArrayList(rozFac.findByMistnostAden(mistnost,new DenVTydnu(new Integer(2))));
            RozvrhNaDen uterLich = createRozvrhNaDen(utery,"Úterý lichý",true);
            RozvrhNaDen uterSud = createRozvrhNaDen(utery,"Úterý sudý",false);
            novy.add(uterLich);
            novy.add(uterSud);
            
            ArrayList<Rozvrhy> streda = new ArrayList(rozFac.findByMistnostAden(mistnost,new DenVTydnu(new Integer(3))));
            RozvrhNaDen stredLich = createRozvrhNaDen(streda,"Středa lichý",true);
            RozvrhNaDen stredSud = createRozvrhNaDen(streda,"Středa sudý",false);
            novy.add(stredLich);
            novy.add(stredSud);
            
            ArrayList<Rozvrhy> ctvrtek = new ArrayList(rozFac.findByMistnostAden(mistnost,new DenVTydnu(new Integer(4))));
            RozvrhNaDen ctvrteLich = createRozvrhNaDen(ctvrtek,"Čtvrtek lichý",true);
            RozvrhNaDen ctvrteSud = createRozvrhNaDen(ctvrtek,"Čtvrtek sudý",false);
            novy.add(ctvrteLich);
            novy.add(ctvrteSud);
            
            ArrayList<Rozvrhy> patek = new ArrayList(rozFac.findByMistnostAden(mistnost,new DenVTydnu(new Integer(5))));
            RozvrhNaDen pateLich = createRozvrhNaDen(patek,"Pátek lichý",true);
            RozvrhNaDen pateSud = createRozvrhNaDen(patek,"Pátek sudý",false);
            novy.add(pateLich);
            novy.add(pateSud);
            
        }
        rozvrhyMistnosti = new ListDataModel(novy);
        
        
        return rozvrhyMistnosti;
    }
    
    
    private RozvrhNaDen createRozvrhNaDen(ArrayList<Rozvrhy> rozvrhy,String naJakyDen,boolean lichy) {

        RozvrhNaDen novy = new RozvrhNaDen(naJakyDen);
        
        Iterator iter = rozvrhy.iterator();
        
        while(iter.hasNext()){
            Rozvrhy curr = (Rozvrhy) iter.next();
            SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
            String odKdy = sdf.format(curr.getOd());
            int poziceOd = getHodinuInt(odKdy);
            String doKdy = sdf.format(curr.getDo1());
            int poziceDo = getHodinuInt(doKdy);
            
            
                for(int i = poziceOd; i <= poziceDo; i++){
                    
                    if(lichy){
                        if(curr.getLichyTyden())
                            novy.addHodinu(curr.getIDpredmetu(), i);
                    }
                    
                    else{
                        if(curr.getSudyTyden())
                            novy.addHodinu(curr.getIDpredmetu(), i);
                    }
                }
                          
        }
        
        return novy;
    }
    
    private int getHodinuInt(String hodina){
        int pozice = 0;
        String convert[] = VytvoreniRezervaceBean.converter;
        int length = VytvoreniRezervaceBean.converter.length;
        for(int i = 0; i<length ; i++){
            if(convert[i].equals(hodina)){
                pozice = i;
                break;
            }
        }
        return pozice;
    }
    
    
    
    /**
     * metoda zapisující novou rezervaci do databáze
     * @param e
     */
    public void provedRezervaci(ActionEvent e){
        try {
            RezervaceMistnosti rez = new RezervaceMistnosti();
            SimpleDateFormat sdf = new SimpleDateFormat("H:m");
            Date od = sdf.parse(odHodiny+":"+odMinuty);
            Date do1 = sdf.parse(doHodiny+":"+doMinuty);
            Date datum ;
            Uzivatel uziv = userFac.getUserByLogin(session.getLoggedUzivatelLogin());
            if(datumRezervace != null && uziv != null){
                datum = datumRezervace;
             
                rez.setIDrezervace(new Integer(10));
                rez.setNaCelouMistnost(true);
                rez.setIDmistnosti(getRezervovanaMistnost());
                rez.setIDuser(uziv);

                rez.setDatumRezervace(datum);        
                rez.setDo1(do1);
                rez.setOd(od);
                rez.setPopis(popis);

                rezFac.create(rez);
            }
        } catch (ParseException ex) {
            Logger.getLogger(VytvoreniRezervaceBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    /**
     * metoda, zjišťující rezervovanou místnost podle zkratky
     * @return objekt Mistnost
     */
    public Mistnost getRezervovanaMistnost(){
        if(selectedNode == null)
            return null;
        return mistFac.findMistnostPodleZkratky((String)(selectedNode.getData().toString()));
    }

    /**
     * 
     */
    public void smazRoot(){
        root = null;
    }
    
    /**
     * metoda pro tvorbu stromové struktury ze středisek a místností PrimeFaces komponenty p:tree
     * @return hotovou stromovou strukturu
     */
    public TreeNode getRoot() { 
        
        if(root == null){
            root = new DefaultTreeNode("Root", null);  
            TreeNode cvut = new DefaultTreeNode("ČVUT", root);
            cvut.setSelectable(false);
            List<Stredisko> tree = stredFac.findAll();
            
            
            Collections.sort(tree, new StrediskoComparator());
            
            
            Iterator iter = tree.iterator();
            if(tree.size() > 0){
                while(iter.hasNext()){
                    Stredisko curr = (Stredisko) iter.next();
                    

                    //System.out.println("Stredisko : "+curr);
                    
                    List<Mistnost> mistnosti = mistFac.findMistnostiStrediska(curr);
                    List<String> zkratky = new ArrayList<String>();
                    for (Iterator<Mistnost> it = mistnosti.iterator(); it.hasNext();) {
                        Mistnost mistnost = it.next();
                        zkratky.add(mistnost.getZkratka());
                        //System.out.println("Mistnost: "+mistnost.getZkratka());
                    }
                    //System.out.println("Pocet mistnosti: "+mistnosti.size());
                    //System.out.println("pocet radku : "+mistnosti.size());
                    if(zkratky.size()>0){
                        //System.out.println("Zkratky.size() = "+zkratky.size());
                        Collections.sort(zkratky);
                        Iterator it = zkratky.iterator();
                        TreeNode nodeStr = new DefaultTreeNode(curr.getNazev(), cvut);  
                        nodeStr.setSelectable(false);
                        
                        while(it.hasNext()){
                            String current = (String) it.next();
                            TreeNode node = new DefaultTreeNode(current, nodeStr);
                        }
                    }
                }
            }
        }
        return root;  
    } 
    
    
    private void addMessage(FacesMessage message) {
        FacesContext.getCurrentInstance().addMessage(null, message);
        
    }
    
    
    /**
     * 
     * @return vrací označenou node
     */
    public TreeNode getSelectedNode() {  
        return selectedNode;  
    }  
  
    /**
     * 
     * @param selectedNode
     */
    public void setSelectedNode(TreeNode selectedNode) {  
        this.selectedNode = selectedNode;  
    }  
    
    
    
    /**
     * @return the odHodiny
     */
    public int getOdHodiny() {
        return odHodiny;
    }

    /**
     * @param odHodiny the odHodiny to set
     */
    public void setOdHodiny(int odHodiny) {
        this.odHodiny = odHodiny;
    }

    /**
     * @return the odMinuty
     */
    public int getOdMinuty() {
        return odMinuty;
    }

    /**
     * @param odMinuty the odMinuty to set
     */
    public void setOdMinuty(int odMinuty) {
        this.odMinuty = odMinuty;
    }

    /**
     * @return the doHodiny
     */
    public int getDoHodiny() {
        return doHodiny;
    }

    /**
     * @param doHodiny the doHodiny to set
     */
    public void setDoHodiny(int doHodiny) {
        this.doHodiny = doHodiny;
    }

    /**
     * @return the doMinuty
     */
    public int getDoMinuty() {
        return doMinuty;
    }

    /**
     * @param doMinuty the doMinuty to set
     */
    public void setDoMinuty(int doMinuty) {
        this.doMinuty = doMinuty;
    }


    /**
     * @return the odCas
     */
    public String getOdCas() {
        return odCas;
    }

    /**
     * @param odCas the odCas to set
     */
    public void setOdCas(String odCas) {
        this.odCas = odCas;
    }

    /**
     * @return the doCas
     */
    public String getDoCas() {
        return doCas;
    }

    /**
     * @param doCas the doCas to set
     */
    public void setDoCas(String doCas) {
        this.doCas = doCas;
    }

    /**
     * @return the popis
     */
    public String getPopis() {
        return popis;
    }

    /**
     * @param popis the popis to set
     */
    public void setPopis(String popis) {
        this.popis = popis;
    }

    /**
     * @return the datumRezervace
     */
    public Date getDatumRezervace() {
        return datumRezervace;
    }

    /**
     * @param datumRezervace the datumRezervace to set
     */
    public void setDatumRezervace(Date datumRezervace) {
        this.datumRezervace = datumRezervace;
    }
    
    
    
}

class StrediskoComparator implements Comparator{
 
    @Override
    public int compare(Object jedna, Object dva){    
 
        String str1name = ((Stredisko)jedna).getNazev();        
        String str2name = ((Stredisko)dva).getNazev();
       
        return str1name.compareTo(str2name);
   
    }
 
}