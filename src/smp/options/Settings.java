package smp.options;

/**
 * Settings class. Holds the settings that
 * the user sets for the program. Most of these
 * will be accesible in the options pane.
 * @author RehdBlob
 * @since 2012.08.14
 */
public class Settings {

	/**
	 * Is the sequencer in the "advanced" mode?
	 * Defaults to false.
	 */
	private static boolean isAdvanced;
	
	/**
	 * Do not make an instance of this class! This 
	 * is meant to be a settings class that others
	 * check.
	 * @deprecated
	 */
	private Settings() {
	}
	
	public static void setMode(boolean t) {
		isAdvanced = t;
	}
	
	public static boolean getMode() {
		return isAdvanced;
	}
	
}
