package smp.components.controls;

import javafx.scene.image.ImageView;
import smp.ImageIndex;
import smp.components.general.ImageButton;
import smp.components.general.ImageRadioButton;
import smp.stateMachine.State;
import smp.stateMachine.StateMachine;

/**
 * Wrapper class for an ImageView that holds the stop button
 * image. Pressing the button changes the image and also changes
 * the state of the program.
 * @author Derek Chou
 * @since 2012.09.14
 */
public class StopButton extends ImageRadioButton implements ImageButton {

    /**
     * Instantiates the stop button.
     * @param i The ImageView that will be manipulated by this class.
     */
    public StopButton(ImageView i) {
        super(i);
        getImages(ImageIndex.STOP_PRESSED, ImageIndex.STOP_RELEASED);
    }

    @Override
    public void doPressBehavior() {
        if (isPressed)
            return;
        StateMachine.setState(State.EDITING);
    }

    @Override
    public void doReleaseBehavior() {
        if (!isPressed)
            return;
    }
}
