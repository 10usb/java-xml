package sunit.xml;

/**
 * This is a utility class to collect any textual content an element may contain
 * @author 10usb
 */
public abstract class TextState implements State {
	private StringBuilder builder = new StringBuilder();
	
	@Override
	public void onOpen(QualifiedName qualifiedName, AttributeCollection attributes) {
	}
	
	@Override
	public void onClose(QualifiedName qualifiedName) {
		this.process(builder.toString());
	}
	
	@Override
	public State onElement(QualifiedName qualifiedName, AttributeCollection attributes) {
		return new TextState() {
			@Override
			protected void process(String text) {
				builder.append(text);
			}
		};
	}
	
	@Override
	public void onText(String value) {
		builder.append(value);
	}
	
	@Override
	public void onComment(String value) {
	}
	
	/**
	 * Called when all text is collected
	 * @param text
	 */
	protected abstract void process(String text);
}
