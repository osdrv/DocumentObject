package document_object.animation;

public class AnimationStep<T> {
	protected T start;
	protected T end;
	protected int duration;
	protected Lambda<T> handler;
	
	public AnimationStep( T start, T end, int duration, Lambda<T> handler ) {
		this.start = start;
		this.end = end;
		this.duration = duration;
		this.handler = handler;
	}
	
	public T getStart() {
		return this.start;
	}
	
	public T getEnd() {
		return this.end;
	}
	
	public int getDuration() {
		return this.duration;
	}
	
	public Lambda<T> getHandler() {
		return this.handler;
	}
}
