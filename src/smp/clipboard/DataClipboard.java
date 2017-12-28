package smp.clipboard;

import java.util.HashMap;
import java.util.HashSet;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.ColorInput;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import smp.ImageIndex;
import smp.ImageLoader;
import smp.components.Values;
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
	
	/* The functions class for copy, cut, paste, etc. */
	private DataClipboardAPI theAPI;
	
	/* The list that keeps track of all the selections' bounds made by the user */
	private HashMap<Integer, StaffNoteLine> selection;
	
	/* The list that will keep track of copied notes (and volumes) */
	private HashMap<Integer, StaffNoteLine> copiedData;

	/* Volumes aren't node references so we keep track of the volumes' lines */
	private HashSet<Integer> highlightedVolumes;
	
	/*
	 * The listener that will update which volume bars are highlighted every
	 * scrollbar change
	 */
	private ChangeListener<Number> highlightedVolumesRedrawer;

	public DataClipboard(Staff st, SMPFXController ct, ImageLoader im) {

		il = im;
		theStaff = st;
		controller = ct;

		selection = new HashMap<>();
		copiedData = new HashMap<>();
		
		//temp? merge the two classes together?
		theAPI = new DataClipboardAPI(this, theStaff, im);

		redrawUI(ct);
		
		addClipboardButton(ct, im);
		
		rubberBandLayer = controller.getBasePane();
        RubberBandEventHandler rbeh = new RubberBandEventHandler(controller, rubberBandLayer, this);
        initializeRBEH(rbeh, controller);
		rubberBandLayer.addEventHandler(MouseEvent.ANY, rbeh);
		
		initializeHighlightedVolumes(ct);
		
		instFilter = new InstrumentFilter(ct.getInstLine());
	}

	private void initializeHighlightedVolumes(SMPFXController ct) {
		
		final ObservableList<Node> volumeBars = ct.getVolumeBars().getChildren();
		ImageView theVolBar = (ImageView) ((StackPane)volumeBars.get(0)).getChildren().get(0);
		final Blend highlightBlendVolume = new Blend(
	            BlendMode.SRC_OVER,
	            null,
	            new ColorInput(
	            		-theVolBar.getFitWidth(),
	                    0,
	                    theVolBar.getFitWidth() * 3,
	                    theVolBar.getFitHeight(),
	                    DataClipboard.HIGHLIGHT_FILL
	            ));

		highlightedVolumes = new HashSet<>();
		highlightedVolumesRedrawer = new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				for(int i = 0; i < Values.NOTELINES_IN_THE_WINDOW; i++) {

					// we want the actual volumebar image to highlight
					ImageView theVolBar = (ImageView) ((StackPane) volumeBars.get(i)).getChildren().get(0);

					if (highlightedVolumes.contains(i + newValue.intValue()))
						theVolBar.setEffect(highlightBlendVolume);
					else
						theVolBar.setEffect(null);
				}
			}
		};
		
		ct.getScrollbar().valueProperty().addListener(highlightedVolumesRedrawer);
	}

	/**
	 * add a button to toggle the dataclipboard. add text to label the
	 * dataclipboard button. translates are temporarily hard coded
	 * 
	 * @param ct
	 * @param im
	 */
	private void addClipboardButton(SMPFXController ct, ImageLoader im) {
		ImageView e = new ImageView(im.getSpriteFX(ImageIndex.STOP_PRESSED));
		final DataClipboardButton dcb = new DataClipboardButton(e, ct, im);
		e.setTranslateX(600);
		e.setTranslateY(460);
		Text t = new Text("clipboard (s-R)");
		t.setTranslateX(580);
		t.setTranslateY(455);
		
		AnchorPane basePane = ct.getBasePane();
		basePane.getChildren().add(e);
		basePane.getChildren().add(t);

		// TODO: should be added to the entire scene
		basePane.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				if(event.isShiftDown() && event.getCode() == KeyCode.R)
					dcb.reactPressed(null);
			}
		});
	}
	
	/**
	 * trigger layout pass in basePane and all its children. this will
	 * initialize the bounds for all UI components.
	 * 
	 * components cannot be modified until this function initializes them.
	 * 
	 * @param ct
	 */
	private void redrawUI(SMPFXController ct) {
		AnchorPane basePane = ct.getBasePane();
		basePane.applyCss();//requestLayout??
		basePane.layout();
	}
	
	/**
	 * redrawUI before calling this 
	 * 
	 * @param rbeh
	 * @param ct
	 */
	private void initializeRBEH(RubberBandEventHandler rbeh, SMPFXController ct) {
		
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
	
	public HashMap<Integer, StaffNoteLine> getSelection() {
		return selection;
	}
	
	public HashMap<Integer, StaffNoteLine> getCopiedData() {
		return copiedData;
	}
	
	public HashSet<Integer> getHighlightedVolumes() {
		return highlightedVolumes;
	}
	
	public ChangeListener<Number> getHighlightedVolumesRedrawer() {
		return highlightedVolumesRedrawer;
	}
	
	public InstrumentFilter getInstrumentFilter() {
		return instFilter;
	}
	
	// temp? merge the two classes together?
	public DataClipboardAPI getAPI() {
		return theAPI;
	}
}
