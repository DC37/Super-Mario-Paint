package smp.components.staff.sounds;

import java.io.File;
import java.io.IOException;

import javax.sound.midi.Instrument;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Soundbank;
import javax.sound.midi.Synthesizer;
import javax.sound.midi.Track;

import com.sun.media.sound.SF2Instrument;
import com.sun.media.sound.SF2Sample;
import com.sun.media.sound.SF2Soundbank;
import com.sun.media.sound.SoftSynthesizer;

import smp.components.staff.Note;
import smp.stateMachine.State;
import smp.stateMachine.StateMachine;

/**
 * Plays a sequence of notes, or a single note.
 * @author RehdBlob
 * @since 2012.08.14
 */
public class Player {
	
	private SoftSynthesizer synth;
	private SF2Soundbank bank;
	private String soundset = "NewSF2.sf2";
	
	public Player() {
		File f = new File("./" + soundset);
		try {
			synth = (SoftSynthesizer) MidiSystem.getSynthesizer();
			System.out.println("Latency: " + synth.getLatency());
			bank = (SF2Soundbank) MidiSystem.getSoundbank(f);
			for (SF2Sample s : bank.getSamples()) {
				System.out.println(s.getName());
			}
			
			for (SF2Instrument i : bank.getInstruments()) {
				System.out.println(i.getName());
				System.out.println(synth.loadInstrument(i));
			}
			
			for (Instrument i : synth.getLoadedInstruments()) {
				System.out.println(i.getName());
			}

			try {
				Sequencer player = MidiSystem.getSequencer();
				player.open();
				Sequence seq = new Sequence(Sequence.PPQ, 4);
				Track track = seq.createTrack();
				ShortMessage a = new ShortMessage();
				a.setMessage(144, 10, 60, 100);
				MidiEvent noteOn = new MidiEvent(a, 1);
				track.add(noteOn);
				a = new ShortMessage();
				a.setMessage(128, 10, 60, 100);
				MidiEvent noteOff = new MidiEvent(a, 16);
				track.add(noteOff);
				player.setSequence(seq);
				player.start();
				Thread.sleep(3000);
			} catch (Exception e) {
				e.printStackTrace();
				// Does not actually handle anything yet.
			} 
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
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
