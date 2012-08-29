package smp.components.controls;

import javafx.scene.image.ImageView;
import smp.ImageIndex;
import smp.ImageLoader;

/**
 * Wrapper class for an ImageView that holds the play button
 * image. Pressing the button changes the image and also changes
 * the state of the program.
 * @author RehdBlob
 * @since 2012.08.28
 */
public class PlayButton  {

	private boolean isPressed;
	
	private ImageView theImage;
	
	public PlayButton(ImageView i) {
	    theImage = i;
	}
	
}
