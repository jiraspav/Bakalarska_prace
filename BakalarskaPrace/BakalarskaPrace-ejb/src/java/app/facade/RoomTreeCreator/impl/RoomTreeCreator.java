/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app.facade.RoomTreeCreator.impl;

import app.baseDataOperators.MistnostOperator;
import app.baseDataOperators.StrediskoOperator;
import app.facade.RoomTreeCreator.RoomTreeCreatorFacade;
import dbEntity.Mistnost;
import dbEntity.Stredisko;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.inject.Inject;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

/**
 *
 * @author Pavel
 */
@Stateless
@Local(RoomTreeCreatorFacade.class)
public class RoomTreeCreator implements RoomTreeCreatorFacade{

    @Inject private StrediskoOperator stredOper;
    @Inject private MistnostOperator mistOper;
    
    /**
     * Metoda pro vytvoření nové stromové struktury a její naplnění daty o střediskách
     * a jejich místnostech určená pro Primefaces p:tree
     * @param root základní node stromu
     * @return základní node stromu s vytvořenou stromovou strukturou
     */
    @Override
    public TreeNode createRoomTree(TreeNode root) {
        
            TreeNode cvut = new DefaultTreeNode("ČVUT", root);
            cvut.setSelectable(false);
            
            List<Stredisko> allStrediska = stredOper.getAll();


            Collections.sort(allStrediska, new StrediskoComparator());

            for(Stredisko curr : allStrediska){
                
                //get all shortnames of all Mistnost from one Stredisko
                List<String> zkratky = getShortNames(mistOper.getMistnosti(curr));

                //add all Strediska to Tree
                if(zkratky.size()>0){
                    
                    Collections.sort(zkratky);

                    TreeNode nodeStr = linkAllToNode(curr.getNazev(), cvut ,zkratky);
                    nodeStr.setSelectable(false);
                    nodeStr.setParent(cvut);
                    //add all Mistnosti for each Stredisko
                    
                }
            }
        
        return root;  
    }
    
    private List<String> getShortNames(List<Mistnost> mistnosti){
        
        List<String> zkratky = new ArrayList<String>();
        
        for (Mistnost mistnost : mistnosti) {
            //System.out.println("Mistnost "+mistnost.getZkratka());
            zkratky.add(mistnost.getZkratka());
        }
        
        return zkratky;
    }
    
    private TreeNode linkAllToNode(String data , TreeNode parent , List<String> strings){
        
        TreeNode node = new DefaultTreeNode(data,parent);
        for(String current : strings){
            TreeNode addedNode = new DefaultTreeNode(current, node);
        }
        
        return node;
    }

    /**
     * @param stredOper the stredOper to set
     */
    public void setStredOper(StrediskoOperator stredOper) {
        this.stredOper = stredOper;
    }

    /**
     * @param mistOper the mistOper to set
     */
    public void setMistOper(MistnostOperator mistOper) {
        this.mistOper = mistOper;
    }
    
}
