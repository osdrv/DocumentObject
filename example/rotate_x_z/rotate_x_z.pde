import document_object.objects.*;
import document_object.animation.*;
import processing.opengl.*;
import java.awt.AWTEvent;

Window window;

void setup() {
  // Here we are creating a new window instance.
  // Window initializes a new screen for size and graphic mode given.
  this.window = new Window( this, 320, 320, OPENGL );
  // A new MyDocumentObject is been created
  MyDocumentObject o = new MyDocumentObject( this );
  // Instance geometry
  o.setDim( 100, 100 );
  o.setPos( 160, 160 );
  // Every child should be appended to it's parent
  // otherwise it won't be drawn
  window.appendChild( o );
}

void draw() {
  background( 0 );
  // Here we call for window .draw() method
  // in DocumentObject. Parent node's method
  // will call .draw() method on every child node
  window.draw();
}

class MyDocumentObject extends DocumentObject {

  // We are going to rotate our shape for 2 angles.
  protected int z_angle = 0;
  protected int x_angle = 0;

  // Just a simple parent constructor call here.
  // Perfect place for initialization code.
  public MyDocumentObject( PApplet p ) {
    super( p );
  }

  // This is the main method-modificator.
  // Here we will define all our custom transformations.
  // Also this method will be used on mouse event handling
  // to calculate mouse event position in new basis
  protected void withModifiers( Runnable scope_runner ) {
    final Runnable _scope_runner = scope_runner;
    final float current_scale = this.scale;
    withTranslate( getPosX(), getPosY(), new Runnable() { public void run() {
      withScale( current_scale, new Runnable() { public void run() {
        withRotateZ( radians( getZAngle() ), new Runnable() { public void run() {
          withRotateX( radians( getXAngle() ), _scope_runner );
        } } );
      } } );
    } } );
  }

  protected void render() {
    // Just a simple rectangle.
    rect( 0, 0, getWidth(), getHeight() );
  }

  // Here we'll define custom getters and setters for angles been transformed
  public void setXAngle( int angle ) { x_angle = angle; }
  public int getXAngle() { return x_angle; }
  public void setZAngle( int angle ) { z_angle = angle; }
  public int getZAngle() { return z_angle; }
  
  // Mouse click handler
  // Take attention on modified event class
  void onMouseClick( CatchableMouseEvent e ) {
    // Animation chain with 2 steps: rotate around Z-axis
    // and when this rotation is complete it will start rotate shape around X-axis.
    // Call for method run to immediate chain execution
    animate( this.z_angle, this.z_angle + 180, 500, new Lambda<Integer>() {
      public void run( Integer angle ) {
        setZAngle( angle );
      }
    } ).then( this.x_angle, this.x_angle + 180, 500, new Lambda<Integer>() {
      public void run( Integer angle ) {
        setXAngle( angle );
      }
    } ).run();
  }
}
