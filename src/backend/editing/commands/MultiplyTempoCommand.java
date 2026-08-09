package backend.editing.commands;

import backend.editing.CommandInterface;
import backend.songs.NoteLine;
import backend.songs.Song;
import backend.songs.TimeSignature;
import gui.Staff;
import gui.Values;
import me.dc37.smp.models.SMPAppModel;

public class MultiplyTempoCommand implements CommandInterface {

    private final SMPAppModel model;
    Staff theStaff;
    int theMultiplyAmount;
    double previousTempo;
    double newTempo;
    TimeSignature previousTimesig;
    TimeSignature newTimesig;
    
    public MultiplyTempoCommand(
            SMPAppModel model,
            Staff staff, int num,
            double previousTempo, double newTempo,
            TimeSignature previousTimesig, TimeSignature newTimesig) {
        
        this.model = model;
        theStaff = staff;
        this.theMultiplyAmount = num;
        this.previousTempo = previousTempo;
        this.newTempo = newTempo;
        this.previousTimesig = previousTimesig;
        this.newTimesig = newTimesig;
    }
    
    @Override
    public void redo() {
        Song seq = theStaff.getSequence();
        expand(seq, theMultiplyAmount);
        seq.setTempo(newTempo);
        model.setTempo(newTempo);
        model.setMaxLine(Math.max(seq.getLength(), Values.DEFAULT_LINES_PER_SONG));
        theStaff.setTimeSignature(newTimesig);
    }

    @Override
    public void undo() {
        Song seq = theStaff.getSequence();
        retract(seq, theMultiplyAmount);
        seq.setTempo(previousTempo);
        model.setTempo(previousTempo);
        model.setMaxLine(Math.max(seq.getLength(), Values.DEFAULT_LINES_PER_SONG));
        theStaff.setTimeSignature(previousTimesig);
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
