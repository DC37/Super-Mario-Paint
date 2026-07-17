package gui;

import java.io.IOException;
import java.util.List;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiUnavailableException;

import backend.songs.StaffArrangement;
import backend.songs.StaffSequence;
import backend.songs.TimeSignature;
import backend.sound.SoundPlayer;
import gui.components.staff.StaffDisplayManager;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.stage.Window;
import utilities.MathUtils;

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

    /** Whether we are playing a song. */
    private boolean songPlaying = false;

    /** Whether we are playing an arrangement. */
    private boolean arrPlaying = false;

    /**
     * The wrapper that holds a series of ImageView objects that are meant to
     * display the staff measure lines.
     */
    private StaffDisplayManager displayManager;

    /** This is the current sequence that we have displaying on the staff. */
    private StaffSequence theSequence = new StaffSequence();

    /** This is the current arrangement that we currently have active. */
    private StaffArrangement theArrangement = new StaffArrangement();

    /** This is the SoundPlayer object that we will invoke to set parameters. */
    private final SoundPlayer soundPlayer;

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
    public Staff(StaffDisplayManager display, SoundPlayer soundPlayer) {
        displayManager = display;
        animationService = new AnimationService();
        this.soundPlayer = soundPlayer;
    }
    
    public SoundPlayer getSoundPlayer() {
        return soundPlayer;
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
        int maxVal = Math.max(StateMachine.getMaxLine() - Values.NOTELINES_IN_THE_WINDOW, 0);
        int newLoc = MathUtils.clamp(num, 0, maxVal);
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
            StateMachine.setMaxLine(Math.max(newSize, Values.DEFAULT_LINES_PER_SONG));
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
        songPlaying = true;
        animationService.restart();
    }

    /** Starts an arrangement. */
    public synchronized void startArrangement() {
        resetLocation();
        arrPlaying = true;
        animationService.restart();
    }

    /** Stops the song that is currently playing. */
    public void stopSong() {
        Platform.runLater(() -> {
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
        });
    }
    

    /**
     * Stop all sounds on the staff.
     */
    public void stopSounds() {
        soundPlayer.stopAllInstruments();
        MidiChannel [] chan = soundPlayer.getChannels();
        for (int ind = 0; ind < InstrumentIndex.values().length; ind++) {
            if (chan[ind] != null) {
                chan[ind].allSoundOff();
            }
        }
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
     * @return The wrapper that holds a series of ImageView objects that are
     *         meant to display the staff measure lines.
     */
    public StaffDisplayManager getDisplayManager() {
        return displayManager;
    }

    /**
     * Populates the staff with the sequence given. Sets the program's soundfont
     * to the sequence's.
     *
     * @param loaded
     *            The loaded sequence.
     */
    public void populateStaff(StaffSequence loaded) {
        setSequence(loaded);
        setTimeSignature(loaded.getTimeSignature());
        StateMachine.setTempo(loaded.getTempo());
        StateMachine.setMaxLine(Math.max(loaded.getLength(), Values.DEFAULT_LINES_PER_SONG));
        resetLocation();
        StateMachine.setCurrentSongName(loaded.getTitle());
        StateMachine.setNoteExtensions(loaded.getNoteExtensions());
        
        try {
            getSoundPlayer().loadFromAppData(getSequence().getSoundset());
        } catch (InvalidMidiDataException | IOException | MidiUnavailableException e) {
            e.printStackTrace();
        }
    }

    /**
     * Populates the staff with the arrangement given. Loads each soundfont
     * from each song in the arrangement into cache.
     *
     * @param loaded
     *            The loaded arrangement.
     */
    public void populateStaffArrangement(StaffArrangement loaded, Window owner) {
    	StaffSequence first = loaded.getSequences().getFirst();
        populateStaff(first);
        
        setArrangement(loaded);
        StateMachine.setCurrentArrangementName(loaded.getTitle());
        
        Task<Void> soundsetsTaskUtilities = new Task<Void>() {
            @Override
            public Void call() {
                getSoundPlayer().clearCache();
                for(StaffSequence s : loaded.getSequences()) {
                    try {
                        getSoundPlayer().loadToCache(s.getSoundset());
                    } catch (InvalidMidiDataException | IOException e) {
                        e.printStackTrace();
                    }
                }
                return null;
            }
        };
        new Thread(soundsetsTaskUtilities).start();
        
        try {
            getSoundPlayer().loadFromAppData(first.getSoundset());
        } catch (InvalidMidiDataException | IOException | MidiUnavailableException e) {
            e.printStackTrace();
        }
    }
    
    public boolean addSongToArrangement() {
        if (theSequence.getTitle() == null)
        	return false;
        
        StateMachine.setArrModified(true);
        theArrangement.getSequences().add(new StaffSequence(theSequence));
        soundPlayer.storeInCache();
        return true;
    }
    
    public boolean deleteSongFromArrangement(int i) {
        if (i >= 0 && i < theArrangement.getSequences().size()) {
            StateMachine.setArrModified(true);
            theArrangement.getSequences().remove(i);
            return true;
            
        } else {
        	return false;
        }
    }
    
    public boolean moveSongInArrangement(int from, int to) {
        if (from >= 0 && from < theArrangement.getSequences().size()) {
            StateMachine.setArrModified(true);
            StaffSequence ss = theArrangement.getSequences().remove(from);
            theArrangement.getSequences().add(to, ss);
            return true;
            
        } else {
        	return false;
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
        
        displayManager.resetSilhouette();
        StateMachine.setPlaybackActive(true);
    }
    
    public void stop() {
        stopSounds();
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
            switch (StateMachine.getMode()) {
            case SONG:
                return new AnimationTask();
            case ARRANGEMENT:
            default:
                return new ArrangementTask();
            }
        }

        /**
         * This class keeps track of animation and sound. Note to self: While
         * running a service or a task, crashes do not print stack traces.
         */
        class AnimationTask extends Task<Staff> {

            /** Milliseconds to delay between updating the play bars. */
            protected long delayMillis;

            /** Nanoseconds to delay in addition to the milliseconds delay. */
            protected int delayNanos;

            /**
             * This is the current index of the measure line that we are on on
             * the staff.
             */
            protected int index = 0;

            /**
             * Whether we need to advance the staff ahead by a few lines or not.
             */
            protected boolean advance = false;

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
            protected void computeDelay(double tempo) {
                double mill = (60.0 / tempo) * 1000;
                delayMillis = (int) mill;
                double nano = (mill - delayMillis) * Math.pow(10, 6);
                delayNanos = (int) nano;
            }

            @Override
            protected Staff call() throws Exception {
                int counter = StateMachine.getMeasureLineNum();
                boolean zero = false;
                int endLine = theSequence.getLength();

                computeDelay(theSequence.getTempo());
                
                StateMachine.setMaxLine(Math.max(endLine + Values.NOTELINES_IN_THE_WINDOW, Values.DEFAULT_LINES_PER_SONG));

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
                
                if (advance) {
                    int loc = currentLoc + Values.NOTELINES_IN_THE_WINDOW;
                    setLocation(loc);
                }
                
                runUI(currentLoc, index);
                
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
             */
            private void runUI(final int currentLoc, final int index) {
                // In principle it's not necessary to send this job to the FXAT,
                // but for some reason the program is more stable that way
                // Leaving things as they are until someone can figure it out --rozlyn
                Platform.runLater(() -> {
                    try {
                        playSoundLine(index);
                    } finally {
                        queue--;
                    }
                });
            }            

            /**
             * Plays a sound line at the index specified.
             * @param index Position of the line to play relative to the current position in the sequence
             */
            private void playSoundLine(int index) {
                int currentLine = StateMachine.getMeasureLineNum();
                soundPlayer.playSoundLine(theSequence.getLine(currentLine + index));
            }

        }

        /**
         * This class runs an arrangement instead of just a song.
         */
        class ArrangementTask extends AnimationTask {

            @Override
            protected Staff call() throws Exception {
                StateMachine.setArrangementSongIndex(0);
                List<StaffSequence> seq = theArrangement.getSequences();
                int endLine;

                queue = 0;
                
                for (int i = 0; i < seq.size(); i++) {
                    setSequence(theArrangement.getSequences().get(i));
                    setSoundset(theSequence.getSoundset());
                    computeDelay(theSequence.getTempo());
                    setTimeSignature(theSequence.getTimeSignature());
                    endLine = theSequence.getLength();
                    
                    StateMachine.setArrangementSongIndex(i);
                    StateMachine.setNoteExtensions(
                            theSequence.getNoteExtensions());
                    StateMachine.setTempo(theSequence.getTempo());
                    StateMachine.setMaxLine(Math.max(endLine + Values.NOTELINES_IN_THE_WINDOW, Values.DEFAULT_LINES_PER_SONG));
                    
                    index = 0;
                    advance = false;
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
                if (!soundPlayer.loadFromCache(soundset)) {
                    try {
                        soundPlayer.loadFromAppData(soundset);
                    } catch (InvalidMidiDataException | IOException | MidiUnavailableException e) {
                        e.printStackTrace();
                    }
                    soundPlayer.storeInCache();
                }
            }
        }
    }
}
