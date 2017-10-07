package org.prado.ide.contentassist.beans;

/**
 * The Property object appearing in the Prado documentation 
 * @author anand
 *
 */
public class Property extends Entity  {
	private Info type;

	public void setType(Info type) {
		this.type = type;
	}

	public Info getType() {
		return type;
	}
}
