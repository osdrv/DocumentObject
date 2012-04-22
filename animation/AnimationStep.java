package document_object.animation;

public class AnimationStep {
	protected float start;
	protected float end;
	protected int duration;
	protected int mode;
	protected Lambda<Float> handler;
	
	public AnimationStep( float start, float end, int duration, Lambda<Float> handler ) {
		this.start = start;
		this.end = end;
		this.duration = duration;
		this.handler = handler;
		this.mode = AnimationChain.EASE_LINEAR;
	}
	
	public AnimationStep( float start, float end, int duration, int mode, Lambda<Float> handler ) {
		this.start = start;
		this.end = end;
		this.duration = duration;
		this.handler = handler;
		this.mode = mode;
	}
	
	public float getStart() {
		return this.start;
	}
	
	public float getEnd() {
		return this.end;
	}
	
	public int getDuration() {
		return this.duration;
	}
	
	public Lambda<Float> getHandler() {
		return this.handler;
	}
	
	public int getMode() {
		return this.mode;
	}
}
