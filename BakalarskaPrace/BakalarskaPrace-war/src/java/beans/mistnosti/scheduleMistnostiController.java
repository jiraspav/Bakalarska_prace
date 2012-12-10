/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans.mistnosti;

import app.baseDataOperators.MistnostOperator;
import app.facade.reservationEditor.ReservationEditorFacade;
import app.facade.schedulerEditorPF.SchedulerEditorPFFacade;
import java.io.Serializable;
import java.util.Date;
import java.util.TimeZone;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.event.DateSelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;
import view.SessionHolder.SessionHolderMB;
import view.bundle.ResourceBundleOperator;
import view.facesMessenger.FacesMessengerUtil;

/**
 *
 * @author Pavel
 */
@Named("scheduleMistnostiController")
@SessionScoped
public class scheduleMistnostiController implements Serializable {

    @Inject private ReservationEditorFacade resFac;
    @Inject private SchedulerEditorPFFacade schedFac;
    @Inject private FacesMessengerUtil facUtil;
    @Inject private ResourceBundleOperator bundle;
    @Inject private VytvoreniRezervaceMB vytBean;
    @Inject private SessionHolderMB session;
    @Inject private MistnostOperator mistOper;
    
    
    private ScheduleModel eventModel;
    private ScheduleEvent event = new DefaultScheduleEvent();
    private String popis = " ";
    private Date casOd = new Date(),casDo = new Date();
    private boolean naCelouMistnost = false;
    
    private TimeZone timeZone = TimeZone.getTimeZone("Europe/Prague");

    
    /**
     * 
     */
    public scheduleMistnostiController() {
        eventModel = new DefaultScheduleModel();
        //System.out.println("Constructor called;");
    }
    
    /**
     * metoda pro přidávání eventů do schedule modelu (nových rezervací)
     * @param actionEvent
     */
    public void addEvent() {
        
        if(vytBean.getSelectedNode() == null){
            facUtil.addFacesMsgError(bundle.getMsg("sysMsgNoRoomSellected"));
       
        }
        else {
            
            if(casOd.before(casDo)){
                
                String mistShort = vytBean.getSelectedNode().getData().toString();
                
                String response = resFac.isReservationOK(mistShort, getEvent().getStartDate(), casOd, casDo);
                

                if(response.equals("ok")){
                        //JE NUTNE PRIDAT MOZNOST VYBERU MNOZSTVI MIST
                        //resFac.createReservation(mistOper.getMistnost(mistShort), event.getStartDate(), casOd, casDo, naCelouMistnost, 5, popis);
                        //
                        getEventModel().addEvent(getEvent());
                        
                        facUtil.addFacesMsgInfo(bundle.getMsg("sysMsgSuccReserved"));

                }
                else if(response.equals("alreadyscheduled")){
                    facUtil.addFacesMsgError(bundle.getMsg("sysMsgAlreadyScheduled"));
                    
                }
                else{
                    facUtil.addFacesMsgError(bundle.getMsg("sysMsgAlreadyReserved"));
                    
                }
            } else {
                facUtil.addFacesMsgError(bundle.getMsg("sysMsgWrongTimes"));
                
            }
            setEvent(new DefaultScheduleEvent());
        }
    }

    /**
     * ActionListener pro schedule komponentu
     * @param e
     */
    public void onDateSelect(DateSelectEvent e) {
        setEvent(new DefaultScheduleEvent("", e.getDate(), e.getDate()));
        
    }

    /**
     * @return the eventModel
     */
    public ScheduleModel getEventModel() {
        
        eventModel = schedFac.createNewModel(vytBean.getSelectedNode());
        
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