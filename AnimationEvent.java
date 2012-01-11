package document_object;

public class AnimationEvent<T> extends CatchableEvent {

	protected T value;
	protected String name;
	
	public static final int ANIMATION_EVENT = 1000 + RESERVED_ID_MAX;
	public static final String ANIMATION_START_MASK = ".start";
	public static final String ANIMATION_COMPLETE_MASK = ".complete";
	
	public AnimationEvent( Object source, String name, T value ) {
		super( source, ANIMATION_EVENT );
		this.name = name;
		this.value = value;
	}

	public T getValue() {
		return this.value;
	}
	
	public String getName() {
		return this.name;
	}
}
