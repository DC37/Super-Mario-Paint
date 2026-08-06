package me.dc37.smp.domain;

import java.io.File;
import java.util.List;

import backend.songs.Song;

public class SaveSongParameters {

    private final File file;
    private final Song song;
    private final List<Song> arrangementSongs;
    private final String songName;
    
    public SaveSongParameters(
            File file, Song song,
            List<Song> arrangementSongs,
            String songName) {
        
        this.file = file;
        this.song = song;
        this.arrangementSongs = arrangementSongs;
        this.songName = songName;
    }
    
    public File getFile() {
        return file;
    }
    
    public Song getSong() {
        return song;
    }
    
    public List<Song> getArrangementSongs() {
        return arrangementSongs;
    }
    
    public String getSongName() {
        return songName;
    }
    
}
