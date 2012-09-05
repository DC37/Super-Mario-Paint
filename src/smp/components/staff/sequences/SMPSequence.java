package smp.components.staff.sequences;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.Sequence;

/**
 * A Super Mario Paint song or arranger sequence.
 * @author RehdBlob
 * @since 2012.09.01
 */
public class SMPSequence {

    /**
     * The Sequence type that this class wraps.
     */
    private Sequence theSequence;

    /**
     * @param numTracks The number of tracks we want this sequence to hold.
     * @throws InvalidMidiDataException If for some reason the Sequence
     * initialization fails.
     */
    public SMPSequence(int numTracks) throws InvalidMidiDataException {
        setSequence(new Sequence(Sequence.PPQ, 4 , numTracks));
    }

    public Sequence getSequence() {
        return theSequence;
    }

    public void setSequence(Sequence seq) {
        theSequence = seq;
    }

}
