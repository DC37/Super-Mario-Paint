package smp.components.staff.sounds;

import java.util.ArrayList;

import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;

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
		// TODO: Debug!
		ArrayList<MidiChannel> all = new ArrayList<MidiChannel>();
		if (theSynths.size() == 1) 
			return theSynths.get(0).getChannels();
		else {
			for (Synthesizer s : theSynths) {
				MidiChannel[] temp = s.getChannels();
				for (MidiChannel m : temp)
					all.add(m);
			}
		}
		MidiChannel[] ret = new MidiChannel[all.size() - theSynths.size()];
		int ord = 0;
		for (int i = 0; i < all.size(); i++) {
			ret[ord] = m;
			ord++;
		}
		return ret;
	}

}
