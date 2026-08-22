package gui.components.buttons;

import java.util.Timer;
import java.util.TimerTask;

import gui.Values;
import javafx.beans.Observable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Subscription;

/**
 * <p>A button that is displayed as an image. This variant implements an optional action that
 * is triggered repeatedly when the button is held.</p>
 */
public class SMPHoldButton extends SMPButton {

	/**
     * If set, this handler will trigger an action to repeat as long as the button is pressed
     */
    private final ObjectProperty<EventHandler<ActionEvent>> onHold;
    
    private Timer repeatOnHoldTimer;
    private Runnable handleOnHoldAction;
    private Subscription armedSubscription = Subscription.EMPTY;
    
    public SMPHoldButton() {
    	this(null);
    }
    
    public SMPHoldButton(String text) {
    	super(text);
    	
    	// This is never actually handled like an event because idk how to do that tbh
    	onHold = new SimpleObjectProperty<>(this, "onHold", null);
    	
    	initialize();
    }
    
    // We'll observe the onHold property so that setting it to some handler will set up the button
    // with a push-and-hold behavior: a change listener on the armed property either starts a timer to
    // repeat the action as we press, or cancels the ongoing timer.
    // Leaving onHold to null makes this a regular button with a possible onAction handler.
    // Please don't set both onAction and onHold, I have no idea what this would do.
    private void initialize() {
        handleOnHoldAction = () -> {
            if (isArmed()) {
                ActionEvent e = new ActionEvent();
                repeatOnHoldTimer = new Timer();
                TimerTask task = new TimerTask() {
                    @Override public void run() { getOnHold().handle(e); }
                };
                getOnHold().handle(e);
                repeatOnHoldTimer.schedule(task, Values.HOLD_TIME, Values.REPEAT_TIME);
                
            } else if (repeatOnHoldTimer != null) {
                repeatOnHoldTimer.cancel();
            }
        };
        
        onHold.addListener(this::onHoldListener);
        
        getStyleClass().addListener((ListChangeListener<String>) change -> {
        	while (change.next()) {
        		if (change.wasAdded()) {
        			innerBtn.getStyleClass().addAll(change.getAddedSubList());
        		}
        		if (change.wasRemoved()) {
        			innerBtn.getStyleClass().removeAll(change.getRemoved());
        		}
        	}
        });
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
    
    /* Extended properties START */
    
    public ObjectProperty<EventHandler<ActionEvent>> onHoldProperty() {
        return onHold;
    }
    
    public EventHandler<ActionEvent> getOnHold() {
    	return onHold.get();
    }
    
    public void setOnHold(EventHandler<ActionEvent> value) {
    	onHold.set(value);
    }
    
    /* Extended properties END */
	
}
