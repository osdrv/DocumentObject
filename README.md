# processing document-object model

    import document_object.objects.*;
    import document_object.animation.*;
    import processing.opengl.*;

    Window window;

    void setup() {
      this.window = new Window( this, 320, 320, OPENGL );
      MyDocumentObject o = new MyDocumentObject( this );
      window.appendChild( o );
    }

    void draw() {
      background( 0 );
      window.draw();
    }

    class MyDocumentObject extends DocumentObject {

      protected int z_angle = 0;
      protected int x_angle = 0;

      public MyDocumentObject( PApplet p ) {
        super( p );
        this.setDim( 100, 100 );
        this.setPos( 160, 160 );
      }

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
        rect( 0, 0, getWidth(), getHeight() );
      }

      public void setXAngle( int angle ) { x_angle = angle; }

      public int getXAngle() { return x_angle; }

      public void setZAngle( int angle ) { z_angle = angle; }

      public int getZAngle() { return z_angle; }

      void onMouseClick( CatchableMouseEvent e ) {
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