package smp.components.topPanel.instrumentLine;

import java.util.ArrayList;

import javax.sound.midi.MidiChannel;
import smp.ImageIndex;
import smp.ImageLoader;
import smp.SoundfontLoader;
import smp.components.staff.Note;
import javafx.event.EventHandler;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

/**
 * The line of buttons that appear for the Instrument
 * Line at the top of the user interface for Super Mario
 * Paint. The user selects the instrument that they're using here.
 * Clicking on a portrait causes a sound to play while the active
 * instrument is changed.
 * @author RehdBlob
 * @since 2012.08.21
 */
public class ButtonLine {

	/**
	 * The largest value that a note velocity can be;
	 * a note played at this will be played as loudly 
	 * as possible.
	 */
	public static final int MAX_VELOCITY = 127;
	
	/**
	 * The smallest value that a note velocity can be;
	 * a note will basically be silent if played at this.
	 */
	public static final int MIN_VELOCITY = 0;
	
	/**
	 * An ArrayList of ImageView objects being used as buttons.
	 */
	private ArrayList<ImageView> buttons = new ArrayList<ImageView>();
	
	private InstrumentIndex selectedInstrument;
	
	/**
	 * The picture of the currently-selected instrument.
	 */
	private ImageView selectedInst;
	
	/**
	 * The MidiChannel array objects that will be holding references for
	 * sound-playing capabilities.
	 */
	private MidiChannel [] chan1, chan2;
	
	/**
	 * Initializes the ImageView ArrayList.
	 * @param i An ArrayList of ImageView references intended to be displayed
	 * as an instrument line.
	 */
	public ButtonLine(ArrayList<ImageView> i) {
		buttons = i;
	}

	/**
	 * Initializes the buttons with event handlers.
	 */
	public void setup(ImageView inst) {
		selectedInst = inst;
		selectedInstrument = InstrumentIndex.MARIO;
		for (final InstrumentIndex i : InstrumentIndex.values()) {
			buttons.get(i.ordinal()).setOnMouseClicked(
					new EventHandler<MouseEvent>() {
				
				@Override
				public void handle(MouseEvent event) {
					playSound(i);
					try {
						selectedInst.setImage(
							ImageLoader.getSpriteFX(
									ImageIndex.valueOf(i.toString())));
						setSelectedInstrument(i);
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
		try {
		    ArrayList<MidiChannel []> chans = SoundfontLoader.getChannels();
		    chan1 = chans.get(0);
		    chan2 = chans.get(1);
		} catch (NullPointerException e) {
			// Can't recover
			System.exit(0);
		}
	}
	
	/**
	 * Plays the default "A" sound when selecting an instrument.
	 * @param i The InstrumentIndex for the instrument
	 */
	public void playSound(InstrumentIndex i) {
		if (i.ordinal() < 16) {
			if (chan1[i.ordinal()] != null)
			    chan1[i.ordinal()].noteOn(
			    		Note.A3.getKeyNum(), MAX_VELOCITY);
		} else {
			if (chan2[i.ordinal() - 16] != null)
				chan2[i.ordinal() - 16].noteOn(
						Note.A3.getKeyNum(), MAX_VELOCITY);
		}
	}
	
	private void setSelectedInstrument(InstrumentIndex i) {
		selectedInstrument = i;
	}
	
	public InstrumentIndex getSelectedInstrument() {
		return selectedInstrument;
	}
	
}
