package gui.components;

import gui.SMPFXController;
import gui.Staff;
import gui.components.buttons.elements.ArrowButton;
import gui.loaders.ImageIndex;
import gui.loaders.ImageLoader;

/**
 * These are the control buttons for the program.
 *
 * @author RehdBlob
 * @since 2012.09.04
 */
public class Controls {

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
	 * Sets the controller class.
	 *
	 * @param ct
	 *            The FXML controller class.
	 */
	public void setController(SMPFXController ct) {
		controller = ct;
	}

}