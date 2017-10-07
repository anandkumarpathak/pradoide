package org.prado.ide.contentassist.loader;

import org.prado.ide.contentassist.beans.Component;

public interface ComponentLoader {
	public Component loadComponent(String componentName) throws Exception;
}
