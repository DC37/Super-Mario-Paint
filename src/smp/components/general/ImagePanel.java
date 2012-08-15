package smp.components.general;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;

/**
 * An image panel, to be used as a background-type object.
 * To be extended by more concrete classes that use
 * images as backgrounds.
 * @author RehdBlob
 * @since 2012.08.14
 * @see ImageButton
 */
public abstract class ImagePanel extends JComponent {

	/**
	 * Generated serial ID.
	 */
	private static final long serialVersionUID = -3828471220970675344L;

	protected BufferedImage background;
	protected Coordinates loc;
	
	
	public ImagePanel(int x, int y) {
		super();
		loc = new Coordinates(x, y);
		setLayout(new BorderLayout());
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(background, loc.getX(), loc.getY(), null);
		int height = background.getHeight();
		int width = background.getWidth();
		setSize(width, height);
	}
	
}
