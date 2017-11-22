package smp.clipboard;

import java.util.ArrayList;
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
import smp.ImageLoader;
import smp.components.staff.Staff;
import smp.components.staff.sequences.StaffNote;
import smp.components.staff.sequences.StaffNoteLine;
import smp.fx.SMPFXController;

/**
 * This class can be used to keep track of the bounds that we will copy, delete,
 * etc. notes from.
 *
 */
class SelectionBounds {
	private int lineBegin;
	private int lineEnd;
	private int positionBegin;
	private int positionEnd;

	public SelectionBounds(int lb, int le, int pb, int pe) {
		this.lineBegin = lb;
		this.lineEnd = le;
		this.positionBegin = pb;
		this.positionEnd = pe;
	}

	public int getLineBegin() {
		return lineBegin;
	}

	public int getLineEnd() {
		return lineEnd;
	}

	public int getPositionBegin() {
		return positionBegin;
	}

	public int getPositionEnd() {
		return positionEnd;
	}
}

public class DataClipboard {

	private Staff theStaff;
	private SMPFXController controller;
	private ImageLoader il;
	private Pane rubberBandLayer;
	private RubberBandEventHandler rbeh;

	private InstrumentFilter instFilter;
	
	private DataClipboardFunctions functions;
	
	/* A list that keeps track of all the selections' bounds made by the user */
	private ArrayList<SelectionBounds> selection;
	
	/* The list that will keep track of copied notes */
	private HashMap<Integer, StaffNoteLine> data;
	
	/* Corresponds with the first selection line */
	private int dataLineBegin = Integer.MAX_VALUE;

	public DataClipboard(Staff st, SMPFXController ct, ImageLoader im) {

		il = im;
		theStaff = st;
		controller = ct;

		selection = new ArrayList<>();
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
	
	/**
	 * Add a new SelectionBounds to the selection ArrayList. 
	 * 
	 * This will be used to keep track of all the selections made by the user.
	 * 
	 * @param lb lineBegin
	 * @param le lineEnd
	 * @param pb positionBegin
	 * @param pe positionEnd
	 */
	public void addSelection(int lb, int le, int pb, int pe) {
		selection.add(new SelectionBounds(lb, le, pb, pe));
	}

	/**
	 * Clear the selection ArrayList.
	 */
	public void clearSelection() {
		selection.clear();
	}
	
	public ArrayList<SelectionBounds> getSelection() {
		return selection;
	}
	
	/**
	 * Copy information from note. Add new note into data.
	 * 
	 * @param line
	 *            where the note occurs
	 * @param note
	 *            that will have its info copied
	 */
	public void copyNote(int line, StaffNote note) {
		StaffNote newNote = new StaffNote(note.getInstrument(), note.getPosition(), note.getAccidental());
		if(!data.containsKey(line))
			data.put(line, new StaffNoteLine());
		data.get(line).add(newNote);
	}
	
	public void clearData() {
		data.clear();
		dataLineBegin = Integer.MAX_VALUE;
	}
	
	public HashMap<Integer, StaffNoteLine> getData() {
		return data;
	}
	
	public int getDataLineBegin() {
		return dataLineBegin;
	}
	
	/**
	 * convenience method, update dataLineBegin only if line is less. Should
	 * call this because dataLineBegin will be used to define bounds of data.
	 * 
	 * @param line
	 *            to update dataLineBegin to
	 */
	public void updateDataLineBegin(int line) {
		if (line < dataLineBegin)
			dataLineBegin = line;
	}
	
	//temp? merge the two classes together?
	public DataClipboardFunctions getFunctions(){
		return functions;
	}
}
