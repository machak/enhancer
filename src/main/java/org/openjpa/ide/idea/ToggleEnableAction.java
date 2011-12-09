package org.openjpa.ide.idea;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.actionSystem.ToggleAction;
import com.intellij.openapi.project.Project;

/**
 * Action to be shown in and triggered by IDEA's 'Build' dialogue.<br/>
 * Build->OpenJpa Enhancer
 */
public class ToggleEnableAction extends ToggleAction {

    @Override
    public boolean isSelected(final AnActionEvent anActionEvent) {
        final ProjectComponent openJpaProjectComponent = getOpenJpaEnhancerComponent(anActionEvent);
        final PersistentState openJpaProjectComponentState = openJpaProjectComponent == null ? null : openJpaProjectComponent.getState();
        return openJpaProjectComponentState != null && openJpaProjectComponentState.isEnhancerEnabled();
    }

    @Override
    public void setSelected(final AnActionEvent anActionEvent, final boolean b) {
        final ProjectComponent dNEProjectComponent = getOpenJpaEnhancerComponent(anActionEvent);
        if (dNEProjectComponent != null) {
            dNEProjectComponent.setEnhancerEnabled(b);
        }
    }

    private static ProjectComponent getOpenJpaEnhancerComponent(final AnActionEvent anActionEvent) {
        final Project project = getProject(anActionEvent);
        return project.getComponent(ProjectComponent.class);
    }

    private static Project getProject(final AnActionEvent anActionEvent) {
        return PlatformDataKeys.PROJECT.getData(anActionEvent.getDataContext());
    }

}
