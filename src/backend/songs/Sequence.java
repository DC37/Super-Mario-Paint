package backend.songs;

/**
 * <p>Abstract class encompassing common functionalities of
 * {@link Song} and {@link Arrangement}.
 */
public abstract class Sequence {
	
	/** The title of this sequence; cannot be {@code null}. */
	private String title;
	
	protected Sequence() {
		this("");
	}
	
	protected Sequence(String title) {
		this.title = emptyIfNull(title);
	}
	
	/**
	 * Get the title of this song or arrangement.
	 * @return The title of this song or arrangement
	 */
	public String getTitle() {
		return title;
	}
	
	/**
	 * Set the title of this song or arrangement.
	 * @param title A new title for this song or arrangement. If {@code null}
	 * 			then the empty string is used instead.
	 */
	public void setTitle(String title) {
		this.title = emptyIfNull(title);
	}
	
	private static String emptyIfNull(String title) {
		return (title != null) ? title : "";
	}

}
