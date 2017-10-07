package org.prado.documentation.finder;

import org.prado.ide.contentassist.beans.Component;

/**
 * Interface which must be implemented by class which is interested in component search results.
 * @author anand
 *
 */
public interface ComponentSearchListener {
	public void found(Component component, String parent, String child);
}
