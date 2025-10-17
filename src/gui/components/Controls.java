package gui.components;

import java.io.File;
import java.util.ArrayList;

import backend.songs.StaffSequence;
import gui.Dialog;
import gui.ProgramState;
import gui.SMPFXController;
import gui.Staff;
import gui.StateMachine;
import gui.Utilities;
import gui.components.buttons.elements.AddButton;
import gui.components.buttons.elements.ArrowButton;
import gui.components.buttons.elements.ClipboardButton;
import gui.components.buttons.elements.DeleteButton;
import gui.components.buttons.elements.LoopButton;
import gui.components.buttons.elements.MoveButton;
import gui.components.buttons.elements.MuteButton;
import gui.components.buttons.elements.MuteInstButton;
import gui.components.buttons.elements.PlayButton;
import gui.components.buttons.elements.StopButton;
import gui.components.buttons.elements.TempoAdjustButton;
import gui.loaders.ImageIndex;
import gui.loaders.ImageLoader;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.ListView;
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

	/** The pointer to the loop button on the staff. */
	private LoopButton loop;

	/** The pointer to the button that adds a new song on the arranger. */
	private AddButton add;

	/** The pointer to the button that deletes a song from a list. */
	private DeleteButton delete;

	/** The pointer to the button that moves a song up. */
	private MoveButton moveUp;

	/** The pointer to the button that moves a song down. */
	private MoveButton moveDown;

	/** The pointer to the mute button on the staff. */
	private MuteButton mute;

	/** The pointer to the mute-all button ont he staff. */
	private MuteInstButton muteA;
	
	/** The pointer to the clipboard selection button on the staff. */
	private ClipboardButton clipboard;

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

	/** The ListView of songs for the arranger. */
	private ListView<String> theList;

	/** The controller object. */
	private SMPFXController controller;

	/** The image loader object. */
	private ImageLoader il;

	/**
	 * Initializes the set of controls that will be used in Super Mario Paint.
	 */
	public Controls(Staff st, SMPFXController ct, ImageLoader im, ListView<String> l) {
		theList = l;
		il = im;
		theStaff = st;
		setController(ct);
		theList = controller.getArrangementList();
		initializeArrows();
		initializeControlButtons();
		initializeTempoButtons();
		initializeArrangementList();

	}

	/**
	 * Adds in the listener behaviour for the arrangement list.
	 */
	private void initializeArrangementList() {
		// theList.setFixedCellSize(20);
		theList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (StateMachine.getState() == ProgramState.ARR_PLAYING)
					return;
				int x = theList.getSelectionModel().getSelectedIndex();
				if (x != -1) {
					ArrayList<StaffSequence> s = theStaff.getArrangement().getTheSequences();
					ArrayList<File> f = theStaff.getArrangement().getTheSequenceFiles();
					Window owner = theList.getScene().getWindow();
					Utilities.loadSequenceFromArrangement(f.get(x), theStaff, controller, owner);
					s.set(x, theStaff.getSequence());
				}
			}
		});
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
					ProgramState curr = StateMachine.getState();
					if (curr == ProgramState.EDITING) {
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
		loop = new LoopButton(controller.getLoopButton(), controller, il);
		mute = new MuteButton(controller.getMuteButton(), controller, il);
		muteA = new MuteInstButton(controller.getMuteAButton(), controller, il);
		clipboard = new ClipboardButton(controller.getClipboardButton(), controller, il);
		add = new AddButton(controller.getAddButton(), controller, il);
		delete = new DeleteButton(controller.getDeleteButton(), controller, il);
		moveUp = new MoveButton(controller.getUpButton(), 1, controller, il);
		moveDown = new MoveButton(controller.getDownButton(), -1, controller, il);

		mute.setMuteButton(muteA);
		muteA.setMuteButton(mute);
		mute.setStaff(theStaff);
		muteA.setStaff(theStaff);

		play.link(stop);
		stop.link(play);

		play.setStaff(theStaff);
		stop.setStaff(theStaff);

		add.setStaff(theStaff);
		delete.setStaff(theStaff);
		moveUp.setStaff(theStaff);
		moveDown.setStaff(theStaff);

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
	 * @param b
	 *            Whether we want to turn on arranger mode or not.
	 */
	public void setArrangerMode(boolean b) {
		if (b)
			setArrangerMode();
		else
			setEditingMode();
	}

	/** Changes the current interface to the arranger mode. */
	private void setArrangerMode() {
		changeCenterList();
		loop.release();
		StateMachine.setState(ProgramState.ARR_EDITING);

	}

	/**
	 * Adds the list of songs characteristic of the arranger mode.
	 */
	private void changeCenterList() {
		theStaff.setSequenceName(controller.getNameTextField().getText());
		controller.getNameTextField().setPromptText("Arrangement Name");
		controller.getNameTextField().setText(theStaff.getArrangementName());
		controller.getArrangementList().setVisible(true);
		controller.getDeleteButton().setVisible(true);
		controller.getAddButton().setVisible(true);
		controller.getUpButton().setVisible(true);
		controller.getDownButton().setVisible(true);
	}

	/** Changes the current interface to the normal song editing mode. */
	private void setEditingMode() {
		revertCenterList();
		StateMachine.setState(ProgramState.EDITING);
	}

	/**
	 * Reverts the center list into just the middle panel.
	 */
	private void revertCenterList() {
		theStaff.setArrangementName(controller.getNameTextField().getText());
		controller.getNameTextField().setPromptText("Song Name");
		controller.getNameTextField().setText(theStaff.getSequenceName());
		controller.getArrangementList().setVisible(false);
		controller.getDeleteButton().setVisible(false);
		controller.getAddButton().setVisible(false);
		controller.getUpButton().setVisible(false);
		controller.getDownButton().setVisible(false);
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
	 * @return The loop button of the controls set.
	 */
	public LoopButton getLoopButton() {
		return loop;
	}

	/**
	 * @return The mute button of the controls set.
	 */
	public MuteButton getMuteButton() {
		return mute;
	}

	/**
	 * @return The 'mute-all' button of the controls set.
	 */
	public MuteInstButton getMuteAButton() {
		return muteA;
	}

	/**
	 * @return The clipboard button.
	 */
	public ClipboardButton getClipboardButton() {
		return clipboard;
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