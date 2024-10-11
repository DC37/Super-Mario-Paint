package smp.components.staff;

import java.io.File;
import java.io.IOException;
import java.io.StreamCorruptedException;
import java.text.ParseException;
import java.util.ArrayList;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiUnavailableException;

import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import smp.ImageIndex;
import smp.ImageLoader;
import smp.SoundfontLoader;
import smp.components.InstrumentIndex;
import smp.components.Values;
import smp.components.controls.Controls;
import smp.components.general.Utilities;
import smp.components.staff.sequences.StaffArrangement;
import smp.components.staff.sequences.StaffEvent;
import smp.components.staff.sequences.StaffNoteLine;
import smp.components.staff.sequences.StaffSequence;
import smp.components.staff.sequences.mpc.MPCDecoder;
import smp.components.topPanel.PanelButtons;
import smp.fx.SMPFXController;
import smp.stateMachine.StateMachine;

/**
 * The staff on which notes go. The staff keeps track of notes in terms of
 * discrete StaffNoteLines, placed inside an array. This class also keeps track
 * of the animation of the staff when one hits the play button.
 *
 * @author RehdBlob
 * @author j574y923
 * @author Cynagen
 * @author seymour
 * @since 2012.08.13
 */
public class Staff {

    /** Milliseconds to delay between updating the play bars. */
    private long delayMillis;

    /** Nanoseconds to delay in addition to the milliseconds delay. */
    private int delayNanos;

    /** Whether we are playing a song. */
    private boolean songPlaying = false;

    /** Whether we are playing an arrangement. */
    private boolean arrPlaying = false;

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

    /** This is the name of the song that we are currently editing. */
    private String theSequenceName = "";

    /** This is the current sequence that we have displaying on the staff. */
    private StaffSequence theSequence = new StaffSequence();

    /** This is the location of the sequence that is currently displaying. */
    private File theSequenceFile = null;

    /** This is the name of the arrangement that we are currently editing. */
    private String theArrangementName = "";

    /** This is the current arrangement that we currently have active. */
    private StaffArrangement theArrangement = new StaffArrangement();

    /** This is the ListView that holds the current arrangement. */
    private ListView<String> theArrangementList;

    /**
     * This is the location of the arrangement file that is currently
     * displaying.
     */
    private File theArrangementFile = null;

    /** The FXML controller class. */
    private SMPFXController controller;

    /** The image loader class. */
    private ImageLoader il;

    /** This is the SoundPlayer object that we will invoke to set parameters. */
    private SoundPlayer soundPlayer;

    /**
     * This is a service that will help run the animation and sound of playing a
     * song.
     */
    private AnimationService animationService;

    /**
     * Creates a new Staff object.
     *
     * @param staffLLines
     *            These are the ledger lines that appear under notes for the
     *            lower and upper portions of the staff.
     * @param arrList
     *            This is the arrangement list object that displays song names,
     *            which is actually a <code>ListView</code> object.
     */
    public Staff(HBox[] staffLLines, SMPFXController ct, ImageLoader i,
            ListView<String> arrList) {
        theMatrix = new NoteMatrix(Values.NOTELINES_IN_THE_WINDOW,
                Values.NOTES_IN_A_LINE, this, i);
        setImageLoader(i);
        setController(ct);
        setArrangementList(arrList);
        staffImages = new StaffImages(i);
        staffImages.setStaff(this);
        staffImages.setController(ct);
        staffImages.setLedgerLines(staffLLines);
        staffImages.initialize();
        animationService = new AnimationService();
        soundPlayer = new SoundPlayer(this);
    }

