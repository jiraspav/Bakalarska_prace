/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans.mistnosti;

import app.baseDataOperators.UzivatelOperator;
import view.auth.LoginVerifier;
import dbEntity.*;
import entityFacade.*;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.model.DefaultScheduleEvent;
import view.SessionHolder.SessionHolderMB;
/**
 *
 * @author Pavel
 */
@Named("mojeRezervaceBean")
@SessionScoped
public class MojeRezervaceBean implements Serializable{

    private DataModel items;
    @Inject LoginVerifier userBean;
    @Inject UzivatelOperator userCon;
    @Inject SessionHolderMB session;
    @Inject RezervaceMistnostiFacade rezFac;
    @Inject MistnostFacade misFac;
    @Inject UzivatelFacade uzivFac;
    @Inject DenVTydnuFacade denFac;
    @Inject RozvrhyFacade rozFac;
    @Inject VytvoreniRezervaceBean vytBean;
    @Inject GroupTableFacade groupFac;
    
    private RezervaceMistnosti selectedRow;
    private String popis = " ",zkratka = " ";
    private String datumRezervace,od,do1;
    private Date dateDatumRezervace,dateOd,dateDo1;
    private boolean naCelouMistnost = false;
    
    /**
     * Creates a new instance of mojeRezervaceBean
     */
    public MojeRezervaceBean() {
    }
    
    /**
     * metoda pro zjištění všech rezervací přihlášeného uživatele
     * @return všechny rezervace přihlášeného uživatele
     */
    public DataModel getItems(){
        
        Uzivatel user = session.getLoggedUzivatel();
        items = new ListDataModel(getRezFac().getRezervaceByUserID(user)) ;
        return items;
    }

