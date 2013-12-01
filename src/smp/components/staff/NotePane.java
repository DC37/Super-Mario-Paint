package smp.components.staff;

import javafx.scene.layout.StackPane;

/**
 * This is a sort of a hack to allow us to get the
 * StaffInstrumentEventHandler from the StackPanes that
 * we have the notes in.
 * @author RehdBlob
 * @since 2013.12.01
 */
public class NotePane {

    /** This handles the events of the StackPane. */
    private StaffInstrumentEventHandler hd;

    /** This is the pane that this NotePane is associated with. */
    private StackPane thePane;

    /**
     * Makes a new NotePane, linking it to the StackPane and
     * StaffInstrumentEventHandler provided.
     * @param st The StackPane that we're interested in.
     * @param h The StaffInstrumentEventHandler we're interested in.
     */
    public NotePane(StackPane st, StaffInstrumentEventHandler h) {
        thePane = st;
        hd = h;
    }

    /**
     * @param s The StaffInstrumentEventHandler that we want to link with this
     * object.
     */
    public void setStaffInstrumentEventHandler(StaffInstrumentEventHandler s) {
        hd = s;
    }

    /**
     * @return The StaffInstrumentEventHandler that we want to get from this
     * StackPane.
     */
    public StaffInstrumentEventHandler getStaffInstrumentEventHandler() {
        return hd;
    }

    /**
     * @return The StackPane that this is associated with.
     */
    public StackPane getPane() {
        return thePane;
    }

}
