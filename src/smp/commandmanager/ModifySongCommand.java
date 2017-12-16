package smp.commandmanager;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import smp.components.Values;
import smp.components.staff.Staff;
import smp.components.staff.StaffVolumeEventHandler;
import smp.components.staff.sequences.StaffNote;
import smp.components.staff.sequences.StaffNoteLine;

@Deprecated
public class ModifySongCommand implements CommandInterface {

	private Staff theStaff;
	
	//Note is the key because note is unique, line is value
	private Map<StaffNote, StaffNoteLine> addedNotes;
	private Map<StaffNote, StaffNoteLine> removedNotes;

	/*
	 * past volume is "removed", new volume is "added" when changing volume
	 */
	//Line is the key because line is unique, volume is value
	private Map<StaffNoteLine, Integer> addedVolumes;
	private Map<StaffNoteLine, Integer> removedVolumes;

	public void undo() {
		System.out.println("===modifySongCommand undo===");
		if (addedNotes != null) {
			for (Entry<StaffNote, StaffNoteLine> entry : addedNotes.entrySet()) {
				entry.getValue().remove(entry.getKey());
			}
			System.out.println("removed new notes...");
		}
		if (removedNotes != null) {
			for (Entry<StaffNote, StaffNoteLine> entry : removedNotes.entrySet()) {
				entry.getValue().add(entry.getKey());
			}
			System.out.println("added old notes...");
		}
		if (addedVolumes != null) {
			for (Entry<StaffNoteLine, Integer> entry : addedVolumes.entrySet()) {
				entry.getKey().setVolume(Values.DEFAULT_VELOCITY);
			}
//			System.out.println("reset volumes...");
		}
		if (removedVolumes != null) {
			for (Entry<StaffNoteLine, Integer> entry : removedVolumes.entrySet()) {
				entry.getKey().setVolume(entry.getValue());
			}
			System.out.println("overwrote volumes to prev values...");
		}
		
		theStaff.redraw();
		//update volume display
		if(addedVolumes != null || removedVolumes != null)
			for(int i = 0; i < Values.NOTELINES_IN_THE_WINDOW; i++){
				StaffVolumeEventHandler sveh = theStaff.getNoteMatrix().getVolHandler(i);
				sveh.updateVolume();
			}
	}

	public void redo() {

		System.out.println("===modifySongCommand redo===");
		if (removedNotes != null) {
			for (Entry<StaffNote, StaffNoteLine> entry : removedNotes.entrySet()) {
				entry.getValue().remove(entry.getKey());
			}
			System.out.println("removed notes...");
		}
		if (addedNotes != null) {
			for (Entry<StaffNote, StaffNoteLine> entry : addedNotes.entrySet()) {
				entry.getValue().add(entry.getKey());
			}
			System.out.println("added new notes...");
		}
		if (removedVolumes != null) {
			for (Entry<StaffNoteLine, Integer> entry : removedVolumes.entrySet()) {
				entry.getKey().setVolume(Values.DEFAULT_VELOCITY);
			}
//			System.out.println("reset volumes...");
		}
		if (addedVolumes != null) {
			for (Entry<StaffNoteLine, Integer> entry : addedVolumes.entrySet()) {
				entry.getKey().setVolume(entry.getValue());
			}
			System.out.println("overwrote volumes to new values...");
		}

		theStaff.redraw();
		//update volume display
		if(addedVolumes != null || removedVolumes != null)
			for(int i = 0; i < Values.NOTELINES_IN_THE_WINDOW; i++){
				StaffVolumeEventHandler sveh = theStaff.getNoteMatrix().getVolHandler(i);
				sveh.updateVolume();
			}
	}

	public ModifySongCommand(Staff st) {
		theStaff = st;
	}
	
	public void addNote(StaffNoteLine line, StaffNote note) {
		if(addedNotes == null)
			addedNotes = new HashMap<>();
		
		addedNotes.putIfAbsent(note, line);
	}

	public void removeNote(StaffNoteLine line, StaffNote note) {
		if(removedNotes == null)
			removedNotes = new HashMap<>();
		
		removedNotes.putIfAbsent(note, line);
	}

	public void addVolume(StaffNoteLine line, int newVolume) {
		if(addedVolumes == null)
			addedVolumes = new HashMap<>();
		
		addedVolumes.putIfAbsent(line, newVolume);
	}

	public void removeVolume(StaffNoteLine line, int oldVolume) {
		if(removedVolumes == null)
			removedVolumes = new HashMap<>();
		
		removedVolumes.putIfAbsent(line, oldVolume);
	}
}
