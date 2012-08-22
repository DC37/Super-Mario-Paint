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
	 * The sound synthesizer we'll be using to produce sound for the program.
	 */
	private static Synthesizer synth;
	
	/**
	 * The soundbank that will hold the sounds that we're trying to play.
	 */
	private static Soundbank bank;
	
	/**
	 * The default soundset name.
	 */
	private String soundset = "soundset3.sf2";
	
	@Override
	public void run() {
		try {
			File f = new File("./" + soundset);
			synth = MidiSystem.getSynthesizer();
			bank = MidiSystem.getSoundbank(f);
			synth.open();
			for (Instrument i : synth.getLoadedInstruments()) 
				synth.unloadInstrument(i);
			
			for (Instrument i : bank.getInstruments())
				if (synth.loadInstrument(i))
				    System.out.println("Loaded: " + i.getName());
			
			for (InstrumentIndex i : InstrumentIndex.values()) {
				MidiChannel [] chan = synth.getChannels();
				chan[i.ordinal()].programChange(i.ordinal());
				System.out.println("Initialized: " + i.toString());
			}
			
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
	 * @param i The InstrumentIndex for the instrument
	 */
	public static void playSound(InstrumentIndex i) {
		MidiChannel [] chan = synth.getChannels(); 
		if (chan[i.ordinal()] != null) {
	         chan[i.ordinal()].noteOn(57, 127);
	    }
	}
	
	/**
	 * Closes the synthesizer.
	 */
	public void close() {
		synth.close();
	}


}
