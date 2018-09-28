package smp.models;

/**
 * Keeps track of the different types of staff events.
 * @author RehdBlob
 * @since 2013.03.29
 */
public enum StaffEventType {

    /** A note. */
    NOTE,

    /** This changes the tempo of the song. */
    SPEEDMARK,

    /**
     * This is a bookmark that keeps the
     * place of whatever it is the user wants.
     */
    BOOKMARK;

}
