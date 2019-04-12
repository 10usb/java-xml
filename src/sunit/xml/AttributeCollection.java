package sunit.xml;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Represents a collection of attributes
 * @author 10usb
 */
public class AttributeCollection implements Iterable<Attribute> {
	HashMap<QualifiedName, Attribute> attributes = new HashMap<>();
	
	@Override
	public Iterator<Attribute> iterator() {
		return attributes.values().iterator();
	}
	
	/**
	 * Sets a attribute with the given name and value
	 * @param name Name of the attribute to set
	 * @param value Value of the attribute
	 */
	public void set(QualifiedName name, String value) {
		attributes.put(name, new Attribute(name, value));
	}
	
	/**
	 * To get a value by a QualifiedName
	 * @param name Name of the attribute
	 * @return
	 */
	public String get(QualifiedName name) {
		return attributes.get(name).getValue();
	}
	
	/**
	 * To get a value by a name without a namespace
	 * @param name Name of the attribute
	 * @return
	 */
	public String get(String name) {
		return get(QualifiedName.Create(name, null));
	}
}
