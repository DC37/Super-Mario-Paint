package smp.components.general;

import java.awt.image.BufferedImage;

/**
 * A button that is actually a displayed image.
 * To be extended by several different classes that
 * display different buttons and have different effects.
 * @author RehdBlob
 * @since 2012.08.14
 */
public abstract class ImageButton extends ImagePanel {
	
	protected BufferedImage background;
	protected Coordinates loc;
	
	/**
	 * Generated serial ID.
	 */
	private static final long serialVersionUID = -6399800186540401167L;
	
	public ImageButton(int x, int y) {
		super(x, y);
	}
	
}
