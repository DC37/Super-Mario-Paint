package smp.clipboard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import smp.ImageLoader;
import smp.components.Values;
import smp.components.staff.NoteMatrix;
import smp.components.staff.Staff;
import smp.components.staff.StaffVolumeEventHandler;
import smp.components.staff.sequences.StaffNote;
import smp.components.staff.sequences.StaffNoteLine;
import smp.stateMachine.StateMachine;

/**
 * Convenience functions for clipboard. These include copy, cut, delete, insert,
 * move, paste.
 *
 * @author J
 */
public class DataClipboardFunctions {
	
	private Staff theStaff;
	private DataClipboard theDataClipboard;
	private ImageLoader il;
	
	public DataClipboardFunctions(DataClipboard dc, Staff st, ImageLoader i) {
		theDataClipboard = dc;
		theStaff = st;
		il = i;
	}

	/**
	 * Take the bounds from DataClipboard's selection ArrayList and use that to
	 * locate all notes to that need to be copied. Copy those notes and set the
	 * data in DataClipboard.
	 */
	public void copy() {
		// TODO: use instrFiltered in DataClipboard
		theDataClipboard.clearData();
		ArrayList<SelectionBounds> selection = theDataClipboard.getSelection();

		for (SelectionBounds bounds : selection) {
			theDataClipboard.updateDataLineBegin(bounds.getLineBegin());
			
			for (int line = bounds.getLineBegin(); line <= bounds.getLineEnd(); line++) {
				StaffNoteLine lineSrc = theStaff.getSequence().getLine(line);

				ArrayList<StaffNote> ntList = lineSrc.getNotes();
				for (StaffNote note : ntList) {
					if (bounds.getPositionBegin() <= note.getPosition()
							&& note.getPosition() <= bounds.getPositionEnd())
						theDataClipboard.copyNote(line, note);
				}
			}
		}

	}

	//
//    /**
//     * Copy and delete.
//     */
//    @Deprecated
//    public static List<MeasureLine> cut(Song song, int lineBegin, Note.Position positionBegin, int lineEnd, Note.Position positionEnd) {
//        copy(song, lineBegin, positionBegin, lineEnd, positionEnd);
//        return delete(song, lineBegin, positionBegin, lineEnd, positionEnd);
//    }
//
//    /**
//     * Use the bounds for notes to delete in the song. 
//     */
//    @Deprecated
//    public static List<MeasureLine> delete(Song song, int lineBegin, Note.Position positionBegin, int lineEnd, Note.Position positionEnd) {
//        //TODO: use instrFiltered in DataClipboard
//        //TODO: ... return deleted notes
//        int rowBegin = lineBegin / Constants.LINES_IN_A_ROW;
//        int rowEnd = lineEnd / Constants.LINES_IN_A_ROW;
//        int rowLineBegin = lineBegin % Constants.LINES_IN_A_ROW;
//        int rowLineEnd = lineEnd % Constants.LINES_IN_A_ROW;
//
//        List<MeasureLine> content = new ArrayList<>();
//        for(int y = rowBegin; y <= rowEnd; y ++) {
//            for (int x = rowLineBegin; x <= rowLineEnd; x++) {
//                MeasureLine measureLineShallowCopy = new MeasureLine();
//
//                int line = y * Constants.LINES_IN_A_ROW + x;
//                MeasureLine measureLineOriginal = song.get(line);
//
//                for (int i = 0; i < measureLineOriginal.size(); i++) {
//                    
//                    //TODO: use instrFiltered in DataClipboard
//                    Note n = measureLineOriginal.get(i);
//                    //if rowBegin, consider positionBegin
//                    //if rowEnd, consider positionEnd
//                    if (!(y == rowBegin && n.getPosition().ordinal() < positionBegin.ordinal()
//                            || y == rowEnd && n.getPosition().ordinal() > positionEnd.ordinal()) 
//                            && instrFiltered(n.getInstrument())){
//                        
//                        measureLineOriginal.remove(n);
//                        measureLineShallowCopy.add(n);
//                        i--;//element removed, adjust index back
//                    }
//                }
//                content.add(measureLineShallowCopy);
//            }          
//            
//            //empty filler for pasting correct offset
//            if(y != rowEnd) {
//                for (int i = 0; i < Constants.LINES_IN_A_ROW - (rowLineEnd - rowLineBegin); i++) {
//                    content.add(new MeasureLine());
//                }
//            }
//        }
//        return content;
//    }
//
//    /**
//     * Shift all content at and after lineMoveTo a number of content.size()
//     * lines over. Paste copied content at lineMoveTo.
//     *
//     * If no copied content then insert one empty line effectively shifting over
//     * all lines at and after lineMoveTo.
//     *
//     * @param lineMoveTo, where this line and all lines after are shifted,
//     * copied content will begin here
//     * @return the lines that have been moved past the song's length defined by
//     * Constants.SONG_LENGTH, null if no lines with notes have moved past
//     */
//    public static List<MeasureLine> insert(Song song, int lineMoveTo) {
//        
//        //Move all content at and after lineMoveTo a number of content.size() lines over
//        List<MeasureLine> linesOOB = shiftBack(song, lineMoveTo, DataClipboard.getContentTrimmed().size());
//        //Paste copied content at lineMoveTo.
//        paste(song, lineMoveTo);
//        pasteVol(song, lineMoveTo);
//        
//        return linesOOB;
//    }
//    
//    /**
//     * Move a selection to a new location and delete where it was before.
//     */
//    @Deprecated
//    public static void move(Song song, int lineBegin, Note.Position positionBegin, int lineEnd, Note.Position positionEnd,
//            int lineMoveTo) {
////        move
////        paste(song, lineMoveTo);
//    }

