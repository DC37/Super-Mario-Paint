package gui.components.buttons;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Skin;
import javafx.scene.control.skin.ToggleButtonSkin;
import javafx.scene.image.Image;
import javafx.util.Subscription;

/**
 * <p>A radio button displayed as an image. See {@link ImageButton} and {@link ImageToggleButton}.</p>
 */
public class SMPRadioButton extends RadioButton implements SMPButtonInterface {

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
    
    public SMPRadioButton() {
        super();
        initialize();
    }
    
    public SMPRadioButton(String text) {
        super(text);
        initialize();
    }
    
    public SMPRadioButton(String text, Image imageReleased) {
        this(text, imageReleased, null);
    }
    
    public SMPRadioButton(String text, Image imageReleased, Image imagePressed) {
        super(text);
        initialize();
        setImageReleased(imageReleased);
        setImagePressed(imagePressed);
    }
    
    private void initialize() {
        getStyleClass().add(STYLE_CLASS_IMAGE_BUTTON);
    }
    
    @Override
    protected Skin<?> createDefaultSkin() {
        // We don't extend RadioButtonSkin because that skin adds an unwanted "dot"
        return new ToggleButtonSkin(this) {
            Subscription imageSubscription;
            ObservableValue<Boolean> conditionPressed = Bindings.or(armedProperty(), selectedProperty());
            
            @Override
            public void install() {
                imageSubscription = SMPButtonInterface.subscribeNodeProperty(SMPRadioButton.this, conditionPressed, graphicProperty());
            }
            
            @Override
            public void dispose() {
                imageSubscription.unsubscribe();
            }
        };
    }

}
