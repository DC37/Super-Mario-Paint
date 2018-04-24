package smp.components.staff.clipboard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.ColorInput;
import smp.ImageLoader;
import smp.commandmanager.ModifySongManager;
import smp.commandmanager.commands.AddNoteCommand;
import smp.commandmanager.commands.AddVolumeCommand;
import smp.commandmanager.commands.RemoveNoteCommand;
import smp.commandmanager.commands.RemoveVolumeCommand;
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
 * @author j574y923
 */
public class StaffClipboardAPI {
	
	private Staff theStaff;
	private StaffClipboard theDataClipboard;
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
	
	public StaffClipboardAPI(StaffClipboard dc, Staff st, ImageLoader i, ModifySongManager cm) {
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
	                    StaffClipboard.HIGHLIGHT_FILL
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
	            commandManager.execute(new RemoveNoteCommand(lineDest, note));

				if (lineDest.isEmpty() && 0 <= line - StateMachine.getMeasureLineNum()
						&& line - StateMachine.getMeasureLineNum() < Values.NOTELINES_IN_THE_WINDOW) {
					StaffVolumeEventHandler sveh = theStaff.getNoteMatrix()
							.getVolHandler(line - StateMachine.getMeasureLineNum());
					sveh.setVolumeVisible(false);
					commandManager.execute(new RemoveVolumeCommand(lineDest, lineDest.getVolume()));
				}
			}
			// idk why but redraw needs to be called every line or else weird
			// stuff happens (like some notes don't get added)
			theStaff.redraw();
		}
		
		clearSelection();
        commandManager.record();
    }

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

		            commandManager.execute(new AddVolumeCommand(lineDest, Values.DEFAULT_VELOCITY));
				}

				if (!lineDest.contains(theStaffNote)) {
		        	lineDest.add(theStaffNote);
		            StateMachine.setSongModified(true);
		            commandManager.execute(new AddNoteCommand(lineDest, theStaffNote));
				}
			}
			
			// paste volume
			if(!ignoreVolumesFlag) {
	            commandManager.execute(new RemoveVolumeCommand(lineDest, lineDest.getVolume()));
				lineDest.setVolume(lineSrc.getVolume());
	            commandManager.execute(new AddVolumeCommand(lineDest, lineDest.getVolume()));
				
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
		StaffClipboardFilter instFilter = theDataClipboard.getInstrumentFilter();

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
