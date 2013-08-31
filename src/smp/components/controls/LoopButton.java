package smp.components.controls;

import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import smp.ImageIndex;
import smp.components.general.ImageToggleButton;
import smp.stateMachine.StateMachine;

public class LoopButton extends ImageToggleButton {

    public LoopButton(ImageView i) {
        super(i);
        getImages(ImageIndex.LOOP_PRESSED, ImageIndex.LOOP_RELEASED);
        releaseImage();
        isPressed = false;
    }

    @Override
    protected void reactClicked(MouseEvent event) {
        // Do nothing

    }

    @Override
    protected void reactPressed(MouseEvent event) {
        if (isPressed) {
            isPressed = false;
            pressImage();
            StateMachine.setLoopPressed();

        } else {
            isPressed = true;
            releaseImage();
            StateMachine.resetLoopPressed();
        }

    }

    @Override
    protected void reactReleased(MouseEvent event) {

    }

}
