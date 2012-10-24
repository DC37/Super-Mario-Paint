package smp;

import smp.stateMachine.Settings;

/**
 * Indicates that some class makes use of the Settings class, and
 * so has a Settings object associated with it.
 * @author RehdBlob
 * @since 2012.10.24
 */
public interface Settable {

    /**
     * Give the loaded Settings object to this class.
     * @param s The loaded Settings object.
     */
    public void setSettings(Settings s);

}
