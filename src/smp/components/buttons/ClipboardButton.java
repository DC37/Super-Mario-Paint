package smp.components.buttons;

import javafx.event.EventHandler;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
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
 * @author j574y923
 * 
 * @since 2018.01.14
 */
public class ClipboardButton extends ImageToggleButton {

    /**
     * Instantiates the clipboard selection button.
     *
     * @param i
     *            The ImageView that will be manipulated by this class.
     * @param ct
     *            The FXML controller object.
     * @param im
     *            The Image loader object.
     */
	public ClipboardButton(ImageView i, SMPFXController ct, ImageLoader im) {
		super(i, ct, im);
		getImages(ImageIndex.CLIPBOARD_PRESSED, ImageIndex.CLIPBOARD_RELEASED);
		releaseImage();
		Tooltip.install(i, new Tooltip("Click (or Shift+R) to toggle region selection\n"
				+ "Hover over instrument & press F to filter instrument\n"
				+ "Ctrl+A to select all\n"
				+ "Ctrl+C to copy notes\n"
				+ "Ctrl+V to paste notes\n"
				+ "Ctrl+X to cut notes\n"
				+ "Delete to delete notes\n"
				+ "Alt+N to toggle notes selection\n"
				+ "Alt+V to toggle volumes selection"));
		
		ct.getBasePane().addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				if(event.isShiftDown() && event.getCode() == KeyCode.R)
					reactPressed(null);
			}
		});
	}

    @Override
    public void reactPressed(MouseEvent e) {
        if (isPressed) {
            isPressed = false;
            releaseImage();
    		StateMachine.resetClipboardPressed();
    		this.controller.getStaffInstruments().setMouseTransparent(false);
    		this.controller.getVolumeBars().setMouseTransparent(false);
    		
    		this.controller.getBasePane().getScene().addEventHandler(MouseEvent.ANY,
    				 					controller.getStaffInstrumentEventHandler());
        } else {
            isPressed = true;
            pressImage();
    		StateMachine.setClipboardPressed();
    		this.controller.getStaffInstruments().setMouseTransparent(true);
    		this.controller.getVolumeBars().setMouseTransparent(true);
    		
    		this.controller.getBasePane().getScene().removeEventHandler(MouseEvent.ANY,
    				 					controller.getStaffInstrumentEventHandler());
        }
    }
}
