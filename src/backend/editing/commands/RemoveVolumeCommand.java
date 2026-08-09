package backend.editing.commands;

import backend.editing.SMPCommand;
import backend.songs.NoteLine;

public class RemoveVolumeCommand implements SMPCommand {

    NoteLine line;
    int oldVolume;
    
    public RemoveVolumeCommand(NoteLine line, int oldVolume) {
        this.line = line;
        this.oldVolume = oldVolume;
    }
    
    @Override
    public void undo() {
        line.setVolume(oldVolume);
    }
    
    @Override
    public void redo() {
        //do nothing, AddVolumeCommand will handle this
    }

}
