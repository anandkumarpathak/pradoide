package org.prado.ide.editor.preferences;

import java.io.File;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.prado.ide.Activator;
import org.prado.ide.resources.Resources;

/**
 * Class used to initialize default preference values for PradoIDE
 * @author anand
 */
public class PradoEditorPreferenceInitializer extends
		AbstractPreferenceInitializer {

	public void initializeDefaultPreferences() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();

		String documentationPath = ResourcesPlugin.getWorkspace().getRoot()
				.getLocation().toFile().getAbsolutePath();

		File file = new File(documentationPath, Resources.getResource()
				.fetchProperty(
						PradoEditorPreferenceConstants.DOCUMENT_LOCAL_PATH));
		file.mkdir();

		store.setDefault(PradoEditorPreferenceConstants.DOCUMENT_LOCAL_PATH,
				file.getAbsolutePath());
		store.setDefault(PradoEditorPreferenceConstants.DOCUMENT_BASE_URL,
				Resources.getResource().fetchProperty(
						PradoEditorPreferenceConstants.DOCUMENT_BASE_URL));		
		store.setDefault(PradoEditorPreferenceConstants.DOCUMENT_INILIALIZE_LEVEL,
				0);
		

	}

}
