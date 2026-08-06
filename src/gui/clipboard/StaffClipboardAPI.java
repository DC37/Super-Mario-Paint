package gui.clipboard;

import java.util.List;
import java.util.Map;

import backend.editing.ModifySongManager;
import backend.editing.commands.AddNoteCommand;
import backend.editing.commands.AddVolumeCommand;
import backend.editing.commands.RemoveNoteCommand;
import backend.editing.commands.RemoveVolumeCommand;
import backend.songs.Note;
import backend.songs.NoteLine;
import gui.Staff;
import gui.StateMachine;
import gui.Values;
import gui.components.staff.StaffVolumeEventHandler;
import me.dc37.smp.models.SMPAppModel;

/**
 * The API will contain functions for <code>StaffClipboard</code>. These include
 * copy, cut, delete, insert, move, paste.
 * 
 * Since v1.1.2, the API will also contain <code>StaffRubberBand</code>
 * functions such as resizing.
 *
 * @author j574y923
 */
public class StaffClipboardAPI {

    private final SMPAppModel model;
    private Staff theStaff;
    private StaffClipboard theStaffClipboard;
    private ModifySongManager commandManager;
    
    /* Corresponds with the first selection line */
    private int selectionLineBegin = Integer.MAX_VALUE;

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
    
    public StaffClipboardAPI(SMPAppModel model,
            StaffClipboard sc, Staff st, ModifySongManager cm) {
        
        this.model = model;
        theStaffClipboard = sc;
        theStaff = st;
        commandManager = cm;
    }

