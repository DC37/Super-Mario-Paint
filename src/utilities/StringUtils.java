package utilities;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class StringUtils {

	private StringUtils() {}
	
	/**
	 * Check if a string contains any of the given characters.
	 * 
	 * @param needle The string to check.
	 * @param haystack The list of characters to verify.
	 * @return Whether the string contains any of the characters.
	 */
	public static boolean contains(String needle, List<Character> haystack) {
		return Optional.ofNullable(needle)
				.map(String::toCharArray)
				.map(Character[].class::cast)
				.map(Arrays::asList)
				.map(lc -> lc.stream().anyMatch(haystack::contains))
				.orElse(false);
	}
	
	/**
	 * Presents a list of items separated by a comma (<code>,</code>),
	 * optionally with a preamble preceding it.
	 * 
	 * @param <T> The type of items to be presented.
	 * @param items The list of items to use.
	 * @param preamble The text to show before the list.
	 * @return A string with the preamble (if exists), followed
	 *         by the comma-separated list of items.
	 */
	public static <T> String showList(List<T> items, String preamble) {
		String listStr = items.stream()
				.map(Object::toString)
				.collect(Collectors.joining(", "));
		
		return String.format("%s%s",
				Optional.ofNullable(preamble).orElse(""), listStr);
	}
	
}
