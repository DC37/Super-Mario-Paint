package me.dc37.smp.controllers;

import java.util.List;

import backend.songs.Song;
import javafx.concurrent.Task;
import me.dc37.smp.models.ResourceModel;

public class SoundsetSaveTask extends Task<Void> {

    private final ResourceModel model;
    private final List<Song> arrangementSongs;
    private final String songName;
    
    public SoundsetSaveTask(ResourceModel model,
            String songName, List<Song> arrangementSongs) {
        
        this.model = model;
        this.songName = songName;
        this.arrangementSongs = arrangementSongs;
    }
    
    @Override
    protected Void call() throws Exception {
        for (Song song : arrangementSongs) {
            if (song.getTitle().equals(songName)) {
                model.getSoundPlayer().storeInCache();
                break;
            }
        }
        
        return null;
    }

}
