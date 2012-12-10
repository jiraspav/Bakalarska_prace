/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app.baseDataOperators;

import dbEntity.Mistnost;
import dbEntity.RezervaceMistnosti;
import dbEntity.Uzivatel;
import entityFacade.RezervaceMistnostiFacade;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 *
 * @author Pavel
 */
@Stateless
public class RezervaceMistnostiOperator {
    
    private @Inject RezervaceMistnostiFacade rezFac;
    
    public void createRezervaceMistnosti(){}
    
    public void createRezervaceMistnosti(Uzivatel uziv, Mistnost mistnost, Date startDate, Date casOd, Date casDo, int pocetMist, String popis){
        
        RezervaceMistnosti rez = new RezervaceMistnosti(uziv, mistnost, startDate, casOd, casDo, pocetMist , popis, "ACTIVE");
        
        System.out.println("CREATE RESERVATION: mistnost - "+mistnost.getZkratka()+" datum - "+rez.getDatumRezervace()+" od - "+rez.getOd()+" do - "+rez.getDo1()+" pocet - "+rez.getPocetRezervovanychMist()+" popis - "+rez.getPopis());
        rezFac.create(rez);
    }
    
    public void editRezervaceMistnosti(RezervaceMistnosti rez, Uzivatel iDuser, Mistnost iDmistnosti, Date datumRezervace, Date od, Date do1, Integer pocetMist, String popis) {
        if(iDuser != null){rez.setIDuser(iDuser);}
        if(iDmistnosti != null){rez.setIDmistnosti(iDmistnosti);}
        if(datumRezervace != null){rez.setDatumRezervace(datumRezervace);}
        if(od != null){rez.setOd(od);}
        if(do1 != null){rez.setDo1(do1);}
        if(pocetMist != null){rez.setPocetRezervovanychMist(pocetMist);}
        if(popis != null){rez.setPopis(popis);}
        
        rezFac.edit(rez);
    
    }
    
    public void deleteReservation(RezervaceMistnosti rez){
        rezFac.remove(rez);
    }
    
    public List<RezervaceMistnosti> getRezervace(Uzivatel uziv){
        return rezFac.getRezervaceByUserID(uziv);
    }
    
    public List<RezervaceMistnosti> getRezervace(Mistnost mist){
        return rezFac.getRezervaceByMistnostID(mist);
    }
    
    public List<RezervaceMistnosti> getRezervace(Date date){
        return rezFac.getRezervaceByDatum(date);
    }
    public List<RezervaceMistnosti> getRezervace(Date date, Mistnost mist){
        return rezFac.getRezervaceByMistnostIDandDatum(date, mist);
    }
    public void delete(RezervaceMistnosti rez){
        rezFac.remove(rez);
    }
    
    public void deleteAll() {
        rezFac.removeAll();
    }
    
    public void changeStatusToOverWritten(RezervaceMistnosti rez){
        rez.setStatus("OVERWRITTEN");
        rezFac.edit(rez);
    }
    
}
