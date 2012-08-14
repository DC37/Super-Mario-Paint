package smp.components.staff;

import java.awt.Component;
import java.util.ArrayList;

import javax.sound.midi.MidiEvent;

import smp.components.staff.sounds.Player;


/**
 * The staff on which notes go.
 * @author RehdBlob
 * @since 2012.08.13
 */
public class Staff extends Component {
	
	/**
	 * Generated serial ID.
	 */
	private static final long serialVersionUID = 5368970028913902003L;
	
	private Player musicPlayer;
	private ArrayList<MidiEvent> events = new ArrayList<MidiEvent>();
	

}
