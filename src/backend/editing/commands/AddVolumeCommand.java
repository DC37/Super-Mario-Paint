package backend.editing.commands;

import backend.editing.CommandInterface;
import backend.songs.NoteLine;

public class AddVolumeCommand implements CommandInterface {

    NoteLine theLine;
    int theNewVolume;
    
    public AddVolumeCommand(NoteLine line, int newVolume) {
        theLine = line;
        theNewVolume = newVolume;
    }
    
    @Override
    public void redo() {
        theLine.setVolume(theNewVolume);
    }

    @Override
    public void undo() {
        //do nothing, RemoveVolumeCommand will handle this
    }

}
