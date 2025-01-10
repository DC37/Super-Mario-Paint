package smp.presenters.staff.StaffInstrumentsPresenter;

import java.util.ArrayList;
import java.util.Set;

import smp.ImageLoader;
import smp.SoundfontLoader;
import smp.TestMain;
import smp.components.Values;
import smp.components.InstrumentIndex;
import smp.models.staff.StaffNote;
import smp.models.staff.StaffNoteLine;
import smp.models.stateMachine.Variables;
import smp.models.stateMachine.Variables.WindowLines;
import smp.models.stateMachine.Settings;
import smp.models.stateMachine.StateMachine;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

/**
 * THIS IS A MODIFIED VERSION OF REHDBLOB's STAFF EVENT HANDLER. IT IS MADE IN
 * RESPONSE TO THE MULTITHREADING ISSUE WITH ARRAYLISTS AND STACKPANES. THERE
 * WILL ONLY BE ONE OF THIS EVENT HANDLER AND IT WILL BE ADDED TO THE ENTIRE
 * SCENE. WITH THIS HANDLER, ALL STACKPANES ARE DISABLED AND EVENTS WILL BE
 * REGISTERED TO THE STACKPANES BY CALCULATING WHICH PANE TO FETCH VIA MOUSE
 * POSITION.
 * 
 * A Staff event handler. The StaffImages implementation was getting bulky and
 * there are still many many features to be implemented here. This handler
 * primarily handles mouse events.
 *
 * @author RehdBlob
 * @author j574y923
 * @since 2013.07.27 (2017.06.22)
 */
public class StaffInstrumentEventHandler implements EventHandler<Event> {

	// TODO: auto-add these model comments
	// ====Models====
	private ObjectProperty<InstrumentIndex> selectedInstrument;
	private IntegerProperty selectedAccidental;
	private BooleanProperty accSilhouetteVisible;
    /** The line number of this note, on the screen. */
	private IntegerProperty selectedLine;
    /** The position of this note. */
	private IntegerProperty selectedPosition;
	private BooleanProperty songModified;
	private BooleanProperty mutePressed;
	private BooleanProperty muteAPressed;
	private DoubleProperty measureLineNum;
	private ObservableSet<KeyCode> buttonsPressed;

    /** Whether the mouse is in the frame or not. */
    private static boolean focus = false;
    
    private ArrayList<ArrayList<StackPane>> matrix;

	private WindowLines windowLines;
	
    /**
     * This is the list of image notes that we have. These should all be
     * ImageView-type objects.
     */
    private static ObservableList<Node> theImages;

    /** The StackPane that will display sharps, flats, etc. */
//ACC    private static ObservableList<Node> accList;

    /**
     * This is the <code>ImageView</code> object responsible for displaying the
     * silhouette of the note that we are about to place on the staff.
     */
    private static ImageView silhouette = new ImageView();

    /**
     * This is the <code>ImageView</code> object responsible for displaying the
     * silhouette of the sharp / flat of the note that we are about to place on
     * the staff.
     */
//ACC    private static ImageView accSilhouette;

    /** The topmost image of the instrument. */
    private StaffNote theStaffNote;
    
    /** This is the ImageLoader class. */
    private static ImageLoader il = (ImageLoader) TestMain.imgLoader;

    /** This is the amount that we want to sharp / flat / etc. a note. */
//ACC    private static int acc = 0;
    
//FIXME:    private ModifySongManager commandManager;
    
    /**
     * Constructor for this StaffEventHandler. This creates a handler that takes
     * a StackPane and a position on the staff.
     *
     * @param s
     *            The pointer to the Staff object that this event handler is
     *            linked to.
     * @param ct 
     * @param i
     * 			  The program's image loader.
     * @param cm
     * 			  The undo/redo manager.
     */
	public StaffInstrumentEventHandler(ArrayList<ArrayList<StackPane>> matrix, WindowLines windowLines) {
		this.matrix = matrix;
		this.windowLines = windowLines;
		
		
//ACC		accSilhouette = new ImageView();
        
//        commandManager = cm;
        
		this.selectedInstrument = Variables.selectedInstrument;
		this.selectedAccidental = Variables.selectedAccidental;
		this.accSilhouetteVisible = Variables.accSilhouetteVisible;
		this.selectedLine = Variables.selectedLine;
		this.selectedPosition = Variables.selectedPosition;
		
		this.songModified = StateMachine.getSongModified();
		this.mutePressed = StateMachine.getMutePressed();
		this.muteAPressed = StateMachine.getMuteAPressed();
		this.measureLineNum = StateMachine.getMeasureLineNum();
		this.buttonsPressed = StateMachine.getButtonsPressed();
		setupKeyListener();
    }

