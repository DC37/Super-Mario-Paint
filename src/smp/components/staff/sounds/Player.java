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

import com.sun.media.sound.SoftSynthesizer;

import smp.components.staff.Note;
import smp.components.topPanel.instrumentLine.InstrumentIndex;
import smp.stateMachine.State;
import smp.stateMachine.StateMachine;

/**
 * Plays a sequence of notes, or a single note.
 * @author RehdBlob
 * @since 2012.08.14
 */
public class Player {
	
	private Synthesizer synth;
	private Synthesizer synth2;
	private Soundbank bank;
	private String soundset = "soundset3.sf2";
	
	public Player() {
		File f = new File("./" + soundset);
		try {
			synth = MidiSystem.getSynthesizer();
			synth2 = new SoftSynthesizer();
			bank = MidiSystem.getSoundbank(f);
			synth.open();
			synth2.open();
			System.out.println("Latency: " + synth.getLatency());
			System.out.println("Max Polyphony: " + synth.getMaxPolyphony());
			System.out.println("Latency: " + synth2.getLatency());
			System.out.println("Max Polyphony: " + synth2.getMaxPolyphony());
			for (Instrument i : synth.getLoadedInstruments()) {
				synth.unloadInstrument(i);
				synth2.unloadInstrument(i);
			}
			for (Instrument i : bank.getInstruments()) {
				if (synth.loadInstrument(i) && synth2.loadInstrument(i))
					System.out.println(i.getName());
			}
			
			for (Instrument i : synth.getLoadedInstruments()) {
				System.out.println(i.getName());
			}
			MidiChannel [] chan = synth.getChannels();
			MidiChannel [] chan2 = synth2.getChannels();
			for (InstrumentIndex i : InstrumentIndex.values()) {
				if (i.ordinal() < 16)
					chan[i.ordinal()].programChange(i.ordinal());
				else
					chan2[i.ordinal()-16].programChange(i.ordinal());
				System.out.println("Initialized: " + i.toString());
			}
			 
		         chan[9].noteOn(60, 127);
			Thread.sleep(3000);
		} catch (Exception e) {
			// Screw exception handling right now. I want 
			// to test things.
			e.printStackTrace();
		} finally {
			synth.close();
			synth2.close();
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
	
	

}
