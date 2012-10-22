package smp;

import java.io.File;
import java.util.Hashtable;

import javafx.scene.image.Image;

/**
 * A class that loads all the necessary images for the
 * program to function when the program first starts.
 * @author RehdBlob
 * @since 2012.08.14
 */
public class ImageLoader implements Runnable {

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
        ImageIndex [] ind = ImageIndex.values();
        for (ImageIndex i : ind) {
            setLoadStatus((i.ordinal() + 1.0) / ind.length);
            f = new File(path + i.toString() + extension);
            Image temp2 =
                    new Image(f.toURI().toString());
            spritesFX.put(i, temp2);
            System.out.println("Loaded Image: " + i.toString() + extension);
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        setLoadStatus(1);

    }

    /**
     * Gets an image from whatever <code>ImageIndex</code> that was passed
     * to it.
     * @param index An ImageIndex, presumably the one that one wants to
     * retreive an image with.
     * @return An <code>Image</code> that was loaded by the ImageLoader in the
     * beginning, linked by the ImageIndex in a Hashtable.
     * @throws NullPointerException
     */
    public static Image getSpriteFX(ImageIndex index)
            throws NullPointerException {
        return spritesFX.get(index);
    }

    /**
     * @return The current load status, which is anywhere between 0 and 1.
     */
    public double getLoadStatus() {
        return loadStatus;
    }

    /**
     * Sets the load status of the Thread version of ImageLoader.
     * @param d A <b>double</b> that is anywhere between 0 and 1.
     */
    public void setLoadStatus(double d) {
        if (d >= 0 && d <= 1)
            loadStatus = d;
    }

}
