package smp.components.staff;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableDoubleValue;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javax.sound.midi.InvalidMidiDataException;

import smp.ImageIndex;
import smp.ImageLoader;
import smp.components.controls.Controls;
import smp.components.Constants;
import smp.components.general.Utilities;
import smp.components.staff.sequences.StaffSequence;
import smp.components.staff.sequences.ams.AMSDecoder;
import smp.components.staff.sequences.mpc.MPCDecoder;
import smp.components.staff.sounds.SMPSequence;
import smp.components.staff.sounds.SMPSequencer;
import smp.fx.SMPFXController;
import smp.stateMachine.StateMachine;

/**
 * The staff on which notes go. The staff keeps track of notes
 * in terms of discrete StaffNoteLines, placed inside an array.
 * @author RehdBlob
 * @since 2012.08.13
 */
public class Staff {

    /** Milliseconds to delay between updating the play bars. */
    private long delayMillis;

    /** Nanoseconds to delay in addition to the milliseconds delay. */
    private int delayNanos;

    /** Whether we are playing a song. */
    private boolean songPlaying = false;

    /** Whether we need to shift the staff by some amount. */
    private boolean shift = false;

    /** This is the current line that we are at. */
    private DoubleProperty currVal;

    /** This is the control panel of the program. */
    private Controls controls;

    /** This is the slider that runs at the bottom of the screen. */
    private Slider tempoScrollbar;

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
     * This is a service that will help run the animation and sound of playing a
     * song.
     */
    private AnimationService animationService;


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
        animationService = new AnimationService();
        initializeBoundSlider();
    }

    /**
     * Sets up the bound value between the slider value and the current
     * staff value.
     */
    private void initializeBoundSlider() {
        currVal = new SimpleDoubleProperty();
        currVal.set(0);
        currVal.addListener(new ChangeListener<Number>() {

            @Override
            public void changed(ObservableValue<? extends Number> arg0,
                    Number oldVal, Number newVal) {

                try {
                    SMPFXController.getControls().getScrollbar().valueProperty().setValue(
                            currVal.getValue());
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        });
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
        theMatrix.redraw();
        Slider s = SMPFXController.getScrollbar();
        s.adjustValue(num);
    }


    /**
     * Force re-draws the staff.
     */
    public synchronized void redraw() {
        setLocation(StateMachine.getMeasureLineNum());
    }


    /**
     * Begins animation of the Staff.

     */
    public void startSong() {
        songPlaying = true;
        setTempo(StateMachine.getTempo());
        animationService.start();
    }

    /**
     * Stops the song that is currently playing.
     */
    public void stopSong() {
        songPlaying = false;
        for (ImageView i : staffImages.getPlayBars()) {
            i.setImage(ImageLoader.getSpriteFX(ImageIndex.NONE));
        }
        animationService.cancel();
        animationService.reset();
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
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (InvalidMidiDataException e) {
            e.printStackTrace();
        } catch (IOException e) {
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
            e.printStackTrace();
        } catch (IOException e) {
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
     * @return The staff images.
     */
    public StaffImages getStaffImages() {
        return staffImages;
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

    /**
     * @param tempo The tempo we want to set this staff to run at,
     * in BPM.
     * Beats per minute * 60 = beats per second <br>
     * Beats per second ^ -1 = seconds per beat <br>
     * Seconds per beat * 1000 = Milliseconds per beat <br>
     * (int) Milliseconds per beat = Milliseconds <br>
     * Milliseconds per beat - milliseconds = remainder <br>
     * (int) (Remainder * 1e6) = Nanoseconds <br>
     * 
     */
    public void setTempo(double tempo) {
        double mill = (1 / (tempo / 60.0)) * 1000;
        delayMillis = (int) mill;
        double nano = (mill - delayMillis) * Math.pow(10, 6);
        delayNanos = (int) nano;
    }

    /**
     * This is a worker thread that helps run the animation on the staff.
     */
    class AnimationService extends Service<Staff> {

        @Override
        protected Task<Staff> createTask() {
            return new AnimationTask();
        }

        /**
         * Bumps the highlight of the notes to the next play bar.
         * @param playBars The list of the measure highlights.
         * @param index The current index of the measure that we're on.
         * @param advance Whether we need to move the staff by some bit.
         */
        private void bumpHighlights(ArrayList<ImageView> playBars, int index,
                boolean advance) {
            if (index == 0) {
                playBars.get(Constants.NOTELINES_IN_THE_WINDOW - 1).setImage(
                        ImageLoader.getSpriteFX(ImageIndex.NONE));
                playBars.get(0).setImage(
                        ImageLoader.getSpriteFX(ImageIndex.PLAY_BAR1));
                if (advance)
                    shiftStaff();
            } else {
                playBars.get(index - 1).setImage(ImageLoader.getSpriteFX(
                        ImageIndex.NONE));
                playBars.get(index).setImage(
                        ImageLoader.getSpriteFX(ImageIndex.PLAY_BAR1));
            }
        }

        /**
         * Shifts the staff over by the number of note lines in the window.
         */
        private void shiftStaff() {
            Platform.runLater(new Runnable() {

                @Override
                public void run() {
                    currVal.setValue(currVal.getValue().doubleValue()
                            + Constants.NOTELINES_IN_THE_WINDOW);
                    System.out.println(currVal);
                }

            });

        }


        /**
         * This class keeps track of animation and sound. Note to self: While
         * running a service or a task, crashes do not print stack traces.
         * Therefore, debug like crazy!
         * Fun fact: This is the first time that I've used a do-while loop in
         * practice!
         */
        class AnimationTask extends Task<Staff> {

            /**
             * This is the current index of the measure line that we are on
             * on the staff.
             */
            private int index = 0;

            /**
             * Whether we need to advance the satff ahead by a few lines or not.
             */
            private boolean advance = false;

            /** These are the play bars on the staff. */
            private ArrayList<ImageView> playBars;

            @Override
            protected Staff call() throws Exception {
                playBars = staffImages.getPlayBars();
                do {
                    playNextLine();
                    try {
                        Thread.sleep(delayMillis, delayNanos);
                    } catch (InterruptedException e) {
                        // Do nothing
                    }
                } while (songPlaying);
                return null;
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
                bumpHighlights(playBars, index, advance);
                advance = false;
                playSoundLine(index);
                if (index < Constants.NOTELINES_IN_THE_WINDOW - 1) {
                    index++;
                } else {
                    index = 0;
                    advance = true;
                }
            }

            /**
             * Plays the current line of notes.
             * Lol, inefficiency - called every time a note line is played.
             */
            private void playSoundLine(int index) {

            }

        }
    }

}