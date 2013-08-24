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
                        reactClicked(event);
                        event.consume();
                    }
                });
        theImage.setOnMousePressed(
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        reactPressed(event);
                        event.consume();
                    }
                });
        theImage.setOnMouseReleased(
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        reactReleased(event);
                        event.consume();
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
     * Sets the <code>isPressed</code> parameter to <b>true</b>
     * but does <b>not</b> define what happens afterwards.
     */
    protected void setPressed() {
        isPressed = true;
    }

    /**
     * Sets the <code>isPressed</code> parameter to <b>false</b>
     * but does <b>not</b> define what happens afterwards.
     */
    protected void resetPressed() {
        isPressed = false;
    }

    /**
     * @return Whether this button is actually pressed or not.
     */
    protected boolean isPressed() {
        return isPressed();
    }

    /**
     * This method is always called when a
     * MouseClicked event occurs.
     * @param event The event that occurred.
     */
    protected abstract void reactClicked(MouseEvent event);

    /**
     * This method is always called when a
     * MousePressed event occurs.
     * @param event The event that occurred.
     */
    protected abstract void reactPressed(MouseEvent event);

    /**
     * This method is always called when a
     * MouseReleased event occurs.
     * @param event The event that occurred.
     */
    protected abstract void reactReleased(MouseEvent event);

    /**
     * Gets the images for the <code>pressed</code> and <code>notPressed</code>
     * versions of the button.
     */
    protected void getImages(ImageIndex pr, ImageIndex notPr) {
        pressed = ImageLoader.getSpriteFX(pr);
        notPressed = ImageLoader.getSpriteFX(notPr);
    }


}
