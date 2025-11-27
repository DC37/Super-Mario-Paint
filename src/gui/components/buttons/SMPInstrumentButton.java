package gui.components.buttons;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.css.PseudoClass;
import javafx.scene.image.Image;

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
	
	public SMPInstrumentButton() {
		super();
		initialize();
	}
	
	public SMPInstrumentButton(String text) {
		super(text);
		initialize();
	}
    
    public SMPInstrumentButton(String text, Image imageReleased) {
        super(text, imageReleased);
        initialize();
    }
    
    private void initialize() {
        getStyleClass().add(STYLE_CLASS_IMAGE_INSTRUMENT_BUTTON);
        
        active().addListener(obs -> pseudoClassStateChanged(FILTERED_PSEUDO_CLASS, !isActive()));
        sustainOn().addListener(obs -> pseudoClassStateChanged(SUSTAINED_PSEUDO_CLASS, isSustainOn()));
    }
    
    private static final PseudoClass SUSTAINED_PSEUDO_CLASS = PseudoClass.getPseudoClass("sustained");
    private static final PseudoClass FILTERED_PSEUDO_CLASS = PseudoClass.getPseudoClass("filtered");

}
