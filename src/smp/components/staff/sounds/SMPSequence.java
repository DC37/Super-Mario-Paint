package smp.components.staff.sounds;

import java.util.ArrayList;
import java.util.LinkedList;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.Sequence;

/**
 * A Super Mario Paint song or arranger sequence.
 * @author RehdBlob
 * @since 2012.09.01
 */
public class SMPSequence {

    /**
     * The number of tracks in a normal Super Mario Paint sequence.
     */
    private static final int NUMTRACKS = 19;

    /**
     * The number of tracks in an advanced Super Mario Paint sequence.
     */
    private static final int NUMTRACKSADV = 50;

    /**
     * List of things that happen during the course of some sequence.
     */
    private LinkedList<MidiEvent> sequenceEvents;

    /**
     * The Sequence type that this class wraps.
     */
    private Sequence theSequence;

    /**
     * @param numTracks The number of tracks we want this sequence to hold.
     * @throws InvalidMidiDataException If for some reason the Sequence
     * initialization fails.
     */
    public SMPSequence() throws InvalidMidiDataException {
        setSequence(new Sequence(Sequence.PPQ, 4 , NUMTRACKS));
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


    /**
     * Sets the time signature of this sequence to the String that is passed
     * to this method.
     * @param timeSig The time signature that we are trying to set the sequence
     * to.
     */
    public void setTime(String timeSig) {
        // TODO: Actually code this
    }

    /**
     * Sets the tempo of this song to whatever we input.
     * @param tempo The tempo that we are trying to set the song to.
     */
    public void setTempo(String t) {
        int baseline = Integer.parseInt(t);
        double d = baseline / 4;
        // TODO: Actually code this
    }

    /**
     * Populates this sequence's data with the events that have supposedely
     * been decoded from either a Mario Paint Composer or Advanced Mario
     * Sequencer file.
     * @param data The ArrayList of MidiData that we wish to send to this
     * sequence.
     */
    public void setData(ArrayList<MidiEvent> data) {
        populateSequence(data);
        // TODO: Actually code this
    }

    /**
     * Populates the sequence with the events stored in the LinkedList
     * <code>sequenceEvents</code>.
     */
    public void populateSequence() {
        for (MidiEvent m : sequenceEvents){
            // TODO: Actually add something here.
        }

    }

    /**
     * Populates the sequence with the events given in some list of MidiEvents.
     * @param data The list of MidiEvents.
     */
    public void populateSequence(ArrayList<MidiEvent> data) {
        // TODO: Actually code this.
    }

}
