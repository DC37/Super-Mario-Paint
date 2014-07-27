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
import smp.components.controls.Controls;
import smp.components.staff.sequences.StaffNote;
import smp.components.staff.sequences.StaffNoteLine;
import smp.components.staff.sequences.StaffSequence;
import smp.components.topPanel.PanelButtons;
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

    /** This is the last line of notes in the song. */
    private int lastLine;

    /** Whether we need to shift the staff by some amount. */
    private boolean shift = false;

    /** This is the current line that we are at. */
    private DoubleProperty currVal;

    /** These are the play button, stop button, etc. */
    private Controls theControls;

    /** This is the top panel of buttons. */
    private PanelButtons topPanel;

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

    /** Turns off all highlights in the play bars in the staff. */
    private void highlightsOff() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                ArrayList<ImageView> playBars = staffImages.getPlayBars();
                for(ImageView i : playBars) {
                    i.setImage(ImageLoader.getSpriteFX(
                            ImageIndex.NONE));
                }
            }
        });
    }


    /**
     * Begins animation of the Staff.
     */
    public void startSong() {
        highlightsOff();
        lastLine = findLastLine();
        if ((lastLine == 0 && theSequence.getLine(0).isEmpty())
                || (lastLine < StateMachine.getMeasureLineNum())) {
            theControls.getStopButton().reactPressed(null);
            return;
        }
        songPlaying = true;
        setTempo(StateMachine.getTempo());
        animationService.restart();
    }

    /**
     * Finds the last line in the sequence that we are playing.
     */
    private int findLastLine() {
        ArrayList<StaffNoteLine> lines = theSequence.getTheLines();
        for (int i = lines.size() - 1; i >= 0; i--)
            if (!lines.get(i).isEmpty()) {
                return i;
            }
        return 0;
    }

    /**
     * Stops the song that is currently playing.
     */
    public void stopSong() {
        songPlaying = false;
        animationService.cancel();
        animationService.reset();
        highlightsOff();
    }

    /**
     * Toggles arranger mode.
     * @param b True or false.
     */
    public void setArrangerMode(boolean b) {
        theControls.setArrangerMode(b);
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
     * @return The wrapper that holds a series of ImageView
     * objects that are meant to display the staff measure lines.
     */
    public StaffImages getStaffImages() {
        return staffImages;
    }

    /**
     * Sets the control panel for this staff.
     * @param s The control panel we want to set.
     */
    public void setControlPanel(Controls s) {
        theControls = s;
    }

    /**
     * @return The control panel.
     */
    public Controls getControlPanel() {
        return theControls;
    }

    /**
     * @param topPanel The top panel we want to set.
     */
    public void setTopPanel(PanelButtons t) {
        topPanel = t;
    }

    /**
     * @return The top panel.
     */
    public PanelButtons getTopPanel() {
        return topPanel;
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
        double mill = (60.0 / tempo) * 1000;
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

        /** Whether we have zeroed the staff or not. */
        private boolean zero = false;

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
            for (int i = 0; i < playBars.size(); i++)
                if (i != index)
                    playBars.get(i).setImage(
                            ImageLoader.getSpriteFX(ImageIndex.NONE));


            playBars.get(index).setImage(
                    ImageLoader.getSpriteFX(ImageIndex.PLAY_BAR1));
            if (advance && !zero)
                shiftStaff();
            if (zero)
                zero = false;

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
         * Sets the staff position to zero.
         */
        private void zeroStaff() {
            Platform.runLater(new Runnable() {

                @Override
                public void run() {
                    currVal.setValue(0);
                    if ((Settings.debug & 0b10000) != 0)
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
                int counter = StateMachine.getMeasureLineNum();
                do {
                    playNextLine();
                    counter++;
                    if (counter > lastLine && counter % 4 == 0) {
                        if (StateMachine.isLoopPressed()) {
                            counter = 0;
                            index = 0;
                            zeroStaff();
                            zero = true;
                        } else {
                            songPlaying = false;
                        }
                    }
                    try {
                        Thread.sleep(delayMillis, delayNanos);
                    } catch (InterruptedException e) {
                        // Do nothing
                    }
                } while (songPlaying);
                highlightsOff();
                hitStop();
                return theMatrix.getStaff();
            }


            /** Hits the stop button for the song. */
            private void hitStop() {
                theControls.getStopButton().reactPressed(null);
            }


            /**
             * Plays the next line of notes in the queue. For ease-of-programming
             * purposes, we'll not care about efficiency and just play things as
             * they are.
             */
            private void playNextLine() {
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
                        tracker.stopNotes(s);
                        for (StaffNote sn : theNotes) {
                            if (sn.muteNoteVal() == 1)
                                stopSound(sn);
                            else if (sn.muteNoteVal() == 2)
                                stopInstrument(sn);
                        }
                        for (StaffNote sn : theNotes) {
                            if (sn.muteNoteVal() == 0)
                                playSound(sn, s);
                        }
                    }

                    /**
                     * Plays a sound.
                     * @param sn The StaffNote.
                     * @param s The StaffNoteLine.
                     */
                    private void playSound(StaffNote sn, StaffNoteLine s) {
                        SoundfontLoader.playSound(
                                Values.staffNotes[sn.getPosition()].getKeyNum(),
                                sn.getInstrument(), sn.getAccidental(),
                                s.getVolume());
                        tracker.addNotePlaying(
                                Values.staffNotes[sn.getPosition()].getKeyNum(),
                                sn.getInstrument(), sn.getAccidental());
                    }

                    /**
                     * Stops a sound.
                     * @param sn The StaffNote.
                     */
                    private void stopSound(StaffNote sn) {
                        SoundfontLoader.stopSound(
                                Values.staffNotes[sn.getPosition()].getKeyNum(),
                                sn.getInstrument(), sn.getAccidental());
                    }

                    /**
                     * Stops a full set of instruments from playing sounds.
                     * @param sn The StaffNote.
                     */
                    private void stopInstrument(StaffNote sn) {
                        tracker.stopInstrument(sn);
                    }
                });

            }

        }
    }


}
