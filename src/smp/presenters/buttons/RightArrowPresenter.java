package smp.presenters.buttons;

import java.util.Timer;
import java.util.TimerTask;

import smp.ImageIndex;
import smp.components.Values;
import smp.models.staff.StaffNoteLine;
import smp.models.staff.StaffSequence;
import smp.models.stateMachine.StateMachine;
import smp.models.stateMachine.Variables;
import smp.presenters.api.button.ImagePushButton;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

/**
 * This is a button that points left or right.
 * 
 * @author RehdBlob
 * @since 2013.08.23
 * 
 */
public class RightArrowPresenter extends ImagePushButton {
	
	//TODO: auto-add these model comments
	//====Models====
	private DoubleProperty measureLineNum;
	private ObjectProperty<StaffSequence> theSequence;

    /** Tells us whether we're at the end of the file. */
    private static boolean endOfFile = false;
    
    /**
     * The amount of movement that this arrow button will cause on the staff.
     * For a regular arrow, this will be a single measure line, but that can be
     * changed. The fast arrows will generally go to the ends of the staff.
     */
    private double skipAmount;

    /** This is a timer object for click-and-hold. */
    private Timer t;

    /**
     * Default constructor.
     * 
     * @param i
     *            The <code>ImageView</code> object that we are going to make
     *            into a button.
     * @param ct
     *            The FXML controller object.
     * @param im
     *            The Image loader object.
     */
    public RightArrowPresenter(ImageView rightArrow) {
        super(rightArrow);
        t = new Timer();
        //TODO: create a stand-alone imageloader presenter and pass in images from there
        getImages(ImageIndex.SCROLLBAR_LEFT2_PRESSED, ImageIndex.SCROLLBAR_LEFT2);
        
        skipAmount = 1;
        
        this.measureLineNum = StateMachine.getMeasureLineNum();
		this.theSequence = Variables.theSequence;
    }

    /**
     * @param i
     *            The amount that we want this arrow button to skip on the
     *            staff. Positive values move the staff to the right while
     *            negative numbers move it to the left.
     */
    public void setSkipAmount(double i) {
        skipAmount = i;
    }

    /**
     * @return The amount of measure lines that the staff will move if one
     *         presses this button. Positive values move the staff to the right
     *         whilst negative values move it to the left.
     */
    public double getSkipAmount() {
        return skipAmount;
    }

    @Override
    protected void reactPressed(MouseEvent event) {
        super.reactPressed(event);
        bumpStaff();
        TimerTask tt = new clickHold();
        t.schedule(tt, Values.HOLDTIME, Values.REPEATTIME);
    }

    /** Bumps the staff by some amount. */
    //TODO: refactor into setupViewUpdater
	private void bumpStaff() {
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				measureLineNum.set(measureLineNum.get() + skipAmount);
				StaffSequence seq = theSequence.get();
				ObservableList<StaffNoteLine> s = seq.getTheLines();
				if (s.size() - Values.NOTELINES_IN_THE_WINDOW <= measureLineNum.get() && endOfFile) {
					int start = (int) s.size();
					for (int i = start; i < start + Values.NOTELINES_IN_THE_WINDOW * 2; i++)
						seq.addLine(new StaffNoteLine());
				}
				if (s.size() - Values.NOTELINES_IN_THE_WINDOW <= measureLineNum.get())
					endOfFile = true;
				else
					endOfFile = false;
			}
		});
	}

	@Override
    protected void reactReleased(MouseEvent event) {
        super.reactReleased(event);
        t.cancel();
        t = new Timer();
    }

    /**
     * This is a timer task that increments the current line of the song.
     * 
     * @author RehdBlob
     * @since 2014.01.02
     */
    class clickHold extends TimerTask {

        @Override
        public void run() {
            bumpStaff();
        }

    }

}
