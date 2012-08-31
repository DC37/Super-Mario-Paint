package smp.components.staff;


import java.util.ArrayList;

import javafx.scene.image.ImageView;


/**
 * The staff on which notes go. The way that the Staff keeps track of
 * notes is that there is a matrix
 * @author RehdBlob
 * @since 2012.08.13
 */
public class Staff {
	
	/**
	 * The Sequencer object that will be used to play sounds.
	 */
	private SMPSequencer seq;
	
	/**
	 * A matrix of notes, which will be displayed on the screen
	 * at any time.
	 */
	private ArrayList<ArrayList<ImageView>> noteImageMatrix;
	
	/**
	 * Holds whatever notes that the user decides to put on the staff.
	 */
	private ArrayList<ArrayList<StaffNote>> noteMatrix;
	
	/**
	 * Creates a new Staff object and gives it the x and y coordinates
	 * as specified by the user.
	 * @param x The x-location that the staff is to be placed at.
	 * @param y The y-location that the staff is to be placed at.
	 */
	public Staff() {
		seq = new SMPSequencer();
		noteImageMatrix = new ArrayList<ArrayList<ImageView>>();
		initializeNoteMatrix();
	}
	
	
	public void addNote(Note n) {
		
	}
	
	/**
	 * Puts a note on the Staff.
	 */
	public void placeNote() {
		
	}
	
	/**
	 * Initializes the note matrix with ImageViews as notes.
	 */
	private void initializeNoteMatrix() {
		
	}
	
	/**
	 * Moves the staff and notes left by 1.
	 */
	public void moveLeft() {
		
	}
	
	/**
	 * Moves the staff and notes right by 1.
	 */
	public void moveRight() {
		
	}
	
	/**
	 * Begins animation of the Staff.
	 */
	public void startAnimation() {
		
	}
	
	/**
	 * Loads a Super Mario Paint song.
	 */
	public void loadSong() {
		
	}
	
	/**
	 * Saves a Super Mario Paint song.
	 */
	public void saveSong() {
		
	}
	
	/**
	 * Imports a Mario Paint Composer song.
	 * @throws MPCException If for some reason the load fails.
	 */
	public void importMPCSong() throws MPCException {
		
	}
	
	
}