	/**
	 * Paste data from clipboard at lineMoveTo.
	 */
	public void paste(int lineMoveTo) {

		HashMap<Integer, StaffNoteLine> data = theDataClipboard.getData();
		
		NoteMatrix matrix = theStaff.getNoteMatrix();
		
		for (Map.Entry<Integer, StaffNoteLine> lineCopy : data.entrySet()) {
			int i = lineMoveTo + lineCopy.getKey() - theDataClipboard.getDataLineBegin();

			ArrayList<StackPane> matrixLineDest = null;
			if(i - StateMachine.getMeasureLineNum() < 10)
				matrix.getLine(i - StateMachine.getMeasureLineNum());
			
			StaffNoteLine lineDest = theStaff.getSequence().getLine(i);
			StaffNoteLine lineSrc = lineCopy.getValue();
			for(StaffNote note : lineSrc.getNotes()) {
				
				// see StaffInstrumentEventHandler's placeNote function
				StaffNote theStaffNote = new StaffNote(note.getInstrument(), note.getPosition(), note.getAccidental());
				theStaffNote.setImage(il.getSpriteFX(note.getInstrument().imageIndex()));
				
				if (matrixLineDest != null) {
					StackPane matrixPosDest = matrixLineDest
							.get(theStaffNote.getPosition());//(Values.NOTES_IN_A_LINE - 1) - 
					ObservableList<Node> matrixNotes = matrixPosDest.getChildren();
					if (!matrixNotes.contains(theStaffNote))
						matrixNotes.add(theStaffNote);
				}

				if (lineDest.isEmpty()) {
					lineDest.setVolumePercent(((double) Values.DEFAULT_VELOCITY) / Values.MAX_VELOCITY);
					
					if (i - StateMachine.getMeasureLineNum() < 10) {
						StaffVolumeEventHandler sveh = theStaff.getNoteMatrix()
								.getVolHandler(i - StateMachine.getMeasureLineNum());
						sveh.updateVolume();
					}
				}

				if (!lineDest.contains(theStaffNote))
		        	lineDest.add(theStaffNote);
			}
		}

        theStaff.redraw();
	}

