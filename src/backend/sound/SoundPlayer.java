package backend.sound;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Soundbank;

import backend.songs.Accidental;
import backend.songs.Pitch;
import backend.songs.StaffNote;
import backend.songs.StaffNoteLine;
import backend.songs.StaffSequence;
import gui.InstrumentIndex;
import gui.StateMachine;
import gui.Values;

/**
 *
 * A sound player thread that is spawned when one hits play. This thread's
 * purpose is to play sound on a non-JavaFX application thread, to hopefully
 * speed up the program's execution speed.
 *
 * @author RehdBlob
 * @since 2014.01.07
 *
 */
public class SoundPlayer {

    /**
     * The sound synthesizer used to hold as many instruments as needed.
     */
    private SMPSynthesizer synthesizer;

    /**
     * The MIDI channels associated with the MultiSynthsizer.
     */
    private MidiChannel [] chan;

    /**
     * The soundbank that will hold the sounds that we're trying to play.
     */
    private Soundbank bank;

	/**
	 * The cache that will contain all soundbanks. Each soundbank is indexed by
	 * its filename.
	 */
	private Map<String, Soundbank> bankCache = new HashMap<>();

    /** This keeps track of which notes are actually playing. */
    final private NoteTracker tracker = new NoteTracker(this);
    
    public SoundPlayer(SMPSynthesizer synthesizer, Soundbank bank, MidiChannel[] chan) {
    	this.synthesizer = synthesizer;
    	this.bank = bank;
    	this.chan = chan;
    }

    /**
     * Plays the current line of notes.
     */
    public void playSoundLine(StaffSequence seq, int index) {
        StaffNoteLine s = seq.getLineSafe(StateMachine.getMeasureLineNum() + index);
        ArrayList<StaffNote> theNotes = s.getNotes();
        tracker.stopNotes(s);
        for (StaffNote sn : theNotes) {
            if (sn.muteNoteVal() == 1)
                stopSound(sn);
            else if (sn.muteNoteVal() == 2)
                stopInstrument(sn);
        }
        for (StaffNote sn : theNotes) {
            if (sn.muteNoteVal() == 0)
                playSound(sn, s.getVolume());
        }
    }

    /**
     * Plays a sound.
     *
     * @param sn
     *            The StaffNote.
     * @param s
     *            The StaffNoteLine.
     */
    public void playSound(StaffNote sn, int vel) {
        playSound(Values.staffNotes[sn.getPosition()].getKeyNum(),
                sn.getInstrument(), sn.getAccidental(), vel);
        tracker.addNotePlaying(Values.staffNotes[sn.getPosition()].getKeyNum(),
                sn.getInstrument(), sn.getAccidental());
    }

    /**
     * Stops a sound.
     *
     * @param sn
     *            The StaffNote.
     */
    private void stopSound(StaffNote sn) {
        stopSound(Values.staffNotes[sn.getPosition()].getKeyNum(),
                sn.getInstrument(), sn.getAccidental());
    }

    /**
     * Stops a full set of instruments from playing sounds.
     *
     * @param sn
     *            The StaffNote.
     */
    private void stopInstrument(StaffNote sn) {
        tracker.stopInstrument(sn);
    }

    
    /**
     * Turn off all sounds.
     */
    public void stopAllInstruments() {
    	for (InstrumentIndex in : InstrumentIndex.values()) {
    		tracker.stopInstrument(new StaffNote(in, 0, Accidental.NATURAL));
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
	private void loadSoundfont(String path) throws InvalidMidiDataException, IOException, MidiUnavailableException {
		File f = new File(path);
		if(f.getName().isEmpty())
			return;
		if (!f.getName().equals(StateMachine.getCurrentSoundset())) {
			bank = MidiSystem.getSoundbank(f);
			synthesizer.loadAllInstruments(bank);
			StateMachine.setCurrentSoundset(f.getName());
		}
	}
	
	/**
	 * Loads the passed-in filename from AppData.
	 * 
	 * @param soundset
	 *            The soundfont name
	 * @throws InvalidMidiDataException
	 * @throws IOException
	 * @throws MidiUnavailableException
	 * @since v1.1.2
	 */
	public void loadFromAppData(String soundset) throws InvalidMidiDataException, IOException, MidiUnavailableException {
		//TODO: check linux or mac, choose platform-specific folder
		if(soundset.isEmpty())
			return;
		loadSoundfont(Values.SOUNDFONTS_FOLDER + soundset);
	}
	
	/**
	 * Stores the current soundbank in cache for quick loading. 
	 * 
	 * @since v1.1.2
	 */
	public void storeInCache() {
		String currentSoundset = StateMachine.getCurrentSoundset();
		if(!bankCache.containsKey(currentSoundset))
			bankCache.put(currentSoundset, bank);
	}
	
	/**
	 * Loads the soundset from AppData and stores the soundbank in cache for
	 * quick loading. This will not change the program's current soundbank.
	 * 
	 * @param soundset
	 *            The soundfont name
	 * @throws IOException 
	 * @throws InvalidMidiDataException 
	 * @since v1.1.2
	 */
	public void loadToCache(String soundset) throws InvalidMidiDataException, IOException {
		//TODO: check linux or mac, choose platform-specific folder
		if(soundset.isEmpty())
			return;
		File f = new File(Values.SOUNDFONTS_FOLDER + soundset);
		if(!bankCache.containsKey(soundset)) {
			Soundbank sb = MidiSystem.getSoundbank(f);
			bankCache.put(soundset, sb);
		}
	}

	/**
	 * Loads the Soundbank with the soundset name from bankCache and sets it as
	 * the program's MultiSynthesizer's current soundfont. This is the fast way
	 * to set the program's soundfont.
	 * 
	 * @param soundset
	 *            The soundset name (e.g. soundset3.sf2)
	 * @return if program's current soundset successfully set to soundset
	 * @since v1.1.2
	 */
	public boolean loadFromCache(String soundset) {
		if(StateMachine.getCurrentSoundset().equals(soundset))
			return true;
		if(bankCache.containsKey(soundset)) {
			bank = bankCache.get(soundset);
			synthesizer.loadAllInstruments(bank);
			StateMachine.setCurrentSoundset(soundset);
			return true;
		}
		return false;
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
     * @return An Array of references for MidiChannel objects needed to
     * play sounds.
     */
    public MidiChannel[] getChannels() {
        return chan;
    }

    public void playSound(Pitch n, InstrumentIndex i) {
        playSound(n.getKeyNum(), i, Accidental.NATURAL);
    }

    public void playSound(int i, InstrumentIndex theInd, Accidental acc) {
        playSound(i, theInd, acc, Values.MAX_VELOCITY);
    }

    public void playSound(int i, InstrumentIndex theInd, Accidental acc, int vel) {
        int ind = theInd.getChannel() - 1;
        chan[ind].noteOn(i + acc.getOffset(), vel);
    }

    public void stopSound(int i, InstrumentIndex theInd, Accidental acc) {
        int ind = theInd.getChannel() - 1;
        chan[ind].noteOff(i + acc.getOffset());
    }

}
