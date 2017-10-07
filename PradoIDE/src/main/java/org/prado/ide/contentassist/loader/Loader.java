package org.prado.ide.contentassist.loader;

import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.prado.ide.contentassist.beans.Component;
import org.prado.ide.contentassist.beans.ElementName;
import org.prado.ide.contentassist.beans.Event;
import org.prado.ide.contentassist.beans.Info;
import org.prado.ide.contentassist.beans.LinkedInfo;
import org.prado.ide.contentassist.beans.Method;
import org.prado.ide.contentassist.beans.Property;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Abstract Loader which loads any Xml document and runs xpath query on it
 * @author anand
 *
 */
abstract public class Loader {

	private Document document;
	private InputStream inputStream;
	private final static XPath xpath = XPathFactory.newInstance().newXPath();

	public Loader(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public String getSimpleNode(String key) throws Exception {

		NodeList nodes = execute(load(), fetchExpression(key));
		return nodes.item(0).getNodeValue();
	}

	public String getTitle() throws Exception {

		NodeList nodes = execute(load(), fetchExpression("title"));
		return nodes.item(0).getNodeValue();
	}

	public String fetchNameSpace() throws Exception {

		NodeList nodes = execute(load(), fetchExpression("namespace"));
		// return nodes.item(0).getNodeValue();
		Info info = fetchInfo(nodes.item(0));
		return info == null ? "" : info.getInfo();
	}

	private Document load() throws Exception {
		if (this.document == null)
			this.document = load(inputStream);
		return this.document;
	}

	protected NodeList execute(Document doc, String expression)
			throws XPathExpressionException {
		XPathExpression expr = Loader.xpath.compile(expression);

		Object result = expr.evaluate(doc, XPathConstants.NODESET);
		NodeList nodes = (NodeList) result;
		return nodes;

	}

	public NodeList fetchAll(String key) throws Exception {

		return execute(load(), fetchExpression(key));
	}

	/**
	 * Fetches list information contained inside this node This implies that
	 * information of every child text node and any other child node is summed
	 * up
	 * 
	 * @param nodeList
	 * @return
	 */
	public ArrayList<Info> fetchInfoList(Node node) {

		try {
			if (ElementName.ANCHOR.equals(node.getNodeName())) {

				ArrayList<Info> infoList = new ArrayList<Info>(1);
				LinkedInfo info = new LinkedInfo();
				info.setInfo(node.getChildNodes().item(0).getNodeValue());
				info.setLink(node.getAttributes().item(0).getNodeValue());
				infoList.add(info);
				return infoList;

			}
			int numberOfChildren = node.getChildNodes().getLength();
			if (numberOfChildren == 0)
				return null;
			ArrayList<Info> infoList = new ArrayList<Info>(numberOfChildren);

			for (int n = 0; n < numberOfChildren; n++) {
				Node child = node.getChildNodes().item(n);
				if (child.getNodeType() == Node.TEXT_NODE) {
					Info info = new Info();
					info.setInfo(child.getNodeValue());
					infoList.add(info);
				}
				if (child.getNodeType() == Node.ELEMENT_NODE
						&& ElementName.ANCHOR.equals(child.getNodeName())) {
					LinkedInfo info = new LinkedInfo();
					info.setLink(child.getAttributes().item(0).getNodeValue()); // href
					info.setInfo(child.getChildNodes().item(0).getNodeValue());
					infoList.add(info);
				}
			}
			return infoList;

		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Fetches list information contained inside this node This implies that
	 * information of every child text node and any other child node is summed
	 * up
	 * 
	 * @param nodeList
	 * @return
	 */
	public Info fetchInfo(Node node) {
		ArrayList<Info> list = fetchInfoList(node);
		return list == null ? null : list.get(0);
	}

	protected String getNodeType(Node node) {
		switch (node.getNodeType()) {
		case Node.TEXT_NODE:
			return "Text";
		case Node.COMMENT_NODE:
			return "Comment";
		case Node.ELEMENT_NODE:
			return "Element";
		}
		return "Unknown";
	}

	/**
	 * 
	 * @param propertyKey
	 *            Key which when used with fetchExpression() must return the
	 *            XPath expression to fetch the rows
	 * @return
	 * @throws Exception
	 */
	public ArrayList<Property> fetchProperties(String propertyKey)
			throws Exception {
		NodeList list = fetchAll(propertyKey);
		ArrayList<Property> properties = new ArrayList<Property>();
		for (int i = 1; i < list.getLength(); i++) { // First row is header row,
			// so skip
			Node node = list.item(i);
			Property property = fetchPropertyFromRow(node);
			properties.add(property);
		}
		return properties;
	}

	/**
	 * Basically children are <td>nodes
	 * 
	 * @param node
	 *            Row node
	 * @return
	 */
	protected Property fetchPropertyFromRow(Node node) {
		NodeList list = node.getChildNodes();
		Property property = new Property();
		// 0th cell is not clear to me, so I skip that :)
		property.setName(fetchInfo(list.item(1)));
		property.setType(fetchInfo(list.item(2)));
		property.setDescription(fetchInfoList(list.item(3)));
		return property;
	}

	public ArrayList<Event> fetchEvents(String eventKey) throws Exception {
		NodeList list = fetchAll(eventKey);
		ArrayList<Event> events = new ArrayList<Event>();
		for (int i = 1; i < list.getLength(); i++) { // First row is header row,
			// so skip
			Node node = list.item(i);
			Event event = fetchEventFromRow(node);
			events.add(event);
		}
		return events;
	}

	protected Event fetchEventFromRow(Node node) {
		NodeList list = node.getChildNodes();
		Event event = new Event();
		event.setName(fetchInfo(list.item(0)));
		event.setDescription(fetchInfoList(list.item(1)));
		return event;
	}

	public ArrayList<Method> fetchMethods(String methodKey) throws Exception {
		NodeList list = fetchAll(methodKey);
		ArrayList<Method> events = new ArrayList<Method>();
		for (int i = 1; i < list.getLength(); i++) { // First row is header row,
			// so skip
			Node node = list.item(i);
			Method method = fetchMethodFromRow(node);
			events.add(method);
		}
		return events;
	}

	protected Method fetchMethodFromRow(Node node) {
		NodeList list = node.getChildNodes();
		Method method = new Method();
		// 0th cell is not clear ...
		method.setName(fetchInfo(list.item(1)));
		method.setDescription(fetchInfoList(list.item(2)));
		return method;
	}

	public ArrayList<Component> fetchSubclasses(String subclassKey)
			throws Exception {
		NodeList list = fetchAll(subclassKey);
		ArrayList<Component> components = new ArrayList<Component>();
		for (int i = 0; i < list.getLength(); i++) { // First row is header row,
			// so skip
			Node node = list.item(i);
			Component component = fetchSubclassFromList(node);
			components.add(component);
		}
		return components;
	}

	protected Component fetchSubclassFromList(Node node) {
		NodeList list = node.getChildNodes();
		Component component = new Component();
		component.setName(fetchInfo(list.item(0)));
		return component;
	}

	public abstract String fetchExpression(String key);

	public abstract Document load(InputStream iis) throws Exception;

}
