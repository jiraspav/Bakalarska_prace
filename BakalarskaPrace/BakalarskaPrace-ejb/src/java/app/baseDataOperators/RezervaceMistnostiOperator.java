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
    
    /**
     * 
     */
    public void createRezervaceMistnosti(){}
    
    /**
     * Metoda pro uložení nové rezervace do databáze
     * @param uziv vlastník této rezervace
     * @param mistnost místnost na kterou je rezervace vytvářena
     * @param datumRezervace datum na kterou je rezervace vytvořena
     * @param casOd čas kdy rezervace začíná
     * @param casDo čas kdy rezervace končí
     * @param pocetMist kolik míst je rezervováno
     * @param popis popis rezervace
     */
    public void createRezervaceMistnosti(Uzivatel uziv, Mistnost mistnost, Date datumRezervace, Date casOd, Date casDo, int pocetMist, String popis){
        
        RezervaceMistnosti rez = new RezervaceMistnosti(uziv, mistnost, datumRezervace, casOd, casDo, pocetMist , popis, "ACTIVE");
        
        System.out.println("CREATE RESERVATION: mistnost - "+mistnost.getZkratka()+" datum - "+rez.getDatumRezervace()+" od - "+rez.getOd()+" do - "+rez.getDo1()+" pocet - "+rez.getPocetRezervovanychMist()+" popis - "+rez.getPopis());
        rezFac.create(rez);
    }
    
    /**
     * Metoda určená pro aktualizaci rezervace, parametry které se nemají měnit mají hodnotu null.
     * @param rez rezervace, která bude aktualizovaná
     * @param iDuser nový vlastní rezervace
     * @param iDmistnosti nová místnost rezervace
     * @param datumRezervace nové datum rezervace
     * @param od nový počáteční čas rezervace
     * @param do1 nový koncový čas rezervace
     * @param pocetMist nový počet míst rezervace
     * @param popis nový popis rezervace
     */
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
    
    /**
     * Metoda pro odstranění rezervace.
     * @param rez rezervace určená pro odstranění
     */
    public void deleteReservation(RezervaceMistnosti rez){
        rezFac.remove(rez);
    }
    /**
     * Metoda určena pro aktualizaci rezervace
     * @param r rezervace s nastavenými novými daty
     */
    public void update(RezervaceMistnosti r){
        rezFac.edit(r);
    }
    /**
     * Metoda pro vyhledávání rezervací v databáze, vyhledává se pomocí ID parametru
     * @param r rezervace s nastaveným ID, podle které se vyhledává
     * @return nalezenou rezervaci
     *         null - pokud rezervace s daným ID neexistuje
     */
    public RezervaceMistnosti getRezervace(RezervaceMistnosti r){
        return rezFac.find(r.getIDrezervace());
    }
    /**
     * Metoda pro vyhledávání rezervací podle zadaných parametrů
     * @param uziv uživatel jehož rezervace se hledají
     * @return List rezervací daného uživatele
     */
    public List<RezervaceMistnosti> getRezervace(Uzivatel uziv){
        return rezFac.getRezervaceByUserID(uziv);
    }
    
    /**
     * Metoda pro vyhledávání podle zadaných parametrů
     * @param mist místnost na kterou se rezervace vztahují
     * @return List rezervací na zadanou místnost
     */
    public List<RezervaceMistnosti> getRezervace(Mistnost mist){
        return rezFac.getRezervaceByMistnostID(mist);
    }
    
    /**
     * Metoda pro vyhledávání podle zadaných parametrů
     * @param date datum na které se vyhledávají rezervace
     * @return List rezervací na dané datum
     */
    public List<RezervaceMistnosti> getRezervace(Date date){
        return rezFac.getRezervaceByDatum(date);
    }
    /**
     * Metoda pro vyhledávání podle zadaných parametrů
     * @param date datum na které se vyhledávají rezervace
     * @param mist místnost na kterou se rezervace vztahují
     * @return List rezervací na dané datum a místnost
     */
    public List<RezervaceMistnosti> getRezervace(Date date, Mistnost mist){
        return rezFac.getRezervaceByMistnostIDandDatum(date, mist);
    }
    /**
     * Metoda pro smazání rezervace z databáze
     * @param rez rezervace určená ke smazání
     */
    public void delete(RezervaceMistnosti rez){
        rezFac.remove(rez);
    }
    
    /**
     * Metoda pro smazání všech rezervací
     */
    public void deleteAll() {
        rezFac.removeAll();
    }
    
    /**
     * Metoda pro změnu statusu dané rezervace na OVERWRITTEN
     * @param rez rezervace jejíž status má být přepsán
     */
    public void changeStatusToOverWritten(RezervaceMistnosti rez){
        rez.setStatus("OVERWRITTEN");
        rezFac.edit(rez);
    }
    
}
