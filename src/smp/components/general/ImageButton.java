package smp.components.general;

/**
 * Some button that happens to be an image
 * instead of a regular button.
 * @author RehdBlob
 */
public interface ImageButton {

    /**
     * Call this method to press the button and apply all the effects
     * of pressing this button.
     */
    public void doPressBehavior();

    /**
     * Call this method to release the button and apply all the effects
     * of releasing the button.
     */
    public void doReleaseBehavior();

}
