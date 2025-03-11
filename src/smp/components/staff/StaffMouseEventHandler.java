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
import smp.components.staff.sequences.Accidental;
import smp.components.staff.sequences.StaffNote;
import smp.components.staff.sequences.StaffNoteLine;
import smp.components.topPanel.ButtonLine;
import smp.stateMachine.Settings;
import smp.stateMachine.StateMachine;
import javafx.collections.ObservableList;
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
public class StaffMouseEventHandler implements EventHandler<MouseEvent> {

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
    
    /** This is the ImageLoader class. */
    private static ImageLoader il;

    /** This is the amount that we want to sharp / flat / etc. a note. */
    private Accidental acc = Accidental.NATURAL;
    
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
    public StaffMouseEventHandler(Staff s, ImageLoader i, ModifySongManager cm) {
    	
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

	@Override
	public void handle(MouseEvent event) {
	    if (StateMachine.isSelectionModeOn())
	        return;
    	
		boolean newNote = false;
    	{
    		int lineTmp = getLine(event.getX());
    		int positionTmp = getPosition(event.getY());
    		
    		//invalid
    		if(!validNote(lineTmp, positionTmp))
    			return;
    		
    		//new note
    		newNote = updateNote(lineTmp, positionTmp);
    	}
    	
        InstrumentIndex theInd = ButtonLine.getSelectedInstrument();
        // Drag-add notes, hold e to drag-remove notes
		if (event.isPrimaryButtonDown() && newNote) {
            int lineTmp = getLine(event.getX());
			leftMousePressed(theInd, lineTmp);
			event.consume();
			StateMachine.setSongModified(true);
		}
		// Drag-remove notes
		else if (event.isSecondaryButtonDown() && newNote) {
			rightMousePressed(theInd);
			event.consume();
			StateMachine.setSongModified(true);
		}
		else if (event.getEventType() == MouseEvent.MOUSE_PRESSED) {
			MouseButton b = event.getButton();
            int lineTmp = getLine(event.getX());
            if (b == MouseButton.PRIMARY)
                leftMousePressed(theInd, lineTmp);
            else if (b == MouseButton.SECONDARY)
                rightMousePressed(theInd);
            event.consume();
            StateMachine.setSongModified(true);
        } else if (event.getEventType() == MouseEvent.MOUSE_MOVED) {
            mouseEntered(theInd);
            event.consume();
        } else if (event.getEventType() == MouseEvent.MOUSE_EXITED) {
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
	 * Updates member variables to new note values.
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
			StackPane notes = theStaff.getDisplayManager().getNotes(line, position);
            StackPane accidentals = theStaff.getDisplayManager().getAccidentals(line, position);
			
			theImages = notes.getChildren();
			accList = accidentals.getChildren();
			
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
    private void leftMousePressed(InstrumentIndex theInd, int lineTmp) {
        if (StateMachine.getButtonsPressed().contains(KeyCode.E)) {
            removeNote();
        } else {        
            StaffNoteLine s = theStaff.getSequence().getLineSafe(
                StateMachine.getMeasureLineNum() + lineTmp);
            placeNote(theInd, s.getVolume());
        }
    }

    /**
     * Places a note where the mouse currently is.
     *
     * @param theInd
     *            The <code>InstrumentIndex</code> that we are going to use to
     *            place this note.
     */
    private void placeNote(InstrumentIndex theInd, int vel) {
        boolean mute = StateMachine.isMutePressed();
        boolean muteA = StateMachine.isMuteAPressed();
        
        if (vel <= 0 || vel >= 128)
            vel = Values.DEFAULT_VELOCITY;
        
        if (!mute && !muteA)
            playSound(theInd, position, acc, vel);

        if ((Settings.debug & 0b10000) != 0)
            System.out.println("Index: " + theInd + "\nPosition: "+ position + "\nAcc: " + acc + "\nVel: " + vel);
        
        StaffNote theStaffNote = new StaffNote(theInd, position, acc);
        theStaffNote.setMuteNote(muteA ? 2 : mute ? 1 : 0);
        
        theStaff.getDisplayManager().resetSilhouette();

        StaffNoteLine temp = theStaff.getSequence().getLineSafe(
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
        StaffVolumeEventHandler sveh = theStaff.getDisplayManager().getVolHandler(
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
        theStaff.getDisplayManager().resetSilhouette();

        StaffNoteLine temp = theStaff.getSequence().getLineSafe(
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
            StaffVolumeEventHandler sveh = theStaff.getDisplayManager()
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
        acc = computeAccidental();
        StaffNote sil = new StaffNote(theInd, position, acc);
        theStaff.getDisplayManager().updateSilhouette(line, sil);

        focus = true;
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

    	theStaff.getDisplayManager().resetSilhouette();
    	
    	focus = false;

    }

    /**
     * Updates how much we want to sharp / flat a note.
     */
    public static Accidental computeAccidental() {        
        Set<KeyCode> bp = StateMachine.getButtonsPressed();
        boolean ctrl = bp.contains(KeyCode.CONTROL);
        boolean shift = bp.contains(KeyCode.SHIFT);
        boolean alt = bp.contains(KeyCode.ALT) || bp.contains(KeyCode.ALT_GRAPH);

        if (alt && ctrl)
            return Accidental.DOUBLE_FLAT;
        else if (ctrl && shift)
            return Accidental.DOUBLE_SHARP;
        else if (shift)
            return Accidental.SHARP;
        else if (alt || ctrl)
            return Accidental.FLAT;
        else
            return Accidental.NATURAL;
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
    private static void playSound(InstrumentIndex theInd, int pos, Accidental acc, int vel) {
        SoundfontLoader.playSound(Values.staffNotes[pos].getKeyNum(), theInd,
                acc, vel);
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
