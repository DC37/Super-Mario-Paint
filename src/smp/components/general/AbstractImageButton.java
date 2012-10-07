package smp.components.general;

import smp.ImageIndex;
import smp.ImageLoader;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

/**
 * An image button, to be used as a background-type object.
 * To be extended by more concrete classes that use
 * images as backgrounds. Updated 2012-August-30 to
 * reflect change to JavaFX2.2.
 * @author RehdBlob
 * @since 2012.08.14
 */
public abstract class AbstractImageButton {

    /**
     * Indicates whether the button is currently pressed.
     */
    protected boolean isPressed;

    /**
     * The image that displays when the button is not pressed.
     */
    protected Image notPressed;

    /**
     * The image that displays when the button is pressed.
     */
    protected Image pressed;

    /**
     * This is the object that this class will be manipulating
     * to act like a button.
     */
    protected ImageView theImage;

    /**
     * @param i The ImageView passed to the Button
     * wrapper.
     */
    public AbstractImageButton(ImageView i) {
        theImage = i;
        initializeHandler();
    }

    /**
     * Initializes the button click handler. Every time
     * the button is pressed, the react() method in this
     * class is called.
     */
    private void initializeHandler() {
        theImage.setOnMouseClicked(
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        reactClick(event);
                    }
                });
        theImage.setOnMousePressed(
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        reactPressed(event);
                    }
                });
        theImage.setOnMouseReleased(
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        reactReleased(event);
                    }
                });
    }

    /**
     * "Presses" the button by setting the image to <code>pressed</code>.
     */
    protected void pressImage() {
        theImage.setImage(pressed);
    }

    /**
     * "Releases" the button by setting the image to <code>notPressed</code>.
     */
    protected void releaseImage() {
        theImage.setImage(notPressed);
    }

    /**
     * Sets the <code>isPressed</code> parameter to <b>true</b> or <b>false</b>
     * but does <b>not</b> define what happens afterwards.
     * @param b Boolean telling whether the button should be pressed
     * or released.
     */
    protected void setPressed(boolean b) {
        isPressed = b;
    }

    /**
     * Call <code>setPressed()</code> first and then execute
     * code from there.
     * @param b Whether the button is pressed or not.
     */
    protected abstract void setPressedState(boolean b);

    /**
     * @return Whether this button is actually pressed or not.
     */
    protected boolean isPressed() {
        return isPressed();
    }

    /**
     * Override this method if there should be an action when a
     * MouseClicked event occurs.
     * @param event The event that occurred.
     */
    protected void reactClick(MouseEvent event) {
    }

    /**
     * Override this method if there should be an action when a
     * MousePressed event occurs.
     * @param event The event that occurred.
     */
    protected void reactPressed(MouseEvent event) {
    }

    /**
     * Override this method if there should be an action when a
     * MouseReleased event occurs.
     * @param event The event that occurred.
     */
    protected void reactReleased(MouseEvent event) {
    }

    /**
     * Gets the images for the <code>pressed</code> and <code>notPressed</code>
     * versions of the button.
     */
    protected void getImages(ImageIndex pr, ImageIndex notPr) {
        pressed = ImageLoader.getSpriteFX(pr);
        notPressed = ImageLoader.getSpriteFX(notPr);
    }

    /**
     * Call this method to press the button and apply all the effects
     * of pressing this button. Exactly when this method is called is
     * defined by the abstract classes that ImageButton is implemented
     * by.
     */
    public abstract void press();

    /**
     * Call this method to release the button and apply all the effects
     * of releasing the button. Exactly when this method is called is
     * defined by the abstract classes that ImageButton is implemented
     * by.
     */
    public abstract void release();

}
