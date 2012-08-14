package smp.components.staff.sounds;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;

import smp.components.staff.Note;
import smp.stateMachine.State;
import smp.stateMachine.StateMachine;

import com.sun.media.sound.*;

/**
 * Plays a sequence of notes, or a single note.
 * @author RehdBlob
 * @since 2012.08.14
 */
public class Player {
	
	/**
	 * Plays a single note sound. This is not to be used to play
	 * songs.
	 * @param n The Note that is to be played as a single note sound.
	 */
	public void playSound(Note n) {
		
	}
	
	/**
	 * Plays a Super Mario Paint song.
	 * @param s A sequence of events to be played on the staff.
	 */
	public void playSong(Sequence s) {
		try {
			Sequencer sq = MidiSystem.getSequencer();
			sq.setSequence(s);
			StateMachine.setState(State.SONG_PLAYING);
			sq.start();
		} catch (MidiUnavailableException e) {
			e.printStackTrace();
		} catch (InvalidMidiDataException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Plays a Super Mario Paint song.
	 * @param s A sequence of events to be played on the staff.
	 */
	public void playArr(Sequence s) {
		try {
			Sequencer sq = MidiSystem.getSequencer();
			sq.setSequence(s);
			StateMachine.setState(State.ARR_PLAYING);
			sq.start();
		} catch (MidiUnavailableException e) {
			e.printStackTrace();
		} catch (InvalidMidiDataException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		}
	}
	
	

}
