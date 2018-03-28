package smp;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.sound.midi.Instrument;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Soundbank;
import javax.sound.midi.Synthesizer;

import smp.components.Values;
import smp.components.InstrumentIndex;
import smp.components.staff.sequences.Note;
import smp.components.staff.sounds.SMPSynthesizer;
import smp.stateMachine.Settings;

/**
 * Loads the soundfonts that will be used to play sounds.
 * Also holds a Synthesizer and Soundbank that will be used
 * to play more sounds.
 * @author RehdBlob
 * @since 2012.08.14
 */
public class SoundfontLoader implements Loader {

    /**
     * A number between 0 and 1 that indicates the
     * completion of the loading Thread's tasks.
     */
    private double loadStatus = 0.0;

    /**
     * The sound synthesizer used to hold as many instruments as needed.
     */
    private static SMPSynthesizer theSynthesizer;

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
    private String defaultSoundset = "soundset3.sf2";

	/**
	 * The current soundset name. This changes when a new soundfont is loaded.
	 */
	private String currentSoundset = defaultSoundset;

	/**
	 * The cache that will contain all soundbanks. Each soundbank is indexed by
	 * its filename.
	 */
	private static Map<String, Soundbank> bankCache = new HashMap<>();

	/**
	 * Initializes a MultiSynthesizer with the soundfont.
	 */
    @Override
    public void run() {
        try {
    		File f = new File("./" + defaultSoundset);
            bank = MidiSystem.getSoundbank(f);
            theSynthesizer = new SMPSynthesizer();
            theSynthesizer.open();
            setLoadStatus(0.1);
            /* if (advanced mode on)
             *     theSynthesizer.ensureCapacity(50);
             * else
             */

            theSynthesizer.ensureCapacity(19);
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

            if (Settings.debug > 0){
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
            if (Settings.debug > 0)
                System.out.println(
                        "Synth Latency: " + theSynthesizer.getLatency());
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
	 * Takes in the absolute path of a soundfont file and constructs a new
	 * soundbank with all the soundfont's instruments loaded in. The
	 * MultiSynthesizer will then use the new soundbank.
	 * 
	 * @param path
	 *            the soundfont file
	 * @throws IOException
	 * @throws InvalidMidiDataException
	 * @throws MidiUnavailableException
	 * @since v1.1.2
	 */
	public void loadSoundfont(String path) throws InvalidMidiDataException, IOException, MidiUnavailableException {
		File f = new File(path);
		if (!f.getName().equals(currentSoundset)) {
			bank = MidiSystem.getSoundbank(f);
			theSynthesizer.loadAllInstruments(bank);
			currentSoundset = f.getName();
		}
	}

	/**
	 * Stores the current soundbank in cache for quick loading. This function
	 * will be used when there is an arrangement with multiple songs with
	 * different soundfonts; the soundfonts will all be stored into cache first
	 * for quick loading {@link #loadFromCache(String)} before starting each
	 * next song.
	 * 
	 * @since v1.1.2
	 */
	public void storeInCache() {
		if(!bankCache.containsKey(currentSoundset))
			bankCache.put(currentSoundset, bank);
	}

	/**
	 * Loads the Soundbank by the soundset name from bankCache and sets it as
	 * the MultiSynthesizer's current soundfont. This is the fast way to set the
	 * program's soundfont.
	 * 
	 * @param soundset
	 *            The soundset name (e.g. soundset3.sf2)
	 * @since v1.1.2
	 */
	public void loadFromCache(String soundset) {
		if(bankCache.containsKey(soundset)) {
			bank = bankCache.get(soundset);
			theSynthesizer.loadAllInstruments(bank);
			currentSoundset = soundset;
		}
	}

	/**
	 * Clears the bankCache.
	 * 
	 * @since v1.1.2
	 */
	public void clearCache() {
		bankCache.clear();
	}

	/**
	 * @return The current soundset name.
	 * @since v1.1.2
	 */
	public String getCurrentSoundset() {
		return currentSoundset;
	}	
	
	/**
	 * @return The default soundset name.
	 * @since v1.1.2
	 */
	public String getDefaultSoundset() {
		return defaultSoundset;
	}
	
	/**
	 * @return The Soundbank cache that holds a map of soundbanks.
	 * @since v1.1.2
	 */
	public static Map<String, Soundbank> getBankCache() {
        return bankCache;
    }

	/**
	 * @return The current MultiSynthesizer that holds a list of Synthesizers.
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

    /**
     * @return A double value between 0 and 1, representing the
     * load status of this class.
     */
    @Override
    public double getLoadStatus() {
        return loadStatus;
    }

    /**
     * Set the load status of the SoundfontLoader.
     * @param d A double value between 0 and 1 that represents the
     * load state of this class.
     */
    @Override
    public void setLoadStatus(double d) {
        if (d >= 0 && d <= 1)
            loadStatus = d;
    }

    /**
     * Plays a certain sound given a Note and some instrument.
     * @param n The Note to play
     * @param i The Instrument to play it with.
     */
    public static void playSound(Note n, InstrumentIndex i) {
        playSound(n.getKeyNum(), i, 0);
    }

    /**
     * Plays a certain sound given a Note and some instrument, along with the
     * accidental we are supposed to play it with.
     * @param i The note index we are supposed to play this note at.
     * @param theInd The InstrumentIndex.
     * @param acc The accidental that we are given.
     */
    public static void playSound(int i, InstrumentIndex theInd, int acc) {
        playSound(i, theInd, acc, Values.MAX_VELOCITY);
    }

    /**
     * Plays a certain sound given a Note and some instrument, along with the
     * accidental we are supposed to play it with and the volume with which we are
     * trying to play at.
     * @param i The note index we are supposed to play this note at.
     * @param theInd The InstrumentIndex.
     * @param acc The accidental that we are given.
     * @param vel The velocity of the note that we are given.
     */
    public static void playSound(int i, InstrumentIndex theInd, int acc, int vel) {
        int ind = theInd.getChannel() - 1;
        chan[ind].noteOn(i + acc, vel);
    }

    /**
     * Stops a certain sound given a Note and some instrument, along with the
     * accidental we are supposed to play it with and the volume with which we are
     * trying to play at.
     * @param i The note index we are supposed to play this note at.
     * @param theInd The InstrumentIndex.
     * @param acc The accidental that we are given.
     */
    public static void stopSound(int i, InstrumentIndex theInd, int acc) {
        int ind = theInd.getChannel() - 1;
        chan[ind].noteOff(i + acc);
    }

    /**
     * Stops a certain sound given a Note and some instrument, along with the
     * accidental we are supposed to play it with and the volume with which we are
     * trying to play at.
     * @param i The note index we are supposed to play this note at.
     * @param theInd The InstrumentIndex.
     * @param acc The accidental that we are given.
     * @param vel The note-off velocity.
     */
    public static void stopSound(int i, InstrumentIndex theInd, int acc, int vel) {
        int ind = theInd.getChannel() - 1;
        chan[ind].noteOff(i + acc, vel);
    }

}
