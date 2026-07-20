package backend.editing.commands;

import backend.editing.CommandInterface;
import backend.songs.Note;
import backend.songs.NoteLine;

public class RemoveNoteCommand implements CommandInterface {

    private NoteLine theLine;
    private Note theNote;
    
    public RemoveNoteCommand(NoteLine line, Note note) {
        theLine = line;
        theNote = note;
    }
    
    @Override
    public void redo() {
        theLine.getNotes().remove(theNote);
    }

    @Override
    public void undo() {
        theLine.getNotes().add(theNote);
    }

}
