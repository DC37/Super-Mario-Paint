package smp.components.general;

import java.util.ArrayList;

import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

/**
 * Image radio button that toggles between a list
 * of buttons. This class holds a list of references
 * to other ImageRadioButtons that it's linked to.
 * @author RehdBlob
 * @since 2012.08.30
 */
public abstract class ImageRadioButton extends AbstractImageButton {

    /**
     * The list of radio buttons that are linked to this button.
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
     * @param b The <code>ImageRadioButton</code> to link this
     * <code>ImageRadioButton</code> to.
     */
    public void link(ImageRadioButton b) {
        linkedButtons.add(b);
    }

    /**
     * Sets this button's <code>isPressed</code> to true and also presses
     * the button image down.
     */
    @Override
    protected void reactPressed(MouseEvent e) {
        setPressedState(true);
    }

    /**
     * Sets <code>isPressed</code> to <b>true</b> and all of the linked
     * <code>ImageRadioButton</code> objects' <code>isPressed</code> to
     * <b>false</b>. If one is attempting to set the button's pressed state
     * to the state that it's already in, then this method is useless.
     * @param b Boolean telling whether the button should be pressed
     * or released.
     */
    @Override
    protected void setPressedState(boolean b) {
        if (b == isPressed()) {
            return;
        }
        setPressed(b);
        pressImage();
        if (b) {
            press();
        } else {
            release();
        }
        if (isPressed()) {
            for (ImageRadioButton bt : linkedButtons) {
                bt.releaseImage();
                bt.setPressedState(false);
            }
        }
    }
}
