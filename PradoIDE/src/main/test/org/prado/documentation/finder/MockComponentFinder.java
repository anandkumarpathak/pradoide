package org.prado.documentation.finder;

import java.util.ArrayList;

import org.prado.ide.contentassist.beans.Component;
import org.prado.ide.contentassist.beans.Info;
import org.prado.ide.contentassist.loader.ComponentLoader;

public class MockComponentFinder extends ComponentFinder{
	@Override
	public Component findComponent(String rootComponentName, String pradoUrl,
			String localCacheFolder) {
		// TODO Auto-generated method stub
		Component c = new Component();
		ArrayList<Component> subclasses = new ArrayList<Component>();
		for(int i=1;i<=26;i++) {
			Component cc = new Component();
			Info f = new Info();
			f.setInfo("Component"+i);
			cc.setName(f);
			subclasses.add(cc);
		}
		c.setSubclasses(subclasses);
		return c;
	}
	
	@Override
	public Component findAllComponent(String rootComponentName,
			String pradoUrl, String localCacheFolder,
			ComponentSearchListener listener, String parent) throws Exception {
		// TODO Auto-generated method stub
		ComponentLoader cl = new MockComponentLoader();
		
		return cl.loadComponent(rootComponentName);
	}
}

class MockComponentLoader implements ComponentLoader {

	@Override
	public Component loadComponent(String componentName) throws Exception {
		// TODO Auto-generated method stub
		Component c = new Component();
		Info i = new Info();
		i.setInfo(componentName);
		c.setName(i);
		return c;
	}
	
}
