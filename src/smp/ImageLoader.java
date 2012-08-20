package smp;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;
import java.io.File;
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
	
	/**
	 * Contains references to all the loaded sprites.
	 */
	private static Hashtable<ImageIndex, BufferedImage> sprites;
	
	/**
	 * Red value of the "transparent" background color.
	 */
	private static final int R = 255;
	
	/**
	 * Green value of the "transparent" background color.
	 */
	private static final int G = 255;
	
	/**
	 * Blue value of the "transparent" background color.
	 */
	private static final int B = 255;
	
	/**
	 * The amount of loading that the imageLoader has done,
	 * anywhere between 0 to 1.
	 */
	private static double loadStatus = 0.0;
	
	private String extension = ".bmp";
	private String path = "./sprites/";
	private Color background;
	
	/**
	 * Initializes the sprites hashtable and sets the color that is to be
	 * made transparent (the background) to the R, G, B values defined at
	 * the top of this class.
	 */
	public ImageLoader() {
		sprites = new Hashtable<ImageIndex, BufferedImage>();
		background = new Color(R, G, B);
	}

	/**
	 * Loads all of the image files that will be used in drawing
	 * the main window and all of the buttons of Super Mario Paint.
	 * There will be a future implementation of a splash screen while this
	 * all takes place.
	 */
	@Override
	public void run() {
		File f;
		try {
			ImageIndex [] ind = ImageIndex.values();
			for (ImageIndex i : ind) {
				setLoadStatus((i.ordinal() + 1) / ind.length);
				f = new File(path + i.toString() + extension);
				BufferedImage temp = ImageIO.read(f);
				temp = makeColorTransparent(temp, background);
				sprites.put(i, temp);
			}
		} catch (IOException e) {
			// If the images fail to load, don't start the program!
			// this.showFailedDialog();
			e.printStackTrace();
			System.exit(0);
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
	
	
	/**
	 * From StackOverflow - "PhiLo" and "corgrath" made this.
	 * @param image An Image to be converted to a BufferedImage
	 * @return A BufferedImage from an Image.
	 */
	private static BufferedImage imageToBufferedImage(Image image) {

        BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), 
        		image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = bufferedImage.createGraphics();
        g2.drawImage(image, 0, 0, null);
        g2.dispose();

        return bufferedImage;

    }
	
	/**
	 * From StackOverflow - "PhiLo" and "corgrath" made this.
	 * @param im An image to have its background be made transparent.
	 * @param color The color to be made transparent.
	 * @return A BufferedImage that now has all of <code>color</code>
	 * removed.
	 */
	private static BufferedImage makeColorTransparent(BufferedImage im, 
			final Color color) {
        ImageFilter filter = new RGBImageFilter() {

                // the color we are looking for... Alpha bits are set to opaque
                public int markerRGB = color.getRGB() | 0xFF000000;

                public final int filterRGB(int x, int y, int rgb) {
                        if ((rgb | 0xFF000000) == markerRGB) {
                                // Mark the alpha bits as zero - transparent
                                return 0x00FFFFFF & rgb;
                        } else {
                                // nothing to do
                                return rgb;
                        }
                }
        };

        ImageProducer ip = new FilteredImageSource(im.getSource(), filter);
        return imageToBufferedImage(
        		Toolkit.getDefaultToolkit().createImage(ip));
    }
	
	public double getLoadStatus() {
		return loadStatus;
	}
	
	public void setLoadStatus(double d) {
		if (d >= 0 && d <= 1)
			loadStatus = d;
	}

}
