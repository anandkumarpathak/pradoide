package org.prado.ide.dialogs;

import org.prado.ide.contentassist.beans.Component;

/**
 * Interface which must be implemented by any class which would like
 * to be informed about Component initialization status
 * @author anand
 *
 */
public interface ComponentInitializeListener {
	public void componentInitialized(String name, Component c);
}
