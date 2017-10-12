package smp.clipboard;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import smp.ImageLoader;
import smp.components.InstrumentIndex;
import smp.components.Values;
import smp.components.staff.Staff;
import smp.components.staff.sequences.StaffNote;
import smp.components.staff.sequences.StaffNoteLine;
import smp.fx.SMPFXController;

/**
 * This class can be used to keep track of the bounds that we will copy, delete,
 * etc. notes from.
 *
 */
class SelectionBounds {
	private int lineBegin;
	private int lineEnd;
	private int positionBegin;
	private int positionEnd;

	public SelectionBounds(int lb, int le, int pb, int pe) {
		this.lineBegin = lb;
		this.lineEnd = le;
		this.positionBegin = pb;
		this.positionEnd = pe;
	}

	public int getLineBegin() {
		return lineBegin;
	}

	public int getLineEnd() {
		return lineEnd;
	}

	public int getPositionBegin() {
		return positionBegin;
	}

	public int getPositionEnd() {
		return positionEnd;
	}
}

public class DataClipboard {

	private Staff theStaff;
	private SMPFXController controller;
	private ImageLoader il;
	private StackPane rubberBandLayer;
	private RubberBandEventHandler rbeh;

	private DataClipboardFunctions functions;
	
	/* A list that keeps track of all the selections' bounds made by the user */
	private ArrayList<SelectionBounds> selection;
	
	/* The list that will keep track of copied notes */
	private ArrayList<StaffNoteLine> data;
	
	/* Corresponds with the first selection line */
	private int dataLineBegin = 0;
	/* Corresponds with the last selection line */
	private int dataLineEnd = -1;

	public DataClipboard(Staff st, SMPFXController ct, ImageLoader im) {

		il = im;
		theStaff = st;
		controller = ct;

		selection = new ArrayList<>();
		data = new ArrayList<>(Values.DEFAULT_LINES_PER_SONG);
		for (int i = 0; i < Values.DEFAULT_LINES_PER_SONG; i++)
			data.add(new StaffNoteLine());
		
		//temp? merge the two classes together?
		functions = new DataClipboardFunctions(this, theStaff, im);
	}

	/**
	 * Add a new SelectionBounds to the selection ArrayList. 
	 * 
	 * This will be used to keep track of all the selections made by the user.
	 * 
	 * @param lb lineBegin
	 * @param le lineEnd
	 * @param pb positionBegin
	 * @param pe positionEnd
	 */
	public void addSelection(int lb, int le, int pb, int pe) {
		selection.add(new SelectionBounds(lb, le, pb, pe));
	}

	/**
	 * Clear the selection ArrayList.
	 */
	public void clearSelection() {
		selection.clear();
	}
	
	public ArrayList<SelectionBounds> getSelection() {
		return selection;
	}
	
	/**
	 * Copy information from note. Add new note into data.
	 * 
	 * @param line
	 *            where the note occurs
	 * @param note
	 *            that will have its info copied
	 */
	public void copyNote(int line, StaffNote note) {
		StaffNote newNote = new StaffNote(note.getInstrument(), note.getPosition(), note.getAccidental());
		data.get(line).add(newNote);
	}
	
	public void clearData() {
		for (int i = dataLineBegin; i <= dataLineEnd; i++) {
			StaffNoteLine line = data.get(i);
			line.getNotes().clear();
		}
		dataLineBegin = 0;
		dataLineEnd = -1;
	}
	
	public ArrayList<StaffNoteLine> getData() {
		return data;
	}
	
	public int getDataLineBegin() {
		return dataLineBegin;
	}
	
	public int getDataLineEnd() {
		return dataLineEnd;
	}
	
	public void setDataLineBegin(int line) {
		dataLineBegin = line;
	}
	
	public void setDataLineEnd(int line) {
		dataLineEnd = line;
	}

	/**
	 * convenience method, update dataLineBegin only if line is less. Should
	 * call this because dataLineBegin will be used to define bounds of data.
	 * 
	 * @param line
	 *            to update dataLineBegin to
	 */
	public void updateDataLineBegin(int line) {
		if (line < dataLineBegin)
			dataLineBegin = line;
	}

	/**
	 * convenience method, update dataLineEnd only if line is more. Should call
	 * this because dataLineEnd will be used to define bounds of data.
	 * 
	 * @param line
	 *            to update dataLineEnd to
	 */
	public void updateDataLineEnd(int line) {
		if(line > dataLineEnd)
			dataLineEnd = line;
	}
	
	//temp? merge the two classes together?
	public DataClipboardFunctions getFunctions(){
		return functions;
	}
}
