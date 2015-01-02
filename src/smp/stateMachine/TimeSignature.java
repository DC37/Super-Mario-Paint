package smp.stateMachine;

/**
 * These are the time signatures that we can select when using Super Mario
 * Paint.
 *
 * @author RehdBlob
 * @since 2013.06.28
 */
public enum TimeSignature {

    /**
     * These are different time signatures that we can have for the program.
     */
    TWO_FOUR("2/4"), THREE_FOUR("3/4"), FOUR_FOUR("4/4"), SIX_FOUR("6/4"), THREE_EIGHT(
            "3/8"), SIX_EIGHT("6/8"), TWELVE_EIGHT("12/8");

    /** The number on the top. */
    private int top;

    /** The number on the bottom. */
    private int bottom;

    /** What happens when you try to display the time signature. */
    private String displayName;

    /** Sets up the display name of the time signature type. */
    private TimeSignature(String disp) {
        displayName = disp;
        top = Integer.parseInt(disp.substring(0, disp.indexOf("/")));
        bottom = Integer.parseInt(disp.substring(disp.indexOf("/") + 1));
    }

    /** @return The integer on the top. */
    public int top() {
        return top;
    }

    /** @return The integer on the bottom. */
    public int bottom() {
        return bottom;
    }

    @Override
    public String toString() {
        return displayName;
    }

}
