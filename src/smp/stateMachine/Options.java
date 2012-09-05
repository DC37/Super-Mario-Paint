package smp.stateMachine;

/**
 * These are the options that users may set when the
 * options pane is open.
 * @author RehdBlob
 * @since 2012.08.20
 */
public enum Options {

    /**
     * Limit the number of notes per line to 5.
     */
    LIM_NOTESPERLINE (true),

    /**
     * Limit the number of measures to 96.
     */
    LIM_96_MEASURES (true),

    /**
     * Limit the number of volume control lines to
     * 1 per line.
     */
    LIM_VOLUME_LINE (true),

    /**
     * Enable or disable the Low A note as a usable
     * note. Default is disabled.
     */
    LIM_LOWA (true),

    /**
     * Enable or disable the High D note as a usable
     * note. Default is disabled.
     */
    LIM_HIGHD (true),

    /**
     * Turn on the Low A Glitch or not.
     */
    LOW_A_ON (true),

    /**
     * Cause fun things to happen when negative tempo is
     * entered. Otherwise, the tempo will be set to ludicrous
     * speeds if this is not enabled.
     */
    NEG_TEMPO_FUN (false),

    /**
     * Turn tempo gaps on or off. Most likely people won't want this
     * and maybe it'll be removed.
     */
    LIM_TEMPO_GAPS (false),

    /**
     * Be able to resize the window to get to the Secret Button.
     */
    RESIZE_WIN (false),

    /**
     * Advanced mode. All of these options don't matter when this is pressed
     * because a completely different interface is loaded when this is set to
     * true...
     */
    ADV_MODE (false);

    /**
     * Whether the option is activated or not.
     */
    private boolean tf;

    /**
     * @return Whether the option is activated or not.
     */
    public boolean isSet() {
        return tf;
    }

    /**
     * @param b This indicates whether the Option is supposed to be set or not.
     */
    private Options(boolean b) {
        tf = b;
    }

}
