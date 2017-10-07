package org.prado.ide.contentassist.beans;

import java.util.ArrayList;

/**
 * Super class of all entities
 * @author anand
 *
 */
abstract public class Entity {
		
	private Info name;	
	private ArrayList<Info> description;
	
	public void setName(Info name) {
		this.name = name;
	}
	public Info getName() {
		return name;
	}
	public void setDescription(ArrayList<Info> description) {
		this.description = description;
	}
	public ArrayList<Info> getDescription() {
		return description;
	}
}
