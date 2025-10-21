package gui.components.buttons;
import java.util.Timer;
import java.util.TimerTask;

import gui.Values;
import javafx.beans.Observable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Skin;
import javafx.scene.control.skin.ButtonSkin;
import javafx.scene.image.Image;
import javafx.util.Subscription;

/**
 * <p>A button that is displayed as an image. Two images can be registered as properties:
 * one for when the button is released and one for when the button is pressed.</p>
 */
public class SMPButton extends Button implements SMPButtonInterface {

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
    
    /**
     * If set, this handler will trigger an action to repeat as long as the button is pressed
     */
    private ObjectProperty<EventHandler<ActionEvent>> onHold;
    public ObjectProperty<EventHandler<ActionEvent>> onHold() {
        if (onHold == null) {
            onHold = new SimpleObjectProperty<EventHandler<ActionEvent>>(this, "onHold", null);
        }
        return onHold;
    }
    public EventHandler<ActionEvent> getOnHold() { return onHold().getValue(); }
    public void setOnHold(EventHandler<ActionEvent> handler) { onHold().setValue(handler); }
    // This is never actually handled like an event because idk how to do that tbh
    
    public SMPButton() {
        super();
        initialize();
    }
    
    public SMPButton(String text) {
        super(text);
        initialize();
    }
    
    public SMPButton(String text, Image imageReleased) {
        this(text, imageReleased, null);
    }
    
    public SMPButton(String text, Image imageReleased, Image imagePressed) {
        super(text);
        initialize();
        setImageReleased(imageReleased);
        setImagePressed(imagePressed);
    }
    
    // We'll observe the onHold property so that setting it to some handler will set up the button
    // with a push-and-hold behavior: a change listener on the armed property either starts a timer to
    // repeat the action as we press, or cancels the ongoing timer.
    // Leaving onHold to null makes this a regular button with a possible onAction handler.
    // Please don't set both onAction and onHold, I have no idea what this would do.
    
    Timer repeatOnHoldTimer;
    Runnable handleOnHoldAction;
    Subscription armedSubscription = Subscription.EMPTY;
    
    private void initialize() {
        getStyleClass().add(STYLE_CLASS_IMAGE_BUTTON);
        
        handleOnHoldAction = () -> {
            if (isArmed()) {
                ActionEvent e = new ActionEvent();
                repeatOnHoldTimer = new Timer();
                TimerTask task = new TimerTask() {
                    @Override public void run() { getOnHold().handle(e); };
                };
                getOnHold().handle(e);
                repeatOnHoldTimer.schedule(task, Values.HOLDTIME, Values.REPEATTIME);
                
            } else if (repeatOnHoldTimer != null) {
                repeatOnHoldTimer.cancel();
            }
        };
        
        onHold().addListener(this::onHoldListener);
    }
    
    private void onHoldListener(Observable obs) {
        armedSubscription.unsubscribe();
        
        if (getOnHold() != null) {
            ObservableValue<Boolean> armedProperty = armedProperty();
            ChangeListener<Boolean> armedListener = this::armedListener;
            armedProperty.addListener(armedListener);
            armedSubscription = () -> armedProperty.removeListener(armedListener);
        } else {
            armedSubscription = Subscription.EMPTY;
        }
    }
    
    private void armedListener(Observable obs, Boolean oldv, Boolean newv) {
        handleOnHoldAction.run();
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
                imageSubscription = SMPButtonInterface.subscribeNodeProperty(SMPButton.this, armedProperty(), graphicProperty());
            }
            
            @Override
            public void dispose() {
                imageSubscription.unsubscribe();
            }
        };
    }

}
