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
import java.util.Iterator;
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
    
    @Override
    public TreeNode createRoomTree(TreeNode root) {
        
        if(root == null){

            //creates one default TreeNode named "CVUT"
            TreeNode cvut = createDefaultTree();

            List<Stredisko> allStrediska = stredOper.getAll();


            Collections.sort(allStrediska, new StrediskoComparator());

            for(Stredisko curr : allStrediska){

                //get all shortnames of all Mistnost from one Stredisko
                List<String> zkratky = getShortNames(mistOper.getMistnosti(curr));

                //add all Strediska to Tree
                if(zkratky.size()>0){
                    
                    Collections.sort(zkratky);

                    TreeNode nodeStr = new DefaultTreeNode(curr.getNazev(), cvut);  
                    nodeStr.setSelectable(false);
                    
                    //add all Mistnosti for each Stredisko
                    linkAllToNode(nodeStr, zkratky);
                }
            }
        }
        return root;  
    }
    
    private TreeNode createDefaultTree(){
        
        TreeNode root = new DefaultTreeNode("Root", null);  
        TreeNode cvut = new DefaultTreeNode("ČVUT", root);
        cvut.setSelectable(false);
        
        return cvut;
    }
    
    private List<String> getShortNames(List<Mistnost> mistnosti){
        
        List<String> zkratky = new ArrayList<String>();
        
        for (Mistnost mistnost : mistnosti) {
            zkratky.add(mistnost.getZkratka());
        }
        
        return zkratky;
    }
    
    private void linkAllToNode(TreeNode node, List<String> strings){
        for(String current : strings){
            TreeNode addedNode = new DefaultTreeNode(current, node);
        }
    }
    
}
