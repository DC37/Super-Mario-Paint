package smp.components.general;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import smp.ImageIndex;
import smp.ImageLoader;

/**
 * An image button, to be used as a background-type object.
 * To be extended by more concrete classes that use
 * images as backgrounds.
 * @author RehdBlob
 * @since 2012.08.14
 * @see ImageButton
 */
public abstract class ImageButton extends JButton {

	/**
	 * Generated serial ID.
	 */
	private static final long serialVersionUID = -3828471220970675344L;

	protected BufferedImage background;
	
	
	public ImageButton(ImageIndex i) {
		super(new ImageIcon(ImageLoader.getSprite(i)));
		setLayout(new BorderLayout());
		background = ImageLoader.getSprite(i);
		Dimension d = new Dimension(background.getWidth(), 
				background.getHeight());
		setPreferredSize(d);
		setBorder(BorderFactory.createEmptyBorder());
		setContentAreaFilled(false);
		validate();
	}

	
}
