package gui.clipboard;

import gui.SMPFXController;

public class HardcodedRubberBandEventHandlerInitializer implements RubberBandEventHandlerInitializer {

	@Override
	public void initialize(StaffRubberBandEventHandler rbeh, SMPFXController ct) {
		// The original code would compute the following values by inspecting certain nodes
		// in the scene. Since I want to encapsulate the staff code, and since those values
		// are always the same (until we make the staff resizable), I'm hardcoding the values
		// until a better solution can be implemented. -rozlyn
		
		rbeh.initializeLineMinBound(185);
        rbeh.initializeLineMaxBound(1017);
        rbeh.initializeLineSpacing(64);
        rbeh.initializePositionMinBound(47);
        rbeh.initializePositionMaxBound(541);
        rbeh.initializePositionSpacing(32);
        rbeh.initializeVolumeYMaxCoord(591);
        rbeh.setMarginVertical(5);
        rbeh.setMarginHorizontal(183);
        
        //set scrollbar resizing
        rbeh.setScrollBarResizable(ct.getScrollbar());
	}
	
}
