package sunit.xml;

/**
 * This class represent the attribute of an element
 * @author 10usb
 */
public class Attribute {
	private QualifiedName name;
	private String value;
	
	/**
	 * Constructs an attribute from an QualifiedName as key and a string value
	 * @param name The name of the attribute
	 * @param value The value of the attribute
	 */
	public Attribute(QualifiedName name, String value) {
		this.name = name;
		this.value = value;
	}
	
	/**
	 * To get the name of the attribute
	 * @return
	 */
	public QualifiedName getName() {
		return name;
	}
	
	/**
	 * To get the value of the attribute
	 * @return
	 */
	public String getValue() {
		return value;
	}
}
