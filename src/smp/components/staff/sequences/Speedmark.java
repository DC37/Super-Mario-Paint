package smp.components.staff.sequences;

/**
 * A marker that changes the speed of the Super Mario
 * Paint Sequence, much like the speedmarks in Advanced Mario
 * Sequencer.
 * @author RehdBlob
 * @since 2012.09.17
 */
public class Speedmark {

    /**
     * The tempo that this speedmark designates.
     */
    private int tempo;

    /* private Index location; */

    /**
     * Creates a speedmark that designates a tempo.
     * @param t The tempo that the song is to change to.
     */
    public Speedmark(int t) {
        setTempo(t);
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
    public int getTempo() {
        return tempo;
    }
}
