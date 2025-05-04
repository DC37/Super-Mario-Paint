package smp.components.staff.sequences;

import java.io.Serializable;

import smp.components.staff.Staff;

/**
 * Some sort of event on the Super Mario Paint staff.
 * @author RehdBlob
 * @since 2012.09.24
 */
public interface StaffEvent extends Serializable {

    /**
     * Tells which line this event occurs at.
     * @return An integer value that denotes the location of the
     * event on the staff.
     */
    public int getLineNum();

    /**
     * Tells which measure this event occurs at.
     * @return An integer value that tells which measure that this
     * event occurs at.
     */
    public int getMeasureNum();

    /**
     * Tells which beat this event occurs on within a measure.
     * @return An integer value that denotes the location of this
     * event within a measure.
     */
    public int getMeasureLineNum();
    
    
    /**
     * Override to do something in the event. 
     */
    public void doEvent(Staff s);
}
