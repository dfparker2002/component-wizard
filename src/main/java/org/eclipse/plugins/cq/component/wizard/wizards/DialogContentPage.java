package org.eclipse.plugins.cq.component.wizard.wizards;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import org.eclipse.plugins.cq.component.wizard.Activator;
import org.eclipse.plugins.cq.component.wizard.preferences.PreferenceConstants;

public class DialogContentPage extends WizardPage {

	private Combo dialogGroup;
	private Button useDlgWithMulti;
	private Button useCss;
	private Button useJs;
	
	protected DialogContentPage(String pageName) {
		super(pageName);
		
		setTitle("CQ5 Component Wizard");
		setDescription("This wizard creates a new files structure");
	}

	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.numColumns = 2;
		layout.verticalSpacing = 9;
		
		/* combobox */
		Label label = new Label(container, SWT.NULL);
		label.setText("SideKick Group :");

		dialogGroup = new Combo(container, SWT.BORDER | SWT.SINGLE);
		dialogGroup.setItems(Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.P_DIALOG_GROUP_NAME).split("\\|"));
		
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		dialogGroup.setLayoutData(gd);

		/** check box*/
		label = new Label(container, SWT.NULL);
		label.setText("Create a dialog with Multi tab:");

		useDlgWithMulti = new Button(container, SWT.CHECK);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		useDlgWithMulti.setLayoutData(gd);

		/** check box*/
		label = new Label(container, SWT.NULL);
		label.setText("Add css clss/file");

		useCss = new Button(container, SWT.CHECK);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		useCss.setLayoutData(gd);

		/** check box*/
		label = new Label(container, SWT.NULL);
		label.setText("Add JS");

		useJs = new Button(container, SWT.CHECK);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		useJs.setLayoutData(gd);
		
		/**/
		initialize();
		setControl(container);
	}

	private void initialize() {
		dialogGroup.setText(".hidden");
		useDlgWithMulti.setSelection(false);
		useCss.setSelection(true);
		useJs.setSelection(true);
	}
	
	public boolean isDialogWithMulti() {
		return useDlgWithMulti.getSelection();
	}

	public String getComponentGroupName() {
		return dialogGroup.getText();
	}
	
	public boolean isUseCss() {
		return useCss.getSelection();
	}
	
	public boolean isUseJs() {
		return useJs.getSelection();
	}
}