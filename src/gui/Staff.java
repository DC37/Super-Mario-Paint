package gui;

import java.io.File;
import java.io.IOException;
import java.io.StreamCorruptedException;
import java.text.ParseException;
import java.util.ArrayList;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiUnavailableException;

import backend.saving.mpc.MPCDecoder;
import backend.songs.StaffArrangement;
import backend.songs.StaffEvent;
import backend.songs.StaffNoteLine;
import backend.songs.StaffSequence;
import backend.songs.TimeSignature;
import backend.sound.SoundPlayer;
import gui.components.Controls;
import gui.components.staff.StaffDisplayManager;
import gui.loaders.SoundfontLoader;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.control.ListView;

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

    /**
     * The wrapper that holds a series of ImageView objects that are meant to
     * display the staff measure lines.
     */
    private StaffDisplayManager displayManager;

    /** This is the current sequence that we have displaying on the staff. */
    private StaffSequence theSequence = new StaffSequence();

    /** This is the location of the sequence that is currently displaying. */
    private File theSequenceFile = null;

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
    public Staff(SMPFXController ct, StaffDisplayManager display, ListView<String> arrList) {
        displayManager = display;
        setController(ct);
        theArrangementList = arrList;
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
     * Jump to the next colored line
     */
    public void jumpToNext() {
        int barLength = theSequence.getTimeSignature().barLength();
        int[] barDivs = theSequence.getTimeSignature().divs();
        int relativeLoc = StateMachine.getMeasureLineNum() % barLength;
        
        int subLength = 0;
        
        for (int i = 0; i < barDivs.length; i++) {
            subLength += barDivs[i];
            
            if (relativeLoc < subLength) {
                shift(subLength - relativeLoc);
                return;
            }
        }
    }
    
    public void jumpToPrevious() {
        int barLength = theSequence.getTimeSignature().barLength();
        int[] barDivs = theSequence.getTimeSignature().divs();
        int relativeLoc = StateMachine.getMeasureLineNum() % barLength;
        
        if (relativeLoc == 0)
            relativeLoc = barLength;
        
        int subLength = barLength;
        
        for (int i = barDivs.length; i > 0; i--) {
            subLength -= barDivs[i - 1];
            
            if (relativeLoc > subLength) {
                shift(subLength - relativeLoc);
                return;
            }
        }
    }

    /**
     * Jumps to a certain position on the staff.
     *
     * @param num
     *            The first measure line number (usually between 1 and 375) that
     *            is to be displayed.
     */
    public synchronized void setLocation(int num) {
        int maxVal = StateMachine.getMaxLine() - Values.NOTELINES_IN_THE_WINDOW;
        int newLoc = (num < 0) ? 0 : (num > maxVal) ? maxVal : num;
        StateMachine.setMeasureLineNum(newLoc);
    }
    
    /**
     * Equivalent to calling {@link setLocation} with argument 0, except that this
     * will force a redraw if the current line is already 0.
     */
    public synchronized void resetLocation() {
        StateMachine.setMeasureLineNum(-1);
        setLocation(0);
    }
    
    public void bumpStaff(int skipAmount) {
        if (StateMachine.isPlaybackActive())
            return;
        
        int currLoc = StateMachine.getMeasureLineNum();
        int newLoc = currLoc + skipAmount;
        
        // Deal with integer overflow
        if (skipAmount > 0 && newLoc < 0)
            newLoc = Integer.MAX_VALUE;
        
        if (skipAmount > 0 && currLoc + Values.NOTELINES_IN_THE_WINDOW == StateMachine.getMaxLine()) {
            int newSize = StateMachine.getMaxLine() + 2*Values.NOTELINES_IN_THE_WINDOW;
            theSequence.resize(newSize);
            StateMachine.setMaxLine(theSequence.getLength());
        }
        
        setLocation(newLoc);
    }
    
    public synchronized void setTimeSignature(TimeSignature t) {
        theSequence.setTimeSignature(t);
        StateMachine.setTimeSignature(t);
    }

    /**
     * Force re-draws the staff.
     */
    public synchronized void redraw() {
        int idx = StateMachine.getMeasureLineNum();
        if (idx == -1)
            return;
        
        int[] barDivs = StateMachine.getTimeSignature().divs();
        
        displayManager.updateNoteDisplay(theSequence, idx);
        displayManager.updateVolumeBars(theSequence, idx);
        displayManager.updateStaffMeasureLines(idx, barDivs);
        displayManager.updateStaffLedgerLines(theSequence, idx);
    }

    /** Begins animation of the Staff. (Starts a song) */
    public synchronized void startSong() {
        soundPlayer.setRun(true);
        songPlaying = true;
        setTempo(StateMachine.getTempo());
        animationService.restart();
    }

    /** Starts an arrangement. */
    public synchronized void startArrangement() {
        soundPlayer.setRun(true);
        ArrayList<StaffSequence> seq = theArrangement.getTheSequences();
        ArrayList<File> files = theArrangement.getTheSequenceFiles();
        resetLocation();
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
     * Imports a Mario Paint Composer song.
     */
    public void importMPCSong() {

    }

    /**
     * Imports an Advanced Mario Sequencer song.
     */
    public void importAMSSong() {

    }

    /** @return The current song that we are displaying. */
    public synchronized StaffSequence getSequence() {
        return theSequence;
    }

    /**
     * This loads a staff sequence.
     *
     * @param other
     *            This is the other sequence.
     */
    public synchronized void setSequence(StaffSequence other) {
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

    /** @return The current arrangement that we are displaying. */
    public ListView<String> getArrangementList() {
        return theArrangementList;
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
     * @return The wrapper that holds a series of ImageView objects that are
     *         meant to display the staff measure lines.
     */
    public StaffDisplayManager getDisplayManager() {
        return displayManager;
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
    
    public void addSongToArrangement() {
        if (StateMachine.isPlaybackActive())
            return;
    
        if ((Settings.debug & 0b100000) != 0)
            System.out.println("Add song");

        if (theSequenceFile == null)
            return;
    
        StateMachine.setArrModified(true);
        theArrangementList.getItems().add(StateMachine.getCurrentSongName());
        theArrangement.add(theSequence, theSequenceFile);
        controller.getSoundfontLoader().storeInCache();
    }
    
    public void deleteSongFromArrangement() {
        if (StateMachine.isPlaybackActive())
            return;
        
        if ((Settings.debug & 0b100000) != 0)
            System.out.println("Delete song");
        
        ObservableList<String> l = theArrangementList.getItems();
        int x = theArrangementList.getSelectionModel()
                .getSelectedIndex();
        if (x != -1) {
            StateMachine.setArrModified(true);
            theArrangement.remove(x);
            l.remove(x);
        }
    }
    
    public void moveSongInArrangement(int moveAmt) {
        if (StateMachine.isPlaybackActive())
            return;

        if ((Settings.debug & 0b100000) != 0)
            System.out.println("Move song " + moveAmt);
        
        ObservableList<String> l = theArrangementList.getItems();
        int x = theArrangementList.getSelectionModel()
                .getSelectedIndex();
        if (x != -1) {
            StateMachine.setArrModified(true);
            Object[] o = theArrangement.remove(x);
            String s = l.remove(x);
            StaffSequence ss = (StaffSequence) o[0];
            File f = (File) o[1];
            int moveTo = x - moveAmt;
            if (moveTo > l.size())
                moveTo = l.size();
            if (moveTo < 0)
                moveTo = 0;
            l.add(moveTo, s);
            theArrangement.add(moveTo, ss, f);
            theArrangementList.getSelectionModel()
            .select(moveTo);
        }
    }
    
    public void play() {
        switch (StateMachine.getMode()) {
        case SONG:
            startSong();
            break;
            
        case ARRANGEMENT:
            startArrangement();
            break;
        }
        
        StateMachine.setPlaybackActive(true);
    }
    
    public void stop() {
        stopSong();
        StateMachine.setPlaybackActive(false);
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

            @Override
            protected Staff call() throws Exception {
                int counter = StateMachine.getMeasureLineNum();
                boolean zero = false;
                int endLine = theSequence.getEndlineIndex();
                
                theSequence.normalize();
                StateMachine.setMaxLine(theSequence.getLength());

                queue = 0;
                
                while (songPlaying) {
                    displayManager.updatePlayBars(index);
                    
                    if (zero) {
                        resetLocation();
                        while (queue > 0);
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
                
                StateMachine.setPlaybackActive(false);
                return Staff.this;
            }

            /**
             * Plays the next line of notes in the queue. For
             * ease-of-programming purposes, we'll not care about efficiency and
             * just play things as they are.
             */
            protected void playNextLine() {
                int currentLoc = StateMachine.getMeasureLineNum();
                runUI(currentLoc, index, advance);
                
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
            private void runUI(final int currentLoc, final int index, final boolean advance) {
                // In principle it's not necessary to send this job to the FXAT,
                // but for some reason the program is more stable that way
                // Leaving things as they are until someone can figure it out --rozlyn
                Platform.runLater(new Runnable() {

                    @Override
                    public void run() {
                        if (advance) {
                            int loc = currentLoc + Values.NOTELINES_IN_THE_WINDOW;
                            setLocation(loc);
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

        }

        /**
         * This class runs an arrangement instead of just a song.
         */
        class ArrangementTask extends AnimationTask {

            @Override
            protected Staff call() throws Exception {
                StateMachine.setArrangementSongIndex(0);
                ArrayList<StaffSequence> seq = theArrangement.getTheSequences();
                ArrayList<File> files = theArrangement.getTheSequenceFiles();
                int endLine;
                
                for (StaffSequence s : seq)
                    s.normalize();

                queue = 0;
                
                for (int i = 0; i < seq.size(); i++) {
                    setSequence(seq.get(i));
                    setSoundset(theSequence.getSoundset());
                    setTempo(theSequence.getTempo());
                    setTimeSignature(theSequence.getTimeSignature());
                    endLine = theSequence.getEndlineIndex();
                    
                    StateMachine.setArrangementSongIndex(i);
                    StateMachine.setNoteExtensions(
                            theSequence.getNoteExtensions());
                    StateMachine.setTempo(theSequence.getTempo());
                    StateMachine.setMaxLine(theSequence.getLength());
                    
                    index = 0;
                    advance = false;
                    theSequenceFile = files.get(i);
                    controller.getInstBLine().updateNoteExtensions();
                    songPlaying = true;

                    resetLocation();
                    
                    int counter = 0;
                    
                    while (songPlaying && arrPlaying) {
                        displayManager.updatePlayBars(index);

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

                    /* Force emptying of queue before changing songs. */
                    while (queue > 0);
                }
                
                StateMachine.setPlaybackActive(false);
                return Staff.this;
            }
            
            /**
			 * Sets the soundset.
			 *
			 * @param soundset
			 *            The soundset.
			 * @since v1.1.2
			 */
            private void setSoundset(final String soundset) {
                if (!controller.getSoundfontLoader().loadFromCache(soundset)) {
                    try {
                        controller.getSoundfontLoader().loadFromAppData(soundset);
                    } catch (InvalidMidiDataException | IOException | MidiUnavailableException e) {
                        e.printStackTrace();
                    }
                    controller.getSoundfontLoader().storeInCache();
                }
			}
        }
    }
}