	private void setupKeyListener() {
		buttonsPressed.addListener(new SetChangeListener<KeyCode>() {

			@Override
			public void onChanged(javafx.collections.SetChangeListener.Change<? extends KeyCode> change) {
				Set<KeyCode> bp = buttonsPressed;
		        boolean ctrl = bp.contains(KeyCode.CONTROL);
		        boolean shift = bp.contains(KeyCode.SHIFT);
		        boolean alt = bp.contains(KeyCode.ALT) || bp.contains(KeyCode.ALT_GRAPH);

		        if (alt && ctrl)
		            selectedAccidental.set(-2);
		        else if (ctrl && shift)
		            selectedAccidental.set(2);
		        else if (shift)
		            selectedAccidental.set(1);
		        else if (alt || ctrl)
		            selectedAccidental.set(-1);
		        else
		            selectedAccidental.set(0);
			}
		});
	}

	/**
	 * Disables all the stack panes.
	 */
	private void disableAllStackPanes() {
		for (int index = 0; index < Values.NOTELINES_IN_THE_WINDOW; index++) {
			for (int i = 0; i < Values.NOTES_IN_A_LINE; i++) {
				StackPane note = matrix.get(index).get(Values.NOTES_IN_A_LINE - i - 1);//TODO: encapsulate getNote function in a standalone matrix class
				note.setDisable(true);
			}
		}
	}

	@Override
    public void handle(Event event) {
    	
		boolean newNote = false;
    	if(event instanceof MouseEvent){
    		int lineTmp = getLine(((MouseEvent)event).getX());
    		int positionTmp = getPosition(((MouseEvent)event).getY());
    		
    		//invalid
    		if(!validNote(lineTmp, positionTmp))
    			return;
    		
    		//new note
    		newNote = updateNote(lineTmp, positionTmp);
    	}
    	
        InstrumentIndex theInd = this.selectedInstrument.get();
        // Drag-add notes, hold e to drag-remove notes
		if (event instanceof MouseEvent && ((MouseEvent) event).isPrimaryButtonDown() && newNote) {
			leftMousePressed(theInd);
			event.consume();
			songModified.set(true);
		}
		// Drag-remove notes
		else if (event instanceof MouseEvent && ((MouseEvent) event).isSecondaryButtonDown() && newNote) {
			rightMousePressed(theInd);
			event.consume();
			songModified.set(true);
		}
		else if (event.getEventType() == MouseEvent.MOUSE_PRESSED) {
			MouseButton b = ((MouseEvent) event).getButton();
            if (b == MouseButton.PRIMARY)
                leftMousePressed(theInd);
            else if (b == MouseButton.SECONDARY)
                rightMousePressed(theInd);
            event.consume();
            songModified.set(true);
        } else if (event.getEventType() == MouseEvent.MOUSE_MOVED) {
            focus = true;
            mouseEntered(theInd);
            event.consume();
        } else if (event.getEventType() == MouseEvent.MOUSE_EXITED) {
            focus = false;
            mouseExited(theInd);
            event.consume();
        } 
        else if (event.getEventType() == MouseEvent.MOUSE_RELEASED) {
        	mouseReleased();
        }
    }

	private void mouseReleased() {
//		commandManager.record();
	}

	/**
	 * Takes in a line and position and check if they are valid. If the note is
	 * not valid then it will call mouseExited().
	 * 
	 * @param lineTmp
	 * @param positionTmp
	 * @return if the given line and position are at a valid note.
	 */
	private boolean validNote(int lineTmp, int positionTmp) {
		// MOUSE_EXITED
		if (lineTmp < 0 || positionTmp < 0) {
	        InstrumentIndex theInd = this.selectedInstrument.get();
			mouseExited(theInd);
			return false;
		}
		return true;
	}
	
