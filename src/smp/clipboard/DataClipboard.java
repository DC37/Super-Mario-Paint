package smp.clipboard;

import java.util.HashMap;

import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import smp.ImageLoader;
import smp.components.staff.Staff;
import smp.components.staff.sequences.StaffNoteLine;
import smp.fx.SMPFXController;

public class DataClipboard {

	public static Color HIGHLIGHT_FILL = new Color(0.5, 0.5, 0.5, 0.5);
	
	private Staff theStaff;
	private SMPFXController controller;
	private ImageLoader il;
	private Pane rubberBandLayer;
	private RubberBandEventHandler rbeh;

	private InstrumentFilter instFilter;
	
	private DataClipboardFunctions functions;
	
	/* A list that keeps track of all the selections' bounds made by the user */
	private HashMap<Integer, StaffNoteLine> selection;
	
	/* The list that will keep track of copied notes */
	private HashMap<Integer, StaffNoteLine> data;

	public DataClipboard(Staff st, SMPFXController ct, ImageLoader im) {

		il = im;
		theStaff = st;
		controller = ct;

		selection = new HashMap<>();
		data = new HashMap<>();
		
		//temp? merge the two classes together?
		functions = new DataClipboardFunctions(this, theStaff, im);
		
		rubberBandLayer = controller.getBasePane();
        RubberBandEventHandler rbeh = new RubberBandEventHandler(theStaff, rubberBandLayer, this);
        initializeRBEH(rbeh, controller);
		rubberBandLayer.addEventHandler(MouseEvent.ANY, rbeh);
		
		instFilter = new InstrumentFilter(ct.getInstLine());
	}

	private void initializeRBEH(RubberBandEventHandler rbeh, SMPFXController ct) {

		//trigger layout pass in basePane and all its children
		//this will initialize the bounds for everything
		AnchorPane basePane = ct.getBasePane();
		basePane.applyCss();//requestLayout??
		basePane.layout();
		
		//initialize lineMinBound
		HBox staffInstruments = ct.getStaffInstruments();
		ObservableList<Node> instrumentLines = staffInstruments.getChildren();
		VBox firstLine = (VBox) instrumentLines.get(0);
		Bounds firstLineBounds = firstLine.localToScene(firstLine.getBoundsInLocal()); 
		rbeh.initializeLineMinBound(firstLineBounds.getMinX());

		//initialize lineMaxBound
		VBox lastLine = (VBox) instrumentLines.get(instrumentLines.size() - 1);
		Bounds lastLineBounds = lastLine.localToScene(lastLine.getBoundsInLocal()); 
		rbeh.initializeLineMaxBound(lastLineBounds.getMaxX());
		
		//initialize lineSpacing
		rbeh.initializeLineSpacing(firstLineBounds.getWidth());
		
		//initialize positionMinBound
		ObservableList<Node> positions = firstLine.getChildren();
		StackPane firstPosition = (StackPane) positions.get(0);
		Bounds firstPositionBounds = firstPosition.localToScene(firstPosition.getBoundsInLocal()); 
		rbeh.initializePositionMinBound(firstPositionBounds.getMinY());		
		
		//initialize positionMaxBound
		StackPane lastPosition = (StackPane) positions.get(positions.size() - 1);
		Bounds lastPositionBounds = lastPosition.localToScene(lastPosition.getBoundsInLocal());
		rbeh.initializePositionMaxBound(lastPositionBounds.getMaxY());
	
		//initialize positionSpacing
		StackPane secondPosition = (StackPane) positions.get(1);
		Bounds secondPositionBounds = secondPosition.localToScene(secondPosition.getBoundsInLocal());
		rbeh.initializePositionSpacing((secondPositionBounds.getMinY() - firstPositionBounds.getMinY()) * 2);
		
		//initialize volume YMax coordinate
		HBox volumeBars = ct.getVolumeBars();
		Bounds volumeBarsBounds = volumeBars.localToScene(volumeBars.getBoundsInLocal());
		rbeh.initializeVolumeYMaxCoord(volumeBarsBounds.getMaxY());
		
		//set margins
		StackPane staffPane = ct.getStaffPane();
		Bounds staffPaneBounds = staffPane.localToScene(staffPane.getBoundsInLocal());
		double marginDeltaX = firstLineBounds.getMinX() - staffPaneBounds.getMinX();
		double marginDeltaY = firstPositionBounds.getMinY() - staffPaneBounds.getMinY();
		rbeh.setMarginVertical(marginDeltaY);
		rbeh.setMarginHorizontal(marginDeltaX);
		
		//set scrollbar resizing
		rbeh.setScrollBarResizable(controller.getScrollbar());
	}
	
//	private void DEBUG(Node x) {
//		System.out.println("NODE:" + x);
//		System.out.println("x.getBoundsInLocal" + x.getBoundsInLocal());
//		System.out.println("x.getBoundsInParent" + x.getBoundsInParent());
//		System.out.println("x.getLayoutBounds" + x.getLayoutBounds());
//		System.out.println(x.localToScene(x.getBoundsInLocal()));
//		System.out.println(x.localToScene(x.getBoundsInParent()));
//		System.out.println(x.localToScene(x.getLayoutBounds()));
//		System.out.println(x.localToParent(x.getBoundsInLocal()));
//		System.out.println(x.localToParent(x.getBoundsInParent()));
//		System.out.println(x.localToParent(x.getLayoutBounds()));
//		System.out.println(x.localToScene(x.getLayoutBounds().getMinX(), x.getLayoutBounds().getMinY()));
//	}
	
	public HashMap<Integer, StaffNoteLine> getSelection() {
		return selection;
	}
	
	public HashMap<Integer, StaffNoteLine> getData() {
		return data;
	}
	
	public InstrumentFilter getInstrumentFilter() {
		return instFilter;
	}
	
	//temp? merge the two classes together?
	public DataClipboardFunctions getFunctions(){
		return functions;
	}
}