    /**
     * metoda pro úpravu rezervací
     */
    public void uprav(){
        
        
        if(getSelectedRow() != null){
            System.out.println("Upravit");

            
            
            
            boolean ok = true,rozvrhCheck = false,rezervaceCheck = false;
            
                if(dateOd.before(dateDo1)){
                    
                    SimpleDateFormat sdf = new SimpleDateFormat("EEEE",Locale.ENGLISH);

                    DenVTydnu denRezervace = denFac.getDenByNazev(sdf.format(dateDatumRezervace));
                    
                    ArrayList rezervaceMistnosti = new ArrayList(rezFac.getRezervaceByMistnostID(getSelectedRow().getIDmistnosti()));
                    ArrayList rozvrhyMistnosti = new ArrayList(rozFac.findByMistnostAden(getSelectedRow().getIDmistnosti(), denRezervace));


                    Iterator iter = rozvrhyMistnosti.iterator();
                    //System.out.println("Prohledavam rozvrhy... mistnost: "+mistnost.getZkratka()+" den: "+denRezervace.getNazev()+" || "+getEvent().getStartDate());
                    while(iter.hasNext()){
                        Rozvrhy rez = (Rozvrhy) iter.next();
                        //System.out.println("Rozvrh na den "+rez.getIDdnu().getNazev()+" od "+rez.getOd()+" do "+rez.getDo1());
                        if(!((dateOd.after(rez.getDo1())) || (dateDo1.before(rez.getOd())))){
                            ok = false;
                            rozvrhCheck = true;
                        }
                        //System.out.println("Kontrola ok = "+ok+" , reozvrhCheck = "+rozvrhCheck);
                    }

                    iter = rezervaceMistnosti.iterator();

                    while(iter.hasNext()){
                        RezervaceMistnosti rez = (RezervaceMistnosti) iter.next();
                        if(dateDatumRezervace.equals(rez.getDatumRezervace()))
                        if(!((dateOd.after(rez.getDo1())) || (dateDo1.before(rez.getOd())))){
                            if(rez.getNaCelouMistnost() && (userBean.getRolePriority(groupFac.getGroup(rez.getIDuser())) >= userBean.getRolePriority(groupFac.getGroup(uzivFac.getUserByLogin(session.getLoggedUzivatel().getLogin())))) ){
                                ok = false;
                                rezervaceCheck = true;
                            }
                        }
                    }


                    if(ok){

                        try {
                            
                            RezervaceMistnosti rezNew = new RezervaceMistnosti(getSelectedRow().getIDrezervace(), dateDatumRezervace, dateOd, dateDo1, naCelouMistnost, popis);
                            rezNew.setIDmistnosti(getSelectedRow().getIDmistnosti());
                            rezNew.setIDuser(getSelectedRow().getIDuser());
                            
                            getRezFac().edit(rezNew);
                            
                            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "" ,"Úspěšně rezervováno.") ;

                            addMessage(message);

                        } catch (Exception e) {
                            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_FATAL, "Fatal error" ,"Could not write into database") ;
                            addMessage(message);
                        }

                    }
                    else if(rozvrhCheck){
                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "" ,"Na tomto místě jsou již rozvrhy.") ;
                        addMessage(message);
                    }
                    else{
                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "" ,"Na tomto místě jsou již rezervace") ;
                        addMessage(message);
                    }
                } else if(!dateOd.before(dateDo1)) {
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "" ,"Čas od musí být před časem do.") ;
                    addMessage(message);
                }

            }else if(getSelectedRow() == null){
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "" ,"Nemáte označenou žádnou rezervaci.") ;
                addMessage(message);
            }
            
            
        
        
        
    }
    
    /**
     * metoda pro rušení rezervací
     */
    public void zrus(){
        if(getSelectedRow() != null){
            //System.out.println("Zrusit");
            getRezFac().remove(getSelectedRow());
            setSelectedRow(null);
        }
    }
    
    
    
    /**
     * @return the selectedRow
     */
    public RezervaceMistnosti getSelectedRow() {
        return selectedRow;
    }

    /**
     * @param selectedRow the selectedRow to set
     */
    public void setSelectedRow(RezervaceMistnosti selectedRow) {
        this.selectedRow = selectedRow;
        if(selectedRow != null){
            this.setZkratka(selectedRow.getIDmistnosti().getZkratka());
            this.setDatumRezervace(selectedRow.getFormatedDatumRezervace());
            this.setPopis(selectedRow.getPopis());
            this.setDo1(selectedRow.getFormatedDo1());
            this.setOd(selectedRow.getFormatedOd());
            this.setNaCelouMistnost(selectedRow.getNaCelouMistnost());
            this.setDateDatumRezervace(getSelectedRow().getDatumRezervace());
            this.setDateOd(getSelectedRow().getOd());
            this.setDateDo1(getSelectedRow().getDo1());
        }
    }

    /**
     * @param items the items to set
     */
    public void setItems(DataModel items) {
        this.items = items;
    }

    /**
     * @return the userBean
     */
    public LoginVerifier getUserBean() {
        return userBean;
    }

    /**
     * @param userBean the userBean to set
     */
    public void setUserBean(LoginVerifier userBean) {
        this.userBean = userBean;
    }

    /**
     * @return the rezFac
     */
    public RezervaceMistnostiFacade getRezFac() {
        return rezFac;
    }

    /**
     * @param rezFac the rezFac to set
     */
    public void setRezFac(RezervaceMistnostiFacade rezFac) {
        this.rezFac = rezFac;
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
     * @return the zkratka
     */
    public String getZkratka() {
        return zkratka;
    }

    /**
     * @param zkratka the zkratka to set
     */
    public void setZkratka(String zkratka) {
        this.zkratka = zkratka;
    }

    /**
     * @return the datumRezervace
     */
    public String getDatumRezervace() {
        return datumRezervace;
    }

    /**
     * @param datumRezervace the datumRezervace to set
     */
    public void setDatumRezervace(String datumRezervace) {
        this.datumRezervace = datumRezervace;
    }

    /**
     * @return the od
     */
    public String getOd() {
        return od;
    }

    /**
     * @param od the od to set
     */
    public void setOd(String od) {
        this.od = od;
    }

    /**
     * @return the do1
     */
    public String getDo1() {
        return do1;
    }

    /**
     * @param do1 the do1 to set
     */
    public void setDo1(String do1) {
        this.do1 = do1;
    }

    /**
     * @return the naCelouMistnost
     */
    public boolean isNaCelouMistnost() {
        return naCelouMistnost;
    }

    /**
     * @param naCelouMistnost the naCelouMistnost to set
     */
    public void setNaCelouMistnost(boolean naCelouMistnost) {
        this.naCelouMistnost = naCelouMistnost;
    }

    /**
     * @return the dateDatumRezervace
     */
    public Date getDateDatumRezervace() {
        return dateDatumRezervace;
    }

    /**
     * @param dateDatumRezervace the dateDatumRezervace to set
     */
    public void setDateDatumRezervace(Date dateDatumRezervace) {
        this.dateDatumRezervace = dateDatumRezervace;
    }

    /**
     * @return the dateOd
     */
    public Date getDateOd() {
        return dateOd;
    }

    /**
     * @param dateOd the dateOd to set
     */
    public void setDateOd(Date dateOd) {
        this.dateOd = dateOd;
    }

    /**
     * @return the dateDo1
     */
    public Date getDateDo1() {
        return dateDo1;
    }

    /**
     * @param dateDo1 the dateDo1 to set
     */
    public void setDateDo1(Date dateDo1) {
        this.dateDo1 = dateDo1;
    }

     private void addMessage(FacesMessage message) {
        FacesContext.getCurrentInstance().addMessage(null, message);
        
    }
    
    
    
}
