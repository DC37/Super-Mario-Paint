package smp.components.general;

import javafx.scene.image.ImageView;

/**
 * Image radio button that toggles between a list
 * of buttons. This class holds a list of references
 * to other ImageRadioButtons that it's linked to.
 * @author RehdBlob
 * @since 2012.08.30
 *
 */
public abstract class ImageRadioButton extends ImageButton {

	/**
	 * @param i The ImageView passed to the Button
	 * wrapper.
	 */
	public ImageRadioButton(ImageView i) {
		super(i);
	}
	
	/**
	 * Links the ImageRadioButton to another ImageRadioButton
	 * such that if it is depressed, the other buttons in the list will
	 * be reverted to an unpressed state.
	 */
	public abstract void link();
	
	
}
