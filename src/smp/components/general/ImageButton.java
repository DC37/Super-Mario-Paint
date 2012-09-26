package smp.components.general;

/**
 * Some button that happens to be an image
 * instead of a regular button.
 * @author RehdBlob
 */
public interface ImageButton {

    /**
     * Call this method to press the button and apply all the effects
     * of pressing this button. Exactly when this method is called is
     * defined by the abstract classes that ImageButton is implemented
     * by.
     */
    public void doPressBehavior();

    /**
     * Call this method to release the button and apply all the effects
     * of releasing the button. Exactly when this method is called is
     * defined by the abstract classes that ImageButton is implemented
     * by.
     */
    public void doReleaseBehavior();

}
