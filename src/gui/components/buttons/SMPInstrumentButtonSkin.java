package gui.components.buttons;

import java.util.Optional;
import java.util.function.Function;

import gui.Values;
import javafx.animation.FadeTransition;
import javafx.beans.binding.Binding;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.skin.ButtonSkin;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import javafx.util.Subscription;

public class SMPInstrumentButtonSkin extends ButtonSkin {

	private SMPInstrumentButton source;
	
	private Subscription mainSubscription;
    private ImageView filterIv;
    private FadeTransition fadeTransition;
	
	public SMPInstrumentButtonSkin(SMPInstrumentButton source) {
		super(source);
		
		this.source = source;
	}
	
	private Binding<Node> buildGraphic(ObservableValue<Node> button, Node filter) {
        return Bindings.createObjectBinding(() -> {
            if (button == null || button.getValue() == null)
                return null;
            
            StackPane stack = new StackPane();
            stack.getChildren().addAll(button.getValue(), filter);
            return stack;
        }, button);
    }
    
    private ImageView buildFilter() {
        ImageView filter = new ImageView();
        filter.setPreserveRatio(true);
        filter.setSmooth(false);
        filter.setMouseTransparent(true);
        return filter;
    }
    
    private Subscription filterSubscription(ImageView filter) {
        filter.fitHeightProperty().bind(source.fitHeight());
        filter.fitWidthProperty().bind(source.fitWidth());
        filter.imageProperty().bind(source.imageFiltered());
        filter.visibleProperty().bind(Bindings.and(source.active(), Bindings.not(Values.INSTRUMENT_BTNS_GROUP.allActive())));
        
        return () -> {
            filter.fitHeightProperty().unbind();
            filter.fitWidthProperty().unbind();
            filter.imageProperty().unbind();
            filter.visibleProperty().unbind();
        };
    }
    
    private Subscription imageSustainSubscription() {
        source.imageReleased().bind(imageSustainBinding());
        return (() -> source.imageReleased().unbind());
    }
    
    private Binding<Image> imageSustainBinding() {
        return Bindings.createObjectBinding(
                () -> (source.isSustainOn()) ? source.getImageSustainOn() : source.getImageSustainOff(),
                source.sustainOn(), source.imageSustainOff(), source.imageSustainOn());
    }
    
    private FadeTransition makeTransition(ImageView iv) {
        FadeTransition ft = new FadeTransition(Duration.millis(2000), iv);
        ft.setFromValue(1.0);
        ft.setToValue(0.0);
        return ft;
    }
    
    private EventHandler<MouseEvent> buildOnMouseEvent(
    		EventType<MouseEvent> target,
    		Function<SMPInstrumentButton, Runnable> fnOnMouseEvent) {
    	
    	return (MouseEvent probe) -> {
    		if (probe.getEventType() != target)
	    		return;
	    	
	    	for (SMPInstrumentButton b : Values.INSTRUMENT_BTNS_GROUP) {
	    		Optional.ofNullable(fnOnMouseEvent)
	    				.map(fn -> fn.apply(b))
	    				.ifPresent(Runnable::run);
	        }
    	};
    }
    
    private Subscription activateFadeoutSubscription() {
        EventHandler<MouseEvent> onEntered = buildOnMouseEvent(
        		MouseEvent.MOUSE_ENTERED, SMPInstrumentButton::getMouseEnterListener);
        
        EventHandler<MouseEvent> onExited = buildOnMouseEvent(
        		MouseEvent.MOUSE_EXITED, SMPInstrumentButton::getMouseExitListener);
        
        source.setMouseEnterListener(this::fadeoutReset);
        source.setMouseExitListener(this::fadeoutPlay);
        
        Button c = getSkinnable();
        c.addEventHandler(MouseEvent.MOUSE_ENTERED, onEntered);
        c.addEventHandler(MouseEvent.MOUSE_EXITED, onExited);
        
        return () -> {
            c.removeEventHandler(MouseEvent.MOUSE_ENTERED, onEntered);
            c.removeEventHandler(MouseEvent.MOUSE_EXITED, onExited);
            source.setMouseEnterListener(null);
            source.setMouseExitListener(null);
        };
    }
    
    private Subscription onMousePressedSubscription() {
        Button b = getSkinnable();
        
        // ~ Dirty hack ~
        // Normally a mouse press with key modifiers such as Ctrl or Shift does not arm the button
        // We override this in the only possible way (original implementation is private API)
        EventHandler<MouseEvent> onMousePressed = (MouseEvent event) -> {
            if (event.getEventType() == MouseEvent.MOUSE_PRESSED)
                b.arm();
        };
        
        b.addEventHandler(MouseEvent.MOUSE_PRESSED, onMousePressed);
        return () -> b.removeEventHandler(MouseEvent.MOUSE_PRESSED, onMousePressed);
    }
    
    @Override
    public void install() {
        Values.INSTRUMENT_BTNS_GROUP.addButton(source);
        ObjectProperty<Node> buttonImageView = new SimpleObjectProperty<>(null);
        filterIv = buildFilter();
        fadeTransition = makeTransition(filterIv);
        ObservableValue<Node> graphic = buildGraphic(buttonImageView, filterIv);
        source.graphicProperty().bind(graphic);
        
        mainSubscription = SMPButtonInterface
        		.subscribeNodeProperty(source, source.armedProperty(), buttonImageView)
                .and(filterSubscription(filterIv))
                .and(imageSustainSubscription())
                .and(activateFadeoutSubscription())
                .and(() -> source.graphicProperty().unbind())
                .and(onMousePressedSubscription())
                .and(() -> Values.INSTRUMENT_BTNS_GROUP.removeButton(source));
    }

    private void fadeoutReset() {
        filterIv.setOpacity(1.0);
        fadeTransition.pause();
    }
    
    private void fadeoutPlay() {
        fadeTransition.playFromStart();
    }
    
    @Override
    public void dispose() {
        mainSubscription.unsubscribe();
    }
	
}
