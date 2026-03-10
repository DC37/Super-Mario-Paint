package gui;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

/**
 * This class contains static methods to access resources or interact with files on the user's system.
 */
public class FileUtils {
    
    /**
     * <p>Get a file that's also a resource of the program (typically a default sprite or soundfont).</p>
     * 
     * <p>This method will attempt to load the file from its expected location. If the file is not found
     * there, it is loaded as a resource and then copied onto the expected location for future calls.</p>
     * 
     * @param filename name of the file or resource
     * @param dir expected location for the file
     */
    public static File getResourceFile(String filename, String dir, boolean forceCopy) throws IOException {
        File ret = new File(dir, filename);
        
        if (!forceCopy && ret.exists())
            return ret;
        
        URL url = getResourceURL(filename);
        
        Files.createDirectories(ret.getParentFile().toPath());
        Files.copy(url.openStream(), ret.toPath(), StandardCopyOption.REPLACE_EXISTING);
        
        return ret;
    }
    
    public static File getResourceFile(String filename, String dir) throws IOException {
        return getResourceFile(filename, dir, false);
    }
    
    public static URL getResourceURL(String filename) {
        URL url = Utilities.class.getResource("/resources/" + filename);
        if (url == null)
            throw new NullPointerException("Cannot load resource: " + filename);
        
        return url;
    }
	
	/**
	 * Creates the soundfont folder if it does not already exists.
	 */
	public static File getSoundfontFolder() throws IOException {
	    File dir = new File(Values.SOUNDFONTS_FOLDER);
	    Files.createDirectories(dir.toPath());
	    return dir;
	}
    
	/**
	 * @return The list of filenames *.sf2 in the soundfonts folder.
	 * @since v1.1.2
	 */
	public static String[] getSoundfontsList() throws IOException {
		File soundfontsFolder = getSoundfontFolder();
		
		return soundfontsFolder.list(new FilenameFilter() {
		    @Override
		    public boolean accept(File dir, String name) {
		        return name.toLowerCase().endsWith(".sf2");
		    }
		});
    }

}
