package smp;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * A class that loads all the necessary images for the 
 * program to function when the program first starts.
 * @author RehdBlob
 * @since 2012.08.14
 */
public class ImageLoader implements Runnable {

	private ArrayList<BufferedImage> sprites = new ArrayList<BufferedImage>();
	private boolean isReady;
	
	public ImageLoader() {
		isReady = false;
	}

	public ArrayList<BufferedImage> getSprites() {
		return sprites;
	}

	public void setSprites(ArrayList<BufferedImage> sp) {
		sprites = sp;
	}

	@Override
	public void run() {
		isReady = true;
	}
	
	/**
	 * 
	 * @param index The index at which to access an image
	 * @return A BufferedImage based on the image request.
	 */
	public static BufferedImage getImage(int index) {
		return null;
	}
	
}
