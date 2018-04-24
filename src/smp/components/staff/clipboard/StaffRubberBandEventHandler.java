package smp.components.staff.clipboard;

import javafx.event.EventHandler;
import javafx.scene.control.Slider;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import smp.components.Values;
import smp.components.staff.sequences.StaffNoteLine;
import smp.fx.SMPFXController;
import smp.stateMachine.StateMachine;

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
    private static double mouseX;
    
    public StaffRubberBandEventHandler(StaffRubberBand rb, SMPFXController ct, Pane basePane, StaffClipboard clippy) {
    	rubberBand = rb;
    	controller = ct;
    	theStaffClipboard = clippy;
    	rubberBandLayer = basePane;

		// TEMPORARY, should probably be in a dataclipboardeventhandler
    	basePane.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				switch (event.getCode()) {
				case A:
					if (event.isControlDown()) {
						theStaffClipboard.getAPI().clearSelection();
						theStaffClipboard.getAPI().select(0, 0,
								(int) controller.getScrollbar().getMax() + Values.NOTELINES_IN_THE_WINDOW,
								Values.NOTES_IN_A_LINE);
					}
					break;
				case C:
					if (event.isControlDown()) {
						System.out.println("COPY");
						theStaffClipboard.getAPI().copy();
						for (StaffNoteLine line : theStaffClipboard.getCopiedData().values()) {
							if (!line.isEmpty())
								System.out.println(line);
						}
					}
					break;
				case N:
					if (event.isAltDown()) {
						theStaffClipboard.getAPI().selectNotesToggle(!theStaffClipboard.getAPI().isSelectNotesOn());
					}
					break;
				case V:
					if (event.isAltDown()) {
						theStaffClipboard.getAPI().selectVolumesToggle(!theStaffClipboard.getAPI().isSelectVolumesOn());
					} else if (event.isControlDown()) {
						int currentLine = getLine(mouseX) + StateMachine.getMeasureLineNum();
						System.out.println("PASTE @ " + currentLine);
						theStaffClipboard.getAPI().paste(currentLine);
					}
					break;
				case X:
					if (event.isControlDown()) {
						theStaffClipboard.getAPI().cut();
					}
					break;
				case DELETE:
					theStaffClipboard.getAPI().delete();
					break;
				default:
					break;
				}
			}
		});
	}

	@Override
    public void handle(MouseEvent mouseEvent) {
        mouseX = mouseEvent.getX();
		if(!StateMachine.isClipboardPressed()){
			
			if (mouseEvent.getEventType() == MouseEvent.MOUSE_CLICKED) 
                theStaffClipboard.getAPI().clearSelection(); 
			
			return;
		}
        if (mouseEvent.getEventType() == MouseEvent.MOUSE_PRESSED) {
        	
            if(!mouseEvent.isControlDown()) {
                theStaffClipboard.getAPI().clearSelection(); 
            } 
        	rubberBandLayer.getChildren().add(rubberBand);
            rubberBand.begin(mouseEvent.getX(), mouseEvent.getY());
        } else if (mouseEvent.isPrimaryButtonDown() || mouseEvent.isMiddleButtonDown()) {
            rubberBand.resizeBand(mouseEvent.getX(), mouseEvent.getY());
        } else if (mouseEvent.getEventType() == MouseEvent.MOUSE_RELEASED) {
            rubberBand.end();
        	rubberBandLayer.getChildren().remove(rubberBand);

			int lb = rubberBand.getLineBegin() + StateMachine.getMeasureLineNum();
			int pb = rubberBand.getPositionBegin();
			int le = rubberBand.getLineEnd() + StateMachine.getMeasureLineNum();
			int pe = rubberBand.getPositionEnd();
			theStaffClipboard.getAPI().select(lb, pb, le, pe);
        }
    }

    /**
	 *
	 * 
	 * 
	 * @relevant Values.NOTELINES_IN_THE_WINDOW,
	 *           StateMachine.getMeasureLineNum()
	 * 
	 * @param x
	 *            mouse pos for entire scene
	 * @return -1 if out of bounds (x < 128 || x > 784), 0-9 otherwise
	 */
	private int getLine(double x) {
		
		if (x < 128 || x > 784) {
			return -1;
		}
		return (((int) x - 128) / 64);
	}
	
	public StaffRubberBand getRubberBand() {
		return rubberBand;
	}
	
	/**
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
