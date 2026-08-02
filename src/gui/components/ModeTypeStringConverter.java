package gui.components;

import java.util.function.Function;
import java.util.function.Predicate;

import javafx.util.StringConverter;

/**
 * A specialized {@link StringConverter} that converts a {@link String}
 * denoting the mode type (either {@code Arr} for arrangements or
 * {@code Song} for songs) into the specified object. 
 * 
 * @param <T> the object to convert the mode type string to/from.
 * @author Aura Lesse Programmer 
 */
public class ModeTypeStringConverter<T> extends StringConverter<T> {

	private static final String ARRANGEMENT = "Arr";
	private static final String SONG = "Song";
	
	private Predicate<T> fnIsArrangement;
	private Function<Boolean, T> fnGetObject;
	
	/**
	 * Create a new {@link StringConverter} capable
	 * of processing the specified object.
	 * 
	 * @param fnIsArrangement
	 *        A {@link Predicate} that determines if the given object
	 *        represents the Arrangement mode type. Used in the
	 *        {@link toString} method.
	 *        
	 * @param fnGetObject
	 *        A {@link Function} that retrieves an object depending on
	 *        whether the evaluated string is an Arrangement or not.
	 *        Used in the {@link fromString} method.
	 * 
	 * @return The newly created {@code ModeTypeStringConverter}.
	 */
	public ModeTypeStringConverter(
			Predicate<T> fnIsArrangement,
			Function<Boolean, T> fnGetObject) {
		
		this.fnIsArrangement = fnIsArrangement;
		this.fnGetObject = fnGetObject;
	}
	
	/**
	 * Given an object, returns its corresponding {@link String}
	 * representation, constrained to whether it represents
	 * an Arrangement ({@code Arr}) or a Song ({@code Song}).
	 * 
	 * @param object The object to convert into the mode type string.
	 * 
	 * @return A {@link String} with the value "{@code Arr}" if
	 *         the mode type is an Arrangement, or "{@code Song}"
	 *         otherwise.
	 */
	@Override
	public String toString(T object) {
		return fnIsArrangement.test(object) ? ARRANGEMENT : SONG;
	}

	/**
	 * Given a {@link String} representing a mode type, returns
	 * its corresponding object.
	 * 
	 * @param string The mode type string to test: it can either
	 *        be "{@code Arr}" for Arrangements or "{@code Song}"
	 *        for Songs.
	 * 
	 * @return The object that corresponds to the mode type.
	 */
	@Override
	public T fromString(String string) {
		return fnGetObject.apply(string.equals(ARRANGEMENT));
	}
	
}
