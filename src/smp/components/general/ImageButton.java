package smp.components.general;

import java.awt.Graphics;
import java.awt.Image;
import javax.swing.JPanel;

/**
 * A button that is actually a displayed image.
 * To be extended by several different classes that
 * display different buttons and have different effects.
 * @author RehdBlob
 * @since 2012.08.14
 */
public abstract class ImageButton extends JPanel {
	
	protected Image image;
	protected int x, y;
	
	/**
	 * Generated serial ID.
	 */
	private static final long serialVersionUID = -6399800186540401167L;
	
	public ImageButton(Image i, int xVal, int yVal) {
		image = i;
		x = xVal;
		y = yVal;
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(image, x, y, null);
	}
	
}
