package org.prado.ide;

import java.util.HashMap;

import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.ui.views.IViewDescriptor;
import org.osgi.framework.BundleContext;
import org.prado.ide.contentassist.beans.Component;
import org.prado.ide.dialogs.ComponentInitializeJob;
import org.prado.ide.dialogs.ComponentInitializeListener;

/**
 * The Activator class controls the plug-in life cycle for Prado IDE
 * @author anand
 */
public class Activator extends AbstractUIPlugin implements ComponentInitializeListener {
	
	private HashMap<String, Component> components = new HashMap<String, Component>();

	public HashMap<String, Component> getComponents() {
		return components;
	}

	// The plug-in ID
	public static final String PLUGIN_ID = "PradoIDE";

	// The shared instance
	private static Activator plugin;
	
	/**
	 * The constructor
	 */
	public Activator() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		initializeDoc();	
	}

	public void initializeDoc() {
		Job job = new ComponentInitializeJob("PradoIDE - Initializing Components", this);
		job.schedule();
		//IViewDescriptor []page =  getWorkbench().getViewRegistry().getViews();
		//System.out.println("doing");
		//for(IViewDescriptor p: page) {
			
			//System.out.println(p.getLabel());
			//p.getId()
			//getWorkbench().getViewRegistry()
			//getWorkbench().
		//}
		//System.out.println("done");
	}	

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}

	@Override
	public void componentInitialized(String name, Component c) {
		// TODO Auto-generated method stub
		components.put(name, c);
	}
}

