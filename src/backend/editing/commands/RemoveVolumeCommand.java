package backend.editing.commands;

import backend.editing.CommandInterface;
import backend.songs.NoteLine;

public class RemoveVolumeCommand implements CommandInterface {

    NoteLine theLine;
    int theOldVolume;
    
    public RemoveVolumeCommand(NoteLine line, int oldVolume) {
        theLine = line;
        theOldVolume = oldVolume;
    }
    
    @Override
    public void redo() {
        //do nothing, AddVolumeCommand will handle this
    }

    @Override
    public void undo() {
        theLine.setVolume(theOldVolume);
    }

}
