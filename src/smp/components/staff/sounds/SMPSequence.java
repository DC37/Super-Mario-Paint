package smp.components.staff.sounds;

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

    /**
     * @return theSequence - A sequence of MIDI events that this program can
     * read.
     */
    public Sequence getSequence() {
        return theSequence;
    }

    /**
     * @param seq A sequence that one wishes to set this Super Mario Paint
     * Sequence to hold.
     */
    public void setSequence(Sequence seq) {
        theSequence = seq;
    }

}
