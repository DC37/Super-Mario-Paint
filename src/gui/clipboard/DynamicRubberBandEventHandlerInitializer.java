package gui.clipboard;

import gui.SMPFXController;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class DynamicRubberBandEventHandlerInitializer implements RubberBandEventHandlerInitializer {

	@Override
	public void initialize(StaffRubberBandEventHandler rbeh, SMPFXController ct) {
		//initialize lineMinBound
		HBox staffInstruments = null; //ct.getStaffInstruments();
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
		StackPane staffPane = null; //ct.getStaffPane();
		Bounds staffPaneBounds = staffPane.localToScene(staffPane.getBoundsInLocal());
		double marginDeltaX = firstLineBounds.getMinX() - staffPaneBounds.getMinX();
		double marginDeltaY = firstPositionBounds.getMinY() - staffPaneBounds.getMinY();
		rbeh.setMarginVertical(marginDeltaY);
		rbeh.setMarginHorizontal(marginDeltaX);
        
        //set scrollbar resizing
        rbeh.setScrollBarResizable(ct.getScrollbar());
	}
	
}
