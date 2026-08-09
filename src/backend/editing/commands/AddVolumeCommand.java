package backend.editing.commands;

import backend.editing.SMPCommand;
import backend.songs.NoteLine;

public class AddVolumeCommand implements SMPCommand {

    NoteLine line;
    int newVolume;
    
    public AddVolumeCommand(NoteLine line, int newVolume) {
        this.line = line;
        this.newVolume = newVolume;
    }
    
    @Override
    public void undo() {
        //do nothing, RemoveVolumeCommand will handle this
    }
    
    @Override
    public void redo() {
        line.setVolume(newVolume);
    }

}
