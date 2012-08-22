package smp;

import java.io.File;
import java.io.IOException;

import javax.sound.midi.Instrument;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Soundbank;
import javax.sound.midi.Synthesizer;

import smp.components.topPanel.instrumentLine.InstrumentIndex;

/**
 * Loads the soundfont that initially will be used to play sounds.
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
		// TODO Auto-generated method stub
		
	}


}
