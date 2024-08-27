package smp.commandmanager.commands;

import java.util.ArrayList;

import smp.commandmanager.CommandInterface;
import smp.components.Values;
import smp.components.staff.Staff;
import smp.components.staff.sequences.StaffNoteLine;
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
		ArrayList<StaffNoteLine> s = theStaff.getSequence().getTheLines();
        ArrayList<StaffNoteLine> n = new ArrayList<StaffNoteLine>();
        for (int i = 0; i < s.size(); i++) {
            n.add(s.get(i));
            for (int j = 0; j < theMultiplyAmount - 1; j++)
                n.add(new StaffNoteLine());
        }
        s.clear();
        s.addAll(n);
        StateMachine.setTempo(newTempo);
        theStaff.getControlPanel().getScrollbar()
                .setMax(s.size() - Values.NOTELINES_IN_THE_WINDOW);
	}

	@Override
	public void undo() {
		ArrayList<StaffNoteLine> s = theStaff.getSequence().getTheLines();
        ArrayList<StaffNoteLine> n = new ArrayList<StaffNoteLine>();
		for (int i = 0; i < s.size(); i+= theMultiplyAmount) {
			n.add(s.get(i));
		}
        s.clear();
        s.addAll(n);
        StateMachine.setTempo(previousTempo);
	    theStaff.getControlPanel().getScrollbar()
	            .setMax(s.size() - Values.NOTELINES_IN_THE_WINDOW);
	}

}
