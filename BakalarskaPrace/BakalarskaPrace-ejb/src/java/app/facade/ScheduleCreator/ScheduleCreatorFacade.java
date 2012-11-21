/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app.facade.ScheduleCreator;

import app.facade.ScheduleCreator.impl.RozvrhNaDen;
import java.util.ArrayList;
import org.primefaces.model.TreeNode;

/**
 *
 * @author Pavel
 */
public interface ScheduleCreatorFacade {
    public ArrayList<RozvrhNaDen> createSchedule(TreeNode selectedNode);
}
