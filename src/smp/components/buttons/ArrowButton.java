package smp.components.buttons;

import java.util.Timer;
import java.util.TimerTask;

import smp.ImageIndex;
import smp.ImageLoader;
import smp.components.Values;
import smp.components.general.ImagePushButton;
import smp.components.staff.sequences.StaffNoteLine;
import smp.components.staff.sequences.StaffSequence;
import smp.fx.SMPFXController;
import smp.stateMachine.StateMachine;
import javafx.application.Platform;
import javafx.scene.control.Slider;
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
    private double skipAmount;

    /** Tells us whether we're at the end of the file. */
    private static boolean endOfFile = false;

    /** This is the slider that the scrollbar will affect. */
    private Slider scrollbar;

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
    public ArrowButton(ImageView i, Slider scr, ImageIndex pr,
            ImageIndex notPr, SMPFXController ct, ImageLoader im) {
        super(i, ct, im);
        t = new Timer();
        scrollbar = scr;
        getImages(pr, notPr);
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
        if (StateMachine.isPlaybackActive())
            return;
        
        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                scrollbar.adjustValue(scrollbar.getValue() + skipAmount);
                if (scrollbar.getMax() <= scrollbar.getValue() && endOfFile) {
                    StateMachine.setMaxLine(StateMachine.getMaxLine() + Values.NOTELINES_IN_THE_WINDOW * 2);
                    StaffSequence s = theStaff.getSequence();
                    int start = (int) scrollbar.getMax();
                    for (int i = start; i < start
                            + Values.NOTELINES_IN_THE_WINDOW * 2; i++)
                        s.addLine(new StaffNoteLine());
                }
                if (scrollbar.getMax() <= scrollbar.getValue())
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
     * @param b
     *            Whether we are at the end of the file or not.
     */
    public static void setEndOfFile(boolean b) {
        endOfFile = b;
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
