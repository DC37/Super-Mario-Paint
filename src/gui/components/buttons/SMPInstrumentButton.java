package gui.components.buttons;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.css.PseudoClass;
import javafx.scene.control.Skin;
import javafx.scene.image.Image;

/**
 * <p>A version of {@link SMPButton} used specifically for instrument buttons in the top panel.</p>
 * 
 * <p>This defines properties that record the state of the instrument (filtered, sustain on/off) and sets up
 * a custom skin.</p>
 */
public class SMPInstrumentButton extends SMPButton {
    
    /**
     * Is this instrument active? (instruments are rendered inactive by filtering)
     */
    private BooleanProperty active;
    public BooleanProperty active() {
        if (active == null) {
            active = new SimpleBooleanProperty(this, "active", true);
        }
        return active;
    }
    public boolean isActive() { return active().getValue(); }
    public boolean isInactive() { return !isActive(); }
    public void setActive(boolean b) { active().setValue(b); }
    
    /**
     * Is sustain currently activated for this instrument?
     */
    private BooleanProperty sustainOn;
    public BooleanProperty sustainOn() {
        if (sustainOn == null) {
            sustainOn = new SimpleBooleanProperty(this, "sustainOn", false);
        }
        return sustainOn;
    }
    public boolean isSustainOn() { return sustainOn().getValue(); }
    public void setSustainOn(boolean b) { sustainOn().setValue(b); }

    /**
     * The image for the regular (released) state
     */
    private ObjectProperty<Image> imageSustainOff;
    public ObjectProperty<Image> imageSustainOff() {
        if (imageSustainOff == null) {
            imageSustainOff = new SimpleObjectProperty<>(this, "imageSustainOff", null);
        }
        return imageSustainOff;
    }
    public Image getImageSustainOff() { return imageSustainOff().getValue(); }
    public void setImageSustainOff(Image imageSustainOff) { imageSustainOff().setValue(imageSustainOff); }

    /**
     * The image for the <i>sustain</i> (released) state
     */
    private ObjectProperty<Image> imageSustainOn;
    public ObjectProperty<Image> imageSustainOn() {
        if (imageSustainOn == null) {
            imageSustainOn = new SimpleObjectProperty<>(this, "imageSustainOn", null);
        }
        return imageSustainOn;
    }
    public Image getImageSustainOn() { return imageSustainOn().getValue(); }
    public void setImageSustainOn(Image imageSustainOn) { imageSustainOn().setValue(imageSustainOn); }
    
    /**
     * The image for the <i>filtered</i> state (overlayed)
     */
    private ObjectProperty<Image> imageFiltered;
    public ObjectProperty<Image> imageFiltered() {
        if (imageFiltered == null) {
            imageFiltered = new SimpleObjectProperty<>(this, "imageFiltered", null);
        }
        return imageFiltered;
    }
    public Image getImageFiltered() { return imageFiltered().getValue(); }
    public void setImageFiltered(Image imageFiltered) { imageFiltered().setValue(imageFiltered); }
    
    private Runnable mouseEnterListener = null;
    public Runnable getMouseEnterListener() {
    	return mouseEnterListener;
    }
    public void setMouseEnterListener(Runnable mouseEnterListener) {
    	this.mouseEnterListener = mouseEnterListener;
    }
    
    private Runnable mouseExitListener = null;    
    public Runnable getMouseExitListener() {
    	return mouseExitListener;
    }
    public void setMouseExitListener(Runnable mouseExitListener) {
    	this.mouseExitListener = mouseExitListener;
    }
    
    public SMPInstrumentButton() {
        super();
        initialize();
    }
    
    public SMPInstrumentButton(String text) {
        super(text);
        initialize();
    }
    
    public SMPInstrumentButton(String text, Image imageSustainOff, Image imageSustainOn) {
        super(text);
        setImageSustainOff(imageSustainOff);
        setImageSustainOn(imageSustainOn);
        initialize();
    }
    
    private void initialize() {
        getStyleClass().add(STYLE_CLASS_IMAGE_INSTRUMENT_BUTTON);
        
        active().addListener(obs -> pseudoClassStateChanged(FILTERED_PSEUDO_CLASS, !isActive()));
        sustainOn().addListener(obs -> pseudoClassStateChanged(SUSTAINED_PSEUDO_CLASS, isSustainOn()));
    }
    
    private static final PseudoClass SUSTAINED_PSEUDO_CLASS = PseudoClass.getPseudoClass("sustained");
    private static final PseudoClass FILTERED_PSEUDO_CLASS = PseudoClass.getPseudoClass("filtered");
    
    @Override
    protected Skin<?> createDefaultSkin() {
    	return new SMPInstrumentButtonSkin(this);
    }

}
