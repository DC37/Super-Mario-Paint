package gui.components;

import gui.Dialog;
import gui.SMPFXController;
import gui.SMPMode;
import gui.Staff;
import gui.StateMachine;
import gui.components.buttons.elements.ArrowButton;
import gui.components.buttons.elements.PlayButton;
import gui.components.buttons.elements.StopButton;
import gui.components.buttons.elements.TempoAdjustButton;
import gui.loaders.ImageIndex;
import gui.loaders.ImageLoader;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Window;

/**
 * These are the control buttons for the program.
 *
 * @author RehdBlob
 * @since 2012.09.04
 */
public class Controls {

	/** The pointer to the play button on the staff. */
	private PlayButton play;

	/** The pointer to the stop button on the staff. */
	private StopButton stop;

	/** The arrow that you click to go left. */
	private ArrowButton leftArrow;

	/** The arrow that you click to go all the way left. */
	private ArrowButton leftFastArrow;

	/** The arrow that you click to go right. */
	private ArrowButton rightArrow;

	/** The arrow that you click to go all the way right. */
	private ArrowButton rightFastArrow;

	/** This is the staff that the controls are linked to. */
	private Staff theStaff;

	/** The controller object. */
	private SMPFXController controller;

	/** The image loader object. */
	private ImageLoader il;

	/**
	 * Initializes the set of controls that will be used in Super Mario Paint.
	 */
	public Controls(Staff st, SMPFXController ct, ImageLoader im) {
		il = im;
		theStaff = st;
		setController(ct);
		initializeArrows();
		initializeControlButtons();
		initializeTempoButtons();

	}

	/** Initializes the plus and minus buttons that can change the tempo. */
	private void initializeTempoButtons() {
		TempoAdjustButton plus = new TempoAdjustButton(controller.getTempoPlus(), controller, il);
		TempoAdjustButton minus = new TempoAdjustButton(controller.getTempoMinus(), controller, il);
		plus.setStaff(theStaff);
		minus.setStaff(theStaff);
		plus.setPositive(true);
		minus.setPositive(false);
		StackPane tBox = controller.getTempoBox();

		tBox.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				try {
					if (StateMachine.getMode() == SMPMode.SONG) {
					    Window owner = ((Node) event.getSource()).getScene().getWindow();
						String tempo = Dialog.showTextDialog("Tempo", owner);
						StateMachine.setTempo(Double.parseDouble(tempo));
						tempo = tempo.trim();
					}
				} catch (NumberFormatException e) {
					// Do nothing.
				}
				event.consume();
			}
		});
	}

	/** Initializes the play button and the stop button. */
	private void initializeControlButtons() {
		play = new PlayButton(controller.getPlayButton(), controller, il);
		stop = new StopButton(controller.getStopButton(), controller, il);

		play.link(stop);
		stop.link(play);

		play.setStaff(theStaff);
		stop.setStaff(theStaff);
	}

	/**
	 * Sets up the slider and arrows that the controls will have.
	 */
	private void initializeArrows() {
		leftArrow = new ArrowButton(controller.getLeftArrow(), ImageIndex.SCROLLBAR_LEFT1_PRESSED,
				ImageIndex.SCROLLBAR_LEFT1, controller, il);
		rightArrow = new ArrowButton(controller.getRightArrow(), ImageIndex.SCROLLBAR_RIGHT1_PRESSED,
				ImageIndex.SCROLLBAR_RIGHT1, controller, il);
		leftFastArrow = new ArrowButton(controller.getLeftFastArrow(), ImageIndex.SCROLLBAR_LEFT2_PRESSED,
				ImageIndex.SCROLLBAR_LEFT2, controller, il);
		rightFastArrow = new ArrowButton(controller.getRightFastArrow(), ImageIndex.SCROLLBAR_RIGHT2_PRESSED,
				ImageIndex.SCROLLBAR_RIGHT2, controller, il);

		leftArrow.setSkipAmount(-1);
		rightArrow.setSkipAmount(1);
		rightFastArrow.setSkipAmount(Integer.MAX_VALUE);
		leftFastArrow.setSkipAmount(-Integer.MAX_VALUE);

		leftArrow.setStaff(theStaff);
		rightArrow.setStaff(theStaff);
		rightFastArrow.setStaff(theStaff);
		leftFastArrow.setStaff(theStaff);
	}

	/**
	 * @return The play button of the controls set.
	 */
	public PlayButton getPlayButton() {
		return play;
	}

	/**
	 * @return The stop button of the controls set.
	 */
	public StopButton getStopButton() {
		return stop;
	}

	/**
	 * Sets the controller class.
	 *
	 * @param ct
	 *            The FXML controller class.
	 */
	public void setController(SMPFXController ct) {
		controller = ct;
	}

}