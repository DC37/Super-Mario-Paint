package smp;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.sound.midi.Instrument;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Soundbank;
import javax.sound.midi.Synthesizer;

import com.sun.media.sound.SoftSynthesizer;

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
	 * The sound synthesizer used to hold instruments 1-16.
	 */
	private static Synthesizer synth1;
	
	/**
	 * The MIDI channels associated with synthesizer 1.
	 */
	private static MidiChannel [] chan1;
	
	/**
	 * The sound synthesizer used to hold instruments 17-32
	 */
	private static Synthesizer synth2;
	
	/**
	 * The MIDI channels associated with synthesizer 2.
	 */
	private static MidiChannel [] chan2;
	
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
			synth1 = MidiSystem.getSynthesizer();
			synth2 = new SoftSynthesizer();
			bank = MidiSystem.getSoundbank(f);
			synth1.open();
			synth2.open();
			for (Instrument i : synth1.getLoadedInstruments()) { 
				synth1.unloadInstrument(i);
				synth2.unloadInstrument(i);
			}
			
			for (Instrument i : bank.getInstruments())
				if (synth1.loadInstrument(i) && synth2.loadInstrument(i))
				    System.out.println("Loaded Instrument: " + i.getName());
			
			for (InstrumentIndex i : InstrumentIndex.values()) {
				chan1 = synth1.getChannels();
				chan2 = synth2.getChannels();
				if (i.ordinal() < 16) {
				    chan1[i.ordinal()].programChange(i.ordinal());
				    System.out.println("Initialized Instrument: " 
				        + i.toString());
				} else {
					chan2[i.ordinal()-16].programChange(i.ordinal());
				    System.out.println("Initialized Instrument: " 
					    + i.toString());
				}
			}
			System.out.println("Synth1 Latency: " + synth1.getLatency());
			System.out.println("Synth2 Latency: " + synth2.getLatency());
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
	public static ArrayList<Synthesizer> getSynths() {
		ArrayList<Synthesizer> synths = new ArrayList<Synthesizer>();
		synths.add(synth1);
		synths.add(synth2);
		return synths;
	}
	
	/**
	 * @return An ArrayList of references for MidiChannel arrays needed to 
	 * play sounds.
	 */
	public static ArrayList<MidiChannel []> getChannels() {
		ArrayList<MidiChannel []> chans = new ArrayList<MidiChannel []>();
		chans.add(chan1);
		chans.add(chan2);
		return chans;
	}
	
	/**
	 * Closes the synthesizers.
	 */
	public void close() {
		synth1.close();
		synth2.close();
	}


}
