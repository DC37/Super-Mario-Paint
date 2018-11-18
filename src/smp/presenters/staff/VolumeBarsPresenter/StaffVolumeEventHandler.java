package smp.presenters.staff.VolumeBarsPresenter;

import smp.components.Values;
import smp.models.staff.StaffNoteLine;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

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

    /** The StaffNoteLine that this event handler is associated with. */
    private ObjectProperty<StaffNoteLine> theLine;
    
    /** The text representing the volume bar the mouse is currently hovering over. */
    private static Text volText = new Text("");
    
	/**
	 * Mouse position. Used for finding which volume bar the mouse is hovering
	 * over. Note: mouseX is set only to the beginning x coordinate of the
	 * volume bar's stack pane it hovers over.
	 */
	private static double mouseX;
	private static double mouseY;

	//TODO:
//	private ModifySongManager commandManager;
	
	/** Makes a new StaffVolumeEventHandler. 
	 * @param windowLine */
    public StaffVolumeEventHandler(StackPane st, ObjectProperty<StaffNoteLine> windowLine) {
        stp = st;
        theLine = windowLine;
//        commandManager = cm;
        setupViewUpdater();
    }

	private void setupViewUpdater() {
		theLine.addListener(new ChangeListener<StaffNoteLine>() {

			@Override
			public void changed(ObservableValue<? extends StaffNoteLine> observable, StaffNoteLine oldValue,
					StaffNoteLine newValue) {
				if (stpHasMouse()) {
					mouseExited();
					mouseEntered();
				}
			}
		});
	}

	@Override
    public void handle(Event event) {
    	if(event instanceof MouseEvent && 
    			((MouseEvent)event).isPrimaryButtonDown()){
    		mouseX = stp.getBoundsInParent().getMinX();
    		mouseY = stp.getBoundsInParent().getMinY();
    		mousePressed((MouseEvent) event);
    		mouseEntered();
    	} else if(event.getEventType() == MouseEvent.MOUSE_ENTERED){
    		mouseX = stp.getBoundsInParent().getMinX();
    		mouseY = stp.getBoundsInParent().getMinY();
    		mouseEntered();
    	} else if(event.getEventType() == MouseEvent.MOUSE_EXITED){
    		mouseX = -1;
    		mouseY = -1;
    		mouseExited();
    	} else if(event.getEventType() == MouseEvent.MOUSE_RELEASED) {
    		mouseReleased();
    	} else {
    		mouseEntered();
    	}
    }

    /** Called whenever the mouse is pressed. */
    private void mousePressed(MouseEvent event) {
        if (!theLine.get().getNotes().isEmpty()) {
        	
//TODO:
//        	if(event.getEventType() == MouseEvent.MOUSE_PRESSED)
//        		commandManager.execute(new RemoveVolumeCommand(theLine, theLine.getVolume()));

        	if(event.getY() < 0 || stp.getHeight() < event.getY())
        		return;
            double h = stp.getHeight() - event.getY();
//            System.out.println("SGH:" + stp.getHeight() + "EGY:" + event.getY());
            try {
                setVolumePercent(h / stp.getHeight());
            } catch (IllegalArgumentException e) {
                setVolume(Values.MAX_VELOCITY);
            }
        }
    }
    
    private void mouseReleased() {
    	//TODO:
//    	commandManager.execute(new AddVolumeCommand(theLine, theLine.getVolume()));//.addVolume(theLine, theLine.getVolume());
//    	commandManager.record();
    }
    
    /** Called whenever the mouse enters the area. */
    private void mouseEntered() {
    	if (!theLine.get().getNotes().isEmpty()) {
	        if(!stp.getChildren().contains(volText)){
	        	stp.getChildren().add(volText);
	        }
	        volText.setText("" + theLine.get().getVolume().get());
    	}
    }
    
    /** Called whenever the mouse exits the area. */
    private void mouseExited() {
    	stp.getChildren().remove(volText);
    }

    /**
     * Sets the volume of this note based on the y location of the click.
     * @param y The y-location of the click.
     */
    private void setVolume(double y) {
        theLine.get().setVolume(y);
    }

    /**
     * Sets the volume of this note based on the y location of the click.
     * @param y The percent we want to set this note line at. Should be
     * between 0 and 1.
     */
    private void setVolumePercent(double y) throws IllegalArgumentException {
        theLine.get().setVolumePercent(y);
    }
    
    /**
     * @return Whether the mouse is currently in the volume bar's stack pane.
     */
	private boolean stpHasMouse(){
    	return mouseX >= stp.getBoundsInParent().getMinX() 
    			&& mouseX < stp.getBoundsInParent().getMaxX()
    			&& mouseY >= stp.getBoundsInParent().getMinY();
    }

    @Override
    public String toString() {
        return "Line: " + line;
    }
}
