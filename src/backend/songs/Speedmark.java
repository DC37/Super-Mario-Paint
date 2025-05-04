package backend.songs;

import gui.Staff;

/**
 * A marker that changes the speed of the Super Mario
 * Paint Sequence, much like the speedmarks in Advanced Mario
 * Sequencer.
 * @author RehdBlob
 * @since 2012.09.17
 */
public class Speedmark extends AbstractStaffEvent {

    /**
     * Generated serial ID.
     */
    private static final long serialVersionUID = 2353273332875239641L;
    /**
     * The tempo that this speedmark designates.
     */
    private double tempo;

    /* private Index location; */

    /**
     * Creates a speedmark that designates a tempo.
     * @param num The tempo that the song is to change to.
     */
    public Speedmark(int num) {
        super(num);
    }

    /**
     * Sets the tempo of the Super Mario Paint song to whatever is
     * given in this speedmark.
     * @param t The tempo that the song is to be set to.
     */
    public void setTempo(int t) {
        tempo = t;
    }

    /**
     * @return The tempo that this speedmark is set at.
     */
    public double getTempo() {
        return tempo;
    }
    
    @Override
    public void doEvent(Staff s) {
        
    }

}
