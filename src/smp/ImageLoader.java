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
	
	private static Hashtable<ImageIndex, BufferedImage> sprites;
	private String extension = ".bmp";
	private String path = "./sprites/";
	
	public ImageLoader() {
		sprites = new Hashtable<ImageIndex, BufferedImage>();
	}

	/**
	 * Loads all of the image files that will be used in drawing
	 * the main window and all of the buttons of Super Mario Paint.
	 */
	@Override
	public void run() {
		File f;
		try {
			ImageIndex [] ind = ImageIndex.values();
			for (ImageIndex i : ind) {
				f = new File(path + i.toString() + extension);
				sprites.put(i, ImageIO.read(f));
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
	        throws NullPointerException {
		return sprites.get(index);
	}
	
}
