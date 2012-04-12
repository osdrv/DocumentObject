package document_object.objects;

import java.awt.AWTEvent;

public class CatchableEvent extends AWTEvent implements Catchable {
	
	protected Boolean stopped = false;
	
	public CatchableEvent( Object source, int id ) {
		super( source, id );
	}
	
	public void stopPropagation() {
		this.stopped = true;
	}
	
	public Boolean isStopped() {
		return this.stopped;
	}
}
