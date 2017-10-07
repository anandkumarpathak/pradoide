package org.prado.ide.contentassist.beans;

import java.util.ArrayList;

/**
 * Same as Prado UI Control and appears in <com:xxx ...></com:xxx>
 * xxx must be WebControl
 * 
 * @author anand
 *
 */
public class Component extends Entity {

	private String namespace;
	
	private ArrayList<Property> properties;
	private ArrayList<Method> methods;
	private ArrayList<Event> events;
	private String parent;
	private ArrayList<Info> inheritance; // Hierarchy from parent
	private ArrayList<Component> subclasses; // Hierarchy from parent
	
	public Component() {
		properties = new ArrayList<Property>();
		methods = new ArrayList<Method>();
		events = new ArrayList<Event>();
	}
	
	public ArrayList<Property> getProperties() {
		return properties;
	}
	public void setProperties(ArrayList<Property> properties) {
		this.properties = properties;
	}
	public ArrayList<Method> getMethods() {
		return methods;
	}
	public void setMethods(ArrayList<Method> methods) {
		this.methods = methods;
	}
	public ArrayList<Event> getEvents() {
		return events;
	}
	public void setEvents(ArrayList<Event> events) {
		this.events = events;
	}
	public String getParent() {
		return parent;
	}
	public void setParent(String parent) {
		this.parent = parent;
	}
	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}
	public String getNamespace() {
		return namespace;
	}

	public void setInheritance(ArrayList<Info> inheritance) {
		this.inheritance = inheritance;
	}

	public ArrayList<Info> getInheritance() {
		return inheritance;
	}

	public void setSubclasses(ArrayList<Component> subclasses) {
		this.subclasses = subclasses;
	}

	public ArrayList<Component> getSubclasses() {
		return subclasses;
	}
	
}
