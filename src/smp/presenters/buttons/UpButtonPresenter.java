package smp.presenters.buttons;

import java.io.File;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import smp.models.staff.StaffArrangement;
import smp.models.staff.StaffSequence;
import smp.models.stateMachine.ProgramState;
import smp.models.stateMachine.Settings;
import smp.models.stateMachine.StateMachine;
import smp.models.stateMachine.Variables;
import smp.presenters.api.button.ImagePushButton;

/**
 * This is a button that moves a song on an arrangement.
 *
 * @author RehdBlob
 * @since 2014.07.27
 */
public class UpButtonPresenter extends ImagePushButton {

    /** The amount to move a song up or down. */
    private int moveAmt = 0;
	private ObservableList<String> arrangementList;
	private ObjectProperty<MultipleSelectionModel<String>> selectionModelProperty;
	private BooleanProperty arrModified;
	private ObjectProperty<StaffArrangement> theArrangement;

    /**
     * Default constructor.
     *
     * @param downButton
     *            The <code>ImageView</code> object that we are going to make
     *            into a button.
     * @param ct
     *            The FXML controller object.
     * @param im
     *            The Image loader object.
     */
    public UpButtonPresenter(ImageView downButton) {
        super(downButton);
        moveAmt = 1;
        
        this.arrangementList = Variables.arrangementList;
        this.selectionModelProperty = Variables.selectionModelProperty;
        this.arrModified = StateMachine.getArrModified();
        this.theArrangement = Variables.theArrangement;
    }

    @Override
    protected void reactPressed(MouseEvent event) {
        ProgramState curr = StateMachine.getState().get();
        if (curr != ProgramState.ARR_PLAYING) {
            if ((Settings.debug & 0b100000) != 0)
                System.out.println("Move song " + moveAmt);
            ObservableList<String> l = this.arrangementList;
            int x = this.selectionModelProperty.get().getSelectedIndex();
            if (x != -1) {
            	this.arrModified.set(true);
                Object[] o = this.theArrangement.get().remove(x);
                String s = l.remove(x);
                StaffSequence ss = (StaffSequence) o[0];
                File f = (File) o[1];
                int moveTo = x - moveAmt;
                if (moveTo > l.size())
                    moveTo = l.size();
                if (moveTo < 0)
                    moveTo = 0;
                l.add(moveTo, s);
				this.theArrangement.get().add(moveTo, ss, f);
				this.selectionModelProperty.get().select(moveTo);
            }
        }
    }

    @Override
    protected void reactReleased(MouseEvent event) {

    }

}
