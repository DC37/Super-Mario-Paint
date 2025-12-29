package gui.components.buttons;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
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
    
    private DoubleProperty fitHeight;
    public DoubleProperty fitHeight() {
    	if (fitHeight == null) {
    		fitHeight = new SimpleDoubleProperty(this, "fitHeight", 0.0);
    	}
    	return fitHeight;
    }
    public double getFitHeight() { return fitHeight().getValue(); }
    public void setFitHeight(double x) { fitHeight().setValue(x); }
    
    private DoubleProperty fitWidth;
    public DoubleProperty fitWidth() {
    	if (fitWidth == null) {
    		fitWidth = new SimpleDoubleProperty(this, "fitWidth", 0.0);
    	}
    	return fitWidth;
    }
    public double getFitWidth() { return fitWidth().getValue(); }
    public void setFitWidth(double x) { fitWidth().setValue(x); }
    
    
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
        // It's not possible to make a class extending ButtonSkin whose getSkinnable
        // method returns anything other than a Button.
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
