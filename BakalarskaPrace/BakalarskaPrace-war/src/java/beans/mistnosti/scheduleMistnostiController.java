/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans.mistnosti;

import view.auth.LoginVerifier;
import beans.mistnosti.VytvoreniRezervaceBean;
import dbEntity.*;
import entityFacade.DenVTydnuFacade;
import entityFacade.GroupTableFacade;
import entityFacade.MistnostFacade;
import entityFacade.RezervaceMistnostiFacade;
import entityFacade.RozvrhyFacade;
import entityFacade.UzivatelFacade;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.event.DateSelectEvent;
import org.primefaces.event.ScheduleEntryMoveEvent;
import org.primefaces.event.ScheduleEntryResizeEvent;
import org.primefaces.event.ScheduleEntrySelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;
import view.SessionHolder.SessionHolderMB;

/**
 *
 * @author Pavel
 */
@Named("scheduleMistnostiController")
@SessionScoped
public class scheduleMistnostiController implements Serializable {

    @Inject SessionHolderMB session;
    @Inject LoginVerifier user;
    @Inject UzivatelFacade uzivFac;
    @Inject DenVTydnuFacade denFac;
    @Inject RozvrhyFacade rozFac;
    @Inject MistnostFacade misFac;
    @Inject VytvoreniRezervaceBean vytBean;
    @Inject RezervaceMistnostiFacade rezFac;
    @Inject GroupTableFacade groupFac;
    private ScheduleModel eventModel;
    private ScheduleEvent event = new DefaultScheduleEvent();
    private String popis = " ";
    private Date casOd = new Date(),casDo = new Date();
    private boolean naCelouMistnost = false;
    private ArrayList<RezervaceMistnosti> rezervace;
    private TimeZone timeZone = TimeZone.getTimeZone("Europe/Prague");

    
    
    
    /**
     * 
     */
    public scheduleMistnostiController() {
        eventModel = new DefaultScheduleModel();
        //System.out.println("Constructor called;");
    }
    
    private Date putTogetherTimeDate(Date date,Date time){
        Calendar datum = GregorianCalendar.getInstance();
        datum.setTime(date);
        Calendar cas = GregorianCalendar.getInstance();
        cas.setTime(time);

        Calendar celkem = GregorianCalendar.getInstance();
        celkem.set(datum.get(Calendar.YEAR), datum.get(Calendar.MONTH), datum.get(Calendar.DAY_OF_MONTH),
                    cas.get(Calendar.HOUR_OF_DAY), cas.get(Calendar.MINUTE), cas.get(Calendar.SECOND));
        Date nove = celkem.getTime();
        return nove;
    }
    
    /**
     * metoda, vytvářející nový model pro PrimeFaces komponentu p:schedule
     * @param mistnost
     * @return hotový model
     */
    public ScheduleModel setSchedule(Mistnost mistnost){
        //FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Generating new ScheduleModel" ,"Delka listu : "+rezervace.size()) ;
        //addMessage(message);
        rezervace = new ArrayList(rezFac.getRezervaceByMistnostID(mistnost));
        Iterator iter = rezervace.iterator();
        while(iter.hasNext()){
            RezervaceMistnosti rezer = (RezervaceMistnosti) iter.next();
            Date datumOd = putTogetherTimeDate(rezer.getDatumRezervace(), rezer.getOd());
            Date datumDo = putTogetherTimeDate(rezer.getDatumRezervace(), rezer.getDo1());
            if(rezer.getIDmistnosti().equals(mistnost)){
                DefaultScheduleEvent event2 = new DefaultScheduleEvent(groupFac.getGroup(rezer.getIDuser())+":"+rezer.getIDuser().getLogin()+":"+rezer.getPopis(), 
                                        datumOd, datumDo);
                eventModel.addEvent(event2);
            }
        }
        
        return eventModel;
    }
    
