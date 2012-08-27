package smp.components.staff.sounds;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.sound.midi.Instrument;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Patch;
import javax.sound.midi.Receiver;
import javax.sound.midi.Soundbank;
import javax.sound.midi.Synthesizer;
import javax.sound.midi.Transmitter;
import javax.sound.midi.VoiceStatus;

/**
 * Skeleton class for multiple synthesizers in one class.
 * Should make it simpler to call synthesizers with more than
 * 16 MIDI channels. One can add multiple different types of
 * Synthesizers into this class because of the abstraction
 * that was implemented; this class all Synthesizer type
 * objects.
 * @author RehdBlob
 * @since 2012.08.24
 */
public class MultiSynthesizer implements Synthesizer {

	/**
	 * Indicates whether this Synthesizer is actually initialized
	 * or not. Many methods will not work if this is not <b>True</b>.
	 */
	private boolean initialized;
	
	/**
	 * The list of synthesizers that this class holds.
	 */
	private ArrayList<Synthesizer> theSynths;
	
	/**
	 * Initializes the ArrayList of Synthesizers.
	 */
	public MultiSynthesizer() {
		theSynths = new ArrayList<Synthesizer>();
		initialized = true;
	}
	
	/**
	 * Add a synthesizer to the list of synthesizers for this
	 * aggregate class.
	 * @param s1 The Synthesizer to be added.
	 * @throws MidiUnavailableException If the MultiSynthesizer isn't
	 * initialized.
	 */
	public void addSynths(Synthesizer s1) throws MidiUnavailableException {
		if (!initialized)
			throw new MidiUnavailableException();
		theSynths.add(s1);
	}
	
	/**
	 * Adds synthesizers to the list of synthesizers for this
	 * aggregate class.
	 * @param s1 The first Synthesizer to be added.
	 * @param s2 The second Synthesizer to be added.
	 * @throws MidiUnavailableException If the MultiSynthesizer is not
	 * initialized.
	 */
	public void addSynths(Synthesizer s1, Synthesizer s2) 
			throws MidiUnavailableException {
		if (!initialized)
			throw new MidiUnavailableException();
		theSynths.add(s1);
		theSynths.add(s2);
	}
	
	/**
	 * Adds synthesizers to the list of synthesizers for this
	 * aggregate class.
	 * @param s1 The first Synthesizer to be added.
	 * @param s2 The second Synthesizer to be added.
	 * @param s As many more Synthesizer objects as needed.
	 * @throws MidiUnavailableException If the MultiSynthesizer is not
	 * initialized.
	 */
	public void addSynths(Synthesizer s1, Synthesizer s2, Synthesizer... s) 
			throws MidiUnavailableException {
		if (!initialized)
			throw new MidiUnavailableException();
		theSynths.add(s1);
		theSynths.add(s2);
		for (Synthesizer more : s)
			theSynths.add(more);
	}

	/**
	 * Adds the default Synthesizer into the list of Synthesizers.
	 * @throws MidiUnavailableException If the MidiSystem.getSynthesizer()
	 * call fails.
	 */
	public void addDefaultSynthesizer() throws MidiUnavailableException {
		if (!initialized)
			throw new MidiUnavailableException();
		theSynths.add(MidiSystem.getSynthesizer());
	}
	
	/**
	 * Gets the device info of the first Synthesizer in the Synthesizer list.
	 */
	@Override
	public Info getDeviceInfo() {
		return theSynths.get(0).getDeviceInfo();
	}

	/**
	 * Opens all of the Synthesizers in the list.
	 * @throws MidiUnavailableException If for some reason any of the .open()
	 * calls doesn't work, or if the MultiSynthesizer is not initialized.
	 */
	@Override
	public void open() throws MidiUnavailableException {
		if (!initialized)
			throw new MidiUnavailableException();
		for (Synthesizer s : theSynths)
		    s.open();
	}

	/**
	 * Closes all of the Synthesizers in the Synthesizer list.
	 */
	@Override
	public void close() {
		for (Synthesizer s : theSynths)
			s.close();
	}

	/**
	 * Checks if all of the Synthesizers are open.
	 */
	@Override
	public boolean isOpen() {
		for (Synthesizer s : theSynths)
			if (!s.isOpen())
				return false;
		return true;
	}

	/**
	 * Gets the microsecond position of the first Synthesizer in the 
	 * Synthesizer list.
	 */
	@Override
	public long getMicrosecondPosition() {
		return theSynths.get(0).getMicrosecondPosition();
	}