	/**
	 * Updates member variables to new note values. Detects if stackpanes are
	 * disabled and if they aren't this will disable them.
	 * 
	 * @param lineTmp
	 *            the note's line
	 * @param positionTmp
	 *            the note's position
	 * @return success if we updated note to new line or position
	 */
	private boolean updateNote(int lineTmp, int positionTmp) {
		if (selectedLine.get() != lineTmp || selectedPosition.get() != positionTmp) {

			selectedLine.set(lineTmp);
			selectedPosition.set(positionTmp);
			StackPane note = matrix.get(lineTmp).get(Values.NOTES_IN_A_LINE - positionTmp - 1);//TODO: encapsulate getNote function in a standalone matrix class
			
			if(!note.isDisabled())
				disableAllStackPanes();
			
			theImages = note.getChildren();
//ACC			accList = note[1].getChildren();
			
			return true;
		}
		return false;
	}
	
    /**
     * The method that is called when the left mouse button is pressed. This is
     * generally the signal to add an instrument to that line.
     *
     * @param theInd
     *            The InstrumentIndex corresponding to what instrument is
     *            currently selected.
     */
    private void leftMousePressed(InstrumentIndex theInd) {
        if (StateMachine.getButtonsPressed().contains(KeyCode.E)) {
            removeNote();
        } else {
            placeNote(theInd);
        }
    }

    /**
     * Places a note where the mouse currently is.
     *
     * @param theInd
     *            The <code>InstrumentIndex</code> that we are going to use to
     *            place this note.
     */
    private void placeNote(InstrumentIndex theInd) {
        boolean mute = this.mutePressed.get();
        boolean muteA = this.muteAPressed.get();

        if (!mute && !muteA)
            playSound(theInd, selectedPosition.get(), selectedAccidental.get(), Values.DEFAULT_VELOCITY);

        theStaffNote = new StaffNote(theInd, selectedPosition.get(), selectedAccidental.get());
        theStaffNote.setMuteNote(muteA ? 2 : mute ? 1 : 0);

        theImages.remove(silhouette);
        this.accSilhouetteVisible.set(false);

        StaffNoteLine temp = this.windowLines.get(selectedLine.get()).get();

        if (temp.isEmpty()) {
            temp.setVolumePercent(((double) Values.DEFAULT_VELOCITY)
                    / Values.MAX_VELOCITY);
//            commandManager.execute(new AddVolumeCommand(temp, Values.DEFAULT_VELOCITY));
        }

        if (!temp.contains(theStaffNote)) {
            temp.add(theStaffNote);
//            commandManager.execute(new AddNoteCommand(temp, theStaffNote));
        }
    }

    /**
     * The method that is called when the right mouse button is pressed. This is
     * generally the signal to remove the instrument from that line.
     *
     * @param theInd
     *            The InstrumentIndex corresponding to what instrument is
     *            currently selected. (currently not actually used, but can be
     *            extended later to selectively remove instruments.
     */
    private void rightMousePressed(InstrumentIndex theInd) {

        removeNote();

    }

    /**
     * This removes a note.
     */
    private void removeNote() {
        theImages.remove(silhouette);
        this.accSilhouetteVisible.set(false);


        StaffNoteLine temp = this.windowLines.get(selectedLine.get()).get();

        if (!temp.isEmpty()) {
            ObservableList<StaffNote> nt = temp.getNotes();
            for (int i = nt.size() - 1; i >= 0; i--) {
                StaffNote s = nt.get(i);
                if (s.getPosition() == selectedPosition.get()) {
                    StaffNote removedNote = nt.remove(i);
//                    commandManager.execute(new RemoveNoteCommand(temp, removedNote));
                    break;
                }
            }
        }

        if (temp.isEmpty()) {
//            commandManager.execute(new RemoveVolumeCommand(temp, temp.getVolume()));
        }
    }

    /**
     * The method that is called when the mouse enters the object.
     *
     * @param theInd
     *            The InstrumentIndex corresponding to what instrument is
     *            currently selected.
     */
    private void mouseEntered(InstrumentIndex theInd) {
        silhouette.setImage(il.getSpriteFX(theInd.imageIndex().silhouette()));
        if (!theImages.contains(silhouette))
            theImages.add(silhouette);
        silhouette.setVisible(true);
        this.accSilhouetteVisible.set(true);
    }