    /**
     * metoda pro přidávání eventů do schedule modelu (nových rezervací)
     * @param actionEvent
     */
    public void addEvent(ActionEvent actionEvent) {
        if (getEvent().getId() == null && vytBean.getSelectedNode() != null) {
            boolean ok = true,rozvrhCheck = false,rezervaceCheck = false;
            if(vytBean.getSelectedNode().isLeaf() && !vytBean.getSelectedNode().equals(vytBean.getRoot())){
            if(casOd.before(casDo)){
                //System.out.println(vytBean.getSelectedNode()+" --- "+vytBean.getRoot());
                RezervaceMistnosti nova = new RezervaceMistnosti(new Integer(10), getEvent().getStartDate(), casOd, casDo, naCelouMistnost, popis);
                Mistnost mistnost = misFac.findMistnostPodleZkratky((String)vytBean.getSelectedNode().getData().toString());
                Uzivatel uziv = uzivFac.getUserByLogin(session.getLoggedUzivatelLogin());

                SimpleDateFormat sdf = new SimpleDateFormat("EEEE",Locale.ENGLISH);

                DenVTydnu denRezervace = denFac.getDenByNazev(sdf.format(getEvent().getStartDate()).toUpperCase());

                ArrayList rezervaceMistnosti = new ArrayList(rezFac.getRezervaceByMistnostID(mistnost));
                ArrayList rozvrhyMistnosti = new ArrayList(rozFac.findByMistnostAden(mistnost, denRezervace));

                Iterator iter = rozvrhyMistnosti.iterator();
                
                while(iter.hasNext()){
                    Rozvrhy rez = (Rozvrhy) iter.next();
                    if(!((casOd.after(rez.getDo1())) || (casDo.before(rez.getOd())))){
                        ok = false;
                        rozvrhCheck = true;
                    }
                }

                iter = rezervaceMistnosti.iterator();

                while(iter.hasNext()){
                    RezervaceMistnosti rez = (RezervaceMistnosti) iter.next();
                    if(getEvent().getStartDate().equals(rez.getDatumRezervace()))
                    if(!((casOd.after(rez.getDo1())) || (casDo.before(rez.getOd())))){
                        if(rez.getNaCelouMistnost() && (user.getRolePriority(groupFac.getGroup(rez.getIDuser())) >= user.getRolePriority(groupFac.getGroup(uzivFac.getUserByLogin(session.getLoggedUzivatelLogin())))) ){
                            ok = false;
                            rezervaceCheck = true;
                        }
                    }
                }


                if(ok){

                    nova.setIDuser(uziv);
                    nova.setIDmistnosti(mistnost);

                    try {
                        rezFac.create(nova);
                        getEventModel().addEvent(getEvent());

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
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "" ,"Již rezervováno.") ;
                    addMessage(message);
                }
            } else if(!casOd.before(casDo)) {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "" ,"Čas od musí být před časem do.") ;
                addMessage(message);
            }
            }else {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "" ,"Nemáte označenou žádnou místnost.") ;
                addMessage(message);
            }
            setEvent(new DefaultScheduleEvent());
        }else if(vytBean.getSelectedNode() == null){
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "" ,"Nemáte označenou žádnou místnost.") ;
            addMessage(message);
        }
    }

    

    /**
     * metoda pro hledání rezervací v List objektu
     * @param rez hledaná rezervace
     * @return pozice v List objektu
     */
    public int findInList(RezervaceMistnosti rez){
        
        for(int i = 0; i<rezervace.size();i++){
            RezervaceMistnosti curr = rezervace.get(i);
            if(curr.equals(rez))
                return i;
        }
        return -1;
    }
    
    /**
     * ActionListener pro schedule komponentu
     * @param selectEvent
     */
    public void onDateSelect(DateSelectEvent selectEvent) {
        setEvent(new DefaultScheduleEvent("", selectEvent.getDate(), selectEvent.getDate()));
        
    }

    

    private void addMessage(FacesMessage message) {
        FacesContext.getCurrentInstance().addMessage(null, message);
        
    }

    /**
     * @return the eventModel
     */
    public ScheduleModel getEventModel() {
        eventModel.clear();
        if(vytBean.getSelectedNode() != null)
            if(vytBean.getSelectedNode().isLeaf() && !vytBean.getSelectedNode().equals(vytBean.getRoot())){
                String zkratka = vytBean.getSelectedNode().getData().toString();
                Mistnost mistnost = misFac.findMistnostPodleZkratky(zkratka);
                return setSchedule(mistnost);
            }
        return eventModel;
    }

    /**
     * @param eventModel the eventModel to set
     */
    public void setEventModel(ScheduleModel eventModel) {
        this.eventModel = eventModel;
    }

    /**
     * @return the event
     */
    public ScheduleEvent getEvent() {
        return event;
    }

    /**
     * @param event the event to set
     */
    public void setEvent(ScheduleEvent event) {
        this.event = event;
    }

    
    /**
     * 
     * @return vrací defaultní timezone
     */
    public TimeZone getTimeZone() {
        return timeZone;
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
     * @return the casOd
     */
    public Date getCasOd() {
        return casOd;
    }

    /**
     * @param casOd the casOd to set
     */
    public void setCasOd(Date casOd) {
        this.casOd = casOd;
    }

    /**
     * @return the casDo
     */
    public Date getCasDo() {
        return casDo;
    }

    /**
     * @param casDo the casDo to set
     */
    public void setCasDo(Date casDo) {
        this.casDo = casDo;
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

    

    
}
