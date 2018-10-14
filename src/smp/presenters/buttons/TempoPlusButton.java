package smp.presenters.buttons;

import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import smp.components.Values;
import smp.models.stateMachine.ProgramState;
import smp.models.stateMachine.StateMachine;
import smp.presenters.api.button.ImagePushButton;

/**
 * This is a class that takes care of the adjustment of tempo in Super Mario
 * Paint.
 *
 * @author RehdBlob
 * @since 2013.09.28
 */
public class TempoPlusButton extends ImagePushButton {

	//TODO: auto-add these model comments
	//====Models====
	private ObjectProperty<ProgramState> programState;
	private DoubleProperty tempo;
	
    /** Tells us whether this is a plus or minus button. */
    private boolean isPositive;

    /** This is a timer object for click-and-hold. */
    private Timer t;

    /**
     * Default constructor
     *
     * @param tempoPlus
     *            The <code>ImageView</code> object that we want this adjustment
     *            button to be linked to.
     * @param ct
     *            The FXML controller object.
     * @param im
     *            The Image loader object.
     */
    public TempoPlusButton(ImageView tempoPlus) {
        super(tempoPlus);
        t = new Timer();
        this.setPositive(true);
        
        this.programState = StateMachine.getState();
        this.tempo = StateMachine.getTempo();
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
        ProgramState curr = this.programState.get();
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
                tempo.set(tempo.get() + ch);
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
