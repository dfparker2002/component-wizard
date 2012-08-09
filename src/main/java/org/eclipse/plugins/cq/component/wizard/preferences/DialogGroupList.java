package org.eclipse.plugins.cq.component.wizard.preferences;

import java.util.Arrays;

import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.preference.ListEditor;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Composite;

import org.eclipse.plugins.cq.component.wizard.Activator;

public class DialogGroupList extends ListEditor {

	private static final String GROUP_NAME = "Group Name";

	private static final String GROUP_NAME_DIALOG_TITLE = "Add component group name";

	private static final String ALWAYS_PRESENT_GROUP_NAME = ".hidden";

	public DialogGroupList(String name, String labelText, Composite parent) {
		super(name, labelText, parent);
		super.setPreferenceStore(Activator.getDefault().getPreferenceStore());
	}

	@Override
	protected String createList(String[] items) {
		
		if (Arrays.asList(items).indexOf(ALWAYS_PRESENT_GROUP_NAME) == -1) {
			Arrays.asList(items).add(ALWAYS_PRESENT_GROUP_NAME);
		}

		StringBuilder result = new StringBuilder();
		for (String value : items) {
			result.append(value).append("|");
		}
		return result.toString();
	}

	@Override
	protected String getNewInputObject() {
		InputDialog dialog = new InputDialog(this.getShell(), GROUP_NAME_DIALOG_TITLE, GROUP_NAME, null, null);
		String result = null;
		if (dialog.open() == Window.OK) {
			if (this.getList().indexOf(dialog.getValue()) == -1) {
				result = dialog.getValue();
			} else {
				dialog.setErrorMessage("This group already exist");
			}
		}
		return result;
	}

	@Override
	protected String[] parseString(String stringList) {
		return stringList.split("\\|");
	}
}