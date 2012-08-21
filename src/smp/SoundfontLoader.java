package smp;

import java.io.File;
import java.io.IOException;

import javax.sound.midi.Instrument;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Soundbank;
import javax.sound.midi.Synthesizer;

/**
 * Loads the soundfont that initially will be used to play sounds.
 * @author RehdBlob
 * @since 2012.08.14
 */
public class SoundfontLoader implements Runnable {

	private Synthesizer synth;
	private Soundbank bank;
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


}
