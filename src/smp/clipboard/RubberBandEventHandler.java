package smp.clipboard;

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
public class RubberBandEventHandler implements EventHandler<MouseEvent> {

    RubberBand rubberBand;
    
    SMPFXController controller;
	Pane rubberBandLayer;
	DataClipboard theDataClipboard;
	
    /* Get line with these */
    private static double mouseX;
    private static double mouseY;
    
    public RubberBandEventHandler(SMPFXController ct, Pane basePane, DataClipboard clippy) {
    	controller = ct;
    	theDataClipboard = clippy;
    	rubberBandLayer = basePane;
    	
        rubberBand = new RubberBand();

		// TEMPORARY, should probably be in a dataclipboardeventhandler
    	basePane.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				switch (event.getCode()) {
				case A:
					if (event.isControlDown()) {
						theDataClipboard.getAPI().clearSelection();
						theDataClipboard.getAPI().select(0, 0,
								(int) controller.getScrollbar().getMax() + Values.NOTELINES_IN_THE_WINDOW,
								Values.NOTES_IN_A_LINE);
					}
					break;
				case C:
					if (event.isControlDown()) {
						System.out.println("COPY");
						theDataClipboard.getAPI().copy();
						for (StaffNoteLine line : theDataClipboard.getCopiedData().values()) {
							if (!line.isEmpty())
								System.out.println(line);
						}
					}
					break;
				case N:
					if (event.isAltDown()) {
						theDataClipboard.getAPI().selectNotesToggle(!theDataClipboard.getAPI().isSelectNotesOn());
					}
					break;
				case V:
					if (event.isAltDown()) {
						theDataClipboard.getAPI().selectVolumesToggle(!theDataClipboard.getAPI().isSelectVolumesOn());
					} else if (event.isControlDown()) {
						int currentLine = getLine(mouseX) + StateMachine.getMeasureLineNum();
						System.out.println("PASTE @ " + currentLine);
						theDataClipboard.getAPI().paste(currentLine);
					}
					break;
				case X:
					if (event.isControlDown()) {
						theDataClipboard.getAPI().cut();
					}
					break;
				case DELETE:
					theDataClipboard.getAPI().delete();
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
        mouseY = mouseEvent.getY();
		if(!StateMachine.isClipboardPressed()){
			
			if (mouseEvent.getEventType() == MouseEvent.MOUSE_CLICKED) 
                theDataClipboard.getAPI().clearSelection(); 
			
			return;
		}
////        rubberBand.toFront();
        if (mouseEvent.getEventType() == MouseEvent.MOUSE_PRESSED) {
//            
            if(!mouseEvent.isControlDown()) {
//                scrollPane.unhighlightAllNotes();
//                scrollPane.unhighlightAllVols();
                theDataClipboard.getAPI().clearSelection(); 
            } else {
            }
            
        	rubberBandLayer.getChildren().add(rubberBand);
            
            rubberBand.begin(mouseEvent.getX(), mouseEvent.getY());
//            System.out.print("begin GetLine:" + getLine(mouseEvent.getX()));
//            System.out.println(" GetPos:" + getPosition(mouseEvent.getY()));
        } else if (mouseEvent.isPrimaryButtonDown() || mouseEvent.isMiddleButtonDown()) {
            rubberBand.resizeBand(mouseEvent.getX(), mouseEvent.getY());
        } else if (mouseEvent.getEventType() == MouseEvent.MOUSE_RELEASED) {
            rubberBand.end();
        	rubberBandLayer.getChildren().remove(rubberBand);

			int lb = rubberBand.getLineBegin() + StateMachine.getMeasureLineNum();//getLine(rubberBand.getTranslateX()) + StateMachine.getMeasureLineNum();
			int pb = rubberBand.getPositionBegin();//getPosition(rubberBand.getTranslateY() + rubberBand.getHeight());
			int le = rubberBand.getLineEnd() + StateMachine.getMeasureLineNum();//getLine(rubberBand.getTranslateX() + rubberBand.getWidth()) + StateMachine.getMeasureLineNum();
			int pe = rubberBand.getPositionEnd();//getPosition(rubberBand.getTranslateY());
			theDataClipboard.getAPI().select(lb, pb, le, pe);
//            System.out.print("end GetLine:" + getLine(mouseEvent.getX()));
//            System.out.println(" GetPos:" + getPosition(mouseEvent.getY()));
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
	
    /**
	 *
	 * temporary call this in RubberBand instead
	 * 
	 * @relevant Values.NOTES_IN_A_LINE
	 * 
	 * @param y
	 *            mouse pos for entire scene
	 * @return note based on y coord
	 */
	private int getPosition(double y) {
		// HARD CODED FOR NOW
		if (y < 0 || y > 304) {
			return -1;
		}
		return (Values.NOTES_IN_A_LINE - ((int) y - 0) / 16);
	}
	
	public RubberBand getRubberBand() {
		return rubberBand;
	}
	
	/**
	 * ----------------------------------------------------------------------
	 * Initialization functions for layout. Needed to calculate line and
	 * position.
	 * ----------------------------------------------------------------------
	 */

	/**
	 * REQUIRED. will define min x bound for rubberband
	 * 
	 * @param x
	 *            coord of left edge of the left-most line in the staff frame.
	 *            Use absolute(window) coordinates
	 */
	public void initializeLineMinBound(double x) {
		rubberBand.setLineMinBound(x);
	}

	/**
	 * REQUIRED. will define max x bound for rubberband
	 * 
	 * @param x
	 *            coord of right edge of the right-most line in the staff frame.
	 *            Use absolute(window) coordinates
	 */
	public void initializeLineMaxBound(double x) {
		rubberBand.setLineMaxBound(x);
	}

	/**
	 * REQUIRED. will define min y bound for rubberband
	 * 
	 * @param y
	 *            coord of top edge of the top-most position in the staff frame.
	 *            Use absolute(window) coordinates
	 */
	public void initializePositionMinBound(double y) {
		rubberBand.setPositionMinBound(y);
	}

	/**
	 * REQUIRED. will define max y bound for rubberband
	 * 
	 * @param y
	 *            coord of bottom edge of the bottom-most position in the staff
	 *            frame. Use absolute(window) coordinates
	 */
	public void initializePositionMaxBound(double y) {
		rubberBand.setPositionMaxBound(y);
	}

	/**
	 * REQUIRED. will define extra bound for selecting volume for rubberband.
	 * should be > positionMaxBound + marginVertical
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
	 * REQUIRED. will be used to define width between lines
	 * 
	 * @param dx
	 *            delta x 
	 */
	public void initializeLineSpacing(double dx) {
		rubberBand.setLineSpacing(dx);
	}

	/**
	 * REQUIRED. will be used to define height between positions
	 * 
	 * @param dy
	 *            delta y
	 */
	public void initializePositionSpacing(double dy) {
		rubberBand.setPositionSpacing(dy);
	}

	/**
	 * increase top and bottom margins around staff for drawing rubberband.
	 * default is 0.0.
	 * 
	 * @param margin
	 */
	public void setMarginVertical(double m) {
		rubberBand.setMarginVertical(m);
	}	
	
	/**
	 * increase left and right margins around staff for drawing rubberband.
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
