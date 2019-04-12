package sunit.xml;

/**
 * This class is an placeholder for any element thats needs to be ignored,
 * It is also an ideal base class, as it implements all methods blank
 * @author 10usb
 */
public class DefaultState implements State {
	@Override
	public void onOpen(QualifiedName qualifiedName, AttributeCollection attributes) {
	}
	
	@Override
	public void onClose(QualifiedName qualifiedName) {
	}
	
	@Override
	public State onElement(QualifiedName qualifiedName, AttributeCollection attributes) {
		return new DefaultState();
	}
	
	@Override
	public void onText(String value) {
	}
	
	@Override
	public void onComment(String value) {
	}
}
