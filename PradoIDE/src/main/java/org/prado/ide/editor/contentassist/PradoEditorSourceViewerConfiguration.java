/*******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package org.prado.ide.editor.contentassist;

import javax.swing.JOptionPane;

import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.wst.html.ui.StructuredTextViewerConfigurationHTML;

/**
 * Source configuration for PradoIDE page editor
 * @author anand
 *
 */
public class PradoEditorSourceViewerConfiguration extends
		StructuredTextViewerConfigurationHTML {
	
	protected IContentAssistProcessor[] getContentAssistProcessors(
			ISourceViewer sourceViewer, String partitionType) {

		return new IContentAssistProcessor[]{new PradoEditorContentAssistProcessor()};
	}

	public PradoEditorSourceViewerConfiguration() {
		super();
		JOptionPane.showMessageDialog(null, "Doing...");
	}

}
