package smp.components.staff.sounds;

import java.util.ArrayList;

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
     * ability of that channel to play any sound, since Channel 10 is
     * reserved for percussion
     */
    @Override
    public MidiChannel[] getChannels() {
        MidiChannel[] oldC = super.getChannels();
        ArrayList<MidiChannel> rem = new ArrayList<MidiChannel>();
        int ordinal = 1;
        for (MidiChannel m : oldC) {
            if (ordinal == 10) {
                ordinal++;
                continue;
            } else {
                rem.add(m);
                ordinal++;
                if (ordinal > 16)
                    ordinal = 1;
            }
        }
        MidiChannel[] ret = new MidiChannel[rem.size()];
        for (int i = 0; i < rem.size(); i++)
            ret[i] = rem.get(i);
        return ret;
    }

}
