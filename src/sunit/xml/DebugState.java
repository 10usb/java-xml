package sunit.xml;

/**
 * This call helps to debug when building the classes needed to parse the XML
 * @author 10usb
 */
public class DebugState implements State {
	@Override
	public void onOpen(QualifiedName qualifiedName, AttributeCollection attributes) {
		System.out.printf("onOpen %s\n", qualifiedName);
		for (Attribute attribute : attributes) {
			System.out.printf("   %s => %s\n", attribute.getName(), attribute.getValue());
		}
	}
	
	@Override
	public void onClose(QualifiedName qualifiedName) {
		System.out.printf("onClose %s\n", qualifiedName);
	}
	
	@Override
	public State onElement(QualifiedName qualifiedName, AttributeCollection attributes) {
		System.out.printf("onElement %s\n", qualifiedName);
		return new DebugState();
	}
	
	@Override
	public void onText(String value) {
		System.out.printf("onText '%s'\n", value.trim());
	}
	
	@Override
	public void onComment(String value) {
		System.out.printf("onComment %s\n", value);
	}
}
