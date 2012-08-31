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

import smp.components.staff.sounds.MPSynthesizer;
import smp.components.topPanel.instrumentLine.InstrumentIndex;
import smp.stateMachine.Settings;

/**
 * Loads the soundfonts that will be used to play sounds.
 * Also holds a Synthesizer and Soundbank that will be used 
 * to play more sounds.
 * @author RehdBlob
 * @since 2012.08.14
 */
public class SoundfontLoader implements Runnable {
	
	/**
	 * A number between 0 and 1 that indicates the
	 * completion of the loading Thread's tasks.
	 */
	private double loadStatus = 0.0;
	
	/**
	 * The sound synthesizer used to hold as many instruments as needed.
	 */
	private static MPSynthesizer theSynthesizer;
	
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
	 * Initializes a MultiSynthesizer with the soundfont.
	 */
	@Override
	public void run() {
		try {
			File f = new File("./" + soundset);
			bank = MidiSystem.getSoundbank(f);
			theSynthesizer = new MPSynthesizer();
			theSynthesizer.open();
			setLoadStatus(0.1);
			/* if (advanced mode on)
			 *     theSynthesizer.ensureCapacity(50);
			 * else
			 bv*/
			
			theSynthesizer.ensureCapacity(19);
			for (Instrument i : theSynthesizer.getLoadedInstruments()) 
				theSynthesizer.unloadInstrument(i);
			setLoadStatus(0.2);
			theSynthesizer.loadAllInstruments(bank);
			setLoadStatus(0.3);
			
			if (Settings.DEBUG){
				System.out.println("Loaded Instruments: ");
				for (Instrument j : theSynthesizer.getLoadedInstruments())
					System.out.println(j.getName());
			}
			
			int ordinal = 0;
			chan = theSynthesizer.getChannels();
			for (InstrumentIndex i : InstrumentIndex.values()) {
				setLoadStatus(0.3 + 0.7
						* ordinal / InstrumentIndex.values().length);
				chan[ordinal].programChange(ordinal);
				if (Settings.DEBUG)
					System.out.println(chan[ordinal].getProgram());
				ordinal++;
				System.out.println("Initialized Instrument: " 
						+ i.toString());
				}
			System.out.println("Synth Latency: " + theSynthesizer.getLatency());
		    setLoadStatus(1);
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
	 * @return The MultiSynthesizer that holds a list of Synthesizers.
	 */
	public static Synthesizer getSynth() {
		return theSynthesizer;
	}
	
	/**
	 * @return An Array of references for MidiChannel objects needed to 
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
	
	public double getLoadStatus() {
		return loadStatus;
	}
	
	public void setLoadStatus(double d) {
		if (d >= 0 && d <= 1)
			loadStatus = d;
	}


}
