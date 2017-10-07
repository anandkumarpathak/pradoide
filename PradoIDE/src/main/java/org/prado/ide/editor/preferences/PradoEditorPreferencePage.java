package org.prado.ide.editor.preferences;

import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.prado.documentation.finder.ComponentFinder;
import org.prado.documentation.finder.ComponentSearchListener;
import org.prado.ide.Activator;
import org.prado.ide.contentassist.beans.Component;
import org.prado.ide.dialogs.DialogData;
import org.prado.ide.resources.Resources;

/**
 * This Page sets preferences for PradoIDE, like Documentation url and local
 * cache for documentation
 * 
 * @author anand
 * 
 */
public class PradoEditorPreferencePage extends FieldEditorPreferencePage
		implements IWorkbenchPreferencePage, SelectionListener, Runnable,
		ComponentSearchListener {

	private DirectoryFieldEditor documentationLocalPath;
	private StringFieldEditor documentationBaseUrl;
	private Label lblDocStatus;
	private Button btnInitialize;
	private SynchProgressDialog progressDialog;

	private DialogData data;
	private volatile int current;
	private volatile int commondiff;


	public PradoEditorPreferencePage() {
		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setDescription(Resources.getResource().fetchProperty(
				PradoEditorPreferenceConstants.PREFERENCES_DESCRIPTION));

	}

	/**
	 * Creates the field editors. Field editors are abstractions of the common
	 * GUI blocks needed to manipulate various types of preferences. Each field
	 * editor knows how to save and restore itself.
	 */
	public void createFieldEditors() {

		addField(documentationBaseUrl = new StringFieldEditor(
				PradoEditorPreferenceConstants.DOCUMENT_BASE_URL, "Url:",
				getFieldEditorParent()));

		addField(documentationLocalPath = new DirectoryFieldEditor(
				PradoEditorPreferenceConstants.DOCUMENT_LOCAL_PATH,
				"Local Cache Path:", getFieldEditorParent()));

		lblDocStatus = new Label(getFieldEditorParent(), SWT.NONE);

		int percent = Activator.getDefault().getPreferenceStore().getInt(
				PradoEditorPreferenceConstants.DOCUMENT_INILIALIZE_LEVEL);

		if (percent == 0)
			lblDocStatus.setText("Cache Status:  Not Initialized");
		else
			lblDocStatus.setText("Cache Status: " + percent + " % Initialized");
		btnInitialize = new Button(getFieldEditorParent(), SWT.PUSH);
		btnInitialize.setText("Initialize");
		btnInitialize.addSelectionListener(this);

	}

	public void init(IWorkbench workbench) {
	}

	protected void checkState() {
		super.checkState();
		if (!isValid())
			return;

		if (null == documentationLocalPath.getStringValue()
				|| documentationLocalPath.getStringValue().length() == 0) {

			setErrorMessage("Directory cannot be empty");
			setValid(false);
		} else {
			setErrorMessage(null);
			setValid(true);
		}
	}

	public void propertyChange(PropertyChangeEvent event) {
		super.propertyChange(event);
		if (event.getProperty().equals(FieldEditor.VALUE)) {
			if (event.getSource() == documentationLocalPath
					|| event.getSource() == documentationBaseUrl)
				checkState();
		}
	}

	@Override
	public void widgetDefaultSelected(SelectionEvent arg0) {

	}

	@Override
	public void widgetSelected(SelectionEvent e) {

		if (e.getSource() == btnInitialize) {
			synchDoc();
		}

	}

	protected void synchDoc() {

		data = new DialogData();
		progressDialog = new SynchProgressDialog(getShell(), data);

		new Thread(this).start();

		try {
			progressDialog.run(true, true, progressDialog);
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		int x = current * commondiff;

		if (x > 100)
			x = 100;

		Activator.getDefault().getPreferenceStore().setValue(
				PradoEditorPreferenceConstants.DOCUMENT_INILIALIZE_LEVEL, x);
		lblDocStatus.setText("Cache Status: " + x + " %");

	}

	protected ComponentFinder createComponentFinder() {
		return ComponentFinder.finder;
		// return new MockComponentFinder();
	}

	
	public void run() {
		doDownload();
		// BusyIndicator.showWhile(display, runnable)
	}

	private void doDownload() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();

		String localCacheFolder = store
				.getString(PradoEditorPreferenceConstants.DOCUMENT_LOCAL_PATH);
		String url = store
				.getString(PradoEditorPreferenceConstants.DOCUMENT_BASE_URL);

		Component roorComponent = null;

		try {
			roorComponent = createComponentFinder().findComponent("TControl",
					url, localCacheFolder);
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		int size = roorComponent.getSubclasses().size();

		int rem = 100 % size;
		commondiff = 100 / size;

		if (rem != 0)
			commondiff += 1;

		try {
			for (current = 1; current <= size; current++) {

				String directChild = roorComponent.getSubclasses().get(
						current - 1).getName().getInfo();
				ComponentFinder.finder.findAllComponent(directChild, url,
						localCacheFolder, this, "TControl");

			}
			Activator.getDefault().initializeDoc();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	@Override
	public void found(Component component, String parent, String name) {
		int x = current * commondiff;

		if (x > 100)
			x = 100;

		progressDialog.setCurrent(x);
		progressDialog.setCurrentStatus(parent + ">" + name);
	}
}

