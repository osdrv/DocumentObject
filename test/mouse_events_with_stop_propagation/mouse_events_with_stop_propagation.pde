import document_object.objects.*;
import document_object.animation.*;
import processing.opengl.*;
import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.Canvas;


TestWindow window;
SquareObject sq1, sq2, sq3;

public void setup() {
  window = new TestWindow( this, 320, 320, OPENGL );
  background( 255 );
  sq1 = new SquareObject( this );
  sq1.setPos( 10, 10 );
  sq1.setScale( 10 );
  sq1.setDim( 10, 10 );
  sq1.setColor( color( 0, 255, 0 ) );
  window.appendChild( sq1 );
  
  sq2 = new SquareObject( this );
  sq2.setPos( 10, 10 );
  sq2.setScale( 2 );
  sq2.setDim( 10, 10 );
  sq2.setColor( color( 0, 0, 255 ) );
  sq1.appendChild( sq2 );
  
  sq3 = new SquareObject( this );
  sq3.setPos( 10, 10 );
  sq3.setScale( 10 );
  sq3.setDim( 10, 10 );
  sq3.setColor( color( 0, 0, 0 ) );
  window.appendChild( sq3 );
}

public void draw() {
  fill( 0, 255, 0 );
  noStroke();
  background( 255 );
  
  window.draw();
  loadPixels();
  try {
    assetClick();
    background( 0, 255, 0 );
  } catch ( AssetException e ) {
    
  }
}

public void assetClick() throws AssetException {
    sq1.setEventFired( false );
    sq2.setEventFired( false );
    sq3.setEventFired( false );
    window.fireClickAt( width / 2, height / 2 );
//    sq2 captures event. so sq1 doesn't handle it
    asset( sq1.getEventFired() == false, "click doesn't fire on sq1" );
    asset( sq2.getEventFired() == true, "click fires on sq2" );
    asset( sq3.getEventFired() == false, "click doesn't fire on sq3" );
}

class SquareObject extends DocumentObject {
  protected color c;
  protected Boolean event_fired; 
  public SquareObject( PApplet p ) {
    super( p );
    event_fired = false;
  }

  public void setColor( color c ) {
    this.c = c;
  }

  public void setEventFired( Boolean val ) {
    event_fired = val;
  }
  
  public Boolean getEventFired() {
    return event_fired;
  }

  protected void render() {
    fill( c );
    rect( 0, 0, getWidth(), getHeight() );
  }
  
  protected void onMouseClick( CatchableMouseEvent me ) {
    setEventFired( true );
    me.stopPropagation();
  }
}

public void asset( Boolean expr, String message ) throws AssetException {
  if ( !expr ) {
    throw new AssetException( this, message );
  }
} 

public color pixelAt( int pos_x, int pos_y ) {
  return pixels[ width * pos_y + pos_x ];
}

class TestWindow extends Window {
  public TestWindow( PApplet p, int width, int height, String mode ) {
    super( p, width, height, mode );
  }
  
  public void fireClickAt( int pos_x, int pos_y ) {
    captureEvent( new MouseEvent(
      new Canvas(),
      MouseEvent.MOUSE_CLICKED,
      System.currentTimeMillis() / 1000,
      0,
      pos_x,
      pos_y,
      1,
      false
    ) );
  }
}

class AssetException extends Exception {
  public AssetException( PApplet p, String message ) {
    super();
    p.background( 255, 0, 0 );
    p.println( String.format( "Asset condition failed: %s", message ) );
  }
}
