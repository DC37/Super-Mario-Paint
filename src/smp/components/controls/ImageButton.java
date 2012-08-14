package smp.components.controls;

import java.awt.Button;

/**
 * A button that is actually a displayed image.
 * To be extended by several different classes that
 * display different buttons and have different effects.
 * @author RehdBlob
 * @since 2012.08.14
 */
public abstract class ImageButton extends Button {

	/**
	 * Default serial ID.
	 */
	private static final long serialVersionUID = 1L;

	public abstract void display();
	
	
}
