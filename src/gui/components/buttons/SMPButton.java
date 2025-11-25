package gui.components.buttons;
import javafx.scene.image.Image;

/**
 * <p>A button that is displayed as an image. Two images can be registered as properties:
 * one for when the button is released and one for when the button is pressed.</p>
 */
public class SMPButton extends SMPButtonBase {
	
	public SMPButton() {
		this("", null, null);
	}
	
	public SMPButton(String text) {
		this(text, null, null);
	}
    
    public SMPButton(String text, Image imageReleased) {
        this(text, imageReleased, null);
    }
    
    public SMPButton(String text, Image imageReleased, Image imagePressed) {
        super(text, imageReleased, imagePressed);
    }

}
