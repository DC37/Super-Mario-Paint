package gui.loaders;

import java.net.URL;
import java.util.EnumMap;
import java.util.Map;

import gui.Utilities;
import gui.Values;
import gui.resources.FetchStrategy;
import gui.resources.SMPResourceUtil;
import javafx.scene.image.Image;
import lombok.extern.slf4j.Slf4j;

/**
 * A class that loads all the necessary images for the program to function when
 * the program first starts.
 * 
 * @author RehdBlob
 * @since 2012.08.14
 */
@Slf4j
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
        Map<ImageIndex, Image> spritesFX = new EnumMap<>(ImageIndex.class);
        ImageIndex [] ind = ImageIndex.values();
        
        for (ImageIndex i : ind) {
            if (i == ImageIndex.NONE || i == ImageIndex.BLANK)
                continue;
            
            setLoadStatus((i.ordinal() + 1.0) / ind.length);
            
            String path = i.toString() + extension;
            URL url = SMPResourceUtil.get(path, FetchStrategy.FROM_COPY, Values.SPRITES_FOLDER);
            Image temp2 = new Image(url.toString());
            spritesFX.put(i, temp2);
            
            log.debug("Loaded image: {}{}", i.toString(), extension);
            
            Utilities.tryWait(1);
        }
        
        setLoadStatus(1);
        return spritesFX;
    }

}
