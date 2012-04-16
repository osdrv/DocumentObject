package document_object.test;

import processing.core.PApplet;
import java.awt.Canvas;
import java.awt.event.MouseEvent;

import document_object.objects.Window;

public class TestWindow extends Window {

	public TestWindow(PApplet p, int width, int height, String mode) {
		super(p, width, height, mode);
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
