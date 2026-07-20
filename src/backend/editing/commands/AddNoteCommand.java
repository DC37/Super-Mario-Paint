package backend.editing.commands;

import backend.editing.CommandInterface;
import backend.songs.Note;
import backend.songs.NoteLine;

public class AddNoteCommand implements CommandInterface {

    private NoteLine theLine;
    private Note theNote;

    public AddNoteCommand(NoteLine line, Note note) {
        theLine = line;
        theNote = note;
    }
    
    @Override
    public void redo() {
        theLine.getNotes().add(theNote);
    }

    @Override
    public void undo() {
        theLine.getNotes().remove(theNote);
    }

}