	/**
	 * @return The maximum number of receivers that the Synthesizers in the
	 * list can hold, determined by the Synthesizer that can hold the least
	 * number of Receivers.
	 */
	@Override
	public int getMaxReceivers() {
		ArrayList<Integer> numbers = new ArrayList<Integer>();
		for (Synthesizer s : theSynths)
			numbers.add(s.getMaxReceivers());
		Collections.sort(numbers);
		return numbers.get(numbers.size() - 1);
	}

	/**
	 * @return The maximum number of Transmitters that the Synthesizers in the
	 * list can hold, determined by the Synthesizer that can hold the least
	 * number of Transmitters.
	 */
	@Override
	public int getMaxTransmitters() {
		ArrayList<Integer> numbers = new ArrayList<Integer>();
		for (Synthesizer s : theSynths)
			numbers.add(s.getMaxTransmitters());
		Collections.sort(numbers);
		return numbers.get(numbers.size() - 1);
	}

	/**
	 * @return The Receiver of the first Synthesizer in the list of
	 * Synthesizers. Not useful in the context of multiple Synthesizers.
	 */
	@Deprecated
	@Override
	public Receiver getReceiver() throws MidiUnavailableException {
		return theSynths.get(0).getReceiver();
	}

	/**
	 * @return All of the Receivers of all of the Synthesizers in the list
	 * of Synthesizers.
	 */
	@Override
	public List<Receiver> getReceivers() {
		ArrayList<Receiver> all = new ArrayList<Receiver>();
		for (Synthesizer s : theSynths)
			all.addAll(s.getReceivers());
		return all;
	}
	
	/**
	 * @return the Transmitter of the first Synthesizer in the list of
	 * Synthesizers. Not useful in the context of multiple Synthesizers.
	 * @throws MidiUnavailableException If the MultiSynthesizer is not open
	 * or if something goes wrong in getting the Trasnmitter.
	 */
	@Deprecated
	@Override
	public Transmitter getTransmitter() throws MidiUnavailableException {
		if (!initialized)
			throw new MidiUnavailableException();
		return theSynths.get(0).getTransmitter();
	}

	/**
	 * @return All of the Transmitters of all of the Synthesizers in the list
	 * of Synthesizers.
	 */
	@Override
	public List<Transmitter> getTransmitters() {
		ArrayList<Transmitter> all = new ArrayList<Transmitter>();
		for (Synthesizer s : theSynths)
			all.addAll(s.getTransmitters());
		return all;
	}

	/**
	 * @return The maximum polyphony of the set of Synthesizers,
	 * determined by the Synthesizer with the minimum polyphony.
	 */
	@Override
	public int getMaxPolyphony() {
		ArrayList<Integer> nums = new ArrayList<Integer>();
		for (Synthesizer s : theSynths) 
			nums.add(s.getMaxPolyphony());
		Collections.sort(nums);
		return nums.get(nums.size() - 1);
	}

	/**
	 * @return The latency of the set of Synthesizers,
	 * determined by the Synthesizer with the highest latency. 
	 */
	@Override
	public long getLatency() {
		ArrayList<Long> nums = new ArrayList<Long>();
		for (Synthesizer s : theSynths) 
			nums.add(s.getLatency());
		Collections.sort(nums);
		return nums.get(0);
	}

	/**
	 * @return An array of MidiChannel objects. Elements 0-15 are from the first
	 * synthesizer, elements 16-31 are from the second synthesizer, and so on.
	 */
	@Override
	public MidiChannel[] getChannels() {
		MidiChannel[] all = theSynths.get(0).getChannels();
		if (theSynths.size() > 1) {
			for (Synthesizer s : theSynths) {
				MidiChannel[] temp = s.getChannels();
				MidiChannel[] concat = 
						new MidiChannel[all.length + temp.length];
				for (int i = 0; i < concat.length; i++) {
					if (i < all.length)
					    concat[i] = all[i];
					else
						concat[i] = temp[i-all.length];
				}
				all = concat;
			}
		}
		return all;
	}

	
	/**
	 * @return the VoiceStatus of all of the Synthesizer objects.
	 */
	@Override
	public VoiceStatus[] getVoiceStatus() {
		VoiceStatus[] all = theSynths.get(0).getVoiceStatus();
		if (theSynths.size() > 1) {
			for (Synthesizer s : theSynths) {
				VoiceStatus[] temp = s.getVoiceStatus();
				VoiceStatus[] concat = 
						new VoiceStatus[all.length + temp.length];
				for (int i = 0; i < concat.length; i++) {
					if (i < all.length)
					    concat[i] = all[i];
					else
						concat[i] = temp[i-all.length];
				}
				all = concat;
			}
		}
		return all;
	}

