package smp.clipboard;

import javafx.event.EventHandler;
import javafx.scene.control.Slider;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import smp.components.Values;
import smp.components.staff.Staff;
import smp.components.staff.sequences.StaffNoteLine;
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
    
    Staff theStaff;
	Pane rubberBandLayer;
	DataClipboard theDataClipboard;
	
    /* Get line with these */
    private static double mouseX;
    private static double mouseY;
    
    private static boolean selVolAtNoteFlag;

    public RubberBandEventHandler(Staff st, Pane basePane, DataClipboard clippy) {
    	theStaff = st;
    	theDataClipboard = clippy;
    	rubberBandLayer = basePane;
    	
        rubberBand = new RubberBand();

		// TEMPORARY, should probably be in a dataclipboardeventhandler
    	basePane.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				if (event.isControlDown()) {
					switch (event.getCode()) {
					case C:
						//select code should not be with the copy code obviously
						System.out.println("COPY");
//						System.out.println("COPY POS: pb" + pb + " pe" + pe);
						theDataClipboard.getFunctions().copy();
						for ( StaffNoteLine line : theDataClipboard.getData().values() ) {
							if(!line.isEmpty())
								System.out.println(line);
						}
						break;
					case V:
						System.out.println("PASTE");
						int currentLine = getLine(mouseX) + StateMachine.getMeasureLineNum();
						System.out.println(currentLine);
						theDataClipboard.getFunctions().paste(currentLine);
						break;
					case X:
						theDataClipboard.getFunctions().cut();
						break;
					default:
						break;
					}
				} else {
					switch (event.getCode()) {

					case DELETE:
						theDataClipboard.getFunctions().delete();
						break;
					default:
						break;
					}
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
                theDataClipboard.getFunctions().clearSelection(); 
			
			return;
		}
////        rubberBand.toFront();
        if (mouseEvent.getEventType() == MouseEvent.MOUSE_PRESSED) {
//            
            if(!mouseEvent.isControlDown()) {
//                scrollPane.unhighlightAllNotes();
//                scrollPane.unhighlightAllVols();
                theDataClipboard.getFunctions().clearSelection(); 
            } else {
            }
            
        	rubberBandLayer.getChildren().add(rubberBand);
            
            rubberBand.begin(mouseEvent.getX(), mouseEvent.getY());
            System.out.print("begin GetLine:" + getLine(mouseEvent.getX()));
            System.out.println(" GetPos:" + getPosition(mouseEvent.getY()));
        } else if (mouseEvent.isPrimaryButtonDown()) {
            rubberBand.resizeBand(mouseEvent.getX(), mouseEvent.getY());
        } else if (mouseEvent.getEventType() == MouseEvent.MOUSE_RELEASED) {
            rubberBand.end();
        	rubberBandLayer.getChildren().remove(rubberBand);

			int lb = rubberBand.getLineBegin() + StateMachine.getMeasureLineNum();//getLine(rubberBand.getTranslateX()) + StateMachine.getMeasureLineNum();
			int pb = rubberBand.getPositionBegin();//getPosition(rubberBand.getTranslateY() + rubberBand.getHeight());
			int le = rubberBand.getLineEnd() + StateMachine.getMeasureLineNum();//getLine(rubberBand.getTranslateX() + rubberBand.getWidth()) + StateMachine.getMeasureLineNum();
			int pe = rubberBand.getPositionEnd();//getPosition(rubberBand.getTranslateY());
			theDataClipboard.getFunctions().select(lb, pb, le, pe);
            System.out.print("end GetLine:" + getLine(mouseEvent.getX()));
            System.out.println(" GetPos:" + getPosition(mouseEvent.getY()));
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
//  public int getLineBegin() {
//  //x, 32px is exactly at the line
//  /* lineOffsetX is indices 0-31 of the row */
//  int lineOffsetX = 0;
//  if (this.getTranslateX() < 122 + 32) {//122 is arbitrary 
//      lineOffsetX = 0;
//  } else if (this.getTranslateX() > Constants.WIDTH_DEFAULT - 48 + 32 - 64) {//48 is arbitrary
////      return -1;
//      lineOffsetX = 32;
//  } else {
//      lineOffsetX = ((int)this.getTranslateX() - (122 + 32)) / Constants.LINE_SPACING + 1;
//  }
//
//  //y
//  /* lineOffsetY is row */
//  int lineOffsetY = 0;
//  if(!zby(this.getTranslateY())){
//      lineOffsetY = (int)this.getTranslateY() / Constants.ROW_HEIGHT_TOTAL + 1;
//  } else {
//      lineOffsetY = (int)this.getTranslateY() / Constants.ROW_HEIGHT_TOTAL;
//  }
//  System.out.println("BEG" + lineOffsetY * Constants.LINES_IN_A_ROW + " " + lineOffsetX);
//	return lineOffsetY * Constants.LINES_IN_A_ROW + lineOffsetX;
////  return getLine(xOrigin, yOrigin);
//}
//
//public int getLineEnd() {
//  //x, 32px is exactly at the line
//  /* lineOffsetX is indices 0-31 of the row */
//  int lineOffsetX = 0;
//  if ((this.getTranslateX() + this.getWidth()) < 122 + 32) {//122 is arbitrary 
////      return -1;
//      lineOffsetX = -1;
//  } else if ((this.getTranslateX() + this.getWidth()) > Constants.WIDTH_DEFAULT - 48 + 32) {//48 is arbitrary
//      lineOffsetX = 31;
//  } else {
//      lineOffsetX = ((int)(this.getTranslateX() + this.getWidth()) - (122 + 32 + 64)) / Constants.LINE_SPACING + 1;
//  }
//
//  //y
//  /* lineOffsetY is row */
//  int lineOffsetY = 0;
//  if(!zby(this.getTranslateY() + this.getHeight())){
//      lineOffsetY = (int)(this.getTranslateY() + this.getHeight()) / Constants.ROW_HEIGHT_TOTAL;
//  } else {
//      lineOffsetY = (int)(this.getTranslateY() + this.getHeight()) / Constants.ROW_HEIGHT_TOTAL;
//  }
//  System.out.println("END" + lineOffsetY * Constants.LINES_IN_A_ROW + " " + lineOffsetX);
//	return lineOffsetY * Constants.LINES_IN_A_ROW + lineOffsetX;
////  return getLine(xOrigin + this.getWidth(), yOrigin + this.getHeight());
//}	
	
//
//    /* If valid y */
//    private boolean zby(double y) {
//        return y % Constants.ROW_HEIGHT_TOTAL <= Constants.ROW_HEIGHT_NOTES;
//    }
    
//    public void selectNotes() {
//        Selection type = ((RibbonMenuMPO) Variables.stageInFocus.getRibbonMenu()).getSelectionToolbar().getSelectionType();
//        if (type.equals(Selection.SELECTION_NOTES) || type.equals(Selection.SELECTION_NOTES_AND_VOL)) {
//            //Note: overlapping rubberbands will highlight the same notes and this might have a big impact on performance and may need to be optimized later.
//            for (RubberBand rb : rubberBands) {
//
//                int lineBegin = rb.getLineBegin();
//                List<MeasureLine> selection = DataClipboardFunctions.sel(scrollPane.getSong(),
//                        lineBegin,
//                        rb.getPositionBegin(),
//                        rb.getLineEnd(),
//                        rb.getPositionEnd());
//                for (MeasureLine ml : selection) {
//                    if(ml != null) {
//                        for (Note n : ml) {
//                            scrollPane.highlightNote(n, true);
//                        }
//                    }
//                }
//            }
//        }
//    }
//    
//    public void selectVols() {
//        Selection type = ((RibbonMenuMPO) Variables.stageInFocus.getRibbonMenu()).getSelectionToolbar().getSelectionType();
//        if (type.equals(Selection.SELECTION_VOL) || type.equals(Selection.SELECTION_NOTES_AND_VOL)) {
//            for (RubberBand rb : rubberBands) {
//                int lineBeginVol = rb.getLineBeginVol();
//                int lineEndVol = rb.getLineEndVol();
//                System.out.println("SV_lBV:" + lineBeginVol);
//                System.out.println("SV_lEV:" + lineEndVol);
//                List<MeasureLine> selectionVol = DataClipboardFunctions.selVol(scrollPane.getSong(),
//                        lineBeginVol,
//                        lineEndVol);
//                for (int i = 0; i < selectionVol.size(); i++) {
//                    if (selectionVol.get(i) != null) {
//                        scrollPane.highlightVol(selectionVol.get(i).getLineNumber(), true);
//                    }
//                }
//            }
//        }
//    }
	
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
