package smp.components.general;

import java.util.ArrayList;

import javafx.scene.image.ImageView;

/**
 * Image radio button that toggles between a list
 * of buttons. This class holds a list of references
 * to other ImageRadioButtons that it's linked to.
 * @author RehdBlob
 * @since 2012.08.30
 *
 */
public abstract class ImageRadioButton extends ImageButton {

    /**
     * THe list of radio buttons that are linked to this button.
     */
    protected ArrayList<ImageRadioButton> linkedButtons;

    /**
     * @param i The ImageView passed to the Button
     * wrapper.
     */
    public ImageRadioButton(ImageView i) {
        super(i);
        linkedButtons = new ArrayList<ImageRadioButton>();
    }

    /**
     * Links the ImageRadioButton to another ImageRadioButton
     * such that if it is depressed, the other buttons in the list will
     * be reverted to an unpressed state.
     */
    public void link(ImageRadioButton b) {
        linkedButtons.add(b);
    }

    /**
     * Sets <code>isPressed</code> to <b>true</b> and all of the linked
     * <code>ImageRadioButton</code> objects' <code>isPressed</code> to
     * <b>false</b>.
     * @param b Boolean telling whether the button should be pressed
     * or released.
     */
    @Override
    protected void setPressed(boolean b) {
        setPressedState(b);
        if (!isPressed) {
            for (ImageRadioButton bt : linkedButtons) {
                bt.releaseImage();
                bt.setPressed(false);
            }
        }
    }

}
