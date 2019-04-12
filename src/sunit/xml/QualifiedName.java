package sunit.xml;

public class QualifiedName {
	/**
	 * Characters not allowed to be in the name
	 */
	private static final char[] invalidChars = new char[] { ':', '<', '>', '=', ' ', '&' };
	
	private String name;
	private String uri;
	
	/**
	 * Constructs a qualified name instance with only the name part
	 * 
	 * @param name
	 */
	private QualifiedName(String name) {
		this.name = name;
		this.uri = null;
	}
	
	/**
	 * Constructs a qualified name instance
	 * 
	 * @param name
	 * @param uri
	 */
	private QualifiedName(String name, String uri) {
		this.name = name;
		this.uri = (uri == null || uri.equals("")) ? null : uri;
	}
	
	/**
	 * Name part of the qualified name
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Uri part of the qualified name
	 * 
	 * @return
	 */
	public String getUri() {
		return uri;
	}
	
	/**
	 * To tell if this qualified name is equal to a given name and no uri
	 * 
	 * @param name
	 * @return
	 */
	public boolean equals(String name) {
		return this.name.equals(name) && uri == null;
	}
	
	/**
	 * To tell if this qualified name is equal to a given name and uri
	 * 
	 * @param name
	 * @param uri
	 * @return
	 */
	public boolean equals(String name, String uri) {
		return this.name.equals(name) && (this.uri == null ? uri == null : this.uri.equals(uri));
	}
	
	/**
	 * To tell if two instances of a qualified name are equal
	 * 
	 * @param other
	 * @return
	 */
	public boolean equals(QualifiedName other) {
		if (other == null) return false;
		return equals(other.name, other.uri);
	}
	
	@Override
	public boolean equals(Object object) {
		if (object == null) return false;
		
		if (object instanceof String) {
			return equals((String) object);
		}
		
		if (object instanceof QualifiedName) {
			return equals((QualifiedName) object);
		}
		
		return false;
	}
	
	@Override
	public int hashCode() {
		return toString().hashCode();
	}
	
	/**
	 * Returns a string representation of this qualified name
	 */
	@Override
	public String toString() {
		if (uri == null) return name;
		return String.format("%s:%s", name, uri);
	}
	
	/**
	 * Creates a new instance of a qualified name
	 * 
	 * @param name
	 * @param uri
	 * @return
	 */
	public static QualifiedName Create(String name, String uri) {
		if (name == null) new XmlExeption("Name can't be null");
		for (char invalidChar : invalidChars) {
			if (name.indexOf(invalidChar) >= 0) new XmlExeption("Invalid name");
		}
		return new QualifiedName(name, uri);
	}
}
