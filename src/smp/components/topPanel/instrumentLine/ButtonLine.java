package smp.components.topPanel.instrumentLine;

import java.util.ArrayList;

import smp.ImageIndex;
import smp.ImageLoader;
import smp.SoundfontLoader;
import javafx.event.EventHandler;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

/**
 * The line of buttons that appear for the Instrument
 * Line at the top of the user interface for Super Mario
 * Paint.
 * @author RehdBlob
 * @since 2012.08.21
 */
public class ButtonLine {

	private ArrayList<ImageView> buttons = new ArrayList<ImageView>();
	
	private ImageView selectedInst;
	
	
	public ButtonLine(ArrayList<ImageView> i) {
		for (ImageView img : i) 
			buttons.add(img);
	}

	/**
	 * Initializes the buttons with event handlers.
	 */
	public void setup(ImageView inst) {
		selectedInst = inst;
		for (final InstrumentIndex i : InstrumentIndex.values()) {
			buttons.get(i.ordinal()).setOnMouseClicked(new EventHandler<MouseEvent>() {
				
				@Override
				public void handle(MouseEvent event) {
					SoundfontLoader.playSound(i);
					try {
						selectedInst.setImage(
							ImageLoader.getSpriteFX(
									ImageIndex.valueOf(i.toString())));
					} catch (IllegalArgumentException e) {
						// Do nothing.
						e.printStackTrace();
					} catch (NullPointerException e) {
						// Do nothing.
						e.printStackTrace();
					}
				}
			});
			
		}
		
	}
	
}
