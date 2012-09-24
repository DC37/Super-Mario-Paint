package smp.components.staff;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import smp.components.general.Utilities;
import smp.components.staff.sequences.Note;
import smp.components.staff.sequences.StaffNote;
import smp.components.staff.sequences.ams.AMSDecoder;
import smp.components.staff.sequences.ams.AMSException;
import smp.components.staff.sequences.mpc.MPCDecoder;
import smp.components.staff.sequences.mpc.MPCException;
import smp.components.staff.sounds.SMPSequencer;



/**
 * The staff on which notes go. The way that the Staff keeps track of
 * notes is that there is a matrix
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
     * Adds a note to the staff.
     * @param n The note to be added to the staff.
     */
    public void addNote(Note n) {

    }

    /**
     * Puts a note on the Staff.
     * @param n The note to be added to the staff.
     */
    public void placeNote(Note n) {

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
     * @throws MPCException If for some reason the load fails.
     * @throws IOException
     * @throws NullPointerException
     */
    public void importMPCSong() throws NullPointerException, IOException {
        MPCDecoder.decode(Utilities.openFileDialog());
    }

    /**
     * Imports an Advanced Mario Sequencer song.
     * @throws AMSException If for some reason the load fails.
     * @throws IOException
     * @throws NullPointerException
     */
    public void importAMSSong() throws NullPointerException, IOException {
        AMSDecoder.decode(Utilities.openFileDialog());
    }
}