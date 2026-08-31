package gui.components;

import java.util.Random;

import gui.SMPInstrument;
import gui.loaders.ImageLoader;
import gui.resources.SMPResourceType;
import gui.resources.SMPResourceUtil;
import javafx.scene.image.Image;

public class SMPIconService {

	/**
	 * The {@link Image} containing the icon to be used by the program.
	 */
	private static Image headerIcon = null;
	
	/**
     * Random number generator (RNG) for icon selection.
     */
    private static final Random RNG = new Random();
	
	/**
     * Selects the header icon to use as Application Icon in the Window Title Bar.
     * 
     * <p>It sets the default icon 9 times out of 10, or a random instrument otherwise.
     * 
     * @return An {@link Image} representing the chosen header icon.
     * 
     * @implNote This cannot use the {@link ImageLoader} infrastructure, as images
     * are loaded through it <i>after</i> the application starts the JavaFX scaffolding.
     */
	private static Image chooseHeaderIcon() {
		int randValue = RNG.nextInt(10 * SMPInstrument.values().length);
        
        String iconName;
        SMPResourceType iconType;
        
        if (randValue < SMPInstrument.values().length) {
            iconName = SMPInstrument.values()[randValue].name();
            iconType = SMPResourceType.INSTRUMENT;
        } else {
            iconName = "ICON";
            iconType = SMPResourceType.UI;
        }
        
        return new Image(SMPResourceUtil.get(iconName + ".png", iconType).toString());
	}
	
	public static Image getHeaderIcon() {
		if (headerIcon == null)
			headerIcon = chooseHeaderIcon();
		
		return headerIcon;
	}
	
}
