package gui.components.toppanel;

import gui.SMPFXController;
import gui.Staff;
import gui.loaders.ImageLoader;

/**
 * Panel buttons that are on the top panel.
 * @author RehdBlob
 * @since 2014.05.21
 */
public class PanelButtons {

    /** The Staff that this is linked to. */
    private Staff theStaff;

    /** The FXML controller class. */
    private SMPFXController controller;

    /** The Image Loader class. */
    private ImageLoader il;

    /** The button line, where one selects instruments. */
    private ButtonLine bl;

    /** Default constructor. */
    public PanelButtons(Staff s, SMPFXController ct, ImageLoader im, ButtonLine b) {
        il = im;
        theStaff = s;
        setController(ct);
        bl = b;
    }

    /**
     * Sets the controller class.
     * @param ct The FXML controller class.
     */
    public void setController(SMPFXController ct) {
        controller = ct;
    }

    /**
     * @param b The Button Line that we want to associate with the top panel.
     */
    public void setButtonLine(ButtonLine b) {
        bl = b;
    }

    /**
     * @return The ButtonLine that is associated with the top panel.
     */
    public ButtonLine getButtonLine() {
        return bl;
    }

}
