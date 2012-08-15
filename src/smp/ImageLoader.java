package smp;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Hashtable;

import javax.imageio.ImageIO;

/**
 * A class that loads all the necessary images for the 
 * program to function when the program first starts.
 * @author RehdBlob
 * @since 2012.08.14
 */
public class ImageLoader implements Runnable {

	private static final int NUM_SPRITES = 0;
	
	private static Hashtable<ImageIndex, BufferedImage> sprites;
	private String extension = ".bmp";
	
	public ImageLoader() {
		sprites = new Hashtable<ImageIndex, BufferedImage>();
	}

	@Override
	public void run() {
		File f;
		try {
			ImageIndex [] ind = ImageIndex.values();
			for (int i = 0; i < NUM_SPRITES; i++) {
				f = new File("SPR_" + i + extension);
				sprites.put(ind[i], ImageIO.read(f));
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param index The index at which to access an image
	 * @return A BufferedImage based on the image request.
	 */
	public static BufferedImage getSprite(ImageIndex index) 
			throws IndexOutOfBoundsException {
		return sprites.get(index);
	}
	
}
