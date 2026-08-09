package backend.editing.commands;

import backend.editing.SMPCommand;
import backend.songs.Note;
import backend.songs.NoteLine;

public class RemoveNoteCommand implements SMPCommand {

    private NoteLine line;
    private Note note;
    
    public RemoveNoteCommand(NoteLine line, Note note) {
        this.line = line;
        this.note = note;
    }
    
    @Override
    public void undo() {
        line.getNotes().add(note);
    }
    
    @Override
    public void redo() {
        line.getNotes().remove(note);
    }

}
