package org.prado.ide.contentassist.loader;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * Loads any XML document using InputStream and Set of XPath Expressions
 * @author anand
 *
 */
public class XMLLoader extends Loader {

	private Properties expressions;
	
	public XMLLoader(InputStream inputStream, InputStream inputStreamExpression ) {
		super(inputStream);
		expressions = new Properties();
		try {
			expressions.load(inputStreamExpression);
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	@Override
	public Document load(InputStream inputStream) throws ParserConfigurationException, SAXException, IOException {
		return LoaderUtil.loadDocument(inputStream); 
	}

	
	
	@Override
	public String fetchExpression(String key) {
		return expressions.getProperty(key) ;
	}
	
}
