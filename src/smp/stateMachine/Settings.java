package smp.stateMachine;

import java.io.Serializable;

/**
 * A loadable settings file that determines whatever is supposed
 * to display eventually on the screen.
 * @author RehdBlob
 * @since 2012.08.28
 */
@SuppressWarnings("unused")
public class Settings implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7243683035937693416L;


	/**
	 * Classic debug on/off.
	 */
	public static final boolean DEBUG = true;
	
	
	/**
	 * For initialization purposes, if Advanced mode is unlocked,
	 * there are some things that are supposed to be initialized.
	 */
	private boolean advModeUnlocked;
	
	
	
	public Settings() {
		
	}
	
	
	/**
	 * 
	 */
	private void setOptions() {
		
	}
	
	/**
	 * 
	 */
	private void load() {
		
	}
	
	/**
	 * 
	 */
	private void save() {
		
	}
	
	
	
	
	
}
