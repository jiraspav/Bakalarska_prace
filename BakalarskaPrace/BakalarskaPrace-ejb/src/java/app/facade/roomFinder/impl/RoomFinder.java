/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app.facade.roomFinder.impl;

import app.baseDataOperators.*;
import app.facade.roomFinder.RoomFinderFacade;
import app.sessionHolder.SessionHolderEJB;
import dbEntity.DenVTydnu;
import dbEntity.Mistnost;
import dbEntity.RezervaceMistnosti;
import dbEntity.Rozvrhy;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 *
 * @author Pavel
 */
@Stateless
@Local(RoomFinderFacade.class)
public class RoomFinder implements RoomFinderFacade{

    @Inject MistnostOperator mistOper;
    @Inject RezervaceMistnostiOperator rezOper;
    @Inject UzivatelOperator uzivOper;
    @Inject SessionHolderEJB session;
    @Inject RozvrhyOperator rozOper;
    @Inject DnyVTydnuOperator denOper;
    
    @Override
    public List<Mistnost> getAllFreeRooms(Date day, Date odCas, Date doCas) {
        List<Mistnost> mistnosti = mistOper.getAll();
        List<RezervaceMistnosti> rezervace = rezOper.getRezervace(day);

        SimpleDateFormat sdf = new SimpleDateFormat("EEEE",Locale.ENGLISH);

        DenVTydnu denRezervace = denOper.getCZDen(sdf.format(day));
        
        //Check all reservation, remove occupied rooms
        for(RezervaceMistnosti rez : rezervace){

            if(!isInterfering(rez, odCas, doCas) && !isHigherPriorityThanLogged(rez)){
                if(rez.getNaCelouMistnost()){
                    Mistnost mistnost = rez.getIDmistnosti(); 
                    mistnosti.remove(mistnost);
                }
            }
        }
        
        //for every remaining Mistnost check schedule
        for(Mistnost mist : mistnosti){
            
            ArrayList<Rozvrhy> rozvrhyMistnosti = new ArrayList(rozOper.getRozvrhy(mist, denRezervace));

            //checks all schedules for this room

            for(Rozvrhy rez : rozvrhyMistnosti){

                if(!(isInterfering(rez, odCas, doCas))){
                    Mistnost mistnost = rez.getIDmistnosti(); 
                    mistnosti.remove(mistnost);
                }
            }
        
        }
        
        
        return mistnosti;
    }
    
    private boolean isInterfering(Rozvrhy rez , Date odCas, Date doCas){
        return !((odCas.after(rez.getDo1())) || (doCas.before(rez.getOd())));
    }
    
    private boolean isInterfering(RezervaceMistnosti rez , Date odCas, Date doCas){
        return !((odCas.after(rez.getDo1())) || (doCas.before(rez.getOd())));
    }
    
    private boolean isHigherPriorityThanLogged(RezervaceMistnosti rez){
        return ((uzivOper.getUzivatelRolePriority(rez.getIDuser())) >= (uzivOper.getUzivatelRolePriority(session.getLoggedUzivatel())));
    }
}
