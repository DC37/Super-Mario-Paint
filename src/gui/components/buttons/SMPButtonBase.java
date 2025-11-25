package gui.components.buttons;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Button;
import javafx.scene.control.Skin;
import javafx.scene.control.skin.ButtonSkin;
import javafx.scene.image.Image;
import javafx.util.Subscription;

public abstract class SMPButtonBase extends Button implements SMPButtonInterface {

    /**
     * The image for the <i>released</i> state
     */
    private ObjectProperty<Image> imageReleased;
    public ObjectProperty<Image> imageReleased() {
        if (imageReleased == null) {
            imageReleased = new SimpleObjectProperty<Image>(this, "imageReleased", null);
        }
        return imageReleased;
    }
    public Image getImageReleased() { return imageReleased().getValue(); }
    public void setImageReleased(Image imageReleased) { imageReleased().setValue(imageReleased); }

    /**
     * The image for the <i>pressed</i> state
     */
    private ObjectProperty<Image> imagePressed;
    public ObjectProperty<Image> imagePressed() {
        if (imagePressed == null) {
            imagePressed = new SimpleObjectProperty<Image>(this, "imagePressed", null);
        }
        return imagePressed;
    }
    public Image getImagePressed() { return imagePressed().getValue(); }
    public void setImagePressed(Image imagePressed) { imagePressed().setValue(imagePressed); }
    
    public SMPButtonBase(String text, Image imageReleased, Image imagePressed) {
        super(text, null);
        initialize();
        setImageReleased(imageReleased);
        setImagePressed(imagePressed);
    }
    
    private void initialize() {
        getStyleClass().add(STYLE_CLASS_IMAGE_BUTTON);
    }
    
    @Override
    protected Skin<?> createDefaultSkin() {
        // We extend an anonymous class because ButtonSkin lacks genericity.
        // It's not possible make a class extending ButtonSkin whose getSkinnable
        // returns anything other than a Button.
        return new ButtonSkin(this) {
            Subscription imageSubscription;
            
            @Override
            public void install() {
                imageSubscription = SMPButtonInterface.subscribeNodeProperty(SMPButtonBase.this, armedProperty(), graphicProperty());
            }
            
            @Override
            public void dispose() {
                imageSubscription.unsubscribe();
            }
        };
    }

}
