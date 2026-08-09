package backend.editing.commands;

import backend.editing.SMPCommand;
import backend.songs.Note;
import backend.songs.NoteLine;

public class AddNoteCommand implements SMPCommand {

    private NoteLine line;
    private Note note;

    public AddNoteCommand(NoteLine line, Note note) {
        this.line = line;
        this.note = note;
    }
    
    @Override
    public void undo() {
        line.getNotes().remove(note);
    }
    
    @Override
    public void redo() {
        line.getNotes().add(note);
    }

}
