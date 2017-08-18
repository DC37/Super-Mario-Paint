package smp.clipboard;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import smp.ImageLoader;
import smp.components.staff.Staff;
import smp.fx.SMPFXController;

public class DataClipboard {

	private Staff theStaff;
	private SMPFXController controller;
	private ImageLoader il;
	private StackPane rubberBandLayer;
	private RubberBandEventHandler rbeh;
	
	public DataClipboard(Staff st, SMPFXController ct, ImageLoader im) {

        il = im;
        theStaff = st;
		controller = ct;
		rubberBandLayer = controller.getRubberBandLayer();
		rbeh = new RubberBandEventHandler(st, rubberBandLayer);
		rubberBandLayer.addEventHandler(MouseEvent.ANY, rbeh);
	}

}
