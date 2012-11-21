/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app.facade.schedulerEditorPF;

import org.primefaces.model.ScheduleModel;
import org.primefaces.model.TreeNode;

/**
 *
 * @author Pavel
 */
public interface SchedulerEditorPFFacade {
    public ScheduleModel createNewModel(TreeNode mistnost);
}
