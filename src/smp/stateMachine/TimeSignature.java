package smp.stateMachine;

/**
 * These are the time signatures that we can select when using
 * Super Mario Paint.
 * @author RehdBlob
 * @since 2013.06.28
 */
public enum TimeSignature {

    /**
     * These are different time signatures that we
     * can have for the program.
     */
    TWO_FOUR("2/4"), THREE_FOUR("3/4"), FOUR_FOUR("4/4"), SIX_FOUR("6/4"),
    THREE_EIGHT("3/8"), SIX_EIGHT("6/8"), TWELVE_EIGHT("12/8");

    /** What happens when you try to display the time signature. */
    private String displayName;

    /** Sets up the display name of the time signature type. */
    private TimeSignature(String disp) {
        displayName = disp;
    }

    @Override
    public String toString() {
        return displayName;
    }

}