	/**
	 * @return <b>True</b> if the given Soundbank is supported by ALL of
	 * the Synthesizers in the list of Synthesizers, <b>False</b> otherwise.
	 */
	@Override
	public boolean isSoundbankSupported(Soundbank soundbank) {
		for (Synthesizer s : theSynths)
			if (!isSoundbankSupported(soundbank))
				return false;
		return true;
	}

	/**
	 * Loads instruments into all of the Synthesizers in the list.
	 * @return <b>True</b> if the Instrument is successfully loaded into
	 * ALL of the Synthesizers in the list of Synthesizers, <b>False</b>
	 * otherwise.
	 */
	@Override
	public boolean loadInstrument(Instrument instrument) {
		boolean isLoadedCorrectly = true;;
		for (Synthesizer s : theSynths)
			if (!s.loadInstrument(instrument))
				isLoadedCorrectly = false;;
		return isLoadedCorrectly;
	}

	/**
	 * Unloads the specified instrument from ALL of the Synthesizers in the
	 * list of Synthesizers.
	 */
	@Override
	public void unloadInstrument(Instrument instrument) {
		for (Synthesizer s : theSynths)
			s.unloadInstrument(instrument);
	}

	/**
	 * @return <b>True</b> if the Instrument remapping was successful in ALL
	 * of the Synthesizer objects in the Synthesizer list, <b>False</b>
	 * otherwise.
	 */
	@Override
	public boolean remapInstrument(Instrument from, Instrument to) {
		boolean didItWork = true;
		for (Synthesizer s : theSynths)
			if (!s.remapInstrument(from, to))
				didItWork = false;
		return didItWork;
	}

	/**
	 * @return The default soundbank of the first Synthesizer element in
	 * the list of Synthesizers.
	 */
	@Override
	public Soundbank getDefaultSoundbank() {
		return theSynths.get(0).getDefaultSoundbank();
	}

	/**
	 * Theoretically, the available instruments in all of the
	 * Synthesizers should be equivalent, but that may not be the case.
	 * Will implement an intersection algorithm sometime, but for now,
	 * we will just keep it simple.
	 * @return The available instruments of the first Synthesizer object
	 * in the list of Synthesizers.
	 */
	@Override
	public Instrument[] getAvailableInstruments() {
		return theSynths.get(0).getAvailableInstruments();
	}

	/**
	 * At some point, an intersection algorithm will determine the
	 * common loaded instruments in all of the Synthesizers in the list.
	 * For now, it will be kept simple.
	 * @return The loaded instruments of the first Synthesizer object in
	 * the list of Synthesizers.
	 */
	@Override
	public Instrument[] getLoadedInstruments() {
		return theSynths.get(0).getLoadedInstruments();
	}

	/**
	 * Loads all of the instruments of a Soundbank into ALL of the Synthesizers
	 * in the list of Synthesizers.
	 * @return <b>True</b> if the load was successful, <b>False</b> otherwise.
	 */
	@Override
	public boolean loadAllInstruments(Soundbank soundbank) {
		boolean success = true;
		for (Synthesizer s : theSynths)
			if (!s.loadAllInstruments(soundbank))
				success = false;
		return success;
	}

	/**
	 * Unloads all of the instruments of a Soundbank from ALL of the
	 * Synthesizers in the list of Synthesizers.
	 */
	@Override
	public void unloadAllInstruments(Soundbank soundbank) {
		for (Synthesizer s : theSynths)
			s.unloadAllInstruments(soundbank);
	}

	/**
	 * Loads the specified instruments from a soundbank into ALL of the
	 * Synthesizers in the list of Synthesizers.
	 * @return <b>True</b> if it succeeded, <b>False</b> otherwise.
	 */
	@Override
	public boolean loadInstruments(Soundbank soundbank, Patch[] patchList) {
		boolean success = true;
		for (Synthesizer s : theSynths)
			if (!s.loadInstruments(soundbank, patchList))
				success = false;
		return success;
	}

	/**
	 * Unloads the specified instruments from ALL of the Synthesizers in the
	 * list of Synthesizers.
	 */
	@Override
	public void unloadInstruments(Soundbank soundbank, Patch[] patchList) {
		for (Synthesizer s : theSynths)
			s.unloadInstruments(soundbank, patchList);
	}
}