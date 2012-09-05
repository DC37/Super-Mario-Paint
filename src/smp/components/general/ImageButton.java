package smp.components.general;

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
public abstract class ImageButton {

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
    public ImageButton(ImageView i) {
        theImage = i;
        initializeHandler();
        getImages();
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
                        react(event);
                    }
                });
    }

    /**
     * "Presses" the button by setting the image to <code>pressed</code>.
     */
    protected void press() {
        isPressed = true;
        theImage.setImage(pressed);
    }

    /**
     * "Releases" the button by setting the image to <code>notPressed</code>.
     */
    protected void release() {
        isPressed = false;
        theImage.setImage(notPressed);
    }


    /**
     * This method is called every time the button is clicked.
     */
    protected abstract void react(MouseEvent event);

    /**
     * Gets the images for the <code>pressed</code> and <code>notPressed</code>
     * versions of the button.
     */
    protected abstract void getImages();

}
