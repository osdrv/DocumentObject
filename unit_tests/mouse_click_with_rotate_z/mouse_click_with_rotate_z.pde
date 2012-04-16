import document_object.objects.*;
import document_object.test.*;
import document_object.animation.*;
import processing.opengl.*;
import java.awt.Canvas;

TestWindow window;
SquareObject sq1;
int angle_z = 2;

public void setup() {
  window = new TestWindow( this, 320, 320, OPENGL );
  background( 255 );
  sq1 = new SquareObject( this );
  sq1.setPos( 160, 160 );
  sq1.setScale( 10 );
  sq1.setDim( 10, 10 );
  sq1.setColor( color( 0, 255, 0 ) );
  window.appendChild( sq1 );
}

public void draw() {
  fill( 0, 255, 0 );
  noStroke();
  background( 255 );
  
  sq1.setZAngle( angle_z );
  
  window.draw();
  
  try {
    assetClick();
    background( 0, 255, 0 );
  } catch ( AssetException e ) {
    
  }
  
  angle_z += 1;
  if ( angle_z >= 360 ) {
    angle_z = 0;
  }
}

public void assetClick() throws AssetException {
  sq1.setEventFired( false );
  float r = ( (float)sq1.getHeight() * (float)Math.sqrt( 2 ) ) - 1;
  window.fireClickAt(
    round( (float)sq1.getPosX() + ( r * sq1.getScale() ) * cos( radians( angle_z + 45 ) ) ),
    round( (float)sq1.getPosY() + ( r * sq1.getScale() ) * sin( radians( angle_z + 45 ) ) )
  );
  asset( sq1.getEventFired() == true,
    String.format( "click fires on sq1 with angle %d", angle_z ) );
}

class SquareObject extends DocumentObject {
  protected color c;
  protected Boolean event_fired;
  protected int z_angle;
  
  public SquareObject( PApplet p ) {
    super( p );
    event_fired = false;
  }

  public void setZAngle( int z_angle ) {
    this.z_angle = z_angle;
  }
  
  public int getZAngle() {
    return this.z_angle;
  }
  
  protected void withModifiers( Runnable scope_runner ) {
    final Runnable _scope_runner = scope_runner;
    super.withModifiers( new Runnable() { public void run() {
      withRotateZ( radians( getZAngle() ), _scope_runner );
    } } );
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
    ellipseMode( CENTER );
    final CatchableMouseEvent _me = me;
    drawPoint( me.getX(), me.getY() );
  }
  
  public void drawPoint( int x, int y ) {
    final int _x = x, _y = y;
    withModifiers( new Runnable() { public void run() {
      fill( 0 );
      ellipse( _x, _y, 1, 1 );
      println( String.format( "%d %d", _x, _y ) );
    } } );
  }
}

public void asset( Boolean expr, String message ) throws AssetException {
  if ( !expr ) {
    throw new AssetException( this, message );
  }
}
