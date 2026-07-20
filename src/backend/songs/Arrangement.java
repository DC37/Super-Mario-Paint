package backend.songs;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>An arrangement is a list of {@link Song}s.
 */
public class Arrangement extends Sequence {

    /** The sequences in this arrangement. */
    private final List<Song> sequences;

    /** Default constructor. Makes an empty arrangement. */
    public Arrangement() {
        sequences = new ArrayList<>();
    }

    /**
     * Get the sequences in this arrangement.
     */
    public List<Song> getSequences() {
        return sequences;
    }
}
