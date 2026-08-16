package gui.components.buttons.v2;

import java.util.function.Supplier;

import org.apache.commons.lang3.NotImplementedException;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.scene.image.Image;

public abstract class SMPAbstractButton<B extends ButtonBase> extends Control {
	
	protected static final String STYLE_CLASS_IMAGE_BUTTON = "smp-button";

	protected final B innerBtn;
	
	protected final ObjectProperty<Image> imageReleased;
    protected final ObjectProperty<Image> imagePressed;
    protected final DoubleProperty fitWidth;
    protected final DoubleProperty fitHeight;
	
	protected SMPAbstractButton(Supplier<B> fnCreateInnerBtn) {
		super();
		
		innerBtn = fnCreateInnerBtn.get();
		
		imageReleased = new SimpleObjectProperty<>(this, "imageReleased", null);
		imagePressed = new SimpleObjectProperty<>(this, "imagePressed", null);
		fitWidth = new SimpleDoubleProperty(this, "fitWidth", 0.0);
		fitHeight = new SimpleDoubleProperty(this, "fitHeight", 0.0);
		
		innerBtn.getStyleClass().add(getClassStyleCssName());
		innerBtn.focusTraversableProperty().bind(focusTraversableProperty());
	}
	
	/* Button methods START */
	
	public void fire() {
		if (!innerBtn.isDisabled()) {
            innerBtn.fireEvent(new ActionEvent());
        }
	}
	
	public StringProperty textProperty() {
		return innerBtn.textProperty();
	}
	
	public String getText() {
		return innerBtn.getText();
	}
	
	public void setText(String value) {
		innerBtn.setText(value);
	}
	
	public ObjectProperty<EventHandler<ActionEvent>> onActionProperty() {
		return innerBtn.onActionProperty();
	}
	
	public EventHandler<ActionEvent> getOnAction() {
		return innerBtn.getOnAction();
	}
	
	public void setOnAction(EventHandler<ActionEvent> value) {
		innerBtn.setOnAction(value);
	}

	public ReadOnlyBooleanProperty armedProperty() {
		return innerBtn.armedProperty();
	}
	
	public boolean isArmed() {
		return innerBtn.isArmed();
	}
	
	public void arm() {
		innerBtn.arm();
	}
	
	public void disarm() {
		innerBtn.disarm();
	}
	
	public ObjectProperty<Node> graphicProperty() {
		return innerBtn.graphicProperty();
	}
	
	public Node getGraphic() {
		return innerBtn.getGraphic();
	}
	
	public void setGraphic(Node value) {
		innerBtn.setGraphic(value);
	}
	
	/* Button methods END */
	
	/* Extended properties START */
	
	public ObjectProperty<Image> imageReleasedProperty() {
		return imageReleased;
	}
	
	public Image getImageReleased() {
		return imageReleased.get();
	}
	
	public void setImageReleased(Image value) {
		imageReleased.set(value);
	}
	
	public ObjectProperty<Image> imagePressedProperty() {
		return imagePressed;
	}
	
	public Image getImagePressed() {
		return imagePressed.get();
	}
	
	public void setImagePressed(Image value) {
		imagePressed.set(value);
	}
	
	public DoubleProperty fitWidthProperty() {
		return fitWidth;
	}
	
	public double getFitWidth() {
		return fitWidth.get();
	}
	
	public void setFitWidth(double value) {
		fitWidth.set(value);
	}
	
	public DoubleProperty fitHeightProperty() {
		return fitHeight;
	}
	
	public double getFitHeight() {
		return fitHeight.get();
	}
	
	public void setFitHeight(double value) {
		fitHeight.set(value);
	}
	
	/* Extended properties END */
	
	public B getInnerButton() {
		return innerBtn;
	}
	
	public String getClassStyleCssName() {
		return STYLE_CLASS_IMAGE_BUTTON;
	}
	
	@Override
	protected Skin<?> createDefaultSkin() {
		return new SMPButtonSkin<>(this);
	}
	
}
