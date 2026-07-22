package gui.clipboard;

import gui.SMPFXController;
import gui.StateMachine;
import gui.events.ClipboardHandlerMaker;
import javafx.event.EventHandler;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

/**
 * Event Handler for rubber band which follows mouse movements. Mouse moves and
 * the rubber band will adjust. Move to the edges of the pane and the pane will
 * readjust position.
 *
 * @author j574y923
 */
public class StaffRubberBandEventHandler implements EventHandler<MouseEvent> {

    StaffRubberBand rubberBand;
    
    SMPFXController controller;
    Pane rubberBandLayer;
    StaffClipboard theStaffClipboard;
    
    /** Get line with these */
    private double mouseX;
    
    public StaffRubberBandEventHandler(StaffRubberBand rb, SMPFXController ct, Pane basePane, StaffClipboard clippy) {
        rubberBand = rb;
        controller = ct;
        theStaffClipboard = clippy;
        rubberBandLayer = basePane;
        
        ClipboardHandlerMaker.of(this).initializeIn(basePane);
    }

    @Override
    public void handle(MouseEvent mouseEvent) {
        mouseX = mouseEvent.getX();
        // the middlebutton can now bypass the clipboard button @since v1.1.2
        if(!StateMachine.isClipboardPressed() && !mouseEvent.isMiddleButtonDown()){
            
            // for middlebutton handling @since v1.1.2
            if (mouseEvent.getEventType() == MouseEvent.MOUSE_RELEASED) {
                if(theStaffClipboard.getAPI().isRubberBandActive())
                    theStaffClipboard.getAPI().endBand();
            }
            else if (mouseEvent.getEventType() == MouseEvent.MOUSE_PRESSED) 
                theStaffClipboard.getAPI().clearSelection(); 
            
            return;
        }
        if (mouseEvent.getEventType() == MouseEvent.MOUSE_PRESSED) {
            
            if(!mouseEvent.isControlDown()) {
                theStaffClipboard.getAPI().clearSelection(); 
            } 
            theStaffClipboard.getAPI().beginBand(mouseEvent.getX(), mouseEvent.getY());
        } else if (mouseEvent.isPrimaryButtonDown() || mouseEvent.isMiddleButtonDown()) {
            theStaffClipboard.getAPI().resizeBand(mouseEvent.getX(), mouseEvent.getY());
        } else if (mouseEvent.getEventType() == MouseEvent.MOUSE_RELEASED) {
            theStaffClipboard.getAPI().endBand();
        }
    }
    
    public StaffRubberBand getRubberBand() {
        return rubberBand;
    }
    
    public SMPFXController getController() {
    	return controller;
    }
    
    public double getMouseX() {
    	return mouseX;
    }
    
    public StaffClipboard getTheStaffClipboard() {
    	return theStaffClipboard;
    }
    
    /*
     * ----------------------------------------------------------------------
     * Initialization functions for layout. Needed to calculate line and
     * position.
     * ----------------------------------------------------------------------
     */

    /**
     * defines min x bound for rubberband. REQUIRED.
     * 
     * @param x
     *            coord of left edge of the left-most line in the staff frame.
     *            Use absolute(window) coordinates
     */
    public void initializeLineMinBound(double x) {
        rubberBand.setLineMinBound(x);
    }

    /**
     * defines max x bound for rubberband. REQUIRED
     * 
     * @param x
     *            coord of right edge of the right-most line in the staff frame.
     *            Use absolute(window) coordinates
     */
    public void initializeLineMaxBound(double x) {
        rubberBand.setLineMaxBound(x);
    }

    /**
     * defines min y bound for rubberband. REQUIRED. 
     * 
     * @param y
     *            coord of top edge of the top-most position in the staff frame.
     *            Use absolute(window) coordinates
     */
    public void initializePositionMinBound(double y) {
        rubberBand.setPositionMinBound(y);
    }

    /**
     * defines max y bound for rubberband. REQUIRED. 
     * 
     * @param y
     *            coord of bottom edge of the bottom-most position in the staff
     *            frame. Use absolute(window) coordinates
     */
    public void initializePositionMaxBound(double y) {
        rubberBand.setPositionMaxBound(y);
    }

    /**
     * defines extra bound for selecting volume for rubberband. should be >
     * positionMaxBound + marginVertical. REQUIRED
     * 
     * @param y
     *            coord of bottom edge of the bottom-most position in the staff
     *            frame. Use absolute(window) coordinates
     */
    public void initializeVolumeYMaxCoord(double y) {
        rubberBand.setVolumeYMaxCoord(y);
    }
    
    //calculate this with line width
    @Deprecated
    public void initializeVolumeSpacing(double x) {
        // Intentionally left blank.
    }

    /**
     * defines width between lines. REQUIRED. 
     * 
     * @param dx
     *            delta x 
     */
    public void initializeLineSpacing(double dx) {
        rubberBand.setLineSpacing(dx);
    }

    /**
     * defines height between positions. REQUIRED. 
     * 
     * @param dy
     *            delta y
     */
    public void initializePositionSpacing(double dy) {
        rubberBand.setPositionSpacing(dy);
    }

    /**
     * increases top and bottom margins around staff for drawing rubberband.
     * default is 0.0.
     * 
     * @param margin
     */
    public void setMarginVertical(double m) {
        rubberBand.setMarginVertical(m);
    }   
    
    /**
     * increases left and right margins around staff for drawing rubberband.
     * default is 0.0.
     * 
     * @param margin
     */
    public void setMarginHorizontal(double m) {
        rubberBand.setMarginHorizontal(m);
    }
    
    public void setScrollBarResizable(Slider slider) {
        rubberBand.setScrollBarResizable(slider);
    }
}