    /**
     * Get all notes from selection and copy them into data. lines are relative
     * to selectionLineBegin. they are not absolute.
     * 
     * also copy volumes.
     */
    public void copy() {
        /* if there's something new selected, make way for new data
           else just use old data */
        if(!theStaffClipboard.getSelection().isEmpty() && (selectNotesFlag || selectVolumesFlag))
            clearCopiedData();

        for (Map.Entry<Integer, NoteLine> noteLine : theStaffClipboard.getSelection().entrySet()) {
            int line = noteLine.getKey();
            List<Note> ntList = noteLine.getValue().getNotes();
            if (selectNotesFlag)
                for(Note note : ntList) 
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

        for (Map.Entry<Integer, NoteLine> noteLine : theStaffClipboard.getSelection().entrySet()) {
            int line = noteLine.getKey();
            List<Note> ntList = noteLine.getValue().getNotes();
            
            NoteLine lineDest = theStaff.getSequence().getLine(line);
            
            for(Note note : ntList){
                lineDest.getNotes().remove(note);
                StateMachine.setSongModified(true);
                commandManager.execute(new RemoveNoteCommand(lineDest, note));

                if (lineDest.getNotes().isEmpty() && 0 <= line - model.getCurrentLine()
                        && line - model.getCurrentLine() < Values.NOTELINES_IN_THE_WINDOW) {
                    StaffVolumeEventHandler sveh = theStaff.getDisplayManager()
                            .getVolHandler(line - model.getCurrentLine());
                    sveh.setVolumeVisible(false);
                    commandManager.execute(new RemoveVolumeCommand(lineDest, lineDest.getVolume()));
                }
            }
            // idk why but redraw needs to be called every line or else weird
            // stuff happens (like some notes don't get added)
            theStaff.redraw();
        }
        
        clearSelection();
        commandManager.doRecord();
    }

    /**
     * Paste copied notes.
     * 
     * @param lineSrc The notes line to copy
     * @param lineDest The notes line where the copy will be received
     * @param line The line number
     */
    private void pasteNotes(NoteLine lineSrc, NoteLine lineDest, int line) {
    	for (Note note : lineSrc.getNotes()) {
            
            // see StaffInstrumentEventHandler's placeNote function
            Note theStaffNote = new Note(note);

            if (lineDest.getNotes().isEmpty()) {
                lineDest.setVolume(Values.getDefaultVolume());
                
                if (line - model.getCurrentLine() < Values.NOTELINES_IN_THE_WINDOW) {
                    StaffVolumeEventHandler sveh = theStaff.getDisplayManager()
                            .getVolHandler(line - model.getCurrentLine());
                    sveh.updateVolume();
                }

                commandManager.execute(new AddVolumeCommand(lineDest, Values.getDefaultVolume()));
            }

            if (!lineDest.getNotes().contains(theStaffNote)) {
                lineDest.getNotes().add(theStaffNote);
                StateMachine.setSongModified(true);
                commandManager.execute(new AddNoteCommand(lineDest, theStaffNote));
            }
        }
    }
    
    /**
     * Paste copied notes and volumes.
     * 
     * @param lineMoveTo starting line to paste data at
     */
    public void paste(int lineMoveTo) {
        Map<Integer, NoteLine> copiedData = theStaffClipboard.getCopiedData();
        
        for (Map.Entry<Integer, NoteLine> lineCopy : copiedData.entrySet()) {
            int line = lineMoveTo + lineCopy.getKey();
            
            NoteLine lineDest = theStaff.getSequence().getLine(line);
            NoteLine lineSrc = lineCopy.getValue();
            
            // Paste notes
            pasteNotes(lineSrc, lineDest, line);
            
            // Paste volume
            if (!ignoreVolumesFlag) {
                commandManager.execute(new RemoveVolumeCommand(lineDest, lineDest.getVolume()));
                lineDest.setVolume(lineSrc.getVolume());
                commandManager.execute(new AddVolumeCommand(lineDest, lineDest.getVolume()));
                
                if (line - model.getCurrentLine() < Values.NOTELINES_IN_THE_WINDOW) {
                    StaffVolumeEventHandler sveh = theStaff.getDisplayManager()
                            .getVolHandler(line - model.getCurrentLine());
                    sveh.updateVolume();
                }
                
                StateMachine.setSongModified(true);
            }
            
        }

        theStaff.redraw();
        commandManager.doRecord();
        StateMachine.setMaxLine(Math.max(theStaff.getSequence().getLength(), Values.DEFAULT_LINES_PER_SONG));
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
        for (int line = lineBegin; line <= lineEnd; line++) {
            NoteLine lineSrc = theStaff.getSequence().getLine(line);

            List<Note> ntList = lineSrc.getNotes();
            for (Note note : ntList) {
                if (positionBegin <= note.getVerticalPosition() && note.getVerticalPosition() <= positionEnd
                        && StateMachine.getFilteredNote(note.getInstrument().ordinal())) {
                    // store the copied note at the relative line
                    selectNote(line, note);
                    // store the staffnoteline's volume at the relative line
                    selectVolume(line, lineSrc.getVolume());
                    updateSelectionLineBegin(line);
                }
            }
        }
        
        theStaff.redraw(); // display visual effect
    }
    
    public void clearCopiedData() {
        theStaffClipboard.getCopiedData().clear();
        ignoreVolumesFlag = false;
    }

    /**
     * Unhighlight all notes and volumes and clear the selection map.
     */
    public void clearSelection() {
        //unhighlight notes
        Map<Integer, NoteLine> selection = theStaffClipboard.getSelection();
        for(NoteLine line : selection.values()) 
            for(Note note : line.getNotes())
                highlightNote(note, false);

        //unhighlight volumes
        theStaffClipboard.getHighlightedVolumes().clear();
        theStaffClipboard.getHighlightedVolumesRedrawer().changed(null, 0, model.getCurrentLine());

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
    public void copyNote(int line, Note note) {
        Note newNote = new Note(note);
        Map<Integer, NoteLine> copiedData = theStaffClipboard.getCopiedData();
        copiedData.computeIfAbsent(line, l -> new NoteLine()).getNotes().add(newNote);
    }
    
    /**
     * Select note. Add existing note into selection. Highlight note.
     * 
     * @param line
     *            where the note occurs
     * @param note
     *            that will be placed into selection
     */
    public void selectNote(int line, Note note) {
        Map<Integer, NoteLine> selection = theStaffClipboard.getSelection();
        selection.computeIfAbsent(line, l -> new NoteLine()).getNotes().add(note);
        highlightNote(note, true);
    }
    
    public void highlightNote(Note note, boolean highlight) {
        note.setSelected(highlight);
    }
    
    public void copyVolume(int line, int volume) {
        Map<Integer, NoteLine> copiedData = theStaffClipboard.getCopiedData();
        copiedData.computeIfAbsent(line, l -> new NoteLine()).setVolume(volume);
    }
    
    public void selectVolume(int line, int volume) {
        Map<Integer, NoteLine> selection = theStaffClipboard.getSelection();        
        selection.computeIfAbsent(line, l -> new NoteLine()).setVolume(volume);
        highlightVolume(line, true);
    }

    public void highlightVolume(int line, boolean highlight) {
        if(highlight) 
            theStaffClipboard.getHighlightedVolumes().add(line);
        else 
            theStaffClipboard.getHighlightedVolumes().remove(line);
        
        // trigger the ChangeListener that will set the highlight effect
        if (model.getCurrentLine() <= line
                && line < model.getCurrentLine() + Values.NOTELINES_IN_THE_WINDOW)
            theStaffClipboard.getHighlightedVolumesRedrawer().changed(null, 0, model.getCurrentLine());
    }
    
    public void selectNotesToggle(boolean selectNotes) {
        selectNotesFlag = selectNotes;
        if(selectNotesFlag) {
            //highlight notes
            for(NoteLine line : theStaffClipboard.getSelection().values()) 
                for(Note note : line.getNotes())
                    highlightNote(note, true);
        } else {
            //unhighlight notes
            for(NoteLine line : theStaffClipboard.getSelection().values()) 
                for(Note note : line.getNotes())
                    highlightNote(note, false);
        }
        theStaff.redraw();
    }
    
    public void selectVolumesToggle(boolean selectVolumes) {
        selectVolumesFlag = selectVolumes;
        if(selectVolumesFlag) {
            for(Integer line : theStaffClipboard.getSelection().keySet())
                highlightVolume(line, true);
        } else {
            //unhighlight volumes
            theStaffClipboard.getHighlightedVolumes().clear();
            theStaffClipboard.getHighlightedVolumesRedrawer().changed(null, 0, model.getCurrentLine());
        }
    }
    
    public boolean isSelectNotesOn() {
        return selectNotesFlag;
    }
    
    public boolean isSelectVolumesOn() {
        return selectVolumesFlag;
    }

    /**
     * Begins the first point for the clipboard's rubberband at the specified x
     * and y coords. Adds the rubberband to the rubberBandLayer so it actually
     * shows up.
     * 
     * @param x
     *            the first x coord
     * @param y
     *            the first y coord
     * @since v1.1.2
     */
    public void beginBand(double x, double y) {
        StaffRubberBand rubberBand = theStaffClipboard.getRubberBand();
        theStaffClipboard.getRubberBandLayer().getChildren().add(rubberBand);
        rubberBand.begin(x, y);
    }
    
    /**
     * Draws and resizes the rubberband from the first point from beginBand() to
     * the next point specified by x and y. beginBand() should be called before
     * this.
     * 
     * @param x
     *            the second x coord
     * @param y
     *            the second y coord
     * @since v1.1.2
     */
    public void resizeBand(double x, double y) {
        theStaffClipboard.getRubberBand().resizeBand(x, y);
    }   

    /**
     * Ends the band and selects all notes inside the region. Notes selected are
     * modified by the <code>StaffClipboardFilter</code>. resizeBand() should be
     * called before this.
     * 
     * @since v1.1.2
     */
    public void endBand() {
        StaffRubberBand rubberBand = theStaffClipboard.getRubberBand();
        rubberBand.end();
        theStaffClipboard.getRubberBandLayer().getChildren().remove(rubberBand);

        int lb = rubberBand.getLineBegin() + model.getCurrentLine();
        int pb = rubberBand.getPositionBegin();
        int le = rubberBand.getLineEnd() + model.getCurrentLine();
        int pe = rubberBand.getPositionEnd();
        select(lb, pb, le, pe);
    }
    
    /**
     * @return if rubberband is in the rubberbandlayer
     * @since v1.1.2
     */
    public boolean isRubberBandActive() {
        return theStaffClipboard.getRubberBandLayer().getChildren().contains(theStaffClipboard.getRubberBand());
    }
}
