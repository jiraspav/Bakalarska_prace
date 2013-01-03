/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app.facade.RoomTreeCreator;

import org.primefaces.model.TreeNode;

/**
 *
 * @author Pavel
 */
public interface RoomTreeCreatorFacade {
    
    /**
     * Metoda pro vytvoření nové stromové struktury a její naplnění daty o střediskách
     * a jejich místnostech určená pro Primefaces p:tree
     * @param root základní node stromu
     * @return základní node stromu s vytvořenou stromovou strukturou
     */
    public TreeNode createRoomTree(TreeNode root);
    
}
