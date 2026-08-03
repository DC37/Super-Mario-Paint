package me.dc37.smp.models;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;

import backend.sound.SoundPlayer;
import gui.loaders.ImageIndex;
import gui.loaders.SMPCursorType;
import javafx.scene.ImageCursor;
import javafx.scene.image.Image;

public class SMPAppModel {

	private static SMPAppModel instance; 
	
	public static SMPAppModel getInstance() {
		if (instance == null) {
			instance = new SMPAppModel();
		}
		
		return instance;
	}
	
	private Image headerIcon;
	
	private SoundPlayer soundPlayer;
	private final Map<ImageIndex, Image> icons = new EnumMap<>(ImageIndex.class);
	private final Map<SMPCursorType, ImageCursor> cursors = new EnumMap<>(SMPCursorType.class);
	
	private SMPAppModel() {}
	
	public Image getHeaderIcon() {
		return headerIcon;
	}
	
	public void setHeaderIcon(Image headerIcon) {
		this.headerIcon = headerIcon;
	}
	
	public SoundPlayer getSoundPlayer() {
		return soundPlayer;
	}
	
	public void setSoundPlayer(SoundPlayer soundPlayer) {
		this.soundPlayer = soundPlayer;
	}
	
	public Map<ImageIndex, Image> getIcons() {
		return icons;
	}
	
	public void setIcons(Map<ImageIndex, Image> icons) {
		this.icons.clear();
		this.icons.putAll(icons);
	}
	
	public Optional<Image> getIcon(ImageIndex imgIdx) {
		return Optional.ofNullable(icons.get(imgIdx));
	}
	
	public Optional<ImageCursor> getCursor(SMPCursorType type) {
		return Optional.ofNullable(cursors.get(type));
	}
	
	public void addCursor(SMPCursorType type, ImageCursor image) {
		cursors.put(type, image);
	}
	
}
