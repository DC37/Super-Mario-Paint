package smp.components.buttons;

import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Platform;
import javafx.beans.property.StringProperty;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import smp.ImageLoader;
import smp.components.Values;
import smp.components.general.ImagePushButton;
import smp.fx.SMPFXController;
import smp.stateMachine.ProgramState;
import smp.stateMachine.StateMachine;

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
        t = new Timer();

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
            addTempo(1);
            TimerTask tt = new clickHold();
            t.schedule(tt, Values.HOLDTIME, Values.REPEATTIME);
        }
    }

    @Override
    protected void reactReleased(MouseEvent event) {
        t.cancel();
        t = new Timer();
        resetPressed();
    }

    /**
     * Makes it such that the application thread changes the tempo of the song.
     *
     * @param add
     *            The amount of tempo that you want to add. Usually an integer.
     */
    private void addTempo(final double add) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                double ch = 0;
                if (isPositive)
                    ch = add;
                else
                    ch = -add;
                double tempo = StateMachine.getTempo() + ch;
                StateMachine.setTempo(tempo);
            }
        });
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
            addTempo(1);
        }

    }

}
