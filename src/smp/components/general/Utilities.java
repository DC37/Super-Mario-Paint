package smp.components.general;

import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;

/**
 * A somewhat useful utilities class for images and such. Not
 * so much use right now yet, but will gain some functionality
 * eventually.
 * @author RehdBlob
 * @since 2012.08.20
 */
@SuppressWarnings("unused")
public class Utilities {

	/**
	 * From StackOverflow - "PhiLo" and "corgrath" made this.
	 * @param im An image to have its background be made transparent.
	 * @param color The color to be made transparent.
	 * @return A BufferedImage that now has all of <code>color</code>
	 * removed.
	 */
	public static BufferedImage makeColorTransparent(BufferedImage im, 
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
	
	/**
	 * From StackOverflow - "PhiLo" and "corgrath" made this.
	 * @param image An Image to be converted to a BufferedImage
	 * @return A BufferedImage from an Image.
	 */
	private static BufferedImage imageToBufferedImage(Image image) {

        BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), 
        		image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        /*Graphics2D g2 = bufferedImage.createGraphics();
        g2.drawImage(image, 0, 0, null);
        g2.dispose();*/

        return bufferedImage;

    }
	
}
