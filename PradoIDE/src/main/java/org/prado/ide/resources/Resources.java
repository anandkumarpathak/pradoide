package org.prado.ide.resources;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Loads various configuration properties and expressions
 * @author anand
 *
 */
public class Resources {

	private static Resources resource = new Resources();

	private Properties properties;

	public Resources() {
		load();
	}

	protected void load() {
		properties = new Properties();
		try {
			properties
					.load(getClass().getResourceAsStream("config.properties"));
		} catch (IOException e) {
		}
	}

	public static Resources getResource() {
		return resource;
	}

	public String fetchProperty(String key) {
		return properties.getProperty(key);
	}
	
	public InputStream fetchExpressions() {
		return getClass().getResourceAsStream("expressions.txt");
	}
}
