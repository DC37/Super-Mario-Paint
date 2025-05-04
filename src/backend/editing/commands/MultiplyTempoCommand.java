package backend.editing.commands;

import backend.editing.CommandInterface;
import backend.songs.StaffSequence;
import backend.songs.TimeSignature;
import gui.Staff;
import gui.StateMachine;

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
	    seq.expand(theMultiplyAmount);
	    seq.setTempo(newTempo);
        StateMachine.setTempo(newTempo);
        StateMachine.setMaxLine(seq.getLength());
        theStaff.setTimeSignature(newTimesig);
	}

	@Override
	public void undo() {
	    StaffSequence seq = theStaff.getSequence();
	    seq.retract(theMultiplyAmount);
	    seq.setTempo(previousTempo);
        StateMachine.setTempo(previousTempo);
        StateMachine.setMaxLine(seq.getLength());
        theStaff.setTimeSignature(previousTimesig);
	}

}
