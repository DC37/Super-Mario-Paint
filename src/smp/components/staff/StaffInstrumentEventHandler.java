package smp.components.staff;

import smp.ImageIndex;
import smp.ImageLoader;
import smp.SoundfontLoader;
import smp.components.Constants;
import smp.components.InstrumentIndex;
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


    /** The position of this note. */
    private int position;

    /**
     * State machine. The different representations are as follows: </br>
     * <b>0x0</b>: Mouse not in frame, nothing has been clicked. </br>
     * <b>0x1</b>: Mouse in frame, nothing has been clicked. A silhouette
     * of the current selected instrument is visible.</br>
     * <b>0x2</b>: Mouse in frame, and we've clicked once. A StaffNote has
     * been placed on the staff. </br>
     * <b>0x3</b>: Mouse not in frame, and we are now displaying a
     * StaffNote on the staff.</br>
     * <b>0x4</b>: Mouse in frame, and we're displaying an image
     * silhouette on top of the previous layer instrument.</br>
     * 
     */
    private int state = 0x0;

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
    private ImageView accidental;


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
            int pos) {
        position = pos;
        theImages = stPane.getChildren();
        accList = acc.getChildren();
    }

    @Override
    public void handle(Event event) {
        InstrumentIndex theInd =
                ButtonLine.getSelectedInstrument();
        if (event.getEventType() == MouseEvent.MOUSE_PRESSED) {
            if (((MouseEvent) event).getButton() == MouseButton.PRIMARY)
                leftMousePressed(theInd);
            else if (((MouseEvent) event).getButton() == MouseButton.SECONDARY)
                rightMousePressed(theInd);
            event.consume();
        } else if (event.getEventType() == MouseEvent.MOUSE_ENTERED) {
            mouseEntered(theInd);
            event.consume();
        } else if (event.getEventType() == MouseEvent.MOUSE_EXITED) {
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
        if (state == 0x1 || state == 0x4) {
            theStaffNote = new StaffNote(theInd, position, acc);
            theStaffNote.setImage(
                    ImageLoader.getSpriteFX(
                            theInd.imageIndex()));
            accidental = new ImageView();
            accidental.setImage(
                    ImageLoader.getSpriteFX(
                            switchAcc()));
            playSound(theInd, position, acc);
            theImages.remove(silhouette);
            accList.remove(accSilhouette);
            if (!theImages.contains(theStaffNote)) {
                theImages.add(theStaffNote);
            }
            if (!accList.contains(accidental)) {
                accList.add(accidental);
            }
            state = 0x2;
        } else if (state == 0x2) {
            playSound(theInd, position, acc);
            theImages.remove(silhouette);
            accList.remove(accSilhouette);
        }


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
        if (state == 0x1) {
            if (!theImages.isEmpty())
                theImages.remove(theImages.size() - 1);
            if (!accList.isEmpty())
                accList.remove(accList.size() - 1);
        } else if (state == 0x2 || state == 0x4) {
            theImages.remove(silhouette);
            theImages.remove(theImages.size() - 1);
            accList.remove(accSilhouette);
            accList.remove(accList.size() - 1);
            state = 0x1;
        }
    }


    /**
     * The method that is called when the mouse enters the object.
     * @param theInd The InstrumentIndex corresponding to what
     * instrument is currently selected.
     */
    private void mouseEntered(InstrumentIndex theInd) {
        StateMachine.setFocusPane(this);
        updateAccidental();
        if (state == 0x0) {
            silhouette.setImage(
                    ImageLoader.getSpriteFX(
                            theInd.imageIndex().silhouette()));
            theImages.add(silhouette);
            accSilhouette.setImage(
                    ImageLoader.getSpriteFX(
                            switchAcc().silhouette()));
            accList.add(accSilhouette);
            state = 0x1;
        } else if (state == 0x3) {
            silhouette.setImage(
                    ImageLoader.getSpriteFX(
                            theInd.imageIndex().silhouette()));
            theImages.add(silhouette);
            accSilhouette.setImage(
                    ImageLoader.getSpriteFX(
                            switchAcc().silhouette()));
            accList.add(accSilhouette);
            state = 0x4;
        }
    }

    /**
     * The method that is called when the mouse exits the object.
     * @param children List of Nodes that we have here, hopefully full of
     * ImageView-type objects.
     * @param theInd The InstrumentIndex corresponding to what
     * instrument is currently selected.
     */
    private void mouseExited(InstrumentIndex theInd) {
        if (state == 0x1) {
            theImages.remove(silhouette);
            accList.remove(accSilhouette);
            state = 0x0;
        } else if (state == 0x2) {
            theImages.remove(silhouette);
            accList.remove(accSilhouette);
            state = 0x3;
        } else if (state == 0x4) {
            theImages.remove(silhouette);
            accList.remove(accSilhouette);
            state = 0x1;
        }
    }

    /**
     * Updates how much we want to sharp / flat a note.
     */
    public void updateAccidental() {
        if (state != 0x1 && state != 0x4)
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
        case 0:
            accSilhouette.setImage(null);
            accList.remove(accSilhouette);
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
            break;
        }
        if (acc != 0 && !accList.contains(accSilhouette))
            accList.add(accSilhouette);
        if (Settings.debug > 1)
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
    public static void setAcc(int accidental) {
        acc = accidental;
    }

    /**
     * @return The amount that a note is to be offset from its usual position.
     */
    public static int getAcc() {
        return acc;
    }

}
