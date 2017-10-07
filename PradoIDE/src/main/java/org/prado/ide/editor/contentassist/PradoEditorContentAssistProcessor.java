/*******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package org.prado.ide.editor.contentassist;

import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.ui.internal.WorkbenchWindow;
import org.eclipse.wst.html.ui.internal.contentassist.HTMLContentAssistProcessor;
import org.eclipse.wst.xml.ui.internal.contentassist.ContentAssistRequest;
import org.prado.ide.Activator;
import org.prado.ide.contentassist.beans.Component;
import org.prado.ide.contentassist.beans.Event;
import org.prado.ide.contentassist.beans.Info;
import org.prado.ide.contentassist.beans.Property;
import org.w3c.dom.Node;

/**
 * 
 * Provides content assist to Prado editor 
 * @author anand
 *
 */
@SuppressWarnings("restriction")
public class PradoEditorContentAssistProcessor extends
		HTMLContentAssistProcessor {

	public static final String PRADO_PREFIX = "com";

	private HashMap<String, Component> components = new HashMap<String, Component>();
	

	public PradoEditorContentAssistProcessor() {
		super();
		initialize();
	}

	protected void initialize() {
		components = Activator.getDefault().getComponents();
	}

	protected void addAttributeNameProposals(
			ContentAssistRequest contentAssistRequest) {

		String criteria = contentAssistRequest.getMatchString();
		if (criteria == null)
			criteria = "";

		try {
			Node node = contentAssistRequest.getNode();

			// System.out.println("Prefix: " + node.getPrefix() + ", Name: " +
			// node.getNodeName() );

			if (PRADO_PREFIX.equals(node.getPrefix())) {
				String compName = node.getNodeName().split(":")[1];
				Component cc = components.get(compName);

				for (Property prop : cc.getProperties()) {

					String assistShow = prop.getName().getInfo();

					if (criteria.length() > 0
							&& !assistShow.toLowerCase().startsWith(
									criteria.toLowerCase())) {
						continue;
					}

					String assist = assistShow + "=\"\"";
					String assistDoc = getConsolidatedInfo(prop
							.getDescription());

					CompletionProposal cp = new CompletionProposal(assist,
							contentAssistRequest.getReplacementBeginPosition(),
							contentAssistRequest.getReplacementLength(), assist
									.length() - 1, null, "<> " + assistShow,
							null, assistDoc);
					contentAssistRequest.addProposal(cp);
				}

				for (Event event : cc.getEvents()) {

					String assistShow = event.getName().getInfo();

					if (criteria.length() > 0
							&& !assistShow.toLowerCase().startsWith(
									criteria.toLowerCase())) {
						continue;
					}

					String assist = assistShow + "=\"\"";
					String assistDoc = getConsolidatedInfo(event
							.getDescription());

					CompletionProposal cp = new CompletionProposal(assist,
							contentAssistRequest.getReplacementBeginPosition(),
							contentAssistRequest.getReplacementLength(), assist
									.length() - 1, null, "<> " + assistShow,
							null, assistDoc);
					contentAssistRequest.addProposal(cp);

				}
			}

			super.addAttributeNameProposals(contentAssistRequest);
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	protected String getConsolidatedInfo(ArrayList<Info> infoList) {
		StringBuilder builder = new StringBuilder();
		for (Info f : infoList) {
			builder.append(f.getInfo());
		}
		return builder.toString();
	}

	@Override
	protected void addTagNameProposals(
			ContentAssistRequest contentAssistRequest, int childPosition) {

		try {
			
		
		String criteria = contentAssistRequest.getMatchString();
		if (criteria == null)
			criteria = "";
		
		for (String compName : components.keySet()) {
			
			String assist = "com:" + compName;
			
			if (criteria.length() > 0
					&& !compName.toLowerCase().startsWith(
							criteria.toLowerCase())) {
				continue;
			}
			
			String assist1 = "<" + assist + "></" + assist + ">";
			Component cc = components.get(compName);
			String assistDoc = cc.getNamespace() + "\n"
					+ cc.getName().getInfo();

			contentAssistRequest.addProposal(new CompletionProposal(assist1,
					contentAssistRequest.getReplacementBeginPosition() - 1,
					contentAssistRequest.getReplacementLength() + 1, assist1
							.length() / 2, null, "<> " + assist, null,
					assistDoc));

		}

		super.addTagNameProposals(contentAssistRequest, childPosition);
		} catch (Throwable e) {
			e.printStackTrace();
			
		}
	}

}
