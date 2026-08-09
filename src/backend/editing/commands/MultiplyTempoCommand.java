package backend.editing.commands;

import backend.editing.SMPCommand;
import backend.songs.NoteLine;
import backend.songs.Song;
import backend.songs.TimeSignature;
import gui.Staff;
import gui.StateMachine;
import gui.Values;

public class MultiplyTempoCommand implements SMPCommand {

    Staff staff;
    int multiplyAmount;
    double oldTempo;
    double newTempo;
    TimeSignature oldTimeSig;
    TimeSignature newTimeSig;
    
    public MultiplyTempoCommand(Staff staff, int multiplyAmount,
    		double previousTempo, double newTempo,
    		TimeSignature previousTimesig, TimeSignature newTimesig) {
    	
        this.staff = staff;
        this.multiplyAmount = multiplyAmount;
        this.oldTempo = previousTempo;
        this.newTempo = newTempo;
        this.oldTimeSig = previousTimesig;
        this.newTimeSig = newTimesig;
    }
    
    @Override
    public void redo() {
        Song song = staff.getSequence();
        expand(song, multiplyAmount);
        song.setTempo(newTempo);
        StateMachine.setTempo(newTempo);
        StateMachine.setMaxLine(Math.max(song.getLength(), Values.DEFAULT_LINES_PER_SONG));
        staff.setTimeSignature(newTimeSig);
    }

    @Override
    public void undo() {
        Song song = staff.getSequence();
        retract(song, multiplyAmount);
        song.setTempo(oldTempo);
        StateMachine.setTempo(oldTempo);
        StateMachine.setMaxLine(Math.max(song.getLength(), Values.DEFAULT_LINES_PER_SONG));
        staff.setTimeSignature(oldTimeSig);
    }
    
    public void expand(Song seq, int n) {
        if (n < 2)
            return;
        
        int sz = seq.getLength();
        for (int i = sz; i > 0; i--) {
            moveLine(seq, i, n * i);
        }
    }
    
    public void retract(Song seq, int n) throws IllegalArgumentException {
        if (n < 2)
            return;
        
        int sz = seq.getLength();
        for (int i = 1; i < sz; i++) {
            moveLine(seq, n * i, i);
        }
    }
    
    private void moveLine(Song seq, int from, int to) {
        NoteLine lineFrom = seq.getLine(from);
        NoteLine lineTo = seq.getLine(to);
        lineTo.getNotes().clear();
        lineTo.getNotes().addAll(lineFrom.getNotes());
        lineFrom.getNotes().clear();
    }

}
