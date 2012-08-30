package smp.components.staff;


import java.util.ArrayList;
import javax.sound.midi.MidiEvent;
import smp.ImageIndex;
import smp.components.general.ImageButton;


/**
 * The staff on which notes go.
 * @author RehdBlob
 * @since 2012.08.13
 */
@SuppressWarnings("unused")
public class Staff {
	
	/**
	 * Generated serial ID.
	 */
	private static final long serialVersionUID = 5368970028913902003L;
	
	private ArrayList<MidiEvent> events = new ArrayList<MidiEvent>();
	
	/**
	 * Creates a new Staff object and gives it the x and y coordinates
	 * as specified by the user.
	 * @param x The x-location that the staff is to be placed at.
	 * @param y The y-location that the staff is to be placed at.
	 */
	public Staff() {
	}
	

}
