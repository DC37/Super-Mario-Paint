package smp.components.topPanel;

import javafx.scene.text.Text;
import smp.components.controls.ModeButton;
import smp.fx.SMPFXController;

/**
 * Panel buttons that are on the top panel.
 * @author RehdBlob
 * @since 2014.05.21
 */
public class PanelButtons {

    /** Mode text. */
    private Text mText;

    /** Mode button. */
    private ModeButton mButton;

    /** Default constructor. */
    public PanelButtons() {
        mButton = new ModeButton(SMPFXController.getModeButton());
        mText = SMPFXController.getModeText();
    }



}
