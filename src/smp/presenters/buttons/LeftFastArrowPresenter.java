package smp.presenters.buttons;

import java.util.Timer;
import java.util.TimerTask;

import smp.ImageIndex;
import smp.components.Values;
import smp.models.stateMachine.StateMachine;
import smp.presenters.api.button.ImagePushButton;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

/**
 * This is a button that points left or right.
 * 
 * @author RehdBlob
 * @since 2013.08.23
 * 
 */
public class LeftFastArrowPresenter extends ImagePushButton {
	
	//TODO: auto-add these model comments
	//====Models====
	private DoubleProperty measureLineNum;
	
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
    public LeftFastArrowPresenter(ImageView leftFastArrow) {
        super(leftFastArrow);
        t = new Timer();
        //TODO: create a stand-alone imageloader presenter and pass in images from there
        getImages(ImageIndex.SCROLLBAR_LEFT2_PRESSED, ImageIndex.SCROLLBAR_LEFT2);
        
        skipAmount = -Double.MAX_VALUE;
        
        this.measureLineNum = StateMachine.getMeasureLineNum();
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
	private void bumpStaff() {
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				measureLineNum.set(measureLineNum.get() + skipAmount);
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
