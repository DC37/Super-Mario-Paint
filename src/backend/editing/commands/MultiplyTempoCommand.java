package backend.editing.commands;

import backend.editing.CommandInterface;
import backend.songs.StaffNoteLine;
import backend.songs.StaffSequence;
import backend.songs.TimeSignature;
import gui.Staff;
import gui.StateMachine;
import gui.Values;

public class MultiplyTempoCommand implements CommandInterface {

    Staff theStaff;
    int theMultiplyAmount;
    double previousTempo;
    double newTempo;
    TimeSignature previousTimesig;
    TimeSignature newTimesig;
    
    public MultiplyTempoCommand(Staff staff, int num, double previousTempo, double newTempo, TimeSignature previousTimesig, TimeSignature newTimesig) {
        theStaff = staff;
        this.theMultiplyAmount = num;
        this.previousTempo = previousTempo;
        this.newTempo = newTempo;
        this.previousTimesig = previousTimesig;
        this.newTimesig = newTimesig;
    }
    
    @Override
    public void redo() {
        StaffSequence seq = theStaff.getSequence();
        expand(seq, theMultiplyAmount);
        seq.setTempo(newTempo);
        StateMachine.setTempo(newTempo);
        StateMachine.setMaxLine(Math.max(seq.getLength(), Values.DEFAULT_LINES_PER_SONG));
        theStaff.setTimeSignature(newTimesig);
    }

    @Override
    public void undo() {
        StaffSequence seq = theStaff.getSequence();
        retract(seq, theMultiplyAmount);
        seq.setTempo(previousTempo);
        StateMachine.setTempo(previousTempo);
        StateMachine.setMaxLine(Math.max(seq.getLength(), Values.DEFAULT_LINES_PER_SONG));
        theStaff.setTimeSignature(previousTimesig);
    }
    
    public void expand(StaffSequence seq, int n) {
        if (n < 2)
            return;
        
        int sz = seq.getLength();
        for (int i = sz; i > 0; i--) {
            moveLine(seq, i, n * i);
        }
    }
    
    public void retract(StaffSequence seq, int n) throws IllegalArgumentException {
        if (n < 2)
            return;
        
        int sz = seq.getLength();
        for (int i = 1; i < sz; i++) {
            moveLine(seq, n * i, i);
        }
    }
    
    private void moveLine(StaffSequence seq, int from, int to) {
        StaffNoteLine lineFrom = seq.getLine(from);
        StaffNoteLine lineTo = seq.getLine(to);
        lineTo.getNotes().clear();
        lineTo.getNotes().addAll(lineFrom.getNotes());
        lineFrom.getNotes().clear();
    }

}
