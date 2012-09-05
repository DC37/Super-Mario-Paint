package smp.components.general;

import javafx.event.EventHandler;
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
                        react();
                    }
                });
    }

    /**
     * This method is called every time the button is pressed.
     */
    protected abstract void react();

}
