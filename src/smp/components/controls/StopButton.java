package smp.components.controls;

import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import smp.ImageIndex;
import smp.components.general.ImageRadioButton;
import smp.components.staff.Staff;
import smp.stateMachine.State;
import smp.stateMachine.StateMachine;

/**
 * Wrapper class for an ImageView that holds the stop button
 * image. Pressing the button changes the image and also changes
 * the state of the program.
 * @author RehdBlob
 * @since 2012.09.14
 */
public class StopButton extends ImageRadioButton {

    /** Pointer to the staff object this button will affect. */
    private Staff theStaff;

    /**
     * Instantiates the stop button.
     * @param i The ImageView that will be manipulated by this class.
     */
    public StopButton(ImageView i) {
        super(i);
        getImages(ImageIndex.STOP_PRESSED, ImageIndex.STOP_RELEASED);
        pressImage();
        isPressed = true;
    }

    @Override
    protected void reactPressed(MouseEvent e) {
        if (isPressed)
            return;
        super.reactPressed(e);
        StateMachine.setState(State.EDITING);
        theStaff.stopAnimation();
    }

    /**
     * @param s Pointer to the staff object that this button is to affect.
     */
    public void setStaff(Staff s) {
        theStaff = s;
    }
}
