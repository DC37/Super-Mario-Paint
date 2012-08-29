package smp.components.staff.sounds;

import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiUnavailableException;

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
	 * ability of that channel to play any sound.
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

}
