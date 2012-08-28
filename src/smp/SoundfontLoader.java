package smp;

import java.io.File;
import java.io.IOException;
import javax.sound.midi.Instrument;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Soundbank;
import javax.sound.midi.Synthesizer;

import smp.components.staff.sounds.MultiSynthesizer;
import smp.components.topPanel.instrumentLine.InstrumentIndex;

/**
 * Loads the soundfonts that will be used to play sounds.
 * Also holds a Synthesizer and Soundbank that will be used 
 * to play more sounds.
 * @author RehdBlob
 * @since 2012.08.14
 */
public class SoundfontLoader implements Runnable {
	
	/**
	 * The sound synthesizer used to hold as many instruments as needed.
	 */
	private static MultiSynthesizer theSynthesizer;
	
	/**
	 * The MIDI channels associated with the MultiSynthsizer.
	 */
	private static MidiChannel [] chan;
	
	/**
	 * The soundbank that will hold the sounds that we're trying to play.
	 */
	private static Soundbank bank;
	
	/**
	 * The default soundset name.
	 */
	private String soundset = "soundset3.sf2";
	
	
	
	/**
	 * Intializes two sound synthesizers with the default soundset.
	 * Current plan may be to write a custom synthesizer class that takes
	 * multiple synthesizers and creates new synths when needed.
	 */
	@Override
	public void run() {
		try {
			File f = new File("./" + soundset);
			theSynthesizer = new MultiSynthesizer();
			bank = MidiSystem.getSoundbank(f);
			theSynthesizer.open();
			/* if (advanced mode on)
			 * theSynthesizer.ensureCapacity(50);
			 */
			theSynthesizer.ensureCapacity(19);
			
			for (Instrument i : bank.getInstruments())
				if (theSynthesizer.loadInstrument(i))
				    System.out.println("Loaded Instrument: " + i.getName());
			
			for (InstrumentIndex i : InstrumentIndex.values()) {
				chan = theSynthesizer.getChannels();
				chan[i.ordinal()].programChange(i.ordinal());
				System.out.println("Initialized Instrument: " 
						+ i.toString());
				}
			System.out.println("Synth Latency: " + theSynthesizer.getLatency());
		} catch (MidiUnavailableException e) {
			// Can't recover.
			e.printStackTrace();
			System.exit(0);
		} catch (InvalidMidiDataException e) {
			// Can't recover.
			e.printStackTrace();
			System.exit(0);
		} catch (IOException e) {
			// Can't recover.
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	/**
	 * @return An ArrayList of Synthesizer object references needed to 
	 * play sounds.
	 */
	public static Synthesizer getSynth() {
		return theSynthesizer;
	}
	
	/**
	 * @return An ArrayList of references for MidiChannel arrays needed to 
	 * play sounds.
	 */
	public static MidiChannel[] getChannels() {
		return chan;
	}
	
	/**
	 * Closes the synthesizers.
	 */
	public void close() {
		theSynthesizer.close();
	}


}
