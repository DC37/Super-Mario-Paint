package smp.components.staff;

import java.io.IOException;
import java.text.ParseException;

import javax.sound.midi.InvalidMidiDataException;

import smp.components.general.Utilities;
import smp.components.staff.sequences.Note;
import smp.components.staff.sequences.StaffNote;
import smp.components.staff.sequences.ams.AMSDecoder;
import smp.components.staff.sequences.mpc.MPCDecoder;
import smp.components.staff.sounds.SMPSequencer;



/**
 * The staff on which notes go. The staff keeps track of notes
 * in terms of discrete StaffNoteLines, placed inside an array.
 * @author RehdBlob
 * @since 2012.08.13
 */
public class Staff {

    /**
     * The wrapper that holds a series of ImageView objects that are meant to
     * display the staff measure lines.
     */
    private StaffImages staffImages;

    /**
     * The Sequencer object that will be used to play sounds.
     */
    private SMPSequencer seq;

    /**
     * Creates a new Staff object.
     */
    public Staff() {
        seq = new SMPSequencer();
        staffImages = new StaffImages();
    }

    /**
     * Puts a note on the Staff.
     * @param n The note to be added to the staff.
     */
    public void placeNote(StaffNote n) {

    }

    /**
     * Removes a note from the staff.
     * @param n The note to be removed from the staff.
     */
    public void eraseNote(StaffNote n) {

    }

    /**
     * Moves the staff and notes left by 1.
     */
    public void moveLeft() {

    }

    /**
     * Moves the staff and notes right by 1.
     */
    public void moveRight() {

    }

    /**
     * Shifts the staff left by <code>num</code> spaces.
     * @param num The number of spaces to shift left.
     */
    private void shiftLeft(int num) {

    }

    /**
     * Shifts the staff right by <code>num</code> spaces.
     * @param num The number of spaces to shift left.
     */
    private void shiftRight(int num) {

    }

    /**
     * Begins animation of the Staff.
     */
    public void startAnimation() {

    }


    /**
     * Stops animation of the Staff.
     */
    public void stopAnimation() {

    }

    /**
     * Loads a Super Mario Paint song.
     */
    public void loadSong() {

    }

    /**
     * Saves a Super Mario Paint song.
     */
    public void saveSong() {

    }

    /**
     * Imports a Mario Paint Composer song.
     */
    public void importMPCSong() {
        try {
            MPCDecoder.decode(Utilities.openFileDialog());
        } catch (NullPointerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvalidMidiDataException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * Imports an Advanced Mario Sequencer song.
     */
    public void importAMSSong() {
        try {
            AMSDecoder.decode(Utilities.openFileDialog());
        } catch (NullPointerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * Starts playing a Super Mario Paint Song.
     */
    public static void startSong() {

    }
}