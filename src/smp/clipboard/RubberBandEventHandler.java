package smp.clipboard;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
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
    List<RubberBand> rubberBands;
    
    Staff theStaff;
	StackPane rubberBandLayer;
	DataClipboard theDataClipboard;
    /**
     * Margin at the edge of the scrollpane. When a rectangle is being created
     * and the mouse resizes into this margin, the scrollpane will scroll to
     * accommodate resizing.
     */
    private static final double MARGIN = 8;
    
    /* Get line with these */
    private static double mouseX;
    private static double mouseY;
    
    private static boolean selVolAtNoteFlag;
//    /**
//     *
//     * @param child contained in parent, this will be where rubber band is added
//     * @param parent is the layout container for child and needed for retrieving
//     * scene dimensions
//     */
//    public EventHandlerRubberBand(Pane child, CompositionPane parent) {
//        DataClipboard.initialize();
//        rubberBands = new ArrayList<>();
//        
////        rubberBand = new RubberBand();
////        child.getChildren().add(rubberBand);
//        this.scrollPane = parent;
//        /* rubber band resize if scrollbars change */
//        this.scrollPane.hvalueProperty().addListener(new ChangeListener<Number>() {
//            @Override
//            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
//                if (autoFlagH) {
//                    if (mutexH) {
//                        return;
//                    }
//                    mutexH = true;
//                    double sizeOffset = scrollPane.getViewportBounds().getWidth();
//                    if (newValue.doubleValue() > oldValue.doubleValue() && newValue.doubleValue() > 0) {
//                        rubberBand.resizeX(newValue.doubleValue() * (Constants.WIDTH_DEFAULT - sizeOffset) + sizeOffset);
//                    } else if (newValue.doubleValue() < 1) {
//                        rubberBand.resizeX(newValue.doubleValue() * (Constants.WIDTH_DEFAULT- sizeOffset));
//                    }
//                    mutexH = false;
//                }
//            }
//
//        });
//        this.scrollPane.vvalueProperty().addListener(new ChangeListener<Number>() {
//            @Override
//            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
//                if (autoFlagV) {
//                    if (mutexV) {
//                        return;
//                    }
//                    mutexV = true;
//                    double sizeOffset = scrollPane.getViewportBounds().getHeight();
//                    /* prevent from redrawing rubberband to the bottom of viewport, scroll goes from 0.0 to -0.005 then -0.005 to 0.0 triggering this if statement */
//                    if (newValue.doubleValue() > oldValue.doubleValue() && newValue.doubleValue() > 0) {
//                        rubberBand.resizeY(newValue.doubleValue() * (Constants.HEIGHT_DEFAULT - sizeOffset) + sizeOffset);
//                    } 
//                    /* prevent from redrawing rubberband to the top of viewport, scroll goes from 1.0 to 1.005 then 1.005 to 1.0 triggering this statement */ 
//                    else if (newValue.doubleValue() < 1) {
//                        rubberBand.resizeY(newValue.doubleValue() * (Constants.HEIGHT_DEFAULT - sizeOffset));
//                    }
//                    mutexV = false;
//                }
//            }
//
//        });
//    }

    public RubberBandEventHandler(Staff st, StackPane rbl, DataClipboard clippy) {
		// TODO Auto-generated constructor stub
    	theStaff = st;
    	rubberBandLayer = rbl;
    	rubberBands = new ArrayList<>();
    	theDataClipboard = clippy;

		// TEMPORARY, should probably be in a dataclipboardeventhandler
		rbl.setFocusTraversable(true);
    	rbl.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				if (event.isControlDown()) {
					switch (event.getCode()) {
					case C:
						//select code should not be with the copy code obviously
						System.out.println("COPY");
						int lb = getLine(rubberBand.getTranslateX()) + StateMachine.getMeasureLineNum();
						int pb = getPosition(rubberBand.getTranslateY() + rubberBand.getHeight());
						int le = getLine(rubberBand.getTranslateX() + rubberBand.getWidth()) + StateMachine.getMeasureLineNum();
						int pe = getPosition(rubberBand.getTranslateY());
						theDataClipboard.getFunctions().select(lb, pb, le, pe);
						theDataClipboard.getFunctions().copy();
						for ( StaffNoteLine line : theDataClipboard.getData() ) {
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
					default:
						break;
					}
				}
			}
		});
	}

	@Override
    public void handle(MouseEvent mouseEvent) {
		if(!StateMachine.isClipboardPressed())
			return;
        mouseX = mouseEvent.getX();
        mouseY = mouseEvent.getY();
////        rubberBand.toFront();
        if (mouseEvent.getEventType() == MouseEvent.MOUSE_PRESSED) {
//            
            if(!mouseEvent.isControlDown()) {
//                scrollPane.unhighlightAllNotes();
//                scrollPane.unhighlightAllVols();
                theDataClipboard.clearSelection(); 
            } else {
            }

        	rubberBandLayer.getChildren().remove(rubberBand);
            rubberBand = new RubberBand();
        	rubberBandLayer.getChildren().add(rubberBand);
            
            rubberBand.begin(mouseEvent.getX(), mouseEvent.getY());
            System.out.print("begin GetLine:" + getLine(mouseEvent.getX()));
            System.out.println(" GetPos:" + getPosition(mouseEvent.getY()));
//            
        } else if (mouseEvent.isPrimaryButtonDown()) {
            rubberBand.resize(mouseEvent.getX(), mouseEvent.getY());
//            navigatePane(mouseEvent);
        } else if (mouseEvent.getEventType() == MouseEvent.MOUSE_RELEASED) {
            rubberBand.end();
            System.out.print("end GetLine:" + getLine(mouseEvent.getX()));
            System.out.println(" GetPos:" + getPosition(mouseEvent.getY()));
//            
//            Selection type = ((RibbonMenuMPO)Variables.stageInFocus.getRibbonMenu()).getSelectionToolbar().getSelectionType();
//            if (type.equals(Selection.SELECTION_NOTES) || type.equals(Selection.SELECTION_NOTES_AND_VOL)) {
//
//                int lineBegin = rubberBand.getLineBegin();
//                List<MeasureLine> selection = DataClipboardFunctions.sel(scrollPane.getSong(),
//                        lineBegin,
//                        rubberBand.getPositionBegin(),
//                        rubberBand.getLineEnd(),
//                        rubberBand.getPositionEnd());
//                for (MeasureLine ml : selection) {
//                    if (ml != null) {
//                        for (Note n : ml) {
//                            scrollPane.highlightNote(n, true);
//                        }
//                    }
//                }
//            }
//            if (type.equals(Selection.SELECTION_VOL) || type.equals(Selection.SELECTION_NOTES_AND_VOL)) {
//                int lineBeginVol = rubberBand.getLineBeginVol();
//                int lineEndVol = rubberBand.getLineEndVol();
//                System.out.println("lineBeginVol" + lineBeginVol);
//                System.out.println("lineEndVol" + lineEndVol);
//                List<MeasureLine> selectionVol = DataClipboardFunctions.selVol(scrollPane.getSong(),
//                        lineBeginVol,
//                        lineEndVol);
//                for (int i = 0; i < selectionVol.size(); i++) {
//                    if (selectionVol.get(i) != null && selectionVol.get(i).getVolume() >= 0) {
//                        scrollPane.highlightVol(selectionVol.get(i).getLineNumber(), true);
//                    }
//                }
//            }
        }
    }

//    private double zdh(int destX) {
//        if (destX == 1) {
//            return 2 - scrollPane.hvalueProperty().get() * 2;
//        } else {
//            return scrollPane.hvalueProperty().get() * 2;
//        }
//    }
//
//    private double zdv(int destY) {
//        if (destY == 1) {
//            return 2 - scrollPane.vvalueProperty().get() * 2;
//        } else {
//            return scrollPane.vvalueProperty().get() * 2;
//        }
//    }
//    
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
		// HARD CODED FOR NOW
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
}
