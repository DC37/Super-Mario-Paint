package smp.components.general;

/**
 * Coordinates class for holding x, y 
 * coordinates for on-screen components.
 * @author RehdBlob
 * @since 2012.08.14
 */
public class Coordinates {

	private int x;
	private int y;
	
	public Coordinates(int xVal, int yVal) {
		setX(xVal);
		setY(yVal);
	}

	public int getX() {
		return x;
	}

	public void setX(int xVal) {
		x = xVal;
	}

	public int getY() {
		return y;
	}

	public void setY(int yVal) {
		y = yVal;
	}
	
	
	
}