	// /**
	// *
	// * @return reference of notes found in the given selection
//     */
//    public static List<MeasureLine> selection(Song song, int lineBegin, Note.Position positionBegin, int lineEnd, Note.Position positionEnd) {
//        //TODO: use instrFiltered in DataClipboard
//        int rowBegin = lineBegin / Constants.LINES_IN_A_ROW;
//        int rowEnd = lineEnd / Constants.LINES_IN_A_ROW;
//        int rowLineBegin = lineBegin % Constants.LINES_IN_A_ROW;
//        int rowLineEnd = lineEnd % Constants.LINES_IN_A_ROW;
//        
//        List<MeasureLine> content = new ArrayList<>();
//        for (int y = rowBegin; y <= rowEnd; y++) {
//            for (int x = rowLineBegin; x <= rowLineEnd; x++) {
//                MeasureLine measureLineShallowCopy = new MeasureLine();
//
//                int line = y * Constants.LINES_IN_A_ROW + x;
//                MeasureLine measureLineOriginal = song.get(line);
//
//                for (Note n : measureLineOriginal) {
//
//                    //TODO: use instrFiltered in DataClipboard
//                    //if rowBegin, consider positionBegin
//                    //if rowEnd, consider positionEnd
//                    if (!(y == rowBegin && n.getPosition().ordinal() < positionBegin.ordinal()
//                            || y == rowEnd && n.getPosition().ordinal() > positionEnd.ordinal())
//                            && instrFiltered(n.getInstrument())) {
////                        Note nCopy = new Note(n.getInstrument(), n.getPosition(), n.getModifier());
//                        measureLineShallowCopy.add(n);//nCopy);
//                    }
//                }
//                content.add(measureLineShallowCopy);
//            }
//
//            //empty filler for pasting correct offset
//            if (y != rowEnd) {
//                for (int i = 0; i < Constants.LINES_IN_A_ROW - (rowLineEnd - rowLineBegin + 1); i++) {
//                    content.add(new MeasureLine());
//                }
//            }
//        }
//        return content;
//    }
//    
//    public static boolean instrFiltered(Note.Instrument instr) {
//        return DataClipboard.getInstrFiltered() == null || DataClipboard.getInstrFiltered()[instr.ordinal()];
//    }
//    
//    public static void copyVol(Song song, int lineBegin, int lineEnd) {
////        DataClipboard.setContentVol(selectionVol(song,lineBegin,lineEnd));
//        int rowBegin = lineBegin / Constants.LINES_IN_A_ROW;
//        int rowEnd = lineEnd / Constants.LINES_IN_A_ROW;
//        int rowLineBegin = lineBegin % Constants.LINES_IN_A_ROW;
//        int rowLineEnd = lineEnd % Constants.LINES_IN_A_ROW;
//
//        DataClipboard.updateContentLineBegin(lineBegin);
//        DataClipboard.updateContentLineEnd(lineEnd);
//        
//        for(int y = rowBegin; y <= rowEnd; y ++) {
//            for (int x = rowLineBegin; x <= rowLineEnd; x++) {
//                MeasureLine measureLineVolCopy = new MeasureLine();
//                
//                int line = y * Constants.LINES_IN_A_ROW + x;
//                MeasureLine measureLineOriginal = song.get(line);
//
//                if(!measureLineOriginal.isEmpty()) {
//                    measureLineVolCopy.setVolume(measureLineOriginal.getVolume());
//                    //check if line already exists in DataClipboard's content
//                    if(DataClipboard.getContent().get(line) == null) {
//                        DataClipboard.getContent().set(line, measureLineVolCopy);
//                    } else {
//                        DataClipboard.getContent().get(line).setVolume(measureLineOriginal.getVolume());
//                    }
//                }
//            } 
//        }
//    }
//    
//    /**
//     * 
//     * @param song set vols to Constants.MAX_VELOCITY
//     * @param lineBegin
//     * @param lineEnd
//     * @return a list of former vols before "deletion"/getting set to MAX_VELOCITY
//     */
//    public static List<Integer> deleteVol(Song song, int lineBegin, int lineEnd) {
//        int rowBegin = lineBegin / Constants.LINES_IN_A_ROW;
//        int rowEnd = lineEnd / Constants.LINES_IN_A_ROW;
//        int rowLineBegin = lineBegin % Constants.LINES_IN_A_ROW;
//        int rowLineEnd = lineEnd % Constants.LINES_IN_A_ROW;
//        
//        List<Integer> contentVol = new ArrayList<>();
//        for(int y = rowBegin; y <= rowEnd; y ++) {
//            for (int x = rowLineBegin; x <= rowLineEnd; x++) {
//                Integer vol = null;
//                
//                int line = y * Constants.LINES_IN_A_ROW + x;
//
//                if(!song.get(line).isEmpty()) {
//                    vol = song.get(line).getVolume();
//                    song.get(line).setVolume(Constants.MAX_VELOCITY);
//                }
//                contentVol.add(vol);
//            }          
//            
//            //empty filler for pasting correct offset
//            if(y != rowEnd) {
//                for (int i = 0; i < Constants.LINES_IN_A_ROW - (rowLineEnd - rowLineBegin); i++) {
//                    contentVol.add(null);
//                }
//            }
//        }
//        return contentVol;
//    }
//    
//    public static void pasteVol(Song song, int lineMoveTo) {
//        List<MeasureLine> contentVol = DataClipboard.getContentTrimmed();
//        for (int i = 0; i < contentVol.size(); i++) {    
//            MeasureLine ml = song.get(lineMoveTo + i);
//
//            if(contentVol.get(i) != null && contentVol.get(i).getVolume() >= 0) {
//                ml.setVolume(contentVol.get(i).getVolume());
//            }
//        }
//    }
//    
//    /** 
//     * @see #selVol(Song song, int lineBegin, int lineEnd)
//     * @return copy of volumes found in the given selection
//     */
//    public static List<Integer> selectionVol(Song song, int lineBegin, int lineEnd) {
//        int rowBegin = lineBegin / Constants.LINES_IN_A_ROW;
//        int rowEnd = lineEnd / Constants.LINES_IN_A_ROW;
//        int rowLineBegin = lineBegin % Constants.LINES_IN_A_ROW;
//        int rowLineEnd = lineEnd % Constants.LINES_IN_A_ROW;
//        
//        List<Integer> contentVol = new ArrayList<>();
//        for (int y = rowBegin; y <= rowEnd; y++) {
//            for (int x = rowLineBegin; x <= rowLineEnd; x++) {
//                Integer vol = null;
//                
//                int line = y * Constants.LINES_IN_A_ROW + x;
//                if(!song.get(line).isEmpty()) {
//                    vol = song.get(line).getVolume();
//                }
//                contentVol.add(vol);
//            }
//            //empty filler for pasting correct offset
//            if (y != rowEnd) {
//                for (int i = 0; i < Constants.LINES_IN_A_ROW - (rowLineEnd - rowLineBegin + 1); i++) {
//                    contentVol.add(null);
//                }
//            }
//        }
//        
//        return contentVol;
//    }
//    
//    //--------------------------------------------------------------------------
//    //
//    //  DataClipboard's selection variable to store the selected region
//    //
//    //--------------------------------------------------------------------------
//    /**
//     * copy over selection to content including all note and vol information if
//     * present
//     */
//    public static void copySel() {
//        DataClipboard.clearContent();
//        if (DataClipboard.getSelectionLineBegin() != -1) {
//            DataClipboard.updateContentLineBegin(DataClipboard.getSelectionLineBegin());
//            DataClipboard.updateContentLineEnd(DataClipboard.getSelectionLineEnd());
//            for (MeasureLine ml : DataClipboard.getSelectionTrimmed()) {
//                if (ml != null) {
//                    DataClipboard.getContent().set(ml.getLineNumber(), ml);
//                }
//            }
//        }
//    }
//    
//    public static List<MeasureLine> cutSel(Song song) {
//        copySel();
//        return deleteSel(song);
//    }
//    
//    public static List<MeasureLine> deleteSel(Song song) {
//        for(MeasureLine ml : DataClipboard.getSelectionTrimmed()) {
//            if(ml != null) {
//                for(Note n : ml) {
//                    song.get(ml.getLineNumber()).remove(n);
//                }
//                if(ml.getVolume() >= 0) {
//                    song.get(ml.getLineNumber()).setVolume(Constants.MAX_VELOCITY);
//                }
//            }
//        }
//        List<MeasureLine> selection = DataClipboard.getSelectionTrimmed();
//        DataClipboard.clearSelection();
//        return selection;
//    }
//    
//    /**
//     * Move all content at and after lineMoveTo a number of content.size() lines
//     * over. Done by deleting selection and "pasting" the selection to
//     * lineMoveTo.
//     *
//     * @param lineMoveTo, where the selection will be moved to
//     * @return the lines that have been moved past the song's length defined by
//     * Constants.SONG_LENGTH, null if none
//     */
//    public static List<MeasureLine> moveSel(Song song, int lineMoveTo) {
//        List<MeasureLine> deletedSel = deleteSel(song);
//        for(int i = lineMoveTo; i < Math.min(Constants.SONG_LENGTH, lineMoveTo + deletedSel.size()); i++) {
//            MeasureLine ml = deletedSel.get(i - lineMoveTo);
//            if(ml != null) {
//                for(Note n : ml) {
//                    song.get(i).add(n);
//                }
//                if(ml.getVolume() >= 0) {
//                    song.get(i).setVolume(ml.getVolume());
//                }
//            }
//        }
//        //lines moved past the song's length defined by Constants.SONG_LENGTH, null if none
//        List<MeasureLine> linesOOB = null;
//        if(lineMoveTo + deletedSel.size() > Constants.SONG_LENGTH) {
//            int numOOB = lineMoveTo + deletedSel.size() - Constants.SONG_LENGTH;
//            linesOOB = new ArrayList<>(deletedSel.subList(deletedSel.size() - numOOB, deletedSel.size()));
//        }
//        return linesOOB;
//    }
//    
    public void select(int lineBegin, int positionBegin, int lineEnd, int positionEnd) {
    	theDataClipboard.addSelection(lineBegin, lineEnd, positionBegin, positionEnd);
    }
//    
//    public static List<MeasureLine> selVol(Song song, int lineBegin, int lineEnd) {
//        //important check to prevent selecting vols when not within bounds
//        if(lineEnd < lineBegin)
//            return new ArrayList<MeasureLine>();
//        
//        int rowBegin = lineBegin / Constants.LINES_IN_A_ROW;
//        int rowEnd = lineEnd / Constants.LINES_IN_A_ROW;
//        int rowLineBegin = lineBegin % Constants.LINES_IN_A_ROW;
//        int rowLineEnd = lineEnd % Constants.LINES_IN_A_ROW;
//        
//        DataClipboard.updateSelectionLineBegin(lineBegin);
//        DataClipboard.updateSelectionLineEnd(lineEnd);
//        
//        for (int y = rowBegin; y <= rowEnd; y++) {
//            for (int x = rowLineBegin; x <= rowLineEnd; x++) {
//                int line = y * Constants.LINES_IN_A_ROW + x;
//                if (!song.get(line).isEmpty()) {
//                    if (DataClipboard.getSelection().get(line) == null) {
//                        MeasureLine measureLineCopy = new MeasureLine();
//                        measureLineCopy.setVolume(song.get(line).getVolume());
//                        measureLineCopy.setLineNumber(line);
//                        DataClipboard.getSelection().set(line, measureLineCopy);
//                    } else {
//                        DataClipboard.getSelection().get(line).setVolume(song.get(line).getVolume());
//                    }
//                }
//            }
//        }
//        
//        return DataClipboard.getSelectionTrimmed();
//    }
//        
//    /**
//     * select only volumes at lines that have notes highlighted
//     * @param song
//     * @return 
//     */
//    public static List<MeasureLine> selVolAtNotes(Song song) {
//        
//        for(MeasureLine ml : DataClipboard.getSelectionTrimmed()) {
//            if(ml != null && ml.size() > 0) {
//                ml.setVolume(song.get(ml.getLineNumber()).getVolume());
//            }
//        }
//        
//        return DataClipboard.getSelectionTrimmed();
//    }
//        
//    /**
//     * remove notes from every measureLine in the selection. if measureLine's
//     * volume < 0 then replace it with null
//     */
//    public static void deselNotes() {
//        List<MeasureLine> selection = DataClipboard.getSelection();
//        for(int i = DataClipboard.getSelectionLineBegin(); i < DataClipboard.getSelectionLineEnd() + 1; i++){
//            if(selection.get(i) != null) {
//                if (selection.get(i).size() > 0) {
//                    selection.get(i).clear();
//                }
//                if(selection.get(i).getVolume() < 0) {
//                    selection.set(i, null);
//                }
//            }
//        }
//        
//        //find new line begin      
//        for(int i = DataClipboard.getSelectionLineBegin(); i < DataClipboard.getSelectionLineEnd() + 1; i++){
//            if(selection.get(i) != null) {
//                DataClipboard.setSelectionLineBegin(i);
//                break;
//            }
//        }
//        
//        //find new line end    
//        for(int i = DataClipboard.getSelectionLineEnd(); i > DataClipboard.getSelectionLineBegin() - 1; i--){
//            if(selection.get(i) != null) {
//                DataClipboard.setSelectionLineEnd(i);
//                break;
//            }
//        }
//    }
//
//    /**
//     * sets all measureLines' volumes to -1. if measureLine is also empty then
//     * replace it with null
//     */
//    public static void deselVols() {
//        List<MeasureLine> selection = DataClipboard.getSelection();
//        for(int i = DataClipboard.getSelectionLineBegin(); i < DataClipboard.getSelectionLineEnd() + 1; i++){
//            if(selection.get(i) != null) {
//                if (selection.get(i).getVolume() >= 0) {
//                    selection.get(i).setVolume(-1);
//                }
//                if(selection.get(i).isEmpty()) {
//                    selection.set(i, null);
//                }
//            }
//        }
//        
//        //find new line begin      
//        for(int i = DataClipboard.getSelectionLineBegin(); i < DataClipboard.getSelectionLineEnd() + 1; i++){
//            if(selection.get(i) != null) {
//                DataClipboard.setSelectionLineBegin(i);
//                break;
//            }
//        }
//        
//        //find new line end    
//        for(int i = DataClipboard.getSelectionLineEnd(); i > DataClipboard.getSelectionLineBegin() - 1; i--){
//            if(selection.get(i) != null) {
//                DataClipboard.setSelectionLineEnd(i);
//                break;
//            }
//        }
//    }
//    
//    /**
//     *
//     * @param lineShift the line at which shifting will start
//     * @param numLines the number of lines that will be shifted toward the end. 
//     * @return the lines that have been moved past the song's length defined by
//     * Constants.SONG_LENGTH, null if no lines with notes have moved past
//     */
//    public static List<MeasureLine> shiftBack(Song song, int lineShift, int numLines) {
//        List<MeasureLine> linesOOB = new ArrayList<>(song.subList(Constants.SONG_LENGTH - numLines, Constants.SONG_LENGTH));
//        for(int i = Constants.SONG_LENGTH - 1; i >= lineShift + numLines; i--) {
//            int mlSize = song.get(i).size();
//            for(int j = 0 ; j < mlSize; j++) {
//                song.get(i).remove(0);//individual remove instead of removeall or clear because those do not trigger the changelistener
//            }
//            song.get(i).addAll(song.get(i - numLines));
//            song.get(i).setVolume(song.get(i - numLines).getVolume());
//        }
//        for (int i = lineShift; i < lineShift + numLines; i++) {
//            int mlSize = song.get(i).size();
//            for (int j = 0; j < mlSize; j++) {
//                song.get(i).remove(0);
//            }
//        }
//        
//        //null if no lines with notes have moved past
//        boolean setNull = true;
//        for (MeasureLine ml : linesOOB) {
//            if (!ml.isEmpty()) {
//                setNull = false;
//                break;
//            }
//        }
//        if (setNull) {
//            linesOOB = null;
//        }
//        return linesOOB;
//    }
//
//    /**
//     *
//     * @param lineShift the line at which shifting will start
//     * @param numLines the number of lines that will be shifted toward the
//     * beginning
//     * @return the lines that have been deleted, null if all those lines are
//     * empty
//     */
//    public static List<MeasureLine> shiftForward(Song song, int lineShift, int numLines) {
//        List<MeasureLine> linesDeleted = new ArrayList<>(song.subList(lineShift - numLines, lineShift));
//        for(int i = lineShift - numLines; i < Constants.SONG_LENGTH - numLines; i++) {
//            int mlSize = song.get(i).size();
//            for(int j = 0 ; j < mlSize; j++) {
//                song.get(i).remove(0);//individual remove instead of removeall or clear because those do not trigger the changelistener
//            }
//            song.get(i).addAll(song.get(i + numLines));
//            song.get(i).setVolume(song.get(i + numLines).getVolume());
//        }
//        for (int i = Constants.SONG_LENGTH - numLines; i < Constants.SONG_LENGTH; i++) {
//            int mlSize = song.get(i).size();
//            for (int j = 0; j < mlSize; j++) {
//                song.get(i).remove(0);
//            }
//        }
//        
//        //null if no lines with notes have moved past
//        boolean setNull = true;
//        for (MeasureLine ml : linesDeleted) {
//            if (!ml.isEmpty()) {
//                setNull = false;
//                break;
//            }
//        }
//        if (setNull) {
//            linesDeleted = null;
//        }
//        return linesDeleted;
//    }
}
