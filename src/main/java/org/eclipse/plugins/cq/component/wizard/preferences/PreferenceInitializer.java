package org.eclipse.plugins.cq.component.wizard.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.StringFieldEditor;

import org.eclipse.plugins.cq.component.wizard.Activator;

/**
 * Class used to initialize default preference values.
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#initializeDefaultPreferences()
	 */
	public void initializeDefaultPreferences() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		store.setDefault(PreferenceConstants.P_INNER_FOLDER_NAME, "src/main/content/jcr_root/apps/my-prj-name/components/");
		store.setDefault(PreferenceConstants.P_PROJECT_NAME, "/my-prj-name");
		store.setDefault(PreferenceConstants.P_TEMPLATE_PATH, "");
		store.setDefault(PreferenceConstants.P_JS_CATEGORY_NAME, "apps.myprj.all-components");
		store.setDefault(PreferenceConstants.P_JS_VARIABLE_NAME, "MYVAR");
		store.setDefault(PreferenceConstants.P_TAGLIB_PREFIX, "mytaglib");
		store.setDefault(PreferenceConstants.P_TAGLIB_URL, "http://mytaglib.com/jsp/taglib/core");
	}

}
