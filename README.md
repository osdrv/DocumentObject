# processing document-object model

    import processing.opengl.*;
    import document_object.*;
    
    Window window;
    
    void setup() {
      this.window = new Window( this, 640, 480, P3D );
      background( 0 );
      MyDocumentObject o = new MyDocumentObject( this );
      window.appendChild( o );
    }
    
    void draw() {
      background( 0 );
      window.render();
    }
    
    class MyDocumentObject extends DocumentObject {
    
      protected int z_angle = 0;
      protected int x_angle = 0;
    
      public MyDocumentObject( PApplet p ) {
        super( p );
        this.setDim( 100, 100 );
        this.setPos( 10, 10 );
      }
    
      void render() {
        final MyDocumentObject d = this;
        withTranslate( getPosX() + getWidth() / 2, getPosY() + getHeight() / 2, new Runnable() { public void run() {
          withRotateZ( radians( d.z_angle ), new Runnable() { public void run() {
            withRotateX( radians( d.x_angle ), new Runnable() { public void run() {
              getApplet().rect( 
                -1 * d.getWidth() / 2,
                -1 * d.getHeight() / 2,
                d.getWidth() / 2,
                d.getHeight() / 2
              );
            } } );
          } } );
        } } );
        super.render();
      }
    
      public void setXAngle( int angle ) {
        this.x_angle = angle;
      }
    
      public void setZAngle( int angle ) {
        this.z_angle = angle;
      }
    
      void onMouseClick( CatchableMouseEvent e ) {
        animation_chain( this.z_angle, this.z_angle + 180, 500, new Lambda<Integer>() {
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