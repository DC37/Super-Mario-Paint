package smp.components.staff.sounds;

import javax.sound.midi.Instrument;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;

import com.sun.media.sound.SoftSynthesizer;

/**
 * The Mario Paint Synthesizer class. Holds multiple SoftSynthesizers
 * like the MultiSynthesizer class, but happens to skip channel 10 because
 * that channel for some reason can't play regular notes.
 * @author RehdBlob
 * @since 2012.08.28
 */
public class MPSynthesizer extends MultiSynthesizer {

	/**
	 * Initializes the <code>MPSynthesizer</code> just like the
	 * <code>MultiSynthesizer</code>.
	 * @see MultiSynthesizer
	 */
	public MPSynthesizer() throws MidiUnavailableException {
		super();
	}
	
	/**
	 * @return An array of MidiChannels that happens to not contain 
	 * Channel 10 of any Synthesizer object, since that would break the
	 * ability of that channel to play any sound, since Channel 10 is 
	 * reserved for percussion
	 */
	@Override
	public MidiChannel[] getChannels() {
		MidiChannel[] oldC = super.getChannels();
		int newSize = theSynths.size() * 15;
		MidiChannel[] newC = new MidiChannel[newSize];
		int ordinal = 0;
		for (int i = 0; i < oldC.length; i++) {
			// Skip MidiChannel 10
			if ((i + 1) % 16 == 10) {
				continue;
			} else {
				newC[ordinal] = oldC[i];
				ordinal++;
			}
		}
		return newC;
	}
	
	/**
	 * Ensures capacity in a program-specific manner. Adds SoftSynthesizers
	 * to <code>theSynths</code> until there are definitely enough Synthesizers
	 * to handle all the required channels.
	 */
	@Override
	public void ensureCapacity(int i) throws MidiUnavailableException {
		if (!initialized)
			throw new MidiUnavailableException();
		int oldSize = theSynths.size();
		int repeat = (int) Math.floor((double)i / 15);
		for (int j = 0; j < repeat; j++)
			theSynths.add(new SoftSynthesizer());
		for (int j = oldSize - 1; j < theSynths.size(); j++) {
			Synthesizer s = theSynths.get(j);
			s.open();
			for (Instrument inst : this.getLoadedInstruments()) 
				s.loadInstrument(inst);
		}
	}

}
