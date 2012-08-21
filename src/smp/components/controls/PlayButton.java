package smp.components.controls;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;

import smp.ImageIndex;
import smp.ImageLoader;
import smp.components.general.ImageButton;

public class PlayButton extends ImageButton {

	/**
	 * Generated serial ID.
	 */
	private static final long serialVersionUID = 5719468280926472887L;
	
	private ImageIcon pressed;
	private ImageIcon released;
	private boolean isPressed;
	
	
	public PlayButton() {
		super(ImageIndex.PLAY_RELEASED);
		addActionListener(new PlayButtonListener());
		pressed = new ImageIcon(
				ImageLoader.getSprite(ImageIndex.PLAY_PRESSED));
		released = new ImageIcon(background);
	}
	
	private void changeState() {
		if (!isPressed){
		    this.setIcon(pressed);
		    isPressed = true;
		} else {
			this.setIcon(released);
			isPressed = false;
		}
	}
	
	private class PlayButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			changeState();
		}
		
	}
	

}
