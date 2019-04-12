package sunit.xml;

import java.io.IOException;

@SuppressWarnings("serial")
public class XmlExeption extends IOException {
	public XmlExeption(String message) {
		super(message);
	}

	public XmlExeption(String message, Throwable cause) {
		super(message, cause);
	}
}
