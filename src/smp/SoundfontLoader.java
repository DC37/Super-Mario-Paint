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
	 * The sound synthesizer used to hold instruments 17-32
	 */
	private static Synthesizer synth2;
	
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
				MidiChannel [] chan1 = synth1.getChannels();
				MidiChannel [] chan2 = synth2.getChannels();
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
		if (i.ordinal() < 16) {
			MidiChannel [] chan = synth1.getChannels();
			if (chan[i.ordinal()] != null)
			    chan[i.ordinal()].noteOn(57, 127);
		} else {
			MidiChannel [] chan = synth2.getChannels();
			if (chan[i.ordinal() - 16] != null)
				chan[i.ordinal() - 16].noteOn(57,127);
		}
	}
	
	/**
	 * Closes the synthesizers.
	 */
	public void close() {
		synth1.close();
		synth2.close();
	}


}
