package smp.clipboard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.ColorInput;
import smp.ImageLoader;
import smp.commandmanager.CommandManager;
import smp.commandmanager.ModifySongManager;
import smp.components.InstrumentIndex;
import smp.components.Values;
import smp.components.staff.Staff;
import smp.components.staff.StaffVolumeEventHandler;
import smp.components.staff.sequences.StaffNote;
import smp.components.staff.sequences.StaffNoteLine;
import smp.stateMachine.StateMachine;

/**
 * The API that will contain functions for clipboard. These include copy, cut,
 * delete, insert, move, paste.
 *
 * @author J
 */
public class DataClipboardAPI {
	
	private Staff theStaff;
	private DataClipboard theDataClipboard;
	private ImageLoader il;
	private ModifySongManager commandManager;
	
	/* Corresponds with the first selection line */
	private int selectionLineBegin = Integer.MAX_VALUE;
	
	private Blend highlightBlend;

	/*
	 * If false, the selected notes remain in selection but are unhighlighted
	 * and will not be copied
	 */
	private boolean selectNotesFlag = true;
	/*
	 * If false, the selected volumes remain in selection but are unhighlighted
	 * and will not be copied but there WILL still be volume data.
	 * ignoreVolumesFlag will toggle on telling not to paste those volumes in
	 * copy().
	 */
	private boolean selectVolumesFlag = true;
	/*
	 * Because there is always volume data, this flag will indicate when to
	 * ignore pasting those volumes. If !selectVolumesFlag then toggle on when
	 * copying and toggle off when clearing copiedData.
	 */
	private boolean ignoreVolumesFlag = false;
	
	public DataClipboardAPI(DataClipboard dc, Staff st, ImageLoader i, ModifySongManager cm) {
		theDataClipboard = dc;
		theStaff = st;
		il = i;
		commandManager = cm;
		
		highlightBlend = new Blend(
	            BlendMode.SRC_OVER,
	            null,
	            new ColorInput(
	                    0,
	                    0,
	                    il.getSpriteFX(InstrumentIndex.BOAT.imageIndex()).getWidth(),
	                    il.getSpriteFX(InstrumentIndex.BOAT.imageIndex()).getHeight(),
	                    DataClipboard.HIGHLIGHT_FILL
	            )
	    );
	}

	/**
	 * Get all notes from selection and copy them into data. lines are relative
	 * to selectionLineBegin. they are not absolute.
	 * 
	 * also copy volumes.
	 */
	public void copy() {
		//if there's something new selected, make way for new data
		//else just use old data
		if(!theDataClipboard.getSelection().isEmpty() && (selectNotesFlag || selectVolumesFlag))
			clearCopiedData();

		for (Map.Entry<Integer, StaffNoteLine> noteLine : theDataClipboard.getSelection().entrySet()) {
			int line = noteLine.getKey();
			ArrayList<StaffNote> ntList = noteLine.getValue().getNotes();
			if (selectNotesFlag)
				for(StaffNote note : ntList) 
					//relative index
					copyNote(line - selectionLineBegin, note);
			
			if (selectVolumesFlag)
				copyVolume(line - selectionLineBegin, noteLine.getValue().getVolume());
		}
		
		if (!selectVolumesFlag)
			ignoreVolumesFlag = true;
	}

    /**
     * Copy selected notes and volumes and delete selected notes.
     */
    public void cut() {
        copy();
        delete();
    }

