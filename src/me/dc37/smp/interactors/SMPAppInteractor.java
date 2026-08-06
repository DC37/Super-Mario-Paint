package me.dc37.smp.interactors;

import java.io.File;

import backend.songs.Song;
import me.dc37.smp.domain.FileService;
import me.dc37.smp.models.SMPAppModel;

public class SMPAppInteractor {

	private final SMPAppModel model;
	
    public SMPAppInteractor(SMPAppModel model) {
    	this.model = model;
    }
    
    public boolean saveSong(File f, Song song) {
        return FileService.trySaveSong(f, song);
    }
	
}
