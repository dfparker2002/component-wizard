package org.eclipse.plugins.cq.component.wizard.preferences;

import org.eclipse.jface.preference.*;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.plugins.cq.component.wizard.Activator;

public class WizardPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public WizardPreferencePage() {
		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setDescription("Set your specific settings for the current project");
	}

	public void createFieldEditors() {
		addField(new StringFieldEditor(PreferenceConstants.P_INNER_FOLDER_NAME, "Components main folder:", getFieldEditorParent()));
		addField(new StringFieldEditor(PreferenceConstants.P_PROJECT_NAME, "Project Name in Eclipse:", getFieldEditorParent()));
		addField(new DirectoryFieldEditor(PreferenceConstants.P_TEMPLATE_PATH, "Path to component template folder:", getFieldEditorParent()));
		addField(new DialogGroupList(PreferenceConstants.P_DIALOG_GROUP_NAME, "Component Group Name (Sidekick):", getFieldEditorParent()));
		
		addField(new StringFieldEditor(PreferenceConstants.P_JS_CATEGORY_NAME, "Javascript script category:", getFieldEditorParent()));
		addField(new StringFieldEditor(PreferenceConstants.P_TAGLIB_PREFIX, "Taglib prefix name:", getFieldEditorParent()));
		addField(new StringFieldEditor(PreferenceConstants.P_TAGLIB_URL, "Taglib url:", getFieldEditorParent()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench workbench) {
	}

}