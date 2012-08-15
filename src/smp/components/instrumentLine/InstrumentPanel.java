package smp.components.instrumentLine;

import smp.ImageIndex;
import smp.ImageLoader;
import smp.components.general.ImagePanel;
import smp.components.staff.sounds.Player;


/**
 * This is a panel that holds instruments, to be placed
 * above the staff.
 * @author RehdBlob
 * @since 2012.08.13
 */
public class InstrumentPanel extends ImagePanel {
	
	/**
	 * Generated serial ID.
	 */
	private static final long serialVersionUID = -1917709647981152606L;
	
	private Player soundPlayer;
	
	public InstrumentPanel(int x, int y) {
		super(x, y);
		background = ImageLoader.getSprite(ImageIndex.INST_LINE);
		setOpaque(false);
	}

}
