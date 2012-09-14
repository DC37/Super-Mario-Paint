package smp.components.general;

import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

/**
 * Image toggle button that is an on/off
 * switch.
 * @author RehdBlob
 * @since 2012.08.30
 */
public abstract class ImageToggleButton extends ImageButton {

    /**
     * @param i The ImageView passed to the Button
     * wrapper.
     */
    public ImageToggleButton(ImageView i) {
        super(i);
    }

    /**
     * When the mouse is pressed on an <code>ImageToggleButton</code>,
     * then the button should be pressed down.
     */
    @Override
    protected void reactPressed(MouseEvent e) {
        setPressedState(!isPressed());
    }

    /**
     * Sets the pressed state of this button to either <b>true</b> or
     * <b>false</b> and reacts accordingly.
     */
    @Override
    protected void setPressedState(boolean b) {
        setPressed(b);
        if (isPressed()) {
            pressImage();
            doPressBehavior();
        } else {
            releaseImage();
            doReleaseBehavior();
        }
    }
}
