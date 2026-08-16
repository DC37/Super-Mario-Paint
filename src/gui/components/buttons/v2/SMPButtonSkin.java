package gui.components.buttons.v2;

import gui.resources.SMPResourceType;
import gui.resources.SMPResourceUtil;
import javafx.beans.binding.Binding;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.SkinBase;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Subscription;

public class SMPButtonSkin<B extends ButtonBase> extends SkinBase<SMPAbstractButton<B>> {

	protected static Image DEFAULT_IMAGE_RELEASED = new Image(
			SMPResourceUtil.getStream("GENERIC_BUTTON_RELEASED.png", SMPResourceType.BUTTON));
    protected static Image DEFAULT_IMAGE_PRESSED = new Image(
    		SMPResourceUtil.getStream("GENERIC_BUTTON_PRESSED.png", SMPResourceType.BUTTON_PRESSED));
	
	protected final SMPAbstractButton<B> smpBtn;
	
	protected Subscription imgSub;
	
	protected SMPButtonSkin(SMPAbstractButton<B> smpBtn) {
		super(smpBtn);
		
		this.smpBtn = smpBtn;
		
		getChildren().add(smpBtn.getInnerButton());
	}
	
	@Override
	public void install() {
		imgSub = subscribeNodeProperty(smpBtn.armedProperty(), smpBtn.graphicProperty());
	}
	
	@Override
	public void dispose() {
		imgSub.unsubscribe();
	}
	
	/**
     * <p>Binds a node, forcing it to be an {@link ImageView} whose image changes depending
     * on this button's state. See {@link subscribeImageProperty}.</p>
     * 
     * @param conditionPressed the observable holding {@code true} when the button is pressed
     * @param imageViewProperty the Node property to bind
     * @return a subscription to undo the binding when desired
     */
    protected Subscription subscribeNodeProperty(
            ObservableValue<Boolean> conditionPressed,
            ObjectProperty<Node> imageViewProperty)
    {
        ImageView imageView = new ImageView();
        imageView.setPreserveRatio(true);
        imageView.setSmooth(false);
        imageView.fitHeightProperty().bind(smpBtn.fitHeightProperty());
        imageView.fitWidthProperty().bind(smpBtn.fitWidthProperty());
        imageViewProperty.bind(new SimpleObjectProperty<>(imageView));
        ObjectProperty<Image> imageProperty = imageView.imageProperty();
        
        return subscribeImageProperty(conditionPressed, imageProperty)
                .and(imageView.fitHeightProperty()::unbind)
                .and(imageView.fitWidthProperty()::unbind)
                .and(imageViewProperty::unbind);
    }
	
	/**
     * <p>Utility method to bind some {@link Image} property (typically the image of an {@link ImageView})
     * to the images of this button implementing {@link ImageButtonInterface}</p>
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
     * @param conditionPressed the observable holding {@code true} when the button is pressed
     * @param imageProperty the Image property to bind
     * @return a subscription to undo the binding when desired
     */
    private Subscription subscribeImageProperty(
            ObservableValue<Boolean> conditionPressed,
            ObjectProperty<Image> imageProperty)
    {
        Binding<Image> imageBinding = imageBinding(conditionPressed,
        		smpBtn.imageReleasedProperty(), smpBtn.imagePressedProperty());
        imageProperty.bind(imageBinding);
        
        return imageProperty::unbind;
    }
    
    private Binding<Image> imageBinding(
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

}
