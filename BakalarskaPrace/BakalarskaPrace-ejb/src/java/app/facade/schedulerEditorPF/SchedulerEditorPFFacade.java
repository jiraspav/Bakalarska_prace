/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app.facade.schedulerEditorPF;

import dbEntity.Uzivatel;
import org.primefaces.model.ScheduleModel;
import org.primefaces.model.TreeNode;

/**
 *
 * @author Pavel
 */
public interface SchedulerEditorPFFacade {
    /**
     * Metoda pro vytváření nového ScheduleModelu
     * 
     * @param mistnost zkratka místnosti pro kterou se model vytváří
     * @param logged přihlášený uživatel
     * @return nový ScheduleModel vytvořený z rozvrhů a rezervací
     */
    public ScheduleModel createNewModel(TreeNode mistnost, Uzivatel logged);
}
