package gui.loaders;

import java.io.IOException;
import java.net.URL;

import javax.sound.midi.Instrument;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Soundbank;

import backend.sound.SMPSynthesizer;
import backend.sound.SoundPlayer;
import gui.InstrumentIndex;
import gui.Settings;
import gui.Values;
import gui.resources.FetchStrategy;
import gui.resources.SMPResourceUtil;

/**
 * Loads the soundfonts that will be used to play sounds.
 * Also holds a Synthesizer and Soundbank that will be used
 * to play more sounds.
 * @author RehdBlob
 * @author j574y923
 * @since 2012.08.14
 */
public class SoundfontLoader extends LoaderBase<SoundPlayer> {

    @Override
    public SoundPlayer call() {
    	SoundPlayer soundPlayer = null;
    	SMPSynthesizer theSynthesizer;
    	Soundbank bank;
    	MidiChannel[] chan;
    	
        try {
    		URL defaultSoundfontFile = SMPResourceUtil.get(Values.DEFAULT_SOUNDFONT, FetchStrategy.FROM_COPY, Values.SOUNDFONTS_FOLDER);
            bank = MidiSystem.getSoundbank(defaultSoundfontFile);
            theSynthesizer = new SMPSynthesizer();
            theSynthesizer.open();
            setLoadStatus(0.1);
            theSynthesizer.ensureCapacity(45);
            for (Instrument i : theSynthesizer.getLoadedInstruments()) {
                theSynthesizer.unloadInstrument(i);
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            setLoadStatus(0.2);
            theSynthesizer.loadAllInstruments(bank);
            setLoadStatus(0.3);

            if ((Settings.debug & 0b01) != 0){
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
                chan[ordinal].controlChange(Values.REVERB, 0);
                ordinal++;
                System.out.println("Initialized Instrument: "
                        + i.toString());
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if ((Settings.debug & 0b01) != 0)
                System.out.println(
                        "Synth Latency: " + theSynthesizer.getLatency());
            setLoadStatus(1);
            
            soundPlayer = new SoundPlayer(theSynthesizer, bank, chan);
            
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
        } catch (NullPointerException e) {
            // Can't recover.
            e.printStackTrace();
            System.exit(0);
        }
        
        return soundPlayer;
	}

}
