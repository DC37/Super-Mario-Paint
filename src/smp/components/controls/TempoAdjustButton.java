package smp.components.controls;

import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Platform;
import javafx.beans.property.StringProperty;
import javafx.event.EventHandler;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import smp.components.general.ImagePushButton;
import smp.stateMachine.StateMachine;

/**
 * This is a class that takes care of the adjustment of tempo in
 * Super Mario Paint.
 * @author RehdBlob
 * @since 2013.09.28
 */
public class TempoAdjustButton extends ImagePushButton {

    /** This is the current tempo. */
    private StringProperty currTempo;

    /** Tells us whether this is a plus or minus button. */
    private boolean isPositive;

    /** This is a timer object for click-and-hold. */
    private Timer t;

    /**
     * Default constructor
     * @param i The <code>ImageView</code> object that we want this
     * adjustment button to be linked to.
     */
    public TempoAdjustButton(ImageView i) {
        super(i);
        t = new Timer();
        overrideHoldAndRelease();

    }

    /**
     * Gives us some click-and-hold functionality of these tempo adjust
     * buttons.
     */
    private void overrideHoldAndRelease() {
        theImage.setOnMousePressed(
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        TimerTask tt = new clickHold();
                        reactPressed(event);
                        t.scheduleAtFixedRate(tt, 100, 10);
                        event.consume();
                    }
                });

        theImage.setOnMouseReleased(
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        reactReleased(event);
                        t.cancel();
                        t = new Timer();
                        event.consume();
                    }
                });
    }

    /**
     * @param b Is this a positive button?
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

    /**
     * Sets the String property to display the tempo.
     * @param s This is the StringProperty that displays the tempo.
     */
    public void setStringProperty(StringProperty s) {
        currTempo = s;
    }

    @Override
    protected void reactPressed(MouseEvent event) {
        setPressed();
        addTempo(1);
    }


    @Override
    protected void reactReleased(MouseEvent event) {
        resetPressed();
    }


    /**
     * Makes it such that the application thread changes the tempo
     * of the song.
     * @param add The amount of tempo that you want to add. Usually
     * an integer.
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
                currTempo.setValue(String.valueOf(tempo));
            }
        });
    }

    /**
     * This is a timer task that increments the tempo of the song.
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
