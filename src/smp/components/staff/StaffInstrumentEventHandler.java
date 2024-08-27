package smp.components.staff;

import java.util.ArrayList;
import java.util.Set;

import smp.ImageIndex;
import smp.ImageLoader;
import smp.SoundfontLoader;
import smp.commandmanager.ModifySongManager;
import smp.commandmanager.commands.AddNoteCommand;
import smp.commandmanager.commands.AddVolumeCommand;
import smp.commandmanager.commands.RemoveNoteCommand;
import smp.commandmanager.commands.RemoveVolumeCommand;
import smp.components.Values;
import smp.components.InstrumentIndex;
import smp.components.staff.sequences.StaffAccidental;
import smp.components.staff.sequences.StaffNote;
import smp.components.staff.sequences.StaffNoteLine;
import smp.components.topPanel.ButtonLine;
import smp.stateMachine.Settings;
import smp.stateMachine.StateMachine;
import javafx.collections.ObservableList;
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

    /** The line number of this note, on the screen. */
    private int line;

    /** The position of this note. */
    private int position;

    /** Whether the mouse is in the frame or not. */
    private static boolean focus = false;

    /**
     * This is the list of image notes that we have. These should all be
     * ImageView-type objects.
     */
    private static ObservableList<Node> theImages;

    /** The StackPane that will display sharps, flats, etc. */
    private static ObservableList<Node> accList;

    /**
     * This is the <code>ImageView</code> object responsible for displaying the
     * silhouette of the note that we are about to place on the staff.
     */
    private static ImageView silhouette = new ImageView();

    /** The pointer to the staff object that this handler is linked to. */
    private Staff theStaff;

    /**
     * This is the <code>ImageView</code> object responsible for displaying the
     * silhouette of the sharp / flat of the note that we are about to place on
     * the staff.
     */
    private static ImageView accSilhouette;

    /** The topmost image of the instrument. */
    private StaffNote theStaffNote;

    /**
     * This is the image that holds the different types of sharps/flats etc.
     */
    private StaffAccidental accidental;
    
    /** This is the ImageLoader class. */
    private static ImageLoader il;

    /** This is the amount that we want to sharp / flat / etc. a note. */
    private static int acc = 0;
    
    private ModifySongManager commandManager;
    
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
    public StaffInstrumentEventHandler(Staff s, ImageLoader i, ModifySongManager cm) {
    	
        il = i;
        theStaff = s;
        accSilhouette = new ImageView();
        
        accSilhouette.setFitWidth(32);
        accSilhouette.setFitHeight(32);
        silhouette.setFitWidth(32);
        silhouette.setFitHeight(36);
        
        commandManager = cm;
        
        if ((Settings.debug & 0b10) == 0b10) {
//            System.out.println("Line: " + l);
//            System.out.println("Position: " + pos);
        }
    }

	/**
	 * Disables all the stack panes. 
	 */
	private void disableAllStackPanes() {
		for (int index = 0; index < Values.NOTELINES_IN_THE_WINDOW; index++) {
			for (int i = 0; i < Values.NOTES_IN_A_LINE; i++) {
				StackPane[] noteAndAcc = theStaff.getNoteMatrix().getNote(index, i);
				noteAndAcc[0].setDisable(true);
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
    	
        InstrumentIndex theInd = ButtonLine.getSelectedInstrument();
        // Drag-add notes, hold e to drag-remove notes
		if (event instanceof MouseEvent && ((MouseEvent) event).isPrimaryButtonDown() && newNote) {
			leftMousePressed(theInd);
			event.consume();
			StateMachine.setSongModified(true);
		}
		// Drag-remove notes
		else if (event instanceof MouseEvent && ((MouseEvent) event).isSecondaryButtonDown() && newNote) {
			rightMousePressed(theInd);
			event.consume();
			StateMachine.setSongModified(true);
		}
		else if (event.getEventType() == MouseEvent.MOUSE_PRESSED) {
			MouseButton b = ((MouseEvent) event).getButton();
            if (b == MouseButton.PRIMARY)
                leftMousePressed(theInd);
            else if (b == MouseButton.SECONDARY)
                rightMousePressed(theInd);
            event.consume();
            StateMachine.setSongModified(true);
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
		commandManager.record();
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
			InstrumentIndex theInd = ButtonLine.getSelectedInstrument();
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
		if(line != lineTmp || position != positionTmp){
			
			line = lineTmp;
			position = positionTmp;
			StackPane[] noteAndAcc = theStaff.getNoteMatrix().getNote(line, position);
			
			if(!noteAndAcc[0].isDisabled())
				disableAllStackPanes();
			
			theImages = noteAndAcc[0].getChildren();
			accList = noteAndAcc[1].getChildren();
			
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
        boolean mute = StateMachine.isMutePressed();
        boolean muteA = StateMachine.isMuteAPressed();

        if (!mute && !muteA)
            playSound(theInd, position, acc);

        theStaffNote = new StaffNote(theInd, position, acc);

        theStaffNote.setMuteNote(muteA ? 2 : mute ? 1 : 0);

        if (!mute && !muteA) {
            theStaffNote.setImage(il.getSpriteFX(theInd.imageIndex()));
        } else if (mute) {
            theStaffNote.setImage(il.getSpriteFX(theInd.imageIndex().alt()));
        } else if (muteA) {
            theStaffNote.setImage(il.getSpriteFX(theInd.imageIndex()
                    .silhouette()));
        }

        accidental = new StaffAccidental(theStaffNote);
        accidental.setImage(il.getSpriteFX(Staff.switchAcc(acc)));
        
        accidental.setFitWidth(32);
        accidental.setFitHeight(32);

        theImages.remove(silhouette);
        accList.remove(accSilhouette);

        if (!theImages.contains(theStaffNote))
            theImages.add(theStaffNote);

        if (!accList.contains(accidental))
            accList.add(accidental);

        StaffNoteLine temp = theStaff.getSequence().getLine(
                line + StateMachine.getMeasureLineNum());

        if (temp.isEmpty()) {
            temp.setVolumePercent(((double) Values.DEFAULT_VELOCITY)
                    / Values.MAX_VELOCITY);
            commandManager.execute(new AddVolumeCommand(temp, Values.DEFAULT_VELOCITY));
        }

        if (!temp.contains(theStaffNote)) {
            temp.add(theStaffNote);
            commandManager.execute(new AddNoteCommand(temp, theStaffNote));
        }
        StaffVolumeEventHandler sveh = theStaff.getNoteMatrix().getVolHandler(
                line);
        sveh.updateVolume();
        theStaff.redraw();
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
        accList.remove(accSilhouette);

        if (!theImages.isEmpty())
            theImages.remove(theImages.size() - 1);
        if (!accList.isEmpty())
            accList.remove(0);

        StaffNoteLine temp = theStaff.getSequence().getLine(
                line + StateMachine.getMeasureLineNum());

        if (!temp.isEmpty()) {
            ArrayList<StaffNote> nt = temp.getNotes();
            for (int i = nt.size() - 1; i >= 0; i--) {
                StaffNote s = nt.get(i);
                if (s.getPosition() == position) {
                    StaffNote removedNote = nt.remove(i);
                    commandManager.execute(new RemoveNoteCommand(temp, removedNote));
                    break;
                }
            }
        }

        if (temp.isEmpty()) {
            StaffVolumeEventHandler sveh = theStaff.getNoteMatrix()
                    .getVolHandler(line);
            sveh.setVolumeVisible(false);
            commandManager.execute(new RemoveVolumeCommand(temp, temp.getVolume()));
        }
        theStaff.redraw();
    }

    /**
     * The method that is called when the mouse enters the object.
     *
     * @param theInd
     *            The InstrumentIndex corresponding to what instrument is
     *            currently selected.
     */
    private void mouseEntered(InstrumentIndex theInd) {
//        StateMachine.setFocusPane(this);
//        theStaff.getNoteMatrix().setFocusPane(this);
        updateAccidental();
        silhouette.setImage(il.getSpriteFX(theInd.imageIndex().silhouette()));
        if (!theImages.contains(silhouette))
            theImages.add(silhouette);
        accSilhouette.setImage(il
                .getSpriteFX(Staff.switchAcc(acc).silhouette()));
        if (!accList.contains(accSilhouette))
            accList.add(accSilhouette);
        silhouette.setVisible(true);
        accSilhouette.setVisible(true);
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
    	if(accSilhouette.getImage() != null)
    		accList.remove(accSilhouette);

    }

    /**
     * Updates how much we want to sharp / flat a note.
     */
    public static void updateAccidental() {
        if (!focus)
            return;
        Set<KeyCode> bp = StateMachine.getButtonsPressed();
        boolean ctrl = bp.contains(KeyCode.CONTROL);
        boolean shift = bp.contains(KeyCode.SHIFT);
        boolean alt = bp.contains(KeyCode.ALT) || bp.contains(KeyCode.ALT_GRAPH);

        if (alt && ctrl)
            acc = -2;
        else if (ctrl && shift)
            acc = 2;
        else if (shift)
            acc = 1;
        else if (alt || ctrl)
            acc = -1;
        else
            acc = 0;

        switch (acc) {
        case 2:
            accSilhouette.setImage(il.getSpriteFX(ImageIndex.DOUBLESHARP_SIL));
            break;
        case 1:
            accSilhouette.setImage(il.getSpriteFX(ImageIndex.SHARP_SIL));
            break;
        case -1:
            accSilhouette.setImage(il.getSpriteFX(ImageIndex.FLAT_SIL));
            break;
        case -2:
            accSilhouette.setImage(il.getSpriteFX(ImageIndex.DOUBLEFLAT_SIL));
            break;
        default:
            accSilhouette.setVisible(false);
            break;
        }

        if (acc != 0)
            accSilhouette.setVisible(true);

        if (acc != 0 && !accList.contains(accSilhouette))
            accList.add(accSilhouette);
        if (acc != 0 && !theImages.contains(silhouette)) {
            theImages.add(silhouette);
            silhouette.setImage(il.getSpriteFX(ButtonLine
                    .getSelectedInstrument().imageIndex().silhouette()));
            silhouette.setVisible(true);
        }
        //Cannot use this in a static context... will fix this later
//        if ((Settings.debug & 0b01) == 0b01) {
//            System.out.println(this);
//        }

    }

    /**
     * Called whenever we request a redraw of the staff.
     */
    public void redraw() {
        if (!focus)
            return;
        InstrumentIndex ind = ButtonLine.getSelectedInstrument();
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
     * Sets the amount that we want to sharp / flat a note.
     *
     * @param accidental
     *            Any integer between -2 and 2.
     */
    public void setAcc(int accidental) {
        acc = accidental;
    }

    /**
     * @return The amount that a note is to be offset from its usual position.
     */
    public int getAcc() {
        return acc;
    }

    /**
     * @return The line that this handler is located on.
     */
    public int getLine() {
        return line;
    }

    /**
     * @return Whether the mouse is currently in the frame.
     */
    public boolean hasMouse() {
        return focus;
    }

    @Override
    public String toString() {
        String out = "Line: " + (StateMachine.getMeasureLineNum() + line)
                + "\nPosition: " + position + "\nAccidental: " + acc;
        return out;
    }
    
    /**
     * 
     * @param x mouse pos
     * @return line in the current window based on x coord
     */
    private static int getLine(double x){
    	if(x < 165 || x >= 997)
    		return -1;
    	return (((int)x - 165) / 64);
    }
    
    /**
     * 
     * @param y mouse pos
     * @return note position based on y coord
     */
    private static int getPosition(double y){
    	if(y < 66 || y >= 526)
    		return -1;
    	return Values.NOTES_IN_A_LINE - (((int)y - 66) / 16) - 1;
    }
    

}
