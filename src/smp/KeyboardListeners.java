package smp;

import java.util.Set;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;
import javafx.stage.Stage;
import smp.components.Values;
import smp.models.staff.StaffSequence;
import smp.models.stateMachine.StateMachine;
import smp.models.stateMachine.Variables;

public class KeyboardListeners {

	// TODO: auto-add these model comments
	// ====Models====
	private DoubleProperty measureLineNum;
	private ObjectProperty<StaffSequence> theSequence;
	private Set<KeyCode> buttonsPressed;
	
	private Stage primaryStage;
	private Scene primaryScene;

	/**
	 * Creates the keyboard listeners that we will be using for various other
	 * portions of the program. Ctrl, alt, and shift are of interest here, but
	 * the arrow keys will also be considered.
	 *
	 * @param primaryStage
	 *            The main window.
	 */
	public KeyboardListeners(Stage primaryStage) {

		this.primaryStage = primaryStage;
		this.primaryScene = primaryStage.getScene();
		
		this.measureLineNum = StateMachine.getMeasureLineNum();
		this.theSequence = Variables.theSequence;
		this.buttonsPressed = StateMachine.getButtonsPressed();
		setupKeyboardListeners();
	}

	private void setupKeyboardListeners() {
		
		primaryScene.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent ke) {

				switch (ke.getCode()) {
				case PAGE_UP:
					measureLineNum.set(measureLineNum.get() - Values.NOTELINES_IN_THE_WINDOW);
					break;
				case PAGE_DOWN:
					measureLineNum.set(measureLineNum.get() + Values.NOTELINES_IN_THE_WINDOW);
					break;
				case HOME:
					if (ke.isControlDown())
						measureLineNum.set(0);
					break;
				case END:
					if (ke.isControlDown())
						measureLineNum.set(theSequence.get().getTheLinesSize().get());
					break;
				// @since v1.1.2, requested by seymour schlong
				case LEFT:
					if (ke.isControlDown())
						measureLineNum.set(measureLineNum.get() - 4);
					break;
				case RIGHT:
					if (ke.isControlDown())
						measureLineNum.set(measureLineNum.get() + 4);
					break;
				default:
				}

				buttonsPressed.add(ke.getCode());
//				StaffInstrumentEventHandler.updateAccidental();
				ke.consume();
			}
		});

		primaryScene.addEventHandler(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent ke) {
				buttonsPressed.remove(ke.getCode());
//				StaffInstrumentEventHandler.updateAccidental();
				ke.consume();
			}
		});

		primaryScene.addEventHandler(ScrollEvent.ANY, new EventHandler<ScrollEvent>() {

			@Override
			public void handle(ScrollEvent se) {
				if (se.getDeltaY() < 0) {
					measureLineNum.set(measureLineNum.get() + 1);
				} else if (se.getDeltaY() > 0) {
					measureLineNum.set(measureLineNum.get() - 1);
				}
				se.consume();
			}
		});

		primaryStage.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
				buttonsPressed.clear();
			}
		});
	}
}
