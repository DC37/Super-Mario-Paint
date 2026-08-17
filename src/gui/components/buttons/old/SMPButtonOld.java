package gui.components.buttons.old;
import javafx.scene.image.Image;

/**
 * <p>A button that is displayed as an image. Two images can be registered as properties:
 * one for when the button is released and one for when the button is pressed.</p>
 */
public class SMPButtonOld extends SMPButtonBase {
    
    public SMPButtonOld() {
        this("", null, null);
    }
    
    public SMPButtonOld(String text) {
        this(text, null, null);
    }
    
    public SMPButtonOld(String text, Image imageReleased) {
        this(text, imageReleased, null);
    }
    
    public SMPButtonOld(String text, Image imageReleased, Image imagePressed) {
        super(text, imageReleased, imagePressed);
    }

}
