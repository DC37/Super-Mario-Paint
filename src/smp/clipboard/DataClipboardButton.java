package smp.clipboard;

import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import smp.ImageIndex;
import smp.ImageLoader;
import smp.components.general.ImageToggleButton;
import smp.fx.SMPFXController;
import smp.stateMachine.StateMachine;

/**
 * Wrapper class for an ImageView that holds the stop button image. Pressing the
 * button changes the image and also changes the state of the program.
 *
 * @author RehdBlob
 * @since 2012.09.14
 */
public class DataClipboardButton extends ImageToggleButton {

    /**
     * Instantiates the stop button.
     *
     * @param i
     *            The ImageView that will be manipulated by this class.
     * @param ct
     *            The FXML controller object.
     * @param im
     *            The Image loader object.
     */
	public DataClipboardButton(ImageView i, SMPFXController ct, ImageLoader im) {
		super(i, ct, im);
		getImages(ImageIndex.STOP_PRESSED, ImageIndex.STOP_RELEASED);
		releaseImage();
		Tooltip.install(i, new Tooltip("Click (or Shift+R) to toggle region selection\n"
				+ "Hover over instrument & press F to filter instrument\n"
				+ "Ctrl+A to select all\n"
				+ "Ctrl+C to copy notes\n"
				+ "Ctrl+V to paste notes\n"
				+ "Ctrl+X to cut notes\n"
				+ "Delete to delete notes\n"));
	}

    @Override
    public void reactPressed(MouseEvent e) {
        if (isPressed) {
            isPressed = false;
            releaseImage();
    		StateMachine.resetClipboardPressed();
    		this.controller.getStaffInstruments().setMouseTransparent(false);
    		this.controller.getVolumeBars().setMouseTransparent(false);
    		
        } else {
            isPressed = true;
            pressImage();
    		StateMachine.setClipboardPressed();
    		this.controller.getStaffInstruments().setMouseTransparent(true);
    		this.controller.getVolumeBars().setMouseTransparent(true);
    		
        }
    }
}
