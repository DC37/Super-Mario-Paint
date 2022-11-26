package smp;

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;

import smp.stateMachine.Settings;
import javafx.scene.ImageCursor;
import javafx.scene.image.Image;

/**
 * A class that loads all the necessary images for the program to function when
 * the program first starts.
 * 
 * @author RehdBlob
 * @since 2012.08.14
 */
public class ImageLoader implements Loader {

    /**
     * Contains references to all the loaded sprites in JavaFX Image form.
     */
    private Hashtable<ImageIndex, Image> spritesFX;

    
    /**
     * Contains references to all of the loaded sprites for the cursors in this 
     * program.
     */
    private ArrayList<ImageCursor> cursors;
    
    /**
     * The amount of loading that the imageLoader has done, anywhere between 0 to 1.
     */
    private static double loadStatus = 0.0;

    /**
     * The extension of the image files that we are to be loading. An advantage of
     * .png files is that they can have transparent pixels.
     */
    private String extension = ".png";

    /**
     * The path where the sprites are located.
     */
    private String path = "./sprites/";

    /**
     * Initializes the sprites hashtables. Will eventually figure out which Image
     * class is better: java.awt.Image, or javafx.scene.image.Image.
     */
    public ImageLoader() {
        cursors = new ArrayList<ImageCursor>();
        spritesFX = new Hashtable<ImageIndex, javafx.scene.image.Image>();
    }

    /**
     * Loads all of the image files that will be used in drawing
     * the main window and all of the buttons of Super Mario Paint.
     * A splash screen runs while this is happening.
     */
    @Override
    public void run() {
        File f;
        ImageIndex [] ind = ImageIndex.values();
        /** Change this if for some reason we want more cursors. */
        int NUM_CURSORS = 4;
        for (int i = 0; i < NUM_CURSORS; i++) {
            String s = "./sprites/CURSOR_" + i + ".png";
            File f2 = new File(s);
            if (f2.exists()) {
                cursors.add(new ImageCursor(
                        new Image(f2.toURI().toString()), 0, 0));
                int j = Settings.debug & 0b01;
                if ((Settings.debug & 0b01) != 0)
                    System.out.println(
                            "Loaded Cursor: " + s);
            }
        }
        for (ImageIndex i : ind) {
            setLoadStatus((i.ordinal() + 1.0) / ind.length);
            f = new File(path + i.toString() + extension);
            try {
                Image temp2 =
                        new Image(f.toURI().toString());
                spritesFX.put(i, temp2);
                if ((Settings.debug & 0b01) != 0)
                    System.out.println(
                            "Loaded Image: " + i.toString() + extension);
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                } catch (Exception e) {
                    e.printStackTrace();
            }
        }
        setLoadStatus(1);

    }

    /**
     * Gets an image from whatever <code>ImageIndex</code> that was passed to it.
     * 
     * @param index An ImageIndex, presumably the one that one wants to retreive an
     *              image with.
     * @return An <code>Image</code> that was loaded by the ImageLoader in the
     *         beginning, linked by the ImageIndex in a Hashtable.
     * @throws NullPointerException
     */
    public Image getSpriteFX(ImageIndex index) throws NullPointerException {
        return spritesFX.get(index);
    }

    /**
     * Gets a cursor from
     * @param type 
     * @return
     */
    public ImageCursor getCursor(int type) {
        if (type >= cursors.size() || type < 0)
            return null;
        return cursors.get(type);
    }
    
    /**
     * @return The current load status, which is anywhere between 0 and 1.
     */
    @Override
    public double getLoadStatus() {
        return loadStatus;
    }

    /**
     * Sets the load status of the Thread version of ImageLoader.
     * 
     * @param d A <b>double</b> that is anywhere between 0 and 1.
     */
    @Override
    public void setLoadStatus(double d) {
        if (d >= 0 && d <= 1)
            loadStatus = d;
    }

}
