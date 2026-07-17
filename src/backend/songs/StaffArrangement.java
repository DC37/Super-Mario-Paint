package backend.songs;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>An arrangement is a list of {@link StaffSequence}s.
 */
public class StaffArrangement extends Sequence {

    /** The sequences in this arrangement. */
    private final List<StaffSequence> sequences;

    /** Default constructor. Makes an empty arrangement. */
    public StaffArrangement() {
        sequences = new ArrayList<>();
    }

    /**
     * Get the sequences in this arrangement.
     */
    public List<StaffSequence> getSequences() {
        return sequences;
    }
}
