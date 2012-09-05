package smp;

import java.awt.image.BufferedImage;
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
     * Contains references to all the loaded sprites in Java AWT BufferedImage form.
     */
    private static Hashtable<ImageIndex, BufferedImage> sprites;

    /**
     * Contains references to all the loaded sprites in JavaFX Image form.
     */
    private static Hashtable<ImageIndex, javafx.scene.image.Image> spritesFX;

    /**
     * The amount of loading that the imageLoader has done,
     * anywhere between 0 to 1.
     */
    private static double loadStatus = 0.0;

    /**
     * The extension of the image files that we are to be loading. An advantage
     * of .png files is that they can have transparent pixels.
     */
    private String extension = ".png";

    /**
     * The path where the sprites are located.
     */
    private String path = "./sprites/";


    /**
     * Initializes the sprites hashtables. Will eventually figure out which
     * Image class is better: java.awt.Image, or javafx.scene.image.Image.
     */
    public ImageLoader() {
        sprites = new Hashtable<ImageIndex, BufferedImage>();
        spritesFX = new Hashtable<ImageIndex, javafx.scene.image.Image>();
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
                javafx.scene.image.Image temp2 =
                        new javafx.scene.image.Image(f.toURI().toString());
                sprites.put(i, temp);
                spritesFX.put(i, temp2);
                System.out.println("Loaded Image: " + i.toString() + extension);
                setLoadStatus(1);
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

    public static javafx.scene.image.Image getSpriteFX(ImageIndex index)
            throws NullPointerException {
        return spritesFX.get(index);
    }

    public double getLoadStatus() {
        return loadStatus;
    }

    public void setLoadStatus(double d) {
        if (d >= 0 && d <= 1)
            loadStatus = d;
    }

}
