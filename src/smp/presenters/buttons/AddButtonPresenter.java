package smp.presenters.buttons;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import smp.models.staff.StaffArrangement;
import smp.models.stateMachine.ProgramState;
import smp.models.stateMachine.Settings;
import smp.models.stateMachine.StateMachine;
import smp.models.stateMachine.Variables;
import smp.presenters.api.button.ImagePushButton;

/**
 * This is a button that adds a song to an arrangement.
 *
 * @author RehdBlob
 * @since 2014.07.27
 */
public class AddButtonPresenter extends ImagePushButton {

	//TODO: auto-add these model comments
	//====Models====
	private ObjectProperty<StaffArrangement> theArrangement;
	private ObjectProperty<ProgramState> programState;
	private StringProperty theSequenceName;

	/**
     * Default constructor.
     *
     * @param addButton
     *            The <code>ImageView</code> object that we are going to make
     *            into a button.
     * @param ct
     *            The FXML controller object.
     * @param im
     *            The Image loader object.
     */
    public AddButtonPresenter(ImageView addButton) {
        super(addButton);
        this.theArrangement = Variables.theArrangement;
        this.theSequenceName = Variables.theSequenceName;
        this.programState = StateMachine.getState();
        setupViewUpdater();
    }

	@Override
    protected void reactPressed(MouseEvent event) {
        if (this.programState.get() != ProgramState.ARR_PLAYING) {
            if ((Settings.debug & 0b100000) != 0)
                System.out.println("Add song");

            if (Variables.theSequenceFile.get() != null) {
                StateMachine.setArrModified(true);
                this.theArrangement.get().getTheSequenceNames().add(this.theSequenceName.get());
                this.theArrangement.get().add(Variables.theSequence.get(),
                        Variables.theSequenceFile.get());
                //TODO: create a stand-alone sound controller presenter
                controller.getSoundfontLoader().storeInCache();
            }
        }
    }

    @Override
    protected void reactReleased(MouseEvent event) {

    }

    private void setupViewUpdater() {
		theImage.setVisible(false);
    	this.programState.addListener(new ChangeListener<Object>() {

			@Override
			public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {
				if (newValue.equals(ProgramState.EDITING))
					theImage.setVisible(false);
				else if (newValue.equals(ProgramState.ARR_EDITING))
					theImage.setVisible(true);
			}
		});
	}
}
