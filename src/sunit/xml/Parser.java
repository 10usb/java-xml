package sunit.xml;

import java.io.InputStream;
import java.util.Stack;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

public class Parser {
	private Stack<Node> stack;
	private StringBuilder buffer;
	private Node current;
	private InputStream inputStream;
	
	/**
	 * Constructs a parser with it's initial state set
	 * 
	 * @param inputStream The input stream contraining the XML
	 * @param state       The initial state
	 */
	public Parser(InputStream inputStream, State state) {
		this.inputStream = inputStream;
		stack = new Stack<Node>();
		current = new Node(null, state);
	}
	
	/**
	 * Starts the parsing of the XML
	 */
	public void parse() {
		try {
			XMLReader reader = XMLReaderFactory.createXMLReader();
			reader.setContentHandler(new ContentHandler() {
				@Override
				public void startPrefixMapping(String prefix, String uri) throws SAXException {
				}
				
				@Override
				public void startDocument() throws SAXException {
				}
				
				@Override
				public void skippedEntity(String name) throws SAXException {
				}
				
				@Override
				public void setDocumentLocator(Locator locator) {
				}
				
				@Override
				public void processingInstruction(String target, String data) throws SAXException {
				}
				
				@Override
				public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
				}
				
				@Override
				public void endPrefixMapping(String prefix) throws SAXException {
				}
				
				@Override
				public void endDocument() throws SAXException {
				}
				
				@Override
				public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
					QualifiedName name = QualifiedName.Create(localName, uri);
					
					AttributeCollection attributes = new AttributeCollection();
					for (int index = 0; index < atts.getLength(); index++) {
						attributes.set(QualifiedName.Create(atts.getLocalName(index), atts.getURI(index)), atts.getValue(index));
					}
					
					Node next = new Node(name, current.state.onElement(name, attributes));
					
					if (next.state == null) throw new SAXException(String.format("Unexpected element '%s'", name));
					
					stack.push(current);
					current = next;
					
					current.state.onOpen(name, attributes);
				}
				
				@Override
				public void endElement(String uri, String localName, String qName) throws SAXException {
					if (current.name == null) throw new SAXException("Unexpected close tag");
					
					QualifiedName name = QualifiedName.Create(localName, uri);
					if (!current.name.equals(name)) throw new SAXException(String.format("Unexpected close tag '%s' expected '%s'", name, current.name));
					
					if (buffer != null) {
						current.state.onText(buffer.toString());
						buffer = null;
					}
					
					current.state.onClose(name);
					current = stack.pop();
				}
				
				@Override
				public void characters(char[] ch, int start, int length) throws SAXException {
					if (buffer == null) {
						buffer = new StringBuilder();
					}
					buffer.append(ch, start, length);
				}
			});
			reader.parse(new InputSource(inputStream));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private class Node {
		public QualifiedName name;
		public State state;
		
		public Node(QualifiedName name, State state) {
			this.name = name;
			this.state = state;
		}
	}
}
