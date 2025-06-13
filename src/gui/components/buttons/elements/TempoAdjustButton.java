package gui.components.buttons.elements;

import java.util.Timer;
import java.util.TimerTask;

import gui.ProgramState;
import gui.SMPFXController;
import gui.StateMachine;
import gui.Values;
import gui.components.buttons.ImagePushButton;
import gui.loaders.ImageLoader;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

/**
 * This is a class that takes care of the adjustment of tempo in Super Mario
 * Paint.
 *
 * @author RehdBlob
 * @since 2013.09.28
 */
public class TempoAdjustButton extends ImagePushButton {

    /** Tells us whether this is a plus or minus button. */
    private boolean isPositive;

    /** This is a timer object for click-and-hold. */
    private Timer t;

    /**
     * Default constructor
     *
     * @param i
     *            The <code>ImageView</code> object that we want this adjustment
     *            button to be linked to.
     * @param ct
     *            The FXML controller object.
     * @param im
     *            The Image loader object.
     */
    public TempoAdjustButton(ImageView i, SMPFXController ct, ImageLoader im) {
        super(i, ct, im);
    }

    /**
     * @param b
     *            Is this a positive button?
     */
    public void setPositive(boolean b) {
        isPositive = b;
    }

    /**
     * @return Whether this is a positive button.
     */
    public boolean isPositive() {
        return isPositive;
    }

    @Override
    protected void reactPressed(MouseEvent event) {
        ProgramState curr = StateMachine.getState();
        if (curr == ProgramState.EDITING) {
            setPressed();
            theStaff.addTempo(1, isPositive);
            TimerTask tt = new clickHold();
            t = new Timer();
            t.schedule(tt, Values.HOLDTIME, Values.REPEATTIME);
        }
    }

    @Override
    protected void reactReleased(MouseEvent event) {
        if (t != null)
            t.cancel();
        resetPressed();
    }

    /**
     * This is a timer task that increments the tempo of the song.
     *
     * @author RehdBlob
     * @since 2013.11.10
     */
    class clickHold extends TimerTask {

        @Override
        public void run() {
            theStaff.addTempo(1, isPositive);
        }

    }

}
