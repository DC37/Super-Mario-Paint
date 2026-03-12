package gui.resources;

/**
 * <p>Enumeration fields for the different strategies we implement to retrieve
 * <i>Super Mario Paint</i> resources.
 */
public enum FetchStrategy {
	
	/**
	 * <p>Retrieve the resource from its file packaged with the distribution.
	 * This is the more direct way to get a resource as it does not involve
	 * interacting with the file system.
	 */
	INTERNAL,
	
	/**
	 * <p>Attempt to copy the resource to some file on the user system. If this
	 * succeeds then the URL of the copied file is used, otherwise we fall back
	 * on {@link #INTERNAL}. The copy will replace any pre-existing file.
	 */
	COPY_INTERNAL,
	
	/**
	 * <p>First look if a file exists on the user system. If not then fall back
	 * on {@link COPY_INTERNAL}.
	 */
	FROM_COPY;

}
