package document_object.objects;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import processing.core.*;

public class Window extends DocumentObject {

	public Window(PApplet p, int width, int height, String mode ) {
		super( p );
		p.size( width, height, mode );
		this.setPos( 0, 0 );
		this.setDim( width, height );
		this.setZIndex( 0 );
		p.registerMouseEvent( this );
		p.registerKeyEvent( this );
	}
	
	public void mouseEvent( MouseEvent e ) {
		this.captureEvent( new CatchableMouseEvent( e ) );
	}
	
	public void keyEvent( KeyEvent e ) {
		this.captureEvent( e );
	}
}
