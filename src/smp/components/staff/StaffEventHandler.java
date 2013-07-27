package smp.components.staff;

import smp.ImageLoader;
import smp.SoundfontLoader;
import smp.components.Constants;
import smp.components.InstrumentIndex;
import smp.components.topPanel.ButtonLine;
import smp.stateMachine.StateMachine;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

/**
 * A Staff event handler. The StaffImages implementation was getting bulky
 * and there are still many many features to be implemented here...
 * @author RehdBlob
 * @since 2013.07.27
 */
public class StaffEventHandler implements EventHandler<MouseEvent> {


    /** The position of this note. */
    private int position;

    /** The topmost image of the instrument. */
    ImageView theImage = new ImageView();

    /** Whether we have clicked on the topmost image. */
    private boolean clicked = false;

    /** The StackPane that this handler is attached to. */
    private StackPane s;

    /**
     * Constructor for this StaffEventHandler. This creates a handler
     * that takes a StackPane and a position on the staff.
     * @param stPane The StackPane that we are interested in.
     * @param position The position that this handler is located on the
     * staff.
     */
    public StaffEventHandler(StackPane stPane, int pos) {
        position = pos;
        s = stPane;
    }

    @Override
    public void handle(MouseEvent event) {
        InstrumentIndex theInd =
                ButtonLine.getSelectedInstrument();
        ObservableList<Node> children = s.getChildren();
        if (event.getEventType() == MouseEvent.MOUSE_PRESSED) {
            theImage.setImage(
                    ImageLoader.getSpriteFX(
                            theInd.imageIndex()));
            if (!children.contains(theImage))
                children.add(theImage);

            playSound(theInd, position);
            theImage = new ImageView();

        } else if (event.getEventType() == MouseEvent.MOUSE_ENTERED) {
            theImage.setImage(
                    ImageLoader.getSpriteFX(
                            theInd.imageIndex().silhouette()));
            if (!children.contains(theImage))
                children.add(theImage);

        } else if (event.getEventType() == MouseEvent.MOUSE_EXITED) {
            if (children.contains(theImage))
                children.remove(theImage);
            if (!children.isEmpty())
                theImage = (ImageView) children.get(0);
            else
                theImage = new ImageView();
        }
    }

    /**
     * Plays a sound given an index and a position.
     * @param theInd The index at which this instrument is located at.
     * @param pos The position at which this note is located at.
     */
    private static void playSound(InstrumentIndex theInd, int pos) {
        int acc;
        if (StateMachine.isAltPressed() || StateMachine.isCtrlPressed())
            acc = -1;
        else if (StateMachine.isShiftPressed())
            acc = 1;
        else
            acc = 0;
        SoundfontLoader.playSound(Constants.staffNotes[pos].getKeyNum(),
                theInd, acc);
    }

}
