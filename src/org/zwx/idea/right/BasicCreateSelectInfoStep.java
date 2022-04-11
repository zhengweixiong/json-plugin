package org.zwx.idea.right;

import com.intellij.ide.util.projectWizard.ModuleWizardStep;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import org.zwx.idea.right.model.NewRightContext;

import javax.swing.*;

/**
 * @author zwx
 */
public class BasicCreateSelectInfoStep extends ModuleWizardStep {

    private JPanel myMainPanel;

    private JPanel myPackagePanel;

    /**
     * 类名
     */
    private JTextField className;

    private Project myProject;
    private Module myModule;

    private JTextArea json;

    /**
     * @param project
     * @param module
     */
    public BasicCreateSelectInfoStep(Project project, Module module) {
        myProject = project;
        myModule = module;
    }

    @Override
    public JComponent getComponent() {
        return myMainPanel;
    }

    @Override
    public boolean validate() throws ConfigurationException {
        if (className.getText().isEmpty()) {
            throw new ConfigurationException("Class name cannot be empty", "Create Class Tips");
        }
        NewRightContext.setClassName(className.getText());
        NewRightContext.setJson(json.getText());
        return super.validate();
    }


    @Override
    public void updateDataModel() {

    }

}
