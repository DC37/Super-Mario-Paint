package smp.presenters;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import smp.models.stateMachine.ProgramState;
import smp.models.stateMachine.StateMachine;
import smp.models.stateMachine.Variables;

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

	//TODO: auto-add these model comments
	//====Models====
	private StringProperty theSequenceName;
	private StringProperty theArrangementName;
	private ObjectProperty<ProgramState> programState;

	TextField songName;

	/**
	 * The event handler that will handle unfocusing the TextField and
	 * refocusing the ScrollBar. 
	 */
	EventHandler<MouseEvent> unfocusMouseEventHandler;

	/**
	 * The default node to focus (hopefully the slider).
	 */
	Node defaultFocusNode;

	boolean mouseExited = false;

	public SongNamePresenter(TextField songName) {
		this.songName = songName;

		// get default focus node
		songName.sceneProperty().addListener(new ChangeListener<Scene>() {

			@Override
			public void changed(ObservableValue<? extends Scene> observable, Scene oldValue, Scene newScene) {
				newScene.focusOwnerProperty().addListener(new ChangeListener<Node>() {

					@Override
					public void changed(ObservableValue<? extends Node> observable, Node oldValue, Node newValue) {
						if (defaultFocusNode == null)
							defaultFocusNode = newValue;
					}
				});
			}
		});
		
		// click somewhere to unfocus only if the mouse exited
		unfocusMouseEventHandler = new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				if (mouseExited)
					defaultFocusNode.requestFocus();
			}
		};

		// remove eventfilter after unfocusing
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
		
		this.theSequenceName = Variables.theSequenceName;	
		this.theArrangementName = Variables.theArrangementName;	
		this.programState = StateMachine.getState();	
		setupViewUpdater();
	}
	
    private void setupViewUpdater() {
		songName.textProperty().bindBidirectional(this.theSequenceName);
		songName.setPromptText("Song Name");
		this.programState.addListener(new ChangeListener<Object>() {

			@Override
			public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {
				if (newValue.equals(ProgramState.EDITING)) {
					songName.textProperty().unbindBidirectional(theArrangementName);
					songName.textProperty().bindBidirectional(theSequenceName);
					songName.setPromptText("Song Name");
				}
				else if (newValue.equals(ProgramState.ARR_EDITING)) {
					songName.textProperty().unbindBidirectional(theSequenceName);
					songName.textProperty().bindBidirectional(theArrangementName);
					songName.setPromptText("Arrangement Name");
				}
			}
		});
	}
}
