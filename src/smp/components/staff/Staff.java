package smp.components.staff;

import java.io.IOException;
import java.text.ParseException;

import javafx.scene.layout.HBox;
import javax.sound.midi.InvalidMidiDataException;

import smp.components.general.Utilities;
import smp.components.staff.sequences.StaffNote;
import smp.components.staff.sequences.ams.AMSDecoder;
import smp.components.staff.sequences.mpc.MPCDecoder;
import smp.components.staff.sounds.SMPSequence;
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
    private static StaffImages staffImages;

    /**
     * The Sequencer object that will be used to play sounds.
     */
    private static SMPSequencer seq;

    /** The song that we are currently editing. */
    private static SMPSequence currentSong;

    /**
     * Creates a new Staff object.
     * @param staffPlayBars The bars that show when a line of notes is played.
     * @param staffMLines These are the lines where notes are placed.
     * @param staffInstruments The instrument image boxes that hold the
     * instruments that should appear on the staff.
     * @param staffAccidentals These are the sharps and flats that you can
     * place.
     * @param staffExtLines These are the lines that appear under notes for the
     * lower and upper portions of the staff.
     * @param staffMeasureNumbers The container for the staff measure numbers.
     */
    public Staff(HBox staffMLines, HBox staffPlayBars, HBox staffInstruments,
            HBox staffAccidentals, HBox[] staffExtLines,
            HBox staffMeasureNumbers) {
        seq = new SMPSequencer();
        try {
            currentSong = new SMPSequence();
        } catch (InvalidMidiDataException e) {
            // Do nothing
            e.printStackTrace();
        }
        staffImages = new StaffImages(staffMLines, staffPlayBars,
                staffInstruments, staffAccidentals, staffExtLines,
                staffMeasureNumbers);
        staffImages.draw();
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
        shiftLeft(1);
    }

    /**
     * Moves the staff and notes right by 1.
     */
    public void moveRight() {
        shiftRight(1);
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
            currentSong = MPCDecoder.decode(Utilities.openFileDialog());
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
            currentSong = AMSDecoder.decode(Utilities.openFileDialog());
        } catch (NullPointerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * Starts playing a Super Mario Paint song.
     */
    public static void startSong() {

    }

    /**
     * Stops playing a Super Mario Paint song.
     */
    public static void stopSong() {

    }

    /**
     * Draws the staff.
     */
    public void draw() {

    }

    /**
     * Does a refresh-draw of the staff.
     */
    public void redraw() {

    }
}