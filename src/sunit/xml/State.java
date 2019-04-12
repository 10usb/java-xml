package sunit.xml;

public interface State {
	/**
	 * Called by the parser when this state it set as current state
	 * 
	 * @param qualifiedName Qualified name of the element
	 * @param attributes    Attributes of the element
	 */
	void onOpen(QualifiedName qualifiedName, AttributeCollection attributes);
	
	/**
	 * Called by the parser when the closing tag of this state if found
	 * 
	 * @param qualifiedName
	 */
	void onClose(QualifiedName qualifiedName);
	
	/**
	 * Called when an element is found, this method should return a new state. Best
	 * is not to return it self
	 * 
	 * @param qualifiedName Qualified name of the element
	 * @param attributes    Attributes of the element
	 * @return
	 */
	State onElement(QualifiedName qualifiedName, AttributeCollection attributes);
	
	/**
	 * Called by the parser when a text node is found
	 * 
	 * @param value The resolved value of the text node
	 */
	void onText(String value);
	
	/**
	 * Called by the parser when a comment element is found
	 * 
	 * @param value The textual value of the comment
	 */
	void onComment(String value);
	
}
