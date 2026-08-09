package gui.tasks;

import java.util.List;

import backend.songs.Song;
import backend.sound.SoundPlayer;
import javafx.concurrent.Task;

public class SoundsetSaveTask extends Task<Void> {

	private final SoundPlayer soundPlayer;
	private final List<Song> arrangementSongs;
    private final String songName;
    
    public SoundsetSaveTask(SoundPlayer soundPlayer,
    		String songName, List<Song> arrangementSongs) {
    	
    	this.soundPlayer = soundPlayer;
    	this.songName = songName;
    	this.arrangementSongs = arrangementSongs;
    }
	
	@Override
	protected Void call() throws Exception {
		for (Song song : arrangementSongs) {
            if (song.getTitle().equals(songName)) {
                soundPlayer.storeInCache();
                break;
            }
        }
        
        return null;
	}
	
}
