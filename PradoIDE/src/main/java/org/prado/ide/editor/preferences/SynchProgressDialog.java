package org.prado.ide.editor.preferences;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Shell;
import org.prado.ide.dialogs.DialogData;

/**
 * Tracks the progress of documentation download
 * @author anand
 *
 */
public class SynchProgressDialog extends ProgressMonitorDialog implements
		IRunnableWithProgress {

	volatile private String currentStatus = "";

	private DialogData data;

	public SynchProgressDialog(Shell parent, DialogData data) {
		super(parent);
		this.data = data;

	}

	@Override
	public void run(IProgressMonitor monitor) throws InvocationTargetException,
			InterruptedException {

		monitor.beginTask("Synchronising the documentation...", 100);

		while (true) {
			if (monitor.isCanceled())
				return;
			monitor.subTask(getCurrentStatus());
			int x = getCurrent();
			monitor.worked(x);
			if (x == 100)
				break;
		}
		monitor.done();

	}

	public int getCurrent() {
		return data.getCurrent();
	}

	public void setCurrent(int current) {
		data.setCurrent(current);
	}

	public void setCurrentStatus(String currentStatus) {
		this.currentStatus = currentStatus;
	}

	public String getCurrentStatus() {
		return currentStatus;
	}

}
