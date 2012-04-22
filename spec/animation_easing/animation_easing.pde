import document_object.objects.*;
import document_object.test.*;
import document_object.animation.*;
import processing.opengl.*;

TestWindow window;
SquareObject sq1;

public void setup() {
  window = new TestWindow( this, 320, 320, OPENGL );
  frameRate( 60 );
  background( 255 );
  sq1 = new SquareObject( this );
  sq1.setPos( 20, 20 );
  sq1.setScale( 10 );
  sq1.setDim( 10, 10 );
  window.appendChild( sq1 );
}

public void draw() {
  fill( 0, 255, 0 );
  noStroke();
  background( 255 );
  
  window.draw();
}


class SquareObject extends DocumentObject {
  
  public SquareObject( PApplet p ) {
    super( p );
  }
  
  protected void render() {
    rect( 0, 0, getWidth(), getHeight() );
  }
  
  protected void onMouseClick( CatchableMouseEvent me ) {
    animate( 20, 200, 1000, AnimationChain.EASE_IN_OUT_CUBIC, new Lambda<Float>() { public void run( Float v ) {
      println( v );
      setPosX( round( v ) );
    } } ).run();
  }
}

public void asset( Boolean expr, String message ) throws AssetException {
  if ( !expr ) {
    throw new AssetException( this, message );
  }
}
