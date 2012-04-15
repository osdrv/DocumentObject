package document_object.objects;

import java.awt.Component;
import java.awt.event.MouseEvent;

public class CatchableMouseEvent extends MouseEvent implements Catchable {

	protected Boolean stopped = false;
	
	public CatchableMouseEvent( MouseEvent e ) {
		super( (Component)e.getSource(), e.getID(), e.getWhen(), e.getModifiers(),
				e.getX(), e.getY(), e.getXOnScreen(), e.getYOnScreen(), e.getClickCount(),
				e.isPopupTrigger(), e.getButton() );
		if ( e instanceof CatchableMouseEvent )
			if ( ( (CatchableMouseEvent)e ).isStopped() )
				stopPropagation();
	}
	
	public CatchableMouseEvent(Component arg0, int arg1, long arg2, int arg3,
			int arg4, int arg5, int arg6, boolean arg7) {
		super(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		// TODO Auto-generated constructor stub
	}

	public CatchableMouseEvent(Component arg0, int arg1, long arg2, int arg3,
			int arg4, int arg5, int arg6, boolean arg7, int arg8) {
		super(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
		// TODO Auto-generated constructor stub
	}

	public CatchableMouseEvent(Component arg0, int arg1, long arg2, int arg3,
			int arg4, int arg5, int arg6, int arg7, int arg8, boolean arg9,
			int arg10) {
		super(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10);
		// TODO Auto-generated constructor stub
	}

	public void stopPropagation() {
		this.stopped = true;
	}

	public Boolean isStopped() {
		return this.stopped;
	}

}
