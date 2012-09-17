package smp.components.staff;

import java.util.ArrayList;

import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import smp.components.staff.sequences.Note;
import smp.components.staff.sequences.SMPSequencer;
import smp.components.staff.sequences.StaffNote;
import smp.components.staff.sequences.ams.AMSException;
import smp.components.staff.sequences.mpc.MPCDecoder;
import smp.components.staff.sequences.mpc.MPCException;



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
     * @param n
     */
    public void addNote(Note n) {

    }

    /**
     * Puts a note on the Staff.
     */
    public void placeNote() {

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
     */
    public void importMPCSong() throws MPCException {

    }

    /**
     * Imports an Advanced Mario Sequencer song.
     * @throws AMSException If for some reason the load fails.
     */
    public void importAMSSong() throws AMSException {

    }


}