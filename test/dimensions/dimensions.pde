import processing.opengl.*;
import document_object.*;

Window window;
SquareObject sq1, sq2;

public void setup() {
  window = new Window( this, 320, 320, OPENGL );
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
}

public void draw() {
  fill( 0, 255, 0 );
  noStroke();
  background( 255 );
  
  window.draw();
  loadPixels();
  
  try {
    asset( sq1.getPosX() == 10, "Квадрат расположен в точке 10, 10" );
    asset( sq1.getWidth() == 10, "Квадрат шириной в 10px" );
    asset( sq1.getHeight() == 10, "Квадрат высотой в 10px" );
    
    asset( pixelAt( 0, 0 ) == color( 255 ), "Цвет в 0, 0 белый" );
    asset( pixelAt( width - 1, height - 1 ) == color( 255 ), "Цвет в w, h белый" );
    asset( pixelAt( 10, 10 ) == color( 0, 255, 0 ), "цвет в 10, 10 зеленый" );
    asset( pixelAt(
      Math.round( sq1.getWidth() * sq1.getScale() ) - 1,
      Math.round( sq1.getHeight() * sq1.getScale() ) - 1
    ) == color( 0, 255, 0 ), "цвет в sq1(w, h) зеленый" );
    asset( pixelAt(
      Math.round( sq1.getPosX() + sq1.getScale() * sq2.getPosX() ),
      Math.round( sq1.getPosY() + sq1.getScale() * sq2.getPosY() )
    ) == color( 0, 0, 255 ), "цвет в sq2(0, 0) синий" );
    asset( pixelAt(
      Math.round( sq1.getPosX() + sq1.getScale() * sq2.getPosX() + sq2.getWidth() * sq1.getScale() * sq2.getScale() - 1 ),
      Math.round( sq1.getPosY() + sq1.getScale() * sq2.getPosY() + sq2.getHeight() * sq1.getScale() * sq2.getScale() - 1 )
    ) == color( 0, 0, 255 ), "цвет в sq2(w, h) синий" );
  } catch ( AssetException e ) {
    
  }
}


class SquareObject extends DocumentObject {
  protected color c;
  public SquareObject( PApplet p ) {
    super( p );
  }

  public void setColor( color c ) {
    this.c = c;
  }

  protected void render() {
    fill( c );
    rect( 0, 0, getWidth(), getHeight() );
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

class AssetException extends Exception {
  public AssetException( PApplet p, String message ) {
    super();
    p.background( 255, 0, 0 );
    p.println( String.format( "Asset condition failed: %s", message ) );
  }
}

