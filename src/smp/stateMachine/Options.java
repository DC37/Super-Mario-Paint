package smp.stateMachine;

/**
 * These are the options that users may set when the
 * options pane is open.
 * @author RehdBlob
 * @since 2012.08.20
 */
public enum Options {
	
	LIM_NOTESPERLINE (true),
	LIM_96_MEASURES (true),
	LIM_VOLUME_LINE (true),
	LOW_A_ON (true),
	NEG_TEMPO_FUN (false),
	RESIZE_WIN (false),
	ADV_MODE (false);
	
	private boolean tf;
	
	public boolean isSet() {
		return tf;
	}
	
	private Options(boolean b) {
		tf = b;
	}

}
