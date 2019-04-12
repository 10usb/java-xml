package sunit.xml;

import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Stack;

/**
 * This class helps when generating an XML file
 * @author 10usb
 */
public class Writer {
	private PrintStream output;
	private Stack<Node> stack;
	private HashMap<String, String> prefixes;
	private boolean documentStarted;
	private boolean elementOpen;
	private boolean hasContents;
	private boolean indent;
	private String indentString;
	private String charset;
	
	public Writer(OutputStream output) throws XmlExeption {
		stack = new Stack<>();
		prefixes = new HashMap<>();
		elementOpen = false;
		hasContents = false;
		indent = true;
		indentString = "\t";
		charset = "utf-8";
		

		try {
			this.output = new PrintStream(output, true, "utf-8");
		} catch (UnsupportedEncodingException cause) {
			throw new XmlExeption("Unsupported encoding", cause);
		}
	}
	
	private void indent() {
		if (indent) {
			output.println();
			for (int index = 0; index < stack.size(); index++) output.print(indentString);
		}
		
	}
	
	private void xmlDeclaration() {
		if (!documentStarted) {
			output.println("<?xml version=\"1.0\" encoding=\"" + charset + "\"?>");
			documentStarted = true;
		}
	}
	
	public void startElement(String name) throws XmlExeption {
		startElement(null, name, null);
	}
	
	public void startElement(String name, String namespace) throws XmlExeption {
		startElement(null, name, namespace);
	}
	
	public void startElement(String prefix, String name, String namespace) throws XmlExeption {
		xmlDeclaration();
		
		if (elementOpen) {
			output.print('>');
			indent();
		} else if (hasContents) {
			indent();
		}
		
		output.print('<');
		if (namespace != null) {
			if (prefix != null) {
				output.print(prefix);
			} else {
				
				output.print(prefixes.get(namespace));
			}
			output.print(':');
		}
		output.print(name);
		
		if (prefix != null) {
			output.print(' ');
			output.print("xmlns:");
			output.print(prefix);
			output.print("=\"");
			output.print(namespace);
			output.print('"');
		}
		
		elementOpen = true;
		hasContents = false;
		if (prefix != null) {
			prefixes.put(namespace, prefix);
		}
		stack.push(new Node(prefix, name, namespace));
	}
	
	public void attribute(String name, String value) throws XmlExeption {
		attribute(null, name, null, value);
	}
	
	public void attribute(String name, String namespace, String value) throws XmlExeption {
		attribute(null, name, namespace, value);
	}
	
	public void attribute(String prefix, String name, String namespace, String value) throws XmlExeption {
		if (!elementOpen) throw new XmlExeption("Can't write attribute when no element is open");
		
		output.print(' ');
		if (namespace != null) {
			if (prefix != null) {
				output.print(prefix);
			} else {
				output.print(prefixes.get(namespace));
				
			}
			output.print(':');
		}
		output.print(name);
		output.print("=\"");
		output.print(escape(value));
		output.print('"');
		
		if (prefix != null) {
			output.print("xmlns:");
			output.print(prefix);
			output.print("=\"");
			output.print(namespace);
			output.print('"');
			
			prefixes.put(namespace, prefix);
		}
	}
	
	public void string(String text) throws XmlExeption {
		if (elementOpen) {
			output.print('>');
			elementOpen = false;
			hasContents = true;
			indent();
			output.print(text);
		}
	}
	
	public void elementString(String name, String value) throws XmlExeption {
		elementString(null, name, null, value);
	}
	
	public void elementString(String name, String namespace, String value) throws XmlExeption {
		elementString(null, name, namespace, value);
	}
	
	public void elementString(String prefix, String name, String namespace, String value) throws XmlExeption {
		xmlDeclaration();
		
		if (elementOpen) {
			output.print('>');
			indent();
		} else if (hasContents) {
			indent();
		}
		
		output.print('<');
		if (namespace != null) {
			if (prefix != null) {
				output.print(prefix);
			} else {
				
				output.print(prefixes.get(namespace));
			}
			output.print(':');
		}
		output.print(name);
		
		if (prefix != null) {
			output.print(' ');
			output.print("xmlns:");
			output.print(prefix);
			output.print("=\"");
			output.print(namespace);
			output.print('"');
		}
		output.print('>');
		
		output.print(escape(value));
		output.print("</");
		if (namespace != null) {
			if (prefix != null) {
				output.print(prefix);
			} else {
				output.print(prefixes.get(namespace));
			}
			output.print(':');
		}
		output.print(name);
		output.print('>');
		
		elementOpen = false;
		hasContents = true;
	}
	
	public void endElement() throws XmlExeption {
		Node node = stack.pop();
		if (elementOpen) {
			output.print(" />");
			elementOpen = false;
			hasContents = true;
		} else {
			indent();
			output.print("</");
			if (node.name.getUri() != null) {
				output.print(prefixes.get(node.name.getUri()));
				output.print(':');
			}
			output.print(node.name.getName());
			output.print('>');
			
			elementOpen = false;
			hasContents = true;
		}
		
		if (node.prefix != null) prefixes.remove(node.name.getUri());
	}
	
	public void fullEndElement() throws XmlExeption {
		Node node = stack.pop();
		
		indent();
		output.print("</");
		if (node.name.getUri() != null) {
			output.print(prefixes.get(node.name.getUri()));
			output.print(':');
		}
		output.print(node.name.getName());
		output.print('>');
		
		elementOpen = false;
		hasContents = true;
		
		if (node.prefix != null) prefixes.remove(node.name.getUri());
	}
	
	public void endDocument() throws XmlExeption {
		while (!stack.empty()) endElement();
	}
	
	public void raw(String xml) throws XmlExeption {
		if (elementOpen) {
			output.print('>');
			indent();
		} else if (hasContents) {
			indent();
		}
		output.print(xml);
	}
	
	public static String escape(String s) {
		StringBuilder out = new StringBuilder(Math.max(16, s.length()));
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (c == '&') {
				out.append("&amp;");
			} else if (c == '<') {
				out.append("&lt;");
			} else if (c == '>') {
				out.append("&gt;");
			} else if (c == '"') {
				out.append("&quot;");
			} else if (c < 10) {
				out.append("&#0");
				out.append((int) c);
				out.append(';');
			}else if (c < 32 || c > 127) {
				out.append("&#");
				out.append((int) c);
				out.append(';');
			} else {
				out.append(c);
			}
		}
		return out.toString();
	}
	
	private class Node {
		QualifiedName name;
		String prefix;
		
		public Node(String prefix, String name, String namespace) {
			this.name = QualifiedName.Create(name, namespace);
			this.prefix = prefix;
		}
	}
}
