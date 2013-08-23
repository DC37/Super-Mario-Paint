package smp.components.staff;

import smp.ImageIndex;
import smp.ImageLoader;
import smp.SoundfontLoader;
import smp.components.Constants;
import smp.components.InstrumentIndex;
import smp.components.staff.sequences.StaffAccidental;
import smp.components.staff.sequences.StaffNote;
import smp.components.topPanel.ButtonLine;
import smp.stateMachine.Settings;
import smp.stateMachine.StateMachine;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

/**
 * A Staff event handler. The StaffImages implementation was getting bulky
 * and there are still many many features to be implemented here. This
 * handler primarily handles mouse events.
 * @author RehdBlob
 * @since 2013.07.27
 */
public class StaffInstrumentEventHandler implements EventHandler<Event> {


    /** The line number of this note, on the screen. */
    private int line;

    /** The position of this note. */
    private int position;

    /** Whether the mouse is in the frame or not. */
    private boolean mouseInFrame = false;

    /**
     * This is the list of image notes that we have. These should
     * all be ImageView-type objects.
     */
    private ObservableList<Node> theImages;

    /** The StackPane that will display sharps, flats, etc. */
    private ObservableList<Node> accList;

    /**
     * This is the <code>ImageView</code> object responsible for
     * displaying the silhouette of the note that we are about to place
     * on the staff.
     */
    private ImageView silhouette = new ImageView();

    /**
     * This is the <code>ImageView</code> object responsible
     * for displaying the silhouette of the sharp / flat of the note
     * that we are about to place on the staff.
     */
    private ImageView accSilhouette = new ImageView();

    /** The topmost image of the instrument. */
    private StaffNote theStaffNote;

    /**
     * This is the image that holds the different types of sharps/flats etc.
     */
    private StaffAccidental accidental;


    /** This is the amount that we want to sharp / flat / etc. a note. */
    private static int acc = 0;

    /**
     * Constructor for this StaffEventHandler. This creates a handler
     * that takes a StackPane and a position on the staff.
     * @param stPane The StackPane that we are interested in.
     * @param acc The accidental display pane.
     * @param position The position that this handler is located on the
     * staff.
     */
    public StaffInstrumentEventHandler(StackPane stPane, StackPane acc,
            int pos, int l) {
        position = pos;
        line = l;
        theImages = stPane.getChildren();
        accList = acc.getChildren();
        if ((Settings.debug & 0b10) == 0b10) {
            System.out.println("Line: " + l);
            System.out.println("Position: " + pos);
        }
    }


    @Override
    public void handle(Event event) {
        InstrumentIndex theInd =
                ButtonLine.getSelectedInstrument();

        if (event.getEventType() == MouseEvent.MOUSE_PRESSED) {
            MouseButton b = ((MouseEvent) event).getButton();
            if (b == MouseButton.PRIMARY)
                leftMousePressed(theInd);
            else if (b == MouseButton.SECONDARY)
                rightMousePressed(theInd);
            event.consume();

        } else if (event.getEventType() == MouseEvent.MOUSE_ENTERED) {
            mouseInFrame = true;
            mouseEntered(theInd);
            event.consume();

        } else if (event.getEventType() == MouseEvent.MOUSE_EXITED) {
            mouseInFrame = false;
            mouseExited(theInd);
            event.consume();
        }
    }

    /**
     * The method that is called when the left mouse button is pressed.
     * This is generally the signal to add an instrument to that line.
     * @param theInd The InstrumentIndex corresponding to what
     * instrument is currently selected.
     */
    private void leftMousePressed(InstrumentIndex theInd) {

        placeNote(theInd);

    }

    /**
     * Places a note where the mouse currently is.
     * @param theInd The <code>InstrumentIndex</code> that we are
     * going to use to place this note.
     */
    private void placeNote(InstrumentIndex theInd) {
        playSound(theInd, position, acc);

        theStaffNote = new StaffNote(theInd, position, acc);
        theStaffNote.setImage(
                ImageLoader.getSpriteFX(
                        theInd.imageIndex()));

        accidental = new StaffAccidental(theStaffNote);
        accidental.setImage(
                ImageLoader.getSpriteFX(
                        switchAcc()));

        theImages.remove(silhouette);
        accList.remove(accSilhouette);

        if (!theImages.contains(theStaffNote))
            theImages.add(theStaffNote);

        if (!accList.contains(accidental))
            accList.add(accidental);

    }

