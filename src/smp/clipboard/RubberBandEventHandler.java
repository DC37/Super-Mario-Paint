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
import smp.components.staff.Staff;
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

    public RubberBandEventHandler(Staff st, StackPane rbl) {
		// TODO Auto-generated constructor stub
    	theStaff = st;
    	rubberBandLayer = rbl;
    	rubberBands = new ArrayList<>();
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
//                DataClipboard.clearSelection();
//                
            	rubberBandLayer.getChildren().removeAll(rubberBands);
                rubberBands.clear();
                rubberBand = null;
            } else {
                rubberBand = new RubberBand();
                rubberBands.add(rubberBand);
                rubberBandLayer.getChildren().add(rubberBand);
            }
//            
            if (rubberBands.isEmpty()) {
                rubberBand = new RubberBand();
                rubberBands.add(rubberBand);
                rubberBandLayer.getChildren().add(rubberBand);
            }
//            
            rubberBand.begin(mouseEvent.getX(), mouseEvent.getY());
            System.out.println(mouseEvent.getX() + " " + mouseEvent.getY());
//            
        } else if (mouseEvent.isPrimaryButtonDown()) {
            rubberBand.resize(mouseEvent.getX(), mouseEvent.getY());
//            navigatePane(mouseEvent);
        } else if (mouseEvent.getEventType() == MouseEvent.MOUSE_RELEASED) {
            rubberBand.end();
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
//    /**
//     *
//     * @param x mouse pos for entire scene
//     * @param y mouse pos for entire scene
//     * @return line based on x and y coord
//     */
//    private int getLine(double x, double y) {
//
//        if (x < 122 || x > Constants.WIDTH_DEFAULT - 48 || !zby(y))//122 is arbitrary, 48 is arbitrary
//        {
//            return -1;
//        }
//        return (((int) x - 122) / 64)
//                + ((int) y / Constants.ROW_HEIGHT_TOTAL) * Constants.LINES_IN_A_ROW;
//    }
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
}
