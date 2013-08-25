package smp.components.controls;

import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import smp.ImageIndex;
import smp.components.general.ImageRadioButton;
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

    /**
     * Instantiates the stop button.
     * @param i The ImageView that will be manipulated by this class.
     */
    public StopButton(ImageView i) {
        super(i);
        getImages(ImageIndex.STOP_PRESSED, ImageIndex.STOP_RELEASED);
        reactPressed(null);
    }


}
