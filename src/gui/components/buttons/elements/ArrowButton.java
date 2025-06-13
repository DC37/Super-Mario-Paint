package gui.components.buttons.elements;

import java.util.Timer;
import java.util.TimerTask;

import gui.SMPFXController;
import gui.Values;
import gui.components.buttons.ImagePushButton;
import gui.loaders.ImageIndex;
import gui.loaders.ImageLoader;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

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
        theStaff.bumpStaff(skipAmount);
        TimerTask tt = new clickHold();
        t = new Timer();
        t.schedule(tt, Values.HOLDTIME, Values.REPEATTIME);
    }

    @Override
    protected void reactReleased(MouseEvent event) {
        super.reactReleased(event);
        if (t != null)
            t.cancel();
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
            theStaff.bumpStaff(skipAmount);
        }

    }

}
