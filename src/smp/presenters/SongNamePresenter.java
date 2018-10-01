package smp.presenters;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import smp.fx.SMPFXController;
import smp.models.Variables;

/**
 * Use this to add several eventhandlers and a focus listener to the song name
 * <code>TextField</code> that will programmatically refocus the
 * <code>ScrollBar</code> after clicking any element outside the textfield.
 * 
 * TODO: if we move to a MVC design pattern, move all textfield interaction into
 * here.
 * 
 * @author J
 * @since v1.1.2
 *
 */
public class SongNamePresenter {

	TextField songName;
	SMPFXController controller;

	/**
	 * The event handler that will handle unfocusing the TextField and
	 * refocusing the ScrollBar. 
	 */
	EventHandler<MouseEvent> unfocusMouseEventHandler;

	boolean mouseExited = false;

	public SongNamePresenter(TextField tf) {
		this.songName = tf;

		// click somewhere to unfocus only if the mouse exited
		unfocusMouseEventHandler = new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				if (mouseExited)
					controller.getScrollbar().requestFocus();
			}
		};

		// when we get focused, prepare for the unfocusing mouse press event
		// when we get unfocused, clean up the event filter
		songName.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue,
					Boolean newPropertyValue) {
				if (newPropertyValue) {
					songName.getScene().addEventFilter(MouseEvent.MOUSE_PRESSED, unfocusMouseEventHandler);
				} else {
					songName.getScene().removeEventFilter(MouseEvent.MOUSE_PRESSED, unfocusMouseEventHandler);
				}
			}
		});

		// mouse enters thus mouse has not exited, we cannot unfocus
		songName.addEventFilter(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent mouseEvent) {
				mouseExited = false;
			}
		});
		
		// mouse exited, we can unfocus
		songName.addEventFilter(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent mouseEvent) {
				mouseExited = true;
			}
		});
		
		setupViewUpdater();
	}
	
    private void setupViewUpdater() {
    	songName.textProperty().bindBidirectional(Variables.songName);
    }
}
