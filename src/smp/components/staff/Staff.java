package smp.components.staff;

import java.util.ArrayList;

import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import smp.ImageIndex;
import smp.ImageLoader;
import smp.SoundfontLoader;
import smp.components.Values;
import smp.components.staff.sequences.StaffNote;
import smp.components.staff.sequences.StaffNoteLine;
import smp.components.staff.sequences.StaffSequence;
import smp.components.staff.sounds.SMPSequencer;
import smp.fx.SMPFXController;
import smp.stateMachine.Settings;
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

    /**
     * The wrapper that holds a series of ImageView objects that are meant to
     * display the staff measure lines.
     */
    private StaffImages staffImages;

    /** This holds the notes on the staff. */
    private NoteMatrix theMatrix;

    /** This keeps track of which notes are actually playing. */
    private NoteTracker tracker;

    /** This is the current sequence that we have displaying on the staff. */
    private StaffSequence theSequence;

    /**
     * The Sequencer object that will be used to play sounds.
     */
    private SMPSequencer seq;

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
        theMatrix = new NoteMatrix(Values.NOTELINES_IN_THE_WINDOW,
                Values.NOTES_IN_A_LINE, this);
        theSequence = new StaffSequence();
        staffImages = new StaffImages(staffExtLines);
        staffImages.setStaff(this);
        staffImages.initialize();
        animationService = new AnimationService();
        tracker = new NoteTracker();
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
     * Imports a Mario Paint Composer song.
     */
    public void importMPCSong() {

    }

    /**
     * Imports an Advanced Mario Sequencer song.
     */
    public void importAMSSong() {

    }


    /**
     * @return The note matrix of the staff that we are working with.
     */
    public NoteMatrix getNoteMatrix() {
        return theMatrix;
    }

    /**
     * @param other The new note matrix we want to load.
     */
    public void setNoteMatrix(NoteMatrix other) {
        theMatrix = other;
    }

    /**
     * @return The current song that we are displaying.
     */
    public StaffSequence getSequence() {
        return theSequence;
    }

    /**
     * This loads a staff sequence.
     * @param other This is the other sequence.
     */
    public void setSequence(StaffSequence other) {
        theSequence = other;
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
     * Sets the DoubleProperty value that we change to move the
     * staff.
     * @param d The DoubleProperty that we want to set.
     */
    public void setCurrVal(DoubleProperty d) {
        currVal = d;
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
                playBars.get(Values.NOTELINES_IN_THE_WINDOW - 1).setImage(
                        ImageLoader.getSpriteFX(ImageIndex.NONE));
                playBars.get(0).setImage(
                        ImageLoader.getSpriteFX(ImageIndex.PLAY_BAR1));
                if (advance) {
                    shiftStaff();
                }
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
                            + Values.NOTELINES_IN_THE_WINDOW);
                    if ((Settings.debug & 0b10000) == 0b10000)
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
             * This is the current measure line.
             */
            private int currMeasureLine = 0;

            /**
             * Whether we need to advance the satff ahead by a few lines or not.
             */
            private boolean advance = false;

            /** These are the play bars on the staff. */
            private ArrayList<ImageView> playBars;

            @Override
            protected Staff call() throws Exception {
                playBars = staffImages.getPlayBars();
                currMeasureLine = StateMachine.getMeasureLineNum();
                int counter = 0;
                do {
                    playNextLine();
                    counter++;
                    if (counter % Values.NOTELINES_IN_THE_WINDOW == 0)
                        currMeasureLine += Values.NOTELINES_IN_THE_WINDOW;
                    try {
                        Thread.sleep(delayMillis, delayNanos);
                    } catch (InterruptedException e) {
                        // Do nothing
                    }
                } while (songPlaying && currMeasureLine
                        <= Values.DEFAULT_LINES_PER_SONG);
                return null;
            }

            /**
             * Plays the next line of notes in the queue. For ease-of-programming
             * purposes, we'll not care about efficiency and just play things as
             * they are.
             */
            private void playNextLine() {
                if (StateMachine.getMeasureLineNum() >=
                        Values.DEFAULT_LINES_PER_SONG
                        - Values.NOTELINES_IN_THE_WINDOW) {
                    songPlaying = false;
                }
                bumpHighlights(playBars, index, advance);
                playSoundLine(index);
                advance = false;
                if (index < Values.NOTELINES_IN_THE_WINDOW - 1) {
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
            private void playSoundLine(final int index) {
                Platform.runLater(new Runnable() {

                    @Override
                    public void run() {
                        StaffNoteLine s =
                                theSequence.getLine(
                                        (int)(currVal.doubleValue() + index));
                        ArrayList<StaffNote> theNotes = s.getNotes();
                        for (StaffNote sn : theNotes) {
                            if (!sn.isMuteNote()) {
                                SoundfontLoader.playSound(
                                        Values.staffNotes[sn.getPosition()].getKeyNum(),
                                        sn.getInstrument(), sn.getAccidental());
                                tracker.addNotePlaying(
                                        Values.staffNotes[sn.getPosition()].getKeyNum(),
                                        sn.getInstrument(), sn.getAccidental());
                                tracker.setNoteOn(sn.getInstrument().getChannel());
                            } else {
                                SoundfontLoader.stopSound(
                                        Values.staffNotes[sn.getPosition()].getKeyNum(),
                                        sn.getInstrument(), sn.getAccidental());
                            }
                        }
                    }
                });

            }

        }
    }
}
