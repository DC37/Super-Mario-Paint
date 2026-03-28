package gui.resources;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

/**
 * <p>This class contains static methods to access <i>Super Mario Paint</i>
 * resources. Resources are files that are essential to the normal functioning
 * of the program; they include sprites, default soundfont, FXML and CSS files.
 * 
 * <p>All resources are available as files packaged with the program.
 * Retrieving one of these files is easy, however, we also want users to be
 * able to replace some files with their own (sprites for the most part). We
 * provide methods that copy internal resources onto the user file system
 * and/or retrieve them from there. If interacting with the file system fails,
 * we default to fetching the internal resource file. This ensures we don't
 * have to handle {@code IOException} whenever we want to access a resource.
 * 
 * <p>See the fields in {@link FetchStrategy} for specification details.
 */
public class SMPResourceUtil {

	/**
	 * <p>Access a SMP resource. The strategy parameter determines where the
	 * resource is taken from and whether a copy of it is made on the user
	 * file system. If this method successfully returns a URL it is
	 * guaranteed to be non-null.
	 * @param name Name of the resource matching its file's name
	 * @param strategy How to retrieve the resource
	 * @param copyDir Where to look for/copy the resource (irrelevant if the
	 *   strategy is {@code INTERNAL})
	 * @return A non-null URL pointing to the resource
	 * @throws NullPointerException if the resource couldn't be retrieved for
	 *   some reason
	 */
	public static URL get(String name, FetchStrategy strategy, String copyDir) {
		switch (strategy) {
		case INTERNAL:
			return get(name);
			
		case COPY_INTERNAL:
			return get(name, copyDir);
			
		case FROM_COPY:
		default:
			File copyFile = new File(copyDir, name);
			
			if (copyFile.exists()) {
				URL ret = fromFile(copyFile);
				if (ret != null) {
					return ret;
				}
				
				System.err.println("Overwriting file " + copyFile + " anyway...");
			}
			
			return copyInto(name, copyFile);
		}
	}
    
	/**
	 * <p>Access a SMP resource from its internal file (packaged with the
	 * distribution). This is the same as {@link #get(String, FetchStrategy, String)}
	 * with {@code INTERNAL} as second argument.
	 * @param name Name of the resource matching its file's name
	 * @return A non-null URL pointing to the resource
	 * @throws NullPointerException if the resource couldn't be retrieved for
	 *   some reason
	 */
    public static URL get(String name) {
        URL url = SMPResourceUtil.class.getClassLoader().getResource(name);
        if (url == null)
            throw new NullPointerException("Cannot load resource: " + name);
        
        return url;
    }
    
    /**
	 * <p>Access a SMP resource from its internal file (packaged with the
	 * distribution), and retrieves its {@link InputStream}.
	 * @param name Name of the resource matching its file's name
	 * @return A non-null {@link InputStream} pointing to the resource
	 * @throws NullPointerException if the resource couldn't be retrieved for
	 *   some reason
	 */
    public static InputStream getStream(String name) {
        InputStream inStream = SMPResourceUtil.class.getClassLoader().getResourceAsStream(name);
        if (inStream == null)
            throw new NullPointerException("Cannot load resource: " + name);
        
        return inStream;
    }
    
    /**
     * <p>Access a SMP resource and make a copy of it on the user file system.
     * This is the same as {@link #get(String, FetchStrategy, String)} with
     * {@code COPY_INTERNAL} as second argument. If the copy fails the result
     * is returned anyway.
	 * @param name Name of the resource matching its file's name
	 * @param strategy How to retrieve the resource
	 * @param copyDir Where to copy the resource
	 * @return A non-null URL pointing to the resource
	 * @throws NullPointerException if the resource couldn't be retrieved for
	 *   some reason
     */
    public static URL get(String name, String copyDir) {
    	File dest = new File(copyDir, name);
    	return copyInto(name, dest);
    }
    
    /**
     * <p>Access a SMP resource and copy its contents onto a file. The previous
     * contents of the file are replaced. If writing the file fails a URL is
     * returned anyway.
	 * @param name Name of the resource matching its file's name
     * @param dest The file to (over)write
	 * @return A non-null URL pointing to the resource
	 * @throws NullPointerException if the resource couldn't be retrieved for
	 *   some reason
     */
    private static URL copyInto(String name, File dest) {
    	URL url = get(name);
    	
    	try {
        	Files.createDirectories(dest.getParentFile().toPath());
        	Files.copy(url.openStream(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
        	URL ret = fromFile(dest);
        	return (ret != null) ? ret : get(name);
    		
    	} catch (IOException e) {
    		System.err.println("An error occured while copying resource " + name + " into file :" + dest);
    		e.printStackTrace();
    	}
    	
    	return url;
    }
    
    /**
     * <p>Get URL from a File.
     * @param f The file
     * @return A URL pointing to this file; can be {@code null}
     */
    private static URL fromFile(File f) {
    	try {
    		return f.toURI().toURL();
    		
    	} catch (MalformedURLException e) {
			System.err.println("Could not get URL from file: " + f);
			e.printStackTrace();
			return null;
    	}
    }

}
