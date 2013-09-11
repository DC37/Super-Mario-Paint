package smp.components.staff;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javax.sound.midi.InvalidMidiDataException;

import smp.ImageIndex;
import smp.ImageLoader;
import smp.components.Constants;
import smp.components.general.Utilities;
import smp.components.staff.sequences.StaffSequence;
import smp.components.staff.sequences.ams.AMSDecoder;
import smp.components.staff.sequences.mpc.MPCDecoder;
import smp.components.staff.sounds.SMPSequence;
import smp.components.staff.sounds.SMPSequencer;
import smp.stateMachine.StateMachine;

/**
 * The staff on which notes go. The staff keeps track of notes
 * in terms of discrete StaffNoteLines, placed inside an array.
 * @author RehdBlob
 * @since 2012.08.13
 */
public class Staff implements Runnable {

    /** Whether we are playing a song. */
    private boolean songPlaying = false;

    /**
     * The wrapper that holds a series of ImageView objects that are meant to
     * display the staff measure lines.
     */
    private StaffImages staffImages;

    /**
     * This is the backend portion of the staff, responsible for keeping track
     * of all of the different positions of notes and sequences.
     */
    private StaffBackend staffBackend;

    /** This holds the notes on the staff. */
    private NoteMatrix theMatrix;

    /** This is the current sequence that we have displaying on the staff. */
    private StaffSequence theSequence;

    /**
     * The Sequencer object that will be used to play sounds.
     */
    private SMPSequencer seq;

    /** The song that we are currently editing. */
    private SMPSequence currentSong;

    /**
     * Creates a new Staff object.
     * @param staffExtLines These are the lines that appear under notes for the
     * lower and upper portions of the staff.
     */
    public Staff(HBox[] staffExtLines) {
        seq = new SMPSequencer();
        theMatrix = new NoteMatrix(Constants.NOTELINES_IN_THE_WINDOW,
                Constants.NOTES_IN_A_LINE, this);
        staffBackend = new StaffBackend();
        try {
            currentSong = new SMPSequence();
        } catch (InvalidMidiDataException e) {
            // Do nothing
            e.printStackTrace();
        }
        theSequence = new StaffSequence();
        staffImages = new StaffImages(staffExtLines);
        staffImages.setStaff(this);
        staffImages.initialize();

    }


    /**
     * Moves the staff and notes left by 1.
     */
    public void moveLeft() {
        shift(-1);
    }

    /**
     * Moves the staff and notes right by 1.
     */
    public void moveRight() {
        shift(1);
    }

    /**
     * Shifts the staff by <code>num</code> spaces.
     * @param num The number of spaces to shift. Positive
     * values indicate an increasing measure number.
     */
    public void shift(int num) {
        setLocation(num + StateMachine.getMeasureLineNum());
    }

    /**
     * Jumps to a certain position on the staff.
     * @param num The first measure line number (usually between 1
     * and 375) that is to be displayed.
     */
    public synchronized void setLocation(int num) {
        for(int i = 0; i < Constants.NOTELINES_IN_THE_WINDOW; i++)
            theMatrix.redraw(i);
        staffImages.updateStaffMeasureLines();
    }

    /**
     * Force re-draws the staff.
     */
    public synchronized void redraw() {
        setLocation(StateMachine.getMeasureLineNum());
    }

    /**
     * Begins animation of the Staff.
     * Fun fact, this is the first time I've actually had a real use for a
     * do-while loop.
     */
    public void startAnimation() {
        Thread runner = new Thread(this);
        runner.start();
    }

    @Override
    public void run() {
        ArrayList<ImageView> playBars = staffImages.getPlayBars();
        int index = 0;
        ImageView current = playBars.get(0);
        ImageView previous = null;
        songPlaying = true;
        do {
            playNextLine();
            if (index + 1 > playBars.size()) {
                index = 0;
            }
            bumpHighlights(playBars, current, previous, index++);
        } while(songPlaying);
    }

    /**
     * Plays the next line of notes in the queue. For ease-of-programming
     * purposes, we'll not care about efficiency and just play things as
     * they are.
     */
    private void playNextLine() {
        if (StateMachine.getMeasureLineNum() >=
                Constants.DEFAULT_LINES_PER_SONG
                - Constants.NOTELINES_IN_THE_WINDOW) {
            songPlaying = false;
        }
    }

    /**
     * Bumps the highlight of the notes to the next play bar.
     * @param playBars The list of the measure highlights.
     * @param current The current measure line that we're at.
     * @param previous The previous measure line that we were at.
     * @param index The current index of the measure that we're on.
     */
    private void bumpHighlights(ArrayList<ImageView> playBars,
            ImageView current, ImageView previous, int index) {
        previous = current;
        previous.setImage(ImageLoader.getSpriteFX(ImageIndex.NONE));
        current = playBars.get(index);
        current.setImage(ImageLoader.getSpriteFX(ImageIndex.PLAY_BAR1));
        for(int i = 0; i < Integer.MAX_VALUE; i++);

    }


    /**
     * Stops animation of the Staff.
     */
    public void stopAnimation() {
        songPlaying = false;
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
     * @return The note matrix of the staff that we are working with.
     */
    public NoteMatrix getNoteMatrix() {
        return theMatrix;
    }

    /**
     * @return The staff backend controller.
     */
    public StaffBackend getStaffBackend() {
        return staffBackend;
    }

    /**
     * @return The current song that we are displaying.
     */
    public StaffSequence getSequence() {
        return theSequence;
    }

    /**
     * @param acc The offset that we are deciding upon.
     * @return An <code>ImageIndex</code> based on the amount of
     * sharp or flat we want to implement.
     */
    public static ImageIndex switchAcc(int acc) {
        switch (acc) {
        case 2:
            return ImageIndex.DOUBLESHARP;
        case 1:
            return ImageIndex.SHARP;
        case 0:
            return ImageIndex.BLANK;
        case -1:
            return ImageIndex.FLAT;
        case -2:
            return ImageIndex.DOUBLEFLAT;
        default:
            return ImageIndex.BLANK;
        }
    }

}