package org.prado.documentation.finder;

import java.util.ArrayList;

import org.prado.ide.contentassist.beans.Component;
import org.prado.ide.contentassist.loader.ComponentLoader;
import org.prado.ide.contentassist.loader.LocalComponentLoader;
import org.prado.ide.contentassist.loader.RemoteComponentLoader;

/**
 * Finds the Prado Component using different loaders ( Local or Remote )
 * @author anand
 *
 */
public class ComponentFinder {

	public static ComponentFinder finder = new ComponentFinder();

	/**
	 * Finds the root component and only its subclasses.
	 * Note that subclasses are not completely initialized and only their name should be retrieved.
	 * For each subclass, this method must be called again to initialize its complete list of properties and events.
	 * @param rootComponentName
	 * @param pradoUrl
	 * @param localCacheFolder
	 * @return
	 * @throws Exception
	 */
	public Component findComponent(String rootComponentName, String pradoUrl,
			String localCacheFolder) throws Exception {

		ArrayList<ComponentLoader> loaderList = createLoaderList(pradoUrl,
				localCacheFolder);

		Component component = null;

		for (ComponentLoader loader : loaderList) {
			component = loader.loadComponent(rootComponentName);
			if (component != null)
				break;
		}
		
		return component;
	}

	/**
	 * Finds all components recursively, starting from this root
	 * Notifies the listener for search results
	 * @param rootComponentName
	 * @param pradoUrl
	 * @param localCacheFolder
	 * @param listener which must be notified for search results
	 * @param parent
	 * @return
	 * @throws Exception
	 */
	public Component findAllComponent(String rootComponentName,
			String pradoUrl, String localCacheFolder,
			ComponentSearchListener listener, String parent) throws Exception {

		ArrayList<ComponentLoader> loaderList = createLoaderList(pradoUrl,
				localCacheFolder);

		Component component = null;

		for (ComponentLoader loader : loaderList) {
			component = loader.loadComponent(rootComponentName);
			if (component != null)
				break;
		}

		if (listener != null)
			listener.found(component, parent, rootComponentName);

		if (component.getSubclasses() != null) {

			ArrayList<Component> subclasses = new ArrayList<Component>();

			for (Component cc : component.getSubclasses()) {

				Component ccc = findAllComponent(cc.getName().getInfo(),
						pradoUrl, localCacheFolder, listener, parent + ">"
								+ rootComponentName);
				subclasses.add(ccc);

			}
			component.setSubclasses(subclasses);
		}

		return component;
	}

	protected ArrayList<ComponentLoader> createLoaderList(String pradoUrl,
			String localCacheFolder) {
		ArrayList<ComponentLoader> loaders = new ArrayList<ComponentLoader>();
		loaders.add(new LocalComponentLoader(localCacheFolder));
		loaders.add(new RemoteComponentLoader(pradoUrl, localCacheFolder));

		return loaders;
	}

	public static void main(String[] args) throws Exception {
		ComponentFinder.finder.findAllComponent("TControl",
				"http://www.pradosoft.com/docs/classdoc", "d:\\pradodoc1",
				null, null);
	}
}
