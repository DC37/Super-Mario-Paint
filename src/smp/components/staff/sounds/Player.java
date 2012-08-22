package smp.components.staff.sounds;

import java.io.File;
import javax.sound.midi.Instrument;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Soundbank;
import javax.sound.midi.Synthesizer;
import smp.components.staff.Note;
import smp.stateMachine.State;
import smp.stateMachine.StateMachine;

/**
 * Plays a sequence of notes, or a single note.
 * @author RehdBlob
 * @since 2012.08.14
 */
public class Player {
	
	private Synthesizer synth;
	private Soundbank bank;
	private String soundset = "soundset3.sf2";
	
	public Player() {
		File f = new File("./" + soundset);
		try {
			synth = MidiSystem.getSynthesizer();
			bank = MidiSystem.getSoundbank(f);
			synth.open();
			System.out.println("Latency: " + synth.getLatency());
			for (Instrument i : synth.getLoadedInstruments()) {
				synth.unloadInstrument(i);
			}
			for (Instrument i : bank.getInstruments()) {
				if (synth.loadInstrument(i))
					System.out.println(i.getName());
				
			}
			
			for (Instrument i : synth.getLoadedInstruments()) {
				System.out.println(i.getName());
			}
			MidiChannel [] chan = synth.getChannels(); 
			if (chan[10] != null) {
		         chan[10].programChange(4);
		         chan[10].noteOn(60, 127);
		    }
			Thread.sleep(3000);
		} catch (Exception e) {
			// Screw exception handling right now. I want 
			// to test things.
			e.printStackTrace();
		} finally {
			synth.close();
			System.exit(0);
		}
	}
	
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
		} finally {
			StateMachine.setState(State.EDITING);
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
		} finally {
			StateMachine.setState(State.EDITING);
		}
	}
	
	

}
