package org.prado.ide.contentassist.loader;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;

import org.prado.ide.contentassist.beans.Component;
import org.prado.ide.contentassist.beans.Info;
import org.prado.ide.resources.Resources;

/**
 * This loader loads component from a local folder. The document must be downloaded before using this loader
 * @author anand
 *
 */
public class LocalComponentLoader implements ComponentLoader {
	
	private String localCacheFolder;
	public String getLocalCacheFolder() {
		return localCacheFolder;
	}

	public void setLocalCacheFolder(String localCacheFolder) {
		this.localCacheFolder = localCacheFolder;
	}

	private HashMap<String, Component> componentCache;
	
	public LocalComponentLoader(String localCacheFolder) {
		this.localCacheFolder = localCacheFolder;
		componentCache = new HashMap<String, Component>();
	}

	@Override
	public Component loadComponent(String componentName) throws Exception {
		// TODO Auto-generated method stub
		Component c = componentCache.get(componentName);
		
		if(c!=null)
			return c;
		
		File file = new File(localCacheFolder + File.separator + componentName);
		
		if(!file.exists())
			return null;
		
		FileInputStream fis = new FileInputStream(file);		
		XMLLoader loader = new XMLLoader(fis, Resources.getResource().fetchExpressions());
		
		c = new Component();
		
		
		
		c.setNamespace(loader.fetchNameSpace());
		
		Info i = new Info();
		i.setInfo(componentName);

		c.setName(i);		
		c.setMethods(loader.fetchMethods("methods"));
		c.setProperties(loader.fetchProperties("properties"));
		c.setEvents(loader.fetchEvents("events"));
		c.setSubclasses(loader.fetchSubclasses("subclasses"));
		
		componentCache.put(componentName, c);
		
		return c;
	}


}