    /**
     * @return An <code>ImageIndex</code> based on the amount of
     * sharp or flat we want to implement.
     */
    private ImageIndex switchAcc() {
        switch (acc) {
        case 2:
            return ImageIndex.DOUBLESHARP;
        case 1:
            return ImageIndex.SHARP;
        case 0:
            return ImageIndex.BLANK;
        case -1:
            return ImageIndex.FLAT;
        case -2:
            return ImageIndex.DOUBLEFLAT;
        default:
            return ImageIndex.BLANK;
        }
    }

    /**
     * The method that is called when the right mouse button is pressed.
     * This is generally the signal to remove the instrument from that line.
     * @param theInd The InstrumentIndex corresponding to what
     * instrument is currently selected.
     */
    private void rightMousePressed(InstrumentIndex theInd) {
        theImages.remove(silhouette);
        if (!theImages.isEmpty())
            theImages.remove(theImages.size() - 1);

        accList.remove(accSilhouette);
        if (!accList.isEmpty())
            accList.remove(accList.size() - 1);
    }


    /**
     * The method that is called when the mouse enters the object.
     * @param theInd The InstrumentIndex corresponding to what
     * instrument is currently selected.
     */
    private void mouseEntered(InstrumentIndex theInd) {
        StateMachine.setFocusPane(this);
        updateAccidental();
        silhouette.setImage(
                ImageLoader.getSpriteFX(
                        theInd.imageIndex().silhouette()));
        if (!theImages.contains(silhouette))
            theImages.add(silhouette);
        accSilhouette.setImage(
                ImageLoader.getSpriteFX(
                        switchAcc().silhouette()));
        if (!accList.contains(accSilhouette))
            accList.add(accSilhouette);
    }

    /**
     * The method that is called when the mouse exits the object.
     * @param children List of Nodes that we have here, hopefully full of
     * ImageView-type objects.
     * @param theInd The InstrumentIndex corresponding to what
     * instrument is currently selected.
     */
    private void mouseExited(InstrumentIndex theInd) {

        theImages.remove(silhouette);
        accList.remove(accSilhouette);

    }

    /**
     * Updates how much we want to sharp / flat a note.
     */
    public void updateAccidental() {
        if (!mouseInFrame)
            return;
        if (StateMachine.isAltPressed() && StateMachine.isCtrlPressed())
            acc = -2;
        else if (StateMachine.isCtrlPressed() && StateMachine.isShiftPressed())
            acc = 2;
        else if (StateMachine.isShiftPressed())
            acc = 1;
        else if (StateMachine.isAltPressed() || StateMachine.isCtrlPressed())
            acc = -1;
        else
            acc = 0;

        switch (acc) {
        case 2:
            accSilhouette.setImage(ImageLoader.getSpriteFX(
                    ImageIndex.DOUBLESHARP_SIL));
            break;
        case 1:
            accSilhouette.setImage(ImageLoader.getSpriteFX(
                    ImageIndex.SHARP_SIL));
            break;
        case -1:
            accSilhouette.setImage(ImageLoader.getSpriteFX(
                    ImageIndex.FLAT_SIL));
            break;
        case -2:
            accSilhouette.setImage(ImageLoader.getSpriteFX(
                    ImageIndex.DOUBLEFLAT_SIL));
            break;
        default:
            accSilhouette.setImage(null);
            accList.remove(accSilhouette);
            break;
        }

        if (acc != 0 && !accList.contains(accSilhouette))
            accList.add(accSilhouette);

        if ((Settings.debug & 0b01) == 0b01)
            System.out.println("Accidental: " + acc);
    }

    /**
     * Plays a sound given an index and a position.
     * @param theInd The index at which this instrument is located at.
     * @param pos The position at which this note is located at.
     * @param acc The sharp / flat that we want to play this note at.
     */
    private static void playSound(InstrumentIndex theInd, int pos, int acc) {
        SoundfontLoader.playSound(Constants.staffNotes[pos].getKeyNum(),
                theInd, acc);
    }

    /**
     * Sets the amount that we want to sharp / flat a note.
     * @param accidental Any integer between -2 and 2.
     */
    public void setAcc(int accidental) {
        acc = accidental;
    }

    /**
     * @return The amount that a note is to be offset from its usual position.
     */
    public int getAcc() {
        return acc;
    }

    /**
     * @return The line that this handler is located on.
     */
    public int getLine() {
        return line;
    }

}
