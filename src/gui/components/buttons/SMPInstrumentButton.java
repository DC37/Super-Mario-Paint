package gui.components.buttons;

import java.util.ArrayList;
import java.util.Collection;

import gui.StateMachine;
import javafx.beans.binding.Binding;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.css.PseudoClass;
import javafx.scene.Node;
import javafx.scene.control.Skin;
import javafx.scene.control.skin.ButtonSkin;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.util.Subscription;

/**
 * <p>A version of {@link SMPButton} used specifically for instrument buttons in the top panel.</p>
 * 
 * <p>This defines properties that record the state of the instrument (filtered, sustain on/off) and sets up
 * a custom skin.</p>
 */
public class SMPInstrumentButton extends SMPButton {
	
	/**
	 * Is this instrument active? (instruments are rendered inactive by filtering)
	 */
	private BooleanProperty active;
	public BooleanProperty active() {
		if (active == null) {
			active = new SimpleBooleanProperty(this, "active", true);
		}
		return active;
	}
	public boolean isActive() { return active().getValue(); }
	public void setActive(boolean b) { active().setValue(b); }
	
	/**
	 * Is sustain currently activated for this instrument?
	 */
	private BooleanProperty sustainOn;
	public BooleanProperty sustainOn() {
		if (sustainOn == null) {
			sustainOn = new SimpleBooleanProperty(this, "sustainOn", false);
		}
		return sustainOn;
	}
	public boolean isSustainOn() { return sustainOn().getValue(); }
	public void setSustainOn(boolean b) { sustainOn().setValue(b); }

    /**
     * The image for the regular (released) state
     */
    private ObjectProperty<Image> imageSustainOff;
    public ObjectProperty<Image> imageSustainOff() {
        if (imageSustainOff == null) {
            imageSustainOff = new SimpleObjectProperty<Image>(this, "imageSustainOff", null);
        }
        return imageSustainOff;
    }
    public Image getImageSustainOff() { return imageSustainOff().getValue(); }
    public void setImageSustainOff(Image imageSustainOff) { imageSustainOff().setValue(imageSustainOff); }

    /**
     * The image for the <i>sustain</i> (released) state
     */
    private ObjectProperty<Image> imageSustainOn;
    public ObjectProperty<Image> imageSustainOn() {
        if (imageSustainOn == null) {
            imageSustainOn = new SimpleObjectProperty<Image>(this, "imageSustainOn", null);
        }
        return imageSustainOn;
    }
    public Image getImageSustainOn() { return imagePressed().getValue(); }
    public void setImageSustainOn(Image imageSustainOn) { imagePressed().setValue(imageSustainOn); }
    
    /**
     * The image for the <i>filtered</i> state (overlayed)
     */
    private ObjectProperty<Image> imageFiltered;
    public ObjectProperty<Image> imageFiltered() {
        if (imageFiltered == null) {
        	imageFiltered = new SimpleObjectProperty<Image>(this, "imageFiltered", null);
        }
        return imageFiltered;
    }
    public Image getImageFiltered() { return imageFiltered().getValue(); }
    public void setImageFiltered(Image imageFiltered) { imageFiltered().setValue(imageFiltered); }
    
    /**
     * The list of all instrument buttons---used to manage all animations for the filter
     */
    private static Collection<SMPInstrumentButton> instrumentButtonGroup = new ArrayList<>(32);
	
	public SMPInstrumentButton() {
		super();
		initialize();
	}
	
	public SMPInstrumentButton(String text) {
		super(text);
		initialize();
	}
    
    public SMPInstrumentButton(String text, Image imageSustainOff, Image imageSustainOn) {
        super(text);
        setImageSustainOff(imageSustainOff);
        setImageSustainOn(imageSustainOn);
        initialize();
    }
    
    private void initialize() {
        getStyleClass().add(STYLE_CLASS_IMAGE_INSTRUMENT_BUTTON);
        
        active().addListener(obs -> pseudoClassStateChanged(FILTERED_PSEUDO_CLASS, !isActive()));
        sustainOn().addListener(obs -> pseudoClassStateChanged(SUSTAINED_PSEUDO_CLASS, isSustainOn()));
    }
    
    private static final PseudoClass SUSTAINED_PSEUDO_CLASS = PseudoClass.getPseudoClass("sustained");
    private static final PseudoClass FILTERED_PSEUDO_CLASS = PseudoClass.getPseudoClass("filtered");
    
    @Override
    protected Skin<?> createDefaultSkin() {
        return new ButtonSkin(this) {
            Subscription graphicSubscription;
            
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
            	return filter;
            }
            
            private Subscription filterSubscription(ImageView filter) {
            	filter.fitHeightProperty().bind(fitHeight());
            	filter.fitWidthProperty().bind(fitWidth());
            	filter.imageProperty().bind(imageFiltered());
            	filter.visibleProperty().bind(Bindings.and(active(), Bindings.notEqual(-1, StateMachine.filteredNotesProperty())));
            	
            	return () -> {
            		filter.fitHeightProperty().unbind();
            		filter.fitWidthProperty().unbind();
            		filter.imageProperty().unbind();
            		filter.visibleProperty().unbind();
            	};
            }
            
            private Subscription imageSustainSubscription() {
            	imageReleased().bind(imageSustainBinding());
            	return (() -> imageReleased().unbind());
            }
            
            private Binding<Image> imageSustainBinding() {
            	return Bindings.createObjectBinding(() -> {
            		return (isSustainOn()) ? getImageSustainOn() : getImageSustainOff();
            	}, sustainOn(), imageSustainOff(), imageSustainOn());
            }
            
            @Override
            public void install() {
            	ObjectProperty<Node> buttonImageView = new SimpleObjectProperty<>(null);
            	ImageView filterIv = buildFilter();
            	ObservableValue<Node> graphic = buildGraphic(buttonImageView, filterIv);
            	graphicProperty().bind(graphic);
            	
                graphicSubscription =
                		SMPButtonInterface.subscribeNodeProperty(SMPInstrumentButton.this, armedProperty(), buttonImageView)
                		.and(filterSubscription(filterIv))
                		.and(imageSustainSubscription())
                		.and(() -> graphicProperty().unbind());
                
                instrumentButtonGroup.add(SMPInstrumentButton.this);
            }
            
            @Override
            public void dispose() {
                graphicSubscription.unsubscribe();
                instrumentButtonGroup.remove(SMPInstrumentButton.this);
            }
        };
    }

}
