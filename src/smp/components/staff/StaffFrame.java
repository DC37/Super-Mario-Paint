package smp.components.staff;

import smp.ImageIndex;
import smp.ImageLoader;
import smp.components.general.ImagePanel;

public class StaffFrame extends ImagePanel {

	/**
	 * Generated serial ID.
	 */
	private static final long serialVersionUID = -8491811586182536580L;
	
	public StaffFrame(int x, int y) {
		super(x, y);
		background = ImageLoader.getSprite(ImageIndex.STAFF_FRAME);
		this.setOpaque(false);
	}
	


}