    /**
     * Delete selected notes (only notes, not volume).
     */
    public void delete() {

		for (Map.Entry<Integer, StaffNoteLine> noteLine : theDataClipboard.getSelection().entrySet()) {
			int line = noteLine.getKey();
			ArrayList<StaffNote> ntList = noteLine.getValue().getNotes();
			
			StaffNoteLine lineDest = theStaff.getSequence().getLine(line);
			
			for(StaffNote note : ntList){
				lineDest.remove(note);
	            StateMachine.setSongModified(true);
	            commandManager.removeNote(lineDest, note);

				if (lineDest.isEmpty() && 0 <= line - StateMachine.getMeasureLineNum()
						&& line - StateMachine.getMeasureLineNum() < Values.NOTELINES_IN_THE_WINDOW) {
					StaffVolumeEventHandler sveh = theStaff.getNoteMatrix()
							.getVolHandler(line - StateMachine.getMeasureLineNum());
					sveh.setVolumeVisible(false);
					commandManager.removeVolume(lineDest, lineDest.getVolume());
				}
			}
			// idk why but redraw needs to be called every line or else weird
			// stuff happens (like some notes don't get added)
			theStaff.redraw();
		}
		
		clearSelection();
        commandManager.record();
    }

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
	 * Paste copied notes and volumes.
	 * 
	 * @param lineMoveTo starting line to paste data at
	 */
	public void paste(int lineMoveTo) {

		HashMap<Integer, StaffNoteLine> copiedData = theDataClipboard.getCopiedData();
		
		for (Map.Entry<Integer, StaffNoteLine> lineCopy : copiedData.entrySet()) {
			int line = lineMoveTo + lineCopy.getKey();
			
			StaffNoteLine lineDest = theStaff.getSequence().getLine(line);
			StaffNoteLine lineSrc = lineCopy.getValue();
			for(StaffNote note : lineSrc.getNotes()) {
				
				// see StaffInstrumentEventHandler's placeNote function
				StaffNote theStaffNote = new StaffNote(note.getInstrument(), note.getPosition(), note.getAccidental());
				theStaffNote.setMuteNote(note.muteNoteVal());
		        if (theStaffNote.muteNoteVal() == 0) {
		            theStaffNote.setImage(il.getSpriteFX(note.getInstrument().imageIndex()));
		        } else if (theStaffNote.muteNoteVal() == 1) {
		            theStaffNote.setImage(il.getSpriteFX(note.getInstrument().imageIndex().alt()));
		        } else if (theStaffNote.muteNoteVal() == 2) {
		            theStaffNote.setImage(il.getSpriteFX(note.getInstrument().imageIndex()
		                    .silhouette()));
		        }

				if (lineDest.isEmpty()) {
					lineDest.setVolumePercent(((double) Values.DEFAULT_VELOCITY) / Values.MAX_VELOCITY);
					
					if (line - StateMachine.getMeasureLineNum() < Values.NOTELINES_IN_THE_WINDOW) {
						StaffVolumeEventHandler sveh = theStaff.getNoteMatrix()
								.getVolHandler(line - StateMachine.getMeasureLineNum());
						sveh.updateVolume();
					}

		            commandManager.addVolume(lineDest, Values.DEFAULT_VELOCITY);
				}

				if (!lineDest.contains(theStaffNote)) {
		        	lineDest.add(theStaffNote);
		            StateMachine.setSongModified(true);
		            commandManager.addNote(lineDest, theStaffNote);
				}
			}
			
			// paste volume
			if(!ignoreVolumesFlag) {
				lineDest.setVolume(lineSrc.getVolume());
				
				if (line - StateMachine.getMeasureLineNum() < Values.NOTELINES_IN_THE_WINDOW) {
					StaffVolumeEventHandler sveh = theStaff.getNoteMatrix()
							.getVolHandler(line - StateMachine.getMeasureLineNum());
					sveh.updateVolume();
				}
	            StateMachine.setSongModified(true);
			}
			
		}

        theStaff.redraw();
        commandManager.record();
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
	/**
	 * get all notes in the line and position bounds that are filtered and put
	 * them into the selection map
	 * 
	 * @param lineBegin
	 * @param positionBegin
	 *            (<= positionEnd, i.e. positionBegin could be lower notes)
	 * @param lineEnd
	 * @param positionEnd
	 *            (>= positionBegin, i.e. positionEnd could be higher notes)
	 */
	public void select(int lineBegin, int positionBegin, int lineEnd, int positionEnd) {
		InstrumentFilter instFilter = theDataClipboard.getInstrumentFilter();

		for (int line = lineBegin; line <= lineEnd; line++) {
			StaffNoteLine lineSrc = theStaff.getSequence().getLine(line);

			ArrayList<StaffNote> ntList = lineSrc.getNotes();
			for (StaffNote note : ntList) {
				if (positionBegin <= note.getPosition() && note.getPosition() <= positionEnd
						&& instFilter.isFiltered(note.getInstrument())) {
					// store the copied note at the relative line
					selectNote(line, note);
					// store the staffnoteline's volume at the relative line
					selectVolume(line, lineSrc.getVolume());
					updateSelectionLineBegin(line);
				}
			}
		}
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
	
	public void clearCopiedData() {
		theDataClipboard.getCopiedData().clear();
		
		ignoreVolumesFlag = false;
	}

	/**
	 * Unhighlight all notes and volumes and clear the selection map.
	 */
	public void clearSelection() {
		//unhighlight notes
		HashMap<Integer, StaffNoteLine> selection = theDataClipboard.getSelection();
		for(StaffNoteLine line : selection.values()) 
			for(StaffNote note : line.getNotes())
				highlightNote(note, false);

		//unhighlight volumes
		theDataClipboard.getHighlightedVolumes().clear();
		theDataClipboard.getHighlightedVolumesRedrawer().changed(null, 0, StateMachine.getMeasureLineNum());

		selection.clear();
		selectionLineBegin = Integer.MAX_VALUE;
		selectNotesFlag = true;
		selectVolumesFlag = true;
	}
	
	/**
	 * set selectionLineBegin to line if line < selectionLineBegin.
	 * selectionLineBegin gets subtracted from copied data lines to get relative
	 * bounds.
	 * 
	 * 
	 * make selection line lower to copy more blank lines before actual content.
	 * 
	 * @param line
	 *            to update selectionLineBegin to
	 */
	public void updateSelectionLineBegin(int line) {
		if (line < selectionLineBegin)
			selectionLineBegin = line;
	}
	
	public int getSelectionLineBegin() {
		return selectionLineBegin;
	}
	
	/**
	 * Copy information from note. Add new note into copiedData.
	 * 
	 * Note: the line information is where the note occurs in copiedData, NOT
	 * where it occurs in the staff.
	 * 
	 * @param line
	 *            where the note will be placed in copiedData
	 * @param note
	 *            info to be copied
	 */
	public void copyNote(int line, StaffNote note) {
		StaffNote newNote = new StaffNote(note.getInstrument(), note.getPosition(), note.getAccidental());
		newNote.setMuteNote(note.muteNoteVal());
		HashMap<Integer, StaffNoteLine> copiedData = theDataClipboard.getCopiedData();
		if(!copiedData.containsKey(line))
			copiedData.put(line, new StaffNoteLine());
		copiedData.get(line).add(newNote);
	}
	
	/**
	 * Select note. Add existing note into selection. Highlight note.
	 * 
	 * @param line
	 *            where the note occurs
	 * @param note
	 *            that will be placed into selection
	 */
	public void selectNote(int line, StaffNote note) {
		HashMap<Integer, StaffNoteLine> selection = theDataClipboard.getSelection();
		if(!selection.containsKey(line))
			selection.put(line, new StaffNoteLine());
		selection.get(line).add(note);
		highlightNote(note, true);
	}
	
	public void highlightNote(StaffNote note, boolean highlight) {
		if(highlight)
			note.setEffect(highlightBlend);
		else
			note.setEffect(null);
	}
	
	public void copyVolume(int line, int volume) {
		HashMap<Integer, StaffNoteLine> copiedData = theDataClipboard.getCopiedData();
		if(!copiedData.containsKey(line))
			copiedData.put(line, new StaffNoteLine());
		copiedData.get(line).setVolume(volume);
	}
	
	public void selectVolume(int line, int volume) {
		HashMap<Integer, StaffNoteLine> selection = theDataClipboard.getSelection();
		if(!selection.containsKey(line))
			selection.put(line, new StaffNoteLine());
		selection.get(line).setVolume(volume);
		highlightVolume(line, true);
	}

	public void highlightVolume(int line, boolean highlight) {
		if(highlight) 
			theDataClipboard.getHighlightedVolumes().add(line);
		else 
			theDataClipboard.getHighlightedVolumes().remove(line);
		
		// trigger the ChangeListener that will set the highlight effect
		if (StateMachine.getMeasureLineNum() <= line
				&& line < StateMachine.getMeasureLineNum() + Values.NOTELINES_IN_THE_WINDOW)
			theDataClipboard.getHighlightedVolumesRedrawer().changed(null, 0, StateMachine.getMeasureLineNum());
	}
	
	public void selectNotesToggle(boolean selectNotes) {
		selectNotesFlag = selectNotes;
		if(selectNotesFlag) {
			//highlight notes
			for(StaffNoteLine line : theDataClipboard.getSelection().values()) 
				for(StaffNote note : line.getNotes())
					highlightNote(note, true);
		} else {
			//unhighlight notes
			for(StaffNoteLine line : theDataClipboard.getSelection().values()) 
				for(StaffNote note : line.getNotes())
					highlightNote(note, false);
		}
	}
	
	public void selectVolumesToggle(boolean selectVolumes) {
		selectVolumesFlag = selectVolumes;
		if(selectVolumesFlag) {
			for(Integer line : theDataClipboard.getSelection().keySet())
				highlightVolume(line, true);
		} else {
			//unhighlight volumes
			theDataClipboard.getHighlightedVolumes().clear();
			theDataClipboard.getHighlightedVolumesRedrawer().changed(null, 0, StateMachine.getMeasureLineNum());
		}
	}
	
	public boolean isSelectNotesOn() {
		return selectNotesFlag;
	}
	
	public boolean isSelectVolumesOn() {
		return selectVolumesFlag;
	}
}
