import document_object.objects.*;
import document_object.test.*;
import document_object.animation.*;
import processing.opengl.*;

TestWindow window;
PFont font;

public void setup() {
  window = new TestWindow( this, 730, 800, OPENGL );
  font = loadFont( "Circe-Light-24.vlw" );
  frameRate( 60 );
  background( 255 );
  
  int row = 0, column = 0;
  addSquare( AnimationChain.EASE_LINEAR, "LINEAR", row++, column );
  addSquare( AnimationChain.EASE_IN_OUT_QUAD, "IN_OUT_QUAD", row++, column );
  addSquare( AnimationChain.EASE_IN_OUT_CUBIC, "IN_OUT_CUBIC", row++, column );
  addSquare( AnimationChain.EASE_IN_OUT_QUART, "IN_OUT_QUART", row++, column );
  addSquare( AnimationChain.EASE_IN_OUT_QUINT, "IN_OUT_QUINT", row++, column );
  
  row = 0;
  ++column;
  addSquare( AnimationChain.EASE_IN_OUT_SINE, "IN_OUT_SINE", row++, column );
  addSquare( AnimationChain.EASE_IN_OUT_EXPO, "IN_OUT_EXPO", row++, column );
  addSquare( AnimationChain.EASE_IN_OUT_CIRC, "IN_OUT_CIRC", row++, column );
  addSquare( AnimationChain.EASE_IN_OUT_ELASTIC, "IN_OUT_ELASTIC", row++, column );
  addSquare( AnimationChain.EASE_IN_OUT_BACK, "IN_OUT_BACK", row++, column );
  addSquare( AnimationChain.EASE_IN_OUT_BOUNCE, "IN_OUT_BOUNCE", row++, column );
}

public void addSquare( int mode, String label, int row, int column ) {
  SquareObject sq1 = new SquareObject( this, mode, label );
  sq1.setPos( 30 + 350 * column, 100 * ( 1 + row ) + 10 * row );
  sq1.setScale( 10 );
  sq1.setDim( 10, 10 );
  window.appendChild( sq1 );
}

public void draw() {
  background( 255 );
  fill( #313131 );
  textFont( font );
  text( "Click square to move it", 240, 50 );
  noStroke();
  
  window.draw();
}


class SquareObject extends DocumentObject {
  
  protected int mode, original_x;
  protected String label;
  
  public SquareObject( PApplet p, int mode, String label ) {
    super( p );
    this.mode = mode;
    this.label = label;
    this.original_x = -99999;
  }
  
  public void setPosX( int x ) {
    super.setPosX( x );
    if ( original_x == -99999 ) {
      original_x = x;
    }
  }
  
  protected void render() {
    fill( #8b00ff );
    rect( 0, 0, getWidth(), getHeight() );
  }
  
  public void draw() {
    super.draw();
    fill( 255 );
    textFont( font );
    final String _label = label;
    withTranslate( getPosX(), getPosY(), new Runnable() { public void run() {
      text( _label, 5, 5, round( getWidth() * getScale() ), round( getHeight() * getScale() ) );
    } } );
  }
  
  protected void onMouseClick( CatchableMouseEvent me ) {
    println( mode );
    int start_pos, end_pos;
    int distance = 220;
    int duration = 800;
    start_pos = getPosX();
    if ( getPosX() > original_x ) {
      end_pos = start_pos - distance;
    } else {
      end_pos = getPosX() + distance;
    }
    
    animate( start_pos, end_pos, duration, mode, new Lambda<Float>() { public void run( Float v ) {
      setPosX( round( v ) );
    } } ).run();
  }
}

public void asset( Boolean expr, String message ) throws AssetException {
  if ( !expr ) {
    throw new AssetException( this, message );
  }
}
