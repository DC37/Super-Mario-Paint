package smp.components.staff;

import smp.ImageIndex;
import smp.ImageLoader;
import smp.components.Values;
import smp.components.staff.sequences.StaffNoteLine;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

/**
 * This takes care of the volume bars on the staff.
 * @author RehdBlob
 * @since 2013.12.01
 *
 */
public class StaffVolumeEventHandler implements EventHandler<Event> {

    /** The line number of this volume bar, on the screen. */
    private int line;

    /** The StackPane that this event handler is linked to. */
    private StackPane stp;

    /** The ImageView object that is this volume bar. */
    private ImageView theVolBar;

    /** The StaffNoteLine that this event handler is associated with. */
    private StaffNoteLine theLine;
    
    /** The ImageLoader class. */
    private ImageLoader il;

    /** Makes a new StaffVolumeEventHandler. */
    public StaffVolumeEventHandler(StackPane st, ImageLoader i) {
        stp = st;
        il = i;
        theVolBar = (ImageView) st.getChildren().get(0);
        theVolBar.setImage(il.getSpriteFX(ImageIndex.VOL_BAR));
        theVolBar.setVisible(false);
    }

    @Override
    public void handle(Event event) {
//        if (event.getEventType() == MouseEvent.MOUSE_PRESSED) {
//            mousePressed((MouseEvent) event);
//        } else if (event.getEventType() == MouseEvent.DRAG_DETECTED) {
//            mouseDragStart();
//        } else if (event.getEventType() == DragEvent.DRAG_DONE) {
//            mouseDragEnd();
//        }
    	if(((MouseEvent)event).isPrimaryButtonDown()){
    		mousePressed((MouseEvent) event);
    	}
    }

    /** Called whenever the mouse is pressed. */
    private void mousePressed(MouseEvent event) {
        if (!theLine.getNotes().isEmpty()) {
        	if(event.getY() < 0 || stp.getHeight() < event.getY())
        		return;
            double h = stp.getHeight() - event.getY();
//            System.out.println("SGH:" + stp.getHeight() + "EGY:" + event.getY());
            setVolumeDisplay(h);
            try {
                setVolumePercent(h / stp.getHeight());
            } catch (IllegalArgumentException e) {
                setVolume(Values.MAX_VELOCITY);
                setVolumeDisplay(stp.getHeight());
            }
        }
    }

    /** Called whenever the mouse is dragged. */
    private void mouseDragStart() {
    	
    }

    /** Called whenever we finish dragging the mouse. */
    private void mouseDragEnd() {

    }

    /**
     * Sets the volume of this note based on the y location of the click.
     * @param y The y-location of the click.
     */
    private void setVolume(double y) {
        theLine.setVolume(y);
    }

    /**
     * Sets the volume of this note based on the y location of the click.
     * @param y The percent we want to set this note line at. Should be
     * between 0 and 1.
     */
    private void setVolumePercent(double y) throws IllegalArgumentException {
        theLine.setVolumePercent(y);
    }

    /**
     * Displays the volume of this note line.
     * @param y The volume that we want to show.
     */
    public void setVolumeDisplay(double y) {
        if (y <= 0) {
            theVolBar.setVisible(false);
            return;
        }
        theVolBar.setVisible(true);
        theVolBar.setFitHeight(y);
    }

    /**
     * Do we actually want this volume bar to be visible?
     * @param b Whether this volume bar is visible or not.
     */
    public void setVolumeVisible(boolean b) {
        theVolBar.setVisible(b);
    }

    /**
     * Sets the StaffNoteLine that this event handler is controlling.
     * @param s The StaffNoteLine that this handler is controlling
     * at the moment.
     */
    public void setStaffNoteLine(StaffNoteLine s) {
        theLine = s;
    }

    /**
     * @return The StaffNoteLine that this handler is currently controlling.
     */
    public StaffNoteLine getStaffNoteLine() {
        return theLine;
    }

    /**
     * Updates the volume display on this volume displayer.
     */
    public void updateVolume() {
        setVolumeDisplay(theLine.getVolumePercent() * stp.getHeight());
        if (theLine.getVolume() == 0 || theLine.isEmpty()) {
            setVolumeVisible(false);
        }
    }

    @Override
    public String toString() {
        return "Line: " + line;
    }



}
