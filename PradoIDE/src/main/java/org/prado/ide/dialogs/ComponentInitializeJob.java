package org.prado.ide.dialogs;

import java.io.File;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.preference.IPreferenceStore;
import org.prado.documentation.finder.ComponentFinder;
import org.prado.ide.Activator;
import org.prado.ide.contentassist.beans.Component;
import org.prado.ide.editor.preferences.PradoEditorPreferenceConstants;

/**
 * Job to initialize the Prado Component list when the plugin is initialized.
 * This happens when user opens a Prado page file. This job also informs any 
 * listener which is interested in update on component initialization
 * @author anand
 *
 */
public class ComponentInitializeJob extends Job {
	
	private ComponentInitializeListener listener;
	
	public ComponentInitializeJob(String name, ComponentInitializeListener listener) {
		super(name);
		this.listener = listener; 
	}
	
	@Override
    protected IStatus run(IProgressMonitor monitor) {
				
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();

		String localCacheFolder = store
				.getString(PradoEditorPreferenceConstants.DOCUMENT_LOCAL_PATH);
				
		String files [] =  new File(localCacheFolder).list();
		
    	monitor.beginTask("", files.length);
    	
    	for(int i = 0; i< files.length; i++) {
    		if (monitor.isCanceled())
				return Status.OK_STATUS;
    		monitor.subTask(files[i]);
    		Component c =null;
    		try {
				c = ComponentFinder.finder.findComponent(files[i], "IGNORE-URL", localCacheFolder);
				if(listener!=null)
					listener.componentInitialized(files[i], c);
			} catch (Exception e1) {
				e1.printStackTrace();
			}

			monitor.worked(1);
		}
		monitor.done();
    	
        return Status.OK_STATUS;
    }
}
