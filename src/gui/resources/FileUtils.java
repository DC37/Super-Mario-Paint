package gui.resources;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

/**
 * <p>This class contains static methods to access resources or interact with files on the user's system.
 * 
 * <p>We want users to be able to mess with some files. Mostly sprites, maybe some other kinds of files in the future. To achieve this, we
 * implement the following strategy for accessing resources, given a name and location:
 * <ol>
 * 	 <li>Look for a file on the system and if it exists, return its locator;
 *   <li>If the file doesn't exist, fetch the {@link URL} from SMP's resources;
 *   <li>Attempt to copy the resource's contents into a new file---if this fails ignore it;
 *   <li>Return the locator obtained in step 2.
 * </ol>
 * Most of the time we are only interested in the URL. The steps above implement a safe way to get it but also create files that the user can modify if she wants to.
 */
public class FileUtils {
    
	/**
	 * <p>Access a SMP resource. This method either returns a non-null URL or raises an exception.
	 * @param name Name of the resource---make sure this corresponds to some file in the {@code resources} directory
	 * @return a {@link URL} pointing to the resource---cannot be {@code null}
	 * @throws NullPointerException if {@code name} is null or the resource cannot be accessed
	 */
    public static URL getSMPResource(String name) {
        URL url = FileUtils.class.getResource("/resources/" + name);
        if (url == null)
            throw new NullPointerException("Cannot load resource: " + name);
        
        return url;
    }
    
    /**
     * <p>Access a SMP resource which is expected to be a file on the user's system. If the file is not present then
     * it is created with the contents of the original resource. If file creation or copying fails for some reason then
     * the URL is simply returned.
     * @param name Name of the resource
     * @param dir Directory in which to look for the resource, or where to copy it
     * @return a {@link URL} pointing to the resource---cannot be {@code null}
     * @throws NullPointerException if {@code name} is null or the resource cannot be accessed
     */
    public static URL getSMPResource(String name, String dir) {
    	File ret = new File(dir, name);
    	
    	if (ret.exists()) {
    		try {
    			return ret.toURI().toURL();
    			
    		} catch (MalformedURLException e) {
    			throw new RuntimeException("Found file '" + ret + "' but was unable to get its URL", e);
    		}
    	}
    	
    	URL url = getSMPResource(name);
    	
    	try {
        	Files.createDirectories(ret.getParentFile().toPath());
        	Files.copy(url.openStream(), ret.toPath(), StandardCopyOption.REPLACE_EXISTING);
    		
    	} catch (IOException e) {
    		System.err.println("An error occured while copying resource '" + name + "' into file '" + ret + "'");
    		e.printStackTrace();
    	}
    	
    	return url;
    }

}
