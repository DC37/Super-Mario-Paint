package smp.components.topPanel.instrumentLine;

import smp.ImageIndex;
import smp.components.general.ImageButton;


/**
 * This is a panel that holds instruments, to be placed
 * above the staff.
 * @author RehdBlob
 * @since 2012.08.13
 */
public class InstrumentPanel extends ImageButton {
	
	/**
	 * Generated serial ID.
	 */
	private static final long serialVersionUID = -1917709647981152606L;
	
	public InstrumentPanel(int x, int y) {
		super(ImageIndex.INST_LINE);
	}

}
