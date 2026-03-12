package gui.loaders;

import java.net.URL;
import java.util.Hashtable;
import java.util.Map;

import gui.Settings;
import gui.Values;
import gui.resources.FileUtils;
import javafx.scene.image.Image;

/**
 * A class that loads all the necessary images for the program to function when
 * the program first starts.
 * 
 * @author RehdBlob
 * @since 2012.08.14
 */
public class ImageLoader extends LoaderBase<Map<ImageIndex, Image>> {

    /**
     * The extension of the image files that we are to be loading. An advantage of
     * .png files is that they can have transparent pixels.
     */
    private String extension = ".png";

    /**
     * Loads all of the image files that will be used in drawing
     * the main window and all of the buttons of Super Mario Paint.
     * A splash screen runs while this is happening.
     */
    @Override
    public Map<ImageIndex, Image> call() {
    	Map<ImageIndex, Image> spritesFX = new Hashtable<ImageIndex, javafx.scene.image.Image>();
        ImageIndex [] ind = ImageIndex.values();
        
        for (ImageIndex i : ind) {
            if (i == ImageIndex.NONE || i == ImageIndex.BLANK)
                continue;
            
            setLoadStatus((i.ordinal() + 1.0) / ind.length);
            
            String path = i.toString() + extension;
            URL url = FileUtils.getSMPResource(path, Values.SPRITES_FOLDER);
            Image temp2 = new Image(url.toString());
            spritesFX.put(i, temp2);
            
            if ((Settings.debug & 0b01) != 0)
            	System.out.println(
            			"Loaded Image: " + i.toString() + extension);
            try {
            	Thread.sleep(1);
            } catch (InterruptedException e) {
            	e.printStackTrace();
            }
        }
        
        setLoadStatus(1);
        return spritesFX;
    }

}
