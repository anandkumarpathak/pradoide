package org.prado.ide.contentassist.loader;

import java.io.InputStream;

import org.w3c.dom.Document;
import org.w3c.tidy.Tidy;

/**
 * This is used to load a document from a inputstream
 * @author anand
 *
 */
public class LoaderUtil {
	
	public static Document loadDocument(InputStream inputFileStream) {
		
		Tidy tidy = new Tidy();
		tidy.setXHTML(true);
		tidy.setQuiet(true);
		tidy.setShowWarnings(false);
		return tidy.parseDOM(inputFileStream, null);
	}
}