    /**
     * The method that is called when the mouse exits the object.
     *
     * @param children
     *            List of Nodes that we have here, hopefully full of
     *            ImageView-type objects.
     * @param theInd
     *            The InstrumentIndex corresponding to what instrument is
     *            currently selected.
     */
    private void mouseExited(InstrumentIndex theInd) {

    	if(silhouette.getImage() != null)
    		theImages.remove(silhouette);
        this.accSilhouetteVisible.set(false);

    }

    /**
     * Updates how much we want to sharp / flat a note.
     */
    public void updateAccidental() {
        if (!focus)
            return;
        Set<KeyCode> bp = StateMachine.getButtonsPressed();//TODO: add back the keyboardlistener
        boolean ctrl = bp.contains(KeyCode.CONTROL);
        boolean shift = bp.contains(KeyCode.SHIFT);
        boolean alt = bp.contains(KeyCode.ALT) || bp.contains(KeyCode.ALT_GRAPH);

        if (alt && ctrl)
            selectedAccidental.set(-2);
        else if (ctrl && shift)
            selectedAccidental.set(2);
        else if (shift)
            selectedAccidental.set(1);
        else if (alt || ctrl)
            selectedAccidental.set(-1);
        else
            selectedAccidental.set(0);
        if ((Settings.debug & 0b01) != 0) {
            System.out.println(this);
        }

    }

    /**
     * Called whenever we request a redraw of the staff.
     */
    @Deprecated
    public void redraw() {
        if (!focus)
            return;
        InstrumentIndex ind = this.selectedInstrument.get();
        mouseExited(ind);
        mouseEntered(ind);
    }

    /**
     * Plays a sound given an index and a position.
     *
     * @param theInd
     *            The index at which this instrument is located at.
     * @param pos
     *            The position at which this note is located at.
     * @param acc
     *            The sharp / flat that we want to play this note at.
     */
    private static void playSound(InstrumentIndex theInd, int pos, int acc) {
        SoundfontLoader.playSound(Values.staffNotes[pos].getKeyNum(), theInd,
                acc);
    }
    

    /**
     * Plays a sound given an index and a position.
     *
     * @param theInd
     *            The index at which this instrument is located at.
     * @param pos
     *            The position at which this note is located at.
     * @param acc
     *            The sharp / flat that we want to play this note at.
     * @param vel
     *            The velocity to play the note at
     */
    private static void playSound(InstrumentIndex theInd, int pos, int acc, int vel) {
        SoundfontLoader.playSound(Values.staffNotes[pos].getKeyNum(), theInd,
                acc, vel);
    }

    /**
     * Sets the amount that we want to sharp / flat a note.
     *
     * @param accidental
     *            Any integer between -2 and 2.
     */
    @Deprecated
    public void setAcc(int accidental) {
//        acc = accidental;
    }

    /**
     * @return The amount that a note is to be offset from its usual position.
     */
    @Deprecated
    public int getAcc() {
        return selectedAccidental.get();
    }

    /**
     * @return The line that this handler is located on.
     */
    @Deprecated
    public int getLine() {
        return selectedLine.get();
    }

    /**
     * @return Whether the mouse is currently in the frame.
     */
    public boolean hasMouse() {
        return focus;
    }

    @Override
    public String toString() {
        String out = "Line: " + (this.measureLineNum.get() + selectedLine.get())
                + "\nPosition: " + selectedPosition.get() + "\nAccidental: " + selectedAccidental.get();
        return out;
    }
    
    /**
     * 
     * @param x mouse pos
     * @return line in the current window based on x coord
     */
    private static int getLine(double x){

    	if(x < 135 || x > 775)
    		return -1;
    	return (((int)x - 135) / 64);
    }
    
    /**
     * 
     * @param y mouse pos
     * @return note position based on y coord
     */
    private static int getPosition(double y){
    	if(y < 66 || y >= 354)
    		return -1;
    	return Values.NOTES_IN_A_LINE - (((int)y - 66) / 16) - 1;
    }
    

}