    /**
     * Sets the controller class.
     *
     * @param ct
     *            The FXML controller class.
     */
    public void setController(SMPFXController ct) {
        controller = ct;
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
     *
     * @param num
     *            The number of spaces to shift. Positive values indicate an
     *            increasing measure number.
     */
    public void shift(int num) {
        setLocation(num + StateMachine.getMeasureLineNum());
    }

    /**
     * Jumps to a certain position on the staff.
     *
     * @param num
     *            The first measure line number (usually between 1 and 375) that
     *            is to be displayed.
     */
    public synchronized void setLocation(int num) {
        theMatrix.redraw();
        Slider s = controller.getScrollbar();
        s.adjustValue(num);
    }

    /**
     * Force re-draws the staff.
     */
    public synchronized void redraw() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                theMatrix.redraw();
            }
        });
    }

    /** Turns off all highlights in the play bars in the staff. */
    private void highlightsOff() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                ArrayList<ImageView> playBars = staffImages.getPlayBars();
                for (ImageView i : playBars) {
                    i.setVisible(false);
                }
            }
        });
    }

    /** Begins animation of the Staff. (Starts a song) */
    public synchronized void startSong() {
    	theControls.getPlayButton().reactPressed(null); // Presses the play button when starting the song. - seymour
        soundPlayer.setRun(true);
        highlightsOff();
        songPlaying = true;
        setTempo(StateMachine.getTempo());
        animationService.restart();
    }

    /** Starts an arrangement. */
    public synchronized void startArrangement() {
    	theControls.getPlayButton().reactPressed(null); // Presses the play button when starting the song. - seymour
        soundPlayer.setRun(true);
        ArrayList<StaffSequence> seq = theArrangement.getTheSequences();
        ArrayList<File> files = theArrangement.getTheSequenceFiles();
        setLocation(0);
        for (int i = 0; i < seq.size(); i++) {
            File f = files.get(i);
            try {
                seq.set(i, Utilities.loadSong(f));
            } catch (StreamCorruptedException | NullPointerException e) {
                try {
                    seq.set(i, MPCDecoder.decode(f));
                } catch (ParseException | IOException e1) {
                    e1.printStackTrace();
                    stopSong();
                    return;
                }
            } catch (IOException e) {
                e.printStackTrace();
                stopSong();
                return;
            }
        }
        arrPlaying = true;
        animationService.restart();
    }

    /**
     * Hits the stop button.
     */
    public void hitStop() {
        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                theControls.getStopButton().reactPressed(null);
            }
        });
    }

    /** Stops the song that is currently playing. */
    public void stopSong() {
        soundPlayer.setRun(false);
        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                songPlaying = false;
                arrPlaying = false;
                animationService.cancel();
                switch (animationService.getState()) {
                case CANCELLED:
                case FAILED:
                case READY:
                case SUCCEEDED:
                    animationService.reset();
                    break;
                default:
                    break;
                }
                highlightsOff();
            }
        });
    }
    

    /**
     * Stop all sounds on the staff.
     */
	public void stopSounds() {
		soundPlayer.stopAllInstruments();
		MidiChannel [] chan = SoundfontLoader.getChannels();
		for (int ind = 0; ind < InstrumentIndex.values().length; ind++) {
	        if (chan[ind] != null) {
	            chan[ind].allSoundOff();
	        }
		}
	}
    
    /**
     * Toggles arranger mode.
     *
     * @param b
     *            True or false.
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
     * @param other
     *            The new note matrix we want to load.
     */
    public void setNoteMatrix(NoteMatrix other) {
        theMatrix = other;
    }

    /** @return The current song that we are displaying. */
    public StaffSequence getSequence() {
        return theSequence;
    }

    /**
     * This loads a staff sequence.
     *
     * @param other
     *            This is the other sequence.
     */
    public void setSequence(StaffSequence other) {
        theSequence = other;
    }

    /**
     *
     * @param other
     *            This is the location of the file that the sequence that we are
     *            currently editing is located at. Usually, this is set by a
     *            <code>save</code> operation, but could be set using other
     *            methods or operations.
     */
    public void setSequenceFile(File other) {
        theSequenceFile = other;
    }

    /**
     * @return This is the location of the sequence that we are currently
     *         editing. It is <b>null</b> if it hasn't been saved yet.
     */
    public File getSequenceFile() {
        return theSequenceFile;
    }

    /**
     * @return The name of the sequence that we are currently editing.
     */
    public String getSequenceName() {
        return theSequenceName;
    }

    /**
     * @param s
     *            The name we want to set.
     */
    public void setSequenceName(String s) {
        theSequenceName = s;
    }

    /** @return The current arrangement that we are displaying. */
    public ListView<String> getArrangementList() {
        return theArrangementList;
    }

    /**
     * Sets the ListView object for the arrangement list.
     *
     * @param arrangementList
     *            <code>ListView</code> object that we want to set.
     */
    public void setArrangementList(ListView<String> arrangementList) {
        theArrangementList = arrangementList;
    }

    /**
     * @return The list of songs in the currently-active arrangement.
     */
    public StaffArrangement getArrangement() {
        return theArrangement;
    }

    /**
     * @param tA
     *            The arrangement file to set.
     */
    public void setArrangement(StaffArrangement tA) {
        theArrangement = tA;
    }

    /**
     * @return The list of arrangement files.
     */
    public File getArrangementFile() {
        return theArrangementFile;
    }

    /**
     * @param tAF
     *            The list of arrangement files to set.
     */
    public void setArrangementFile(File tAF) {
        theArrangementFile = tAF;
    }

    /**
     * @return The name of the arrangement that we are currently editing.
     */
    public String getArrangementName() {
        return theArrangementName;
    }

    /**
     * @param s
     *            The arrangement name that we want to set.
     */
    public void setArrangementName(String s) {
        theArrangementName = s;
    }

    /**
     * @return The wrapper that holds a series of ImageView objects that are
     *         meant to display the staff measure lines.
     */
    public StaffImages getStaffImages() {
        return staffImages;
    }

    /**
     * Sets the control panel for this staff.
     *
     * @param s
     *            The control panel we want to set.
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
     * @param topPanel
     *            The top panel we want to set.
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
     * @param acc
     *            The offset that we are deciding upon.
     * @return An <code>ImageIndex</code> based on the amount of sharp or flat
     *         we want to implement.
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
     * @param tempo
     *            The tempo we want to set this staff to run at, in BPM. Beats
     *            per minute * 60 = beats per second <br>
     *            Beats per second ^ -1 = seconds per beat <br>
     *            Seconds per beat * 1000 = Milliseconds per beat <br>
     *            (int) Milliseconds per beat = Milliseconds <br>
     *            Milliseconds per beat - milliseconds = remainder <br>
     *            (int) (Remainder * 1e6) = Nanoseconds <br>
     *
     */
    public void setTempo(double tempo) {
        double mill = (60.0 / tempo) * 1000;
        delayMillis = (int) mill;
        double nano = (mill - delayMillis) * Math.pow(10, 6);
        delayNanos = (int) nano;
    }

    /**
     * @return The Staff's Image Loader
     */
    public ImageLoader getImageLoader() {
        return il;
    }

    /**
     * @param i
     *            The Image Loader to set.
     */
    public void setImageLoader(ImageLoader i) {
        il = i;
    }
    
    
   /**
     * 
     * @param index The index to do events from
     */
    private void doEvents(int index) {
        StaffNoteLine s = getSequence().getLineSafe(
                StateMachine.getMeasureLineNum() + index);
        ArrayList<StaffEvent> theEvents = s.getEvents();
        for (StaffEvent e : theEvents) {
            e.doEvent(this);
        }
    }
    

    /**
     * This is a worker thread that helps run the animation on the staff.
     */
    class AnimationService extends Service<Staff> {

        /** Number of lines queued up to play. */
        protected volatile int queue = 0;

        @Override
        protected Task<Staff> createTask() {
            if (!arrPlaying)
                return new AnimationTask();
            else
                return new ArrangementTask();
        }

        /**
         * This class keeps track of animation and sound. Note to self: While
         * running a service or a task, crashes do not print stack traces.
         */
        class AnimationTask extends Task<Staff> {

            /**
             * This is the current index of the measure line that we are on on
             * the staff.
             */
            protected int index = 0;

            /**
             * Whether we need to advance the staff ahead by a few lines or not.
             */
            protected boolean advance = false;

            /** These are the play bars on the staff. */
            protected ArrayList<ImageView> playBars;

            /**
             * Zeros the staff to the beginning point. Use only at the beginning
             * of a new song file.
             */
            protected void zeroEverything() {
                Platform.runLater(new Runnable() {

                    @Override
                    public void run() {
                        setLocation(0);
                        StateMachine.setMeasureLineNum(0);
                        playBars.get(0).setVisible(true);
                        for (int i = 1; i < playBars.size(); i++)
                            playBars.get(i).setVisible(false);
                        queue--;
                    }

                });
            }

            @Override
            protected Staff call() throws Exception {
                playBars = staffImages.getPlayBars();
                int counter = StateMachine.getMeasureLineNum();
                boolean zero = false;
                int endLine = theSequence.getEndlineIndex();
                
                theSequence.normalize();
                
                while (songPlaying) {
                    if (zero) {
                        queue++;
                        zeroEverything();
                        while (queue > 0)
                            ;
                        zero = false;
                    }
                    queue++;
                    playNextLine();
                    counter++;
                    if (counter >= endLine) {
                        if (StateMachine.isLoopPressed()) {
                            counter = 0;
                            index = 0;
                            advance = false;
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
                }
                highlightsOff();
                hitStop();
                return theMatrix.getStaff();
            }

            /**
             * Plays the next line of notes in the queue. For
             * ease-of-programming purposes, we'll not care about efficiency and
             * just play things as they are.
             */
            protected void playNextLine() {
                runUI(playBars, index, advance);
                
                advance = index >= Values.NOTELINES_IN_THE_WINDOW - 1;
                index = advance ? 0 : (index + 1);
            }

            /**
             * Bumps the highlight of the notes to the next play bar.
             *
             * @param playBars
             *            The list of the measure highlights.
             * @param index
             *            The current index of the measure that we're on.
             * @param advance
             *            Whether we need to move the staff by some bit.
             */
            private void runUI(final ArrayList<ImageView> playBars,
                    final int index, final boolean advance) {
                Platform.runLater(new Runnable() {

                    @Override
                    public void run() {
                        bumpHighlights(playBars, index);
                        if (advance) {
                            int loc = StateMachine.getMeasureLineNum()
                                    + Values.NOTELINES_IN_THE_WINDOW;
                            setLocation(loc);
                            StateMachine.setMeasureLineNum(loc);
                        }
                        doEvents(index);
                        playSoundLine(index);
                        queue--;
                    }
                });
            }            

            /**
             * Plays a sound line at the index specified. Or rather, tells the
             * SoundPlayer thread to do that.
             *
             * @param index
             *            The index to play.
             */
            private void playSoundLine(int index) {
                soundPlayer.playSoundLine(index);
            }

            /**
             * Bumps the highlights on the staff by a certain amount.
             *
             * @param playBars
             *            The list of playbar objects
             * @param index
             *            The index that we are currently at
             */
            private void bumpHighlights(ArrayList<ImageView> playBars,
                    int index) {
                playBars.get(index).setVisible(true);
                for (int i = 0; i < playBars.size(); i++)
                    if (i != index)
                        playBars.get(i).setVisible(false);

            }

        }

        /**
         * This class runs an arrangement instead of just a song.
         */
        class ArrangementTask extends AnimationTask {

            @Override
            protected Staff call() throws Exception {
                highlightsOff();
                ArrayList<StaffSequence> seq = theArrangement.getTheSequences();
                ArrayList<File> files = theArrangement.getTheSequenceFiles();
                int endLine;
                
                for (StaffSequence s : seq)
                    s.normalize();
                
                for (int i = 0; i < seq.size(); i++) {
                    while (queue > 0)
                        ;
                    /* Force emptying of queue before changing songs. */
                    queue++;
                    setSoundset(seq.get(i).getSoundset());
                    index = 0;
                    advance = false;
                    queue++;
                    highlightSong(i);
                    theSequence = seq.get(i);
                    theSequenceFile = files.get(i);
                    StateMachine.setNoteExtensions(
                            theSequence.getNoteExtensions());
                    controller.getInstBLine().updateNoteExtensions();
                    StateMachine.setTempo(theSequence.getTempo());
                    queue++;
                    setScrollbar();
                    endLine = seq.get(i).getEndlineIndex();
                    songPlaying = true;
                    setTempo(theSequence.getTempo());
                    playBars = staffImages.getPlayBars();
                    int counter = 0;
                    StateMachine.setMeasureLineNum(0);
                    queue++;
                    zeroEverything();
                    while (queue > 0)
                        ;
                    /* Force operations to complete before starting a song. */
                    while (songPlaying && arrPlaying) {
                        queue++;
                        playNextLine();
                        counter++;
                        if (counter >= endLine) {
                            songPlaying = false;
                        }
                        try {
                            Thread.sleep(delayMillis, delayNanos);
                        } catch (InterruptedException e) {
                            // Do nothing
                        }
                    }
                    if (!arrPlaying)
                        break;
                }
                highlightsOff();
                hitStop();
                return theMatrix.getStaff();
            }
            
            /**
			 * Sets the soundset.
			 *
			 * @param soundset
			 *            The soundset.
			 * @since v1.1.2
			 */
			private void setSoundset(final String soundset) {
				Platform.runLater(new Runnable() {

					@Override
					public void run() {
						if (!controller.getSoundfontLoader().loadFromCache(soundset)) {
							try {
								controller.getSoundfontLoader().loadFromAppData(soundset);
							} catch (InvalidMidiDataException | IOException | MidiUnavailableException e) {
								e.printStackTrace();
							}
							controller.getSoundfontLoader().storeInCache();
						}
						queue--;
					}
				});
			}

            /**
             * Highlights the currently-playing song in the arranger list.
             *
             * @param i
             *            The index to highlight.
             */
            private void highlightSong(final int i) {
                Platform.runLater(new Runnable() {

                    @Override
                    public void run() {
                        theArrangementList.getSelectionModel().select(i);
                        setSequenceName(theArrangementList.getSelectionModel().getSelectedItem());
                        theArrangementList.scrollTo(i);
                        queue--;
                    }

                });
            }

            /** Sets the scrollbar max/min to the proper values. */
            private void setScrollbar() {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        theControls.getScrollbar().setMax(
                                theSequence.getLength()
                                        - Values.NOTELINES_IN_THE_WINDOW);
                        queue--;
                    }
                });
            }
        }
    }
}
