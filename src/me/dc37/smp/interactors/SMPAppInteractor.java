package me.dc37.smp.interactors;

import java.util.Random;

import gui.SMPInstrument;
import gui.loaders.ImageLoader;
import gui.resources.SMPResourceType;
import gui.resources.SMPResourceUtil;
import javafx.scene.image.Image;
import me.dc37.smp.models.SMPAppModel;

public class SMPAppInteractor {

	/**
     * Random number generator (RNG) for icon selection.
     */
    private static final Random RNG = new Random();
    
    private final SMPAppModel model;
    
    public SMPAppInteractor(SMPAppModel model) {
    	this.model = model;
    }
    
    /**
     * Selects the header icon to use as Application Icon in the Window Title Bar.
     * 
     * <p>It sets the default icon 9 times out of 10, or a random instrument otherwise.
     * 
     * @implNote This cannot use the {@link ImageLoader} infrastructure, as images
     * are loaded through it <i>after</i> the application starts the JavaFX scaffolding.
     */
    public void setHeaderIcon() {
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
        
        model.setHeaderIcon(new Image(SMPResourceUtil.get(iconName + ".png", iconType).toString()));
    }
	
}
