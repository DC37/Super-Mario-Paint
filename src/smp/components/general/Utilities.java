package smp.components.general;

import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * A somewhat useful utilities class for images and such. Not
 * so much use right now yet, but will gain some functionality
 * eventually.
 * @author RehdBlob
 * @since 2012.08.20
 */
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

            @Override
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

    /**
     * Opens a file dialog that people can choose a file from.
     * This is a Java Swing version.
     * @return A File reference that the user has chosen.
     * @throws IOException
     */
    public static File openFileDialog()
            throws IOException, NullPointerException {
        JFileChooser fd = new JFileChooser(".");
        FileFilter filter = new FileNameExtensionFilter(
                "Java Source File (*.java)", "java");
        fd.addChoosableFileFilter(filter);
        fd.setFileFilter(filter);
        int returnVal = fd.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            return fd.getSelectedFile();
        } else if (returnVal == JFileChooser.CANCEL_OPTION) {
            throw new NullPointerException();
        } else {
            System.out.print("Error opening file!");
            throw new IOException();
        }
    }

}
