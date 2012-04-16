package document_object.test;

import processing.core.PApplet;

public class AssetException extends Exception {
	public AssetException( PApplet p, String message ) {
		super();
		p.background( 255, 0, 0 );
		p.println( String.format( "Asset failed: %s", message ) );
	}
}
