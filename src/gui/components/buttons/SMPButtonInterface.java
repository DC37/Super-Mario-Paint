package gui.components.buttons;
import javafx.beans.binding.Binding;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Subscription;

/**
 * <p>Common interface for buttons that must be displayed as one of two images, depending
 * on whether they are pressed or not. The interface only includes two properties for
 * holding the images, and defines a few utility static methods.</p>
 */
public interface SMPButtonInterface {
    
    /**
     * Image to display when the button is not pressed
     * @return the image to display when the button is not pressed
     */
    public ObjectProperty<Image> imageReleased();
    public Image getImageReleased();
    public void setImageReleased(Image imageReleased);
    
    /**
     * Image to display when the button is pressed
     * @return the image to display when the button is pressed
     */
    public ObjectProperty<Image> imagePressed();
    public Image getImagePressed();
    public void setImagePressed(Image imagePressed);
    
    /**
     * Bound to the {@link ImageView} property of the same name
     */
    public DoubleProperty fitHeight();
    public double getFitHeight();
    public void setFitHeight(double x);
    
    /**
     * Bound to the {@link ImageView} property of the same name
     */
    public DoubleProperty fitWidth();
    public double getFitWidth();
    public void setFitWidth(double x);
    
    /**
     * <p>Utility method to bind some {@link Image} property (typically the image of an {@link ImageView})
     * to the images of a button implementing {@link ImageButtonInterface}</p>
     * 
     * <p>Each of the button's image properties may hold a {@code null} value. Depending on which
     * images are set, we fall into one of three cases:</p>
     * <ul>
     * <li>Both images are set and the binding works as expected;</li>
     * <li>Only the <i>released</i> image is set, in which case that image is also used for
     * the <i>pressed</i> state</li>
     * <li>The <i>released</i> image is not set, in which case default images are used for
     * both states (if an image was set for the <i>pressed</i> state then it is ignored.)</li>
     * </ul>
     * 
     * @param button the button
     * @param conditionPressed the observable holding {@code true} when the button is pressed
     * @param imageProperty the Image property to bind
     * @return a subscription to undo the binding when desired
     */
    static Subscription subscribeImageProperty(
            SMPButtonInterface button,
            ObservableValue<Boolean> conditionPressed,
            ObjectProperty<Image> imageProperty)
    {
        Binding<Image> imageBinding = imageBinding(conditionPressed, button.imageReleased(), button.imagePressed());
        imageProperty.bind(imageBinding);
        
        return () -> imageProperty.unbind();
    }
    
    /**
     * <p>Binds a node, forcing it to be an {@link ImageView} whose image changes depending
     * on the button's state. See {@link subscribeImageProperty}.</p>
     * @param button the button
     * @param conditionPressed the observable holding {@code true} when the button is pressed
     * @param imageViewProperty the Node property to bind
     * @return a subscription to undo the binding when desired
     */
    static Subscription subscribeNodeProperty(
            SMPButtonInterface button,
            ObservableValue<Boolean> conditionPressed,
            ObjectProperty<Node> imageViewProperty)
    {
        ImageView imageView = new ImageView();
        imageView.setPreserveRatio(true);
        imageView.setSmooth(false);
        imageView.fitHeightProperty().bind(button.fitHeight());
        imageView.fitWidthProperty().bind(button.fitWidth());
        imageViewProperty.bind(new SimpleObjectProperty<>(imageView));
        ObjectProperty<Image> imageProperty = imageView.imageProperty();
        
        return subscribeImageProperty(button, conditionPressed, imageProperty)
        		.and(() -> imageView.fitHeightProperty().unbind())
        		.and(() -> imageView.fitWidthProperty().unbind())
                .and(() -> imageViewProperty.unbind());
    }
    
    private static Binding<Image> imageBinding(
            ObservableValue<Boolean> conditionPressed,
            ObservableValue<Image> imageReleased,
            ObservableValue<Image> imagePressed)
    {
        return Bindings.<Image>createObjectBinding(() -> {
            if (conditionPressed == null || conditionPressed.getValue() == null) {
                return DEFAULT_IMAGE_RELEASED;
            }
            
            boolean isPressed = conditionPressed.getValue();
            
            if (imageReleased == null || imageReleased.getValue() == null) {
                return isPressed ? DEFAULT_IMAGE_PRESSED : DEFAULT_IMAGE_RELEASED;
            }
            
            if (imagePressed == null || imagePressed.getValue() == null) {
                return imageReleased.getValue();
            }
            
            return isPressed ? imagePressed.getValue() : imageReleased.getValue();
        }, conditionPressed, imageReleased, imagePressed);
    }
    
    public static Image DEFAULT_IMAGE_RELEASED = new Image(SMPButtonInterface.class.getResourceAsStream("/resources/GENERIC_BUTTON_RELEASED.png"));
    public static Image DEFAULT_IMAGE_PRESSED = new Image(SMPButtonInterface.class.getResourceAsStream("/resources/GENERIC_BUTTON_PRESSED.png"));
    
    public static String STYLE_CLASS_IMAGE_BUTTON = "smp-button";
    public static String STYLE_CLASS_IMAGE_INSTRUMENT_BUTTON = "smp-instrument-button";

}
