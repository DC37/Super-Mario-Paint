package smp.presenters.buttons;

import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import smp.ImageIndex;
import smp.models.stateMachine.StateMachine;
import smp.presenters.api.button.ImageToggleButton;

/**
 * Wrapper class for an ImageView that holds the stop button image. Pressing the
 * button changes the image and also changes the state of the program.
 *
 * @author RehdBlob
 * @author j574y923
 * 
 * @since 2018.01.14
 */
public class ClipboardButtonPresenter extends ImageToggleButton {

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
	public ClipboardButtonPresenter(ImageView i) {
		super(i);
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
		
		// TODO: create getClipboardButton() somewhere so adding a hotkey can be done elsewhere
//		ct.getBasePane().addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
//
//			@Override
//			public void handle(KeyEvent event) {
//				if(event.isShiftDown() && event.getCode() == KeyCode.R)
//					reactPressed(null);
//			}
//		});
	}

    @Override
    public void reactPressed(MouseEvent e) {
    	//TODO: fix clipboard
        if (isPressed) {
            isPressed = false;
            releaseImage();
    		StateMachine.setClipboardPressed(false);
    		this.controller.getStaffInstruments().setMouseTransparent(false);
    		this.controller.getVolumeBars().setMouseTransparent(false);
    		
    		this.controller.getBasePane().getScene().addEventHandler(MouseEvent.ANY,
    				 					controller.getStaffInstrumentEventHandler());
        } else {
            isPressed = true;
            pressImage();
    		StateMachine.setClipboardPressed(true);
    		this.controller.getStaffInstruments().setMouseTransparent(true);
    		this.controller.getVolumeBars().setMouseTransparent(true);
    		
    		this.controller.getBasePane().getScene().removeEventHandler(MouseEvent.ANY,
    				 					controller.getStaffInstrumentEventHandler());
        }
    }
}
