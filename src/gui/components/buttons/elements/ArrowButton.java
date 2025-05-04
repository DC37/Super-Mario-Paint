package smp.components.buttons;

import java.util.Timer;
import java.util.TimerTask;

import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import smp.ImageIndex;
import smp.ImageLoader;
import smp.components.Values;
import smp.components.general.ImagePushButton;
import smp.fx.SMPFXController;
import smp.stateMachine.StateMachine;

/**
 * This is a button that points left or right.
 * 
 * @author RehdBlob
 * @since 2013.08.23
 * 
 */
public class ArrowButton extends ImagePushButton {

    /**
     * The amount of movement that this arrow button will cause on the staff.
     * For a regular arrow, this will be a single measure line, but that can be
     * changed. The fast arrows will generally go to the ends of the staff.
     */
    private int skipAmount;

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
    public ArrowButton(ImageView i, ImageIndex pr,
            ImageIndex notPr, SMPFXController ct, ImageLoader im) {
        super(i, ct, im);
        t = new Timer();
        getImages(pr, notPr);
    }

    /**
     * @param i
     *            The amount that we want this arrow button to skip on the
     *            staff. Positive values move the staff to the right while
     *            negative numbers move it to the left.
     */
    public void setSkipAmount(int i) {
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
        if (StateMachine.isPlaybackActive())
            return;
        
        int currLoc = StateMachine.getMeasureLineNum();
        int newLoc = currLoc + skipAmount;
        
        // Deal with integer overflow
        if (skipAmount > 0 && newLoc < 0)
            newLoc = Integer.MAX_VALUE;
        
        if (skipAmount > 0 && currLoc + Values.NOTELINES_IN_THE_WINDOW == StateMachine.getMaxLine()) {
            int newSize = StateMachine.getMaxLine() + 2*Values.NOTELINES_IN_THE_WINDOW;
            theStaff.getSequence().resize(newSize);
            StateMachine.setMaxLine(theStaff.getSequence().getLength());
        }
        
        theStaff.setLocation(newLoc);
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
