package gui.components.staff;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * Immutable object providing the representation for a coordinate on the staff: column, row, and depth
 * For given maximum values of row and depth, we provide converters to and from int.
 */
public class StaffNoteCoordinate {

	private Supplier<Integer> fnGetHeight;
	private Supplier<Integer> fnGetDepth;
	
	public final int col;
    public final int row;
    public final int dep; // special value -1 refers to the layer for silhouettes (dep is irrelevant)
    
    public StaffNoteCoordinate(StaffDisplayManager sdm, int col, int row, int dep) {
    	this.fnGetHeight = () -> sdm.height;
    	this.fnGetDepth = () -> sdm.depth;
    	
        this.col = col;
        this.row = row;
        this.dep = dep;
    }
    
    public StaffNoteCoordinate(StaffNoteCoordinate oth) {
    	this.fnGetHeight = oth.fnGetHeight;
    	this.fnGetDepth = oth.fnGetDepth;
    	
        this.col = oth.col;
        this.row = oth.row;
        this.dep = oth.dep;
    }
    
    @Override
    public boolean equals(Object obj) {
    	if (!(obj instanceof StaffNoteCoordinate))
    		return false;
    	
    	StaffNoteCoordinate other = (StaffNoteCoordinate) obj;
    	
    	return this.col == other.col && this.row == other.row && this.dep == other.dep;
    }
    
    @Override
    public int hashCode() {
    	return Objects.hash(col, row, dep);
    }
    
    public int lin() {
    	int height = fnGetHeight.get();
    	int depth = fnGetDepth.get();
    	
        return (dep == -1) ? (height * col) + row : (depth * ((height * col) + row)) + dep;
    }
	
}
