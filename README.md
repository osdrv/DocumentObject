# processing document-object model

    import processing.opengl.*;
    import document_object.*;

    Window window;

    void setup() {
      this.window = new Window( this, 640, 480, OPENGL );
      background( 0 );
      MyDocumentObject o = new MyDocumentObject( this );
      window.appendChild( o );
    }

    void draw() {
      window.render();
    }

    class MyDocumentObject extends DocumentObject {
      public MyDocumentObject( PApplet p ) {
        super( p );
        this.setDim( 50, 50 );
        this.setPos( 10, 10 );
      }
  
      void render() {
        getApplet().rect( this.getPosX(), this.getPosY(),
          this.getWidth(), this.getHeight()
        );
        super.render();
      }
  
      void onMouseClick( MouseEvent e ) {
        println( "child element captured click!" );
      }
    }