package backend;

import utilities.CollectionUtils;

public class BackendUtils {

	private BackendUtils() {}
	
	/**
	 * Given an instrument index, retrieves it possibly swapped, if and
	 * only if the input corresponds to the entries of the piranha
	 * and the coin (if one is given, it returns the other).
	 * 
	 * @param instId The instrument index to swap
	 * @return The instrument index, possibly swapped
	 */
	public static int swapCoinPiranhaInstrumentIdxs(int instId) {
		if (instId == 15) return 16;
		if (instId == 16) return 15;
		return instId;
	}
	
	/**
	 * Given an array of note extensions, create another with
	 * the entries of the coin and piranha swapped around.
	 * 
	 * @param noteExts The note extension array to process
	 * @return A copy of the array, with the entries
	 *         of the coin and piranha swapped.
	 */
	public static boolean[] swapCoinPiranhaNoteExtensions(boolean[] noteExts) {
		return CollectionUtils.swapItems(noteExts, 15, 16);
	}
	
}
