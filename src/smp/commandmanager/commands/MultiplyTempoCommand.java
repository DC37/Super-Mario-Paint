package smp.commandmanager.commands;

import smp.commandmanager.CommandInterface;
import smp.components.staff.Staff;
import smp.components.staff.sequences.StaffSequence;
import smp.stateMachine.StateMachine;

public class MultiplyTempoCommand implements CommandInterface {

	Staff theStaff;
	int theMultiplyAmount;
	double previousTempo;
	double newTempo;
	
	public MultiplyTempoCommand(Staff staff, int num, double previousTempo, double newTempo) {
		theStaff = staff;
		this.theMultiplyAmount = num;
		this.previousTempo = previousTempo;
		this.newTempo = newTempo;
	}
	
	@Override
	public void redo() {
	    StaffSequence seq = theStaff.getSequence();
	    seq.expand(theMultiplyAmount);
	    seq.setTempo(newTempo);
        StateMachine.setTempo(newTempo);
        StateMachine.setMaxLine(seq.getLength());
	}

	@Override
	public void undo() {
	    StaffSequence seq = theStaff.getSequence();
	    seq.retract(theMultiplyAmount);
	    seq.setTempo(previousTempo);
        StateMachine.setTempo(previousTempo);
        StateMachine.setMaxLine(seq.getLength());
	}

}
