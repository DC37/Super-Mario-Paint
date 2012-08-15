package smp.components.staff;


import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.sound.midi.MidiEvent;
import javax.swing.JPanel;

import smp.ImageIndex;
import smp.ImageLoader;
import smp.components.staff.sounds.Player;


/**
 * The staff on which notes go.
 * @author RehdBlob
 * @since 2012.08.13
 */
public class Staff extends JPanel {
	
	private BufferedImage background;
	
	/**
	 * Generated serial ID.
	 */
	private static final long serialVersionUID = 5368970028913902003L;
	
	private Player musicPlayer;
	private ArrayList<MidiEvent> events = new ArrayList<MidiEvent>();
	
	public Staff() {
		background = ImageLoader.getSprite(ImageIndex.STAFF_BG);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(background, 0, 0, null);
	}
	

}
