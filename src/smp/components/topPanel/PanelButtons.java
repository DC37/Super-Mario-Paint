package smp.components.topPanel;

import javafx.scene.text.Text;
import smp.components.buttons.ModeButton;
import smp.components.staff.Staff;
import smp.fx.SMPFXController;

/**
 * Panel buttons that are on the top panel.
 * @author RehdBlob
 * @since 2014.05.21
 */
public class PanelButtons {

    /** The Staff that this is linked to. */
    private Staff theStaff;

    /** Mode button. */
    private ModeButton mButton;

    /** Default constructor. */
    public PanelButtons(Staff s) {
        theStaff = s;
        mButton = new ModeButton(SMPFXController.getModeButton(),
                SMPFXController.getModeText());
        mButton.setStaff(theStaff);
    }



}
