package document_object.objects;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.event.*;
import java.util.Observable;
import java.util.Observer;
import java.util.ArrayList;
import java.lang.Math;

import document_object.animation.*;
import processing.core.*;

public class DocumentObject extends Observable implements Observer,
		Comparable<DocumentObject> {
	
	protected PApplet applet;
	protected int width = 0;
	protected int height = 0;
	protected int pos_x = 0;
	protected int pos_y = 0;
	protected float scale = (float) 1.0;
	protected int z_index = 0;
	protected ArrayList<DocumentObject> children;
	protected DocumentObject parent = null;

	public DocumentObject( PApplet p ) {
		this.setApplet( p );
		this.children = new ArrayList<DocumentObject>();
	}

	public PApplet getApplet() {
		return applet;
	}

	public void setApplet( PApplet applet ) {
		this.applet = applet;
	}
	
	public void draw() {
		withModifiers( new Runnable() { public void run() {
			render();
			for ( DocumentObject child : children ) {
				child.draw();
			}
		} } );
	}
	
	protected void render() {}

	protected void withModifiers( Runnable scope_runner ) {
		final Runnable _scope_runner = scope_runner;
		final float current_scale = this.scale;
		withTranslate( getPosX(), getPosY(), new Runnable() { public void run() {
			withScale( current_scale, _scope_runner );
		} } );
	}
	
	public void appendChild( DocumentObject child ) {
		if ( !containsChild( child ) ) {
			child.setParent( this );
			children.add( child );
		}
	}

	public void removeChild( DocumentObject child ) {
		int index_of_child = children.indexOf( child );
		if ( index_of_child != -1 ) {
			child.noParent();
			children.remove( index_of_child );
		}
	}

	public Boolean containsChild( DocumentObject child ) {
		return children.contains( child );
	}

	public ArrayList<DocumentObject> getChildren() {
		return children;
	}

	public void truncateChildren() {
		for ( int i = 0; i < children.size(); ++i ) {
			children.get( i ).noParent();
		}
		children.clear();
	}

	public void noParent() {
		this.parent = null;
		this.setZIndex( 0 );
	}
	  
	public void setParent( DocumentObject parent ) {
		this.parent = parent;
		this.setZIndex( 1 + parent.getZIndex() );
	}

	public DocumentObject getParent() {
		return parent;
	}

	public Boolean hasParent() {
		return !( this.parent == null );
	}

	public int getZIndex() {
		return this.z_index;
	}

	public void setZIndex( int z_index) {
		this.z_index = z_index;
		final int child_z_index = 1 + z_index;
		for ( int i = 0; i < children.size(); ++i ) {
			children.get( i ).setZIndex( 1 + child_z_index );
		}
	}

	public void setScale( float scale ) {
		this.scale = scale;
	}
	
	public float getScale() {
		return this.scale;
	}
	
	public void update( Observable observable, Object arg ) {
		if ( arg instanceof AWTEvent ) {
			AWTEvent e = (AWTEvent)arg;
			handle( e );
			this.getParent().update( observable, arg );
		}
	}

	public int compareTo( DocumentObject d ) {
		final int z_index1 = this.getZIndex();
	    final int z_index2 = d.getZIndex();
	    return ( z_index1 == z_index2 ) ?
	    		( 0 ) : 
	    			( ( z_index1 > z_index2 ) ? -1 : 1 );
	}

	public Boolean intersect( AWTEvent e ) {
		if ( e instanceof MouseEvent ) {
			return this.intersectMouse( (MouseEvent)e );
		}
		return true;
	}

	public int getPosX() {
		return this.pos_x;
	}
	
	public void setPosX( int pos_x ) {
		this.pos_x = pos_x;
	}

	public int getPosY() {
		return this.pos_y;
	}
	
	public void setPosY( int pos_y ) {
		this.pos_y = pos_y;
	}

	public void setPos( int pos_x, int pos_y ) {
		this.setPosX( pos_x );
		this.setPosY( pos_y );
	}
	
	public int getWidth() {
		return this.width;
	}

	public void setWidth( int width ) {
		this.width = width;
	}
	
	public int getHeight() {
		return this.height;
	}
	
	public void setHeight( int height ) {
		this.height = height;
	}
	
	public void setDim( int width, int height ) {
		this.setWidth( width );
		this.setHeight( height );
	}
	
	public AnimationChain<Integer> animate( int start, int end, int duration, Lambda<Integer> handler ) {
		AnimationChain<Integer> ac = new AnimationChain<Integer>();
		ac.queue( start, end, duration, handler );
		return ac;
	}
	
	public AnimationChain<Float> animate( float start, float end, int duration, Lambda<Float> handler ) {
		AnimationChain<Float> ac = new AnimationChain<Float>();
		ac.queue( start, end, duration, handler );
		return ac;
	}
	
	protected Boolean intersectMouse( MouseEvent me ) {
		me = (MouseEvent)modifyEvent( me );
		Boolean res = ( me.getX() >= 0 && 
				me.getY() >= 0 && 
				me.getX() <= this.getWidth() && 
				me.getY() <= getHeight() );
		return ( res || intersectAnyChild( me ) );
	}
	
	protected AWTEvent modifyEvent( AWTEvent e ) {
		AWTEvent _e = e;
		if ( _e instanceof MouseEvent ) {
			MouseEvent me = (MouseEvent)e;
			CatchableMouseEvent scaled_event = new CatchableMouseEvent(
				(Component)me.getSource(),
				me.getID(),
				me.getWhen(),
				me.getModifiers(),
				Math.round( (float)( me.getX() - getPosX() ) / scale ),
				Math.round( (float)( me.getY() - getPosY() ) / scale ),
				me.getXOnScreen(),
				me.getYOnScreen(),
				me.getClickCount(),
				me.isPopupTrigger(),
				me.getButton()
			);
			if ( _e instanceof CatchableMouseEvent ) {
				if ( ( (CatchableMouseEvent)_e ).isStopped() ) {
					scaled_event.stopPropagation();
				}
			}
			_e = scaled_event;
		}
		
		return _e;
	}
	
	protected AWTEvent unmodifyEvent( AWTEvent e ) {
		AWTEvent _e = e;
		if ( _e instanceof MouseEvent ) {
			MouseEvent me = (MouseEvent)e;
			CatchableMouseEvent scaled_event = new CatchableMouseEvent(
				(Component)me.getSource(),
				me.getID(),
				me.getWhen(),
				me.getModifiers(),
				Math.round( (float)me.getX() * scale ) + getPosX(),
				Math.round( (float)me.getY() * scale ) + getPosY(),
				me.getXOnScreen(),
				me.getYOnScreen(),
				me.getClickCount(),
				me.isPopupTrigger(),
				me.getButton()
			);
			if ( _e instanceof CatchableMouseEvent ) {
				if ( ( (CatchableMouseEvent)_e ).isStopped() ) {
					scaled_event.stopPropagation();
				}
			}
			_e = scaled_event;
		}
		return _e;
	}
	
	protected Boolean intersectAnyChild( MouseEvent e ) {
		for ( int i = 0; i < children.size(); ++i ) {
			if ( children.get( i ).intersectMouse( e ) ) {
				return true;
			}
		}
		return false;
	}
	
	protected void onMouseMove( CatchableMouseEvent e ) {}
	protected void onMouseDown( CatchableMouseEvent e ) {}
	protected void onMouseUp( CatchableMouseEvent e ) {}
	protected void onMouseDrag( CatchableMouseEvent e ) {}
	protected void onMouseClick( CatchableMouseEvent e ) {}
	protected void onKeyDown( KeyEvent e ) {}
	protected void onKeyUp( KeyEvent e ) {}
	protected void onKeyPress( KeyEvent e ) {}
	
	protected void handle( AWTEvent e ) {
		switch ( e.getID() ) {
		case MouseEvent.MOUSE_PRESSED:
			this.onMouseDown( (CatchableMouseEvent)e );
			break;
		case MouseEvent.MOUSE_RELEASED:
			this.onMouseUp( (CatchableMouseEvent)e );
			break;
		case MouseEvent.MOUSE_CLICKED:
			this.onMouseClick( (CatchableMouseEvent)e );
			break;
		case MouseEvent.MOUSE_DRAGGED:
			this.onMouseDrag( (CatchableMouseEvent)e );
			break;
		case MouseEvent.MOUSE_MOVED:
			this.onMouseMove( (CatchableMouseEvent)e );
			break;
		case KeyEvent.KEY_PRESSED:
			this.onKeyDown( (KeyEvent)e );
			break;
		case KeyEvent.KEY_RELEASED:
			this.onKeyUp( (KeyEvent)e );
			break;
		case KeyEvent.KEY_TYPED:
			this.onKeyPress( (KeyEvent)e );
			break;
		}
	}

	public int captureEvent( AWTEvent e ) {
		int captured = -1;
		AWTEvent _e = modifyEvent( e );
		if ( this.children.size() > 0 ) {
			for ( DocumentObject child : children ) {
				if ( child.intersect( _e ) ) {
					captured = 0;
					child.captureEvent( _e );
				}
			}
		}
		if ( captured != 0 ) {
			this.bubbleEvent( _e );
		}
		return captured;
	}
	
	public int bubbleEvent( AWTEvent e ) {
		int bubbled = -1;
		this.handle( e );
		if ( this.hasParent()) {
			AWTEvent _e = unmodifyEvent( e );
			if ( _e instanceof Catchable ) {
				if ( ( (Catchable)_e ).isStopped() ) {
					return bubbled;
				}
			}
			bubbled = 0;
			if ( this.parent.intersect( _e ) ) {
				this.parent.bubbleEvent( _e );
			}
		}
		return bubbled;
	}
	
	protected void withTranslate( float tx, float ty, Runnable scope_runner ) {
		applet.pushMatrix();
		applet.translate( tx, ty );
		scope_runner.run();
		applet.popMatrix();
	}
	
	protected void withTranslate( float tx, float ty, float tz, Runnable scope_runner ) {
		applet.pushMatrix();
		applet.translate( tx, ty, tz );
		scope_runner.run();
		applet.popMatrix();
	}
	
	public void withRotate( float angle, Runnable scope_runner ) {
		applet.pushMatrix();
		applet.rotate( angle );
		scope_runner.run();
		applet.popMatrix();
	}

	public void withRotateX( float angle, Runnable scope_runner ) {
		applet.pushMatrix();
		applet.rotateX( angle );
		scope_runner.run();
		applet.popMatrix();
	}

	public void withRotateY( float angle, Runnable scope_runner ) {
		applet.pushMatrix();
		applet.rotateY( angle );
		scope_runner.run();
		applet.popMatrix();
	}

	public void withRotateZ( float angle, Runnable scope_runner ) {
		applet.pushMatrix();
		applet.rotateZ( angle );
		scope_runner.run();
		applet.popMatrix();
	}

	public void withRotate( float angle, float vx, float vy, float vz, Runnable scope_runner ) {
		applet.pushMatrix();
		applet.rotate( angle, vx, vy, vz );
		scope_runner.run();
		applet.popMatrix();
	}

	public void withScale( float s, Runnable scope_runner ) {
		applet.pushMatrix();
		applet.scale( s );
		scope_runner.run();
		applet.popMatrix();
	}

	public void withScale( float sx, float sy, Runnable scope_runner ) {
		applet.pushMatrix();
		applet.scale( sx, sy );
		scope_runner.run();
		applet.popMatrix();
	}

	public void withScale( float sx, float sy, float sz, Runnable scope_runner ) {
		applet.pushMatrix();
		applet.pushMatrix();
		applet.scale( sx, sy, sz );
		scope_runner.run();
		applet.popMatrix();
	}

	public void withShearX( float angle, Runnable scope_runner ) {
		applet.pushMatrix();
		applet.shearX( angle );
		scope_runner.run();
		applet.popMatrix();
	}

	public void withShearY( float angle, Runnable scope_runner ) {
		applet.pushMatrix();
		applet.shearY( angle );
		scope_runner.run();
		applet.popMatrix();
	}

	public void withRectMode( int mode, Runnable scope_runner ) {
		final int current_mode = applet.g.rectMode;
		applet.rectMode( mode );
		scope_runner.run();
		applet.rectMode( current_mode );
	}

	public void withEllipseMode( int mode, Runnable scope_runner ) {
		final int current_mode = applet.g.ellipseMode;
		applet.ellipseMode( mode );
		scope_runner.run();
		applet.ellipseMode( current_mode );
	}

	public void withTextFont( PFont font, float size, Runnable scope_runner ) {
		final PFont current_font = applet.g.textFont;
		final float current_text_size = applet.g.textSize;
		applet.textFont( font, size );
		scope_runner.run();
		if ( current_font != null )
			applet.textFont( current_font, current_text_size );
	}

	public void withTextFont( PFont font, Runnable scope_runner ) {
		final PFont current_font = applet.g.textFont;
		applet.textFont( font );
		scope_runner.run();
		if ( current_font != null )
			applet.textFont( current_font );
	}

	public void withShapeMode( int mode, Runnable scope_runner ) {
		final int current_mode = applet.g.shapeMode;
		applet.shapeMode( mode );
		scope_runner.run();
		applet.shapeMode( current_mode );
	}

	public void withImageMode( int mode, Runnable scope_runner ) {
		final int current_mode = applet.g.imageMode;
		applet.imageMode( mode );
		scope_runner.run();
		applet.imageMode( current_mode );
	}
	
	public void withStrokeJoin( int join, Runnable scope_runner ) {
		final int current_join = applet.g.strokeJoin;
		applet.strokeJoin( join );
		scope_runner.run();
		applet.strokeJoin( current_join );
	}
	
	public void withStrokeWeight( float weight, Runnable scope_runner ) {
		final float current_weight = applet.g.strokeWeight;
		applet.strokeWeight( weight );
		scope_runner.run();
		applet.strokeWeight( current_weight );
	}
	
	public void withStrokeCap( int cap, Runnable scope_runner ) {
		final int current_cap = applet.g.strokeCap;
		applet.strokeCap( cap );
		scope_runner.run();
		applet.strokeCap( current_cap );
	}
}
