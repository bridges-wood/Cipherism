import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

public class ProbableSubstitutions {
	public static char[] frequencyOrder = "ETAOINSRHDLUCMFYWGPBVKXQJZ".toLowerCase().toCharArray();
	public static char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toLowerCase().toCharArray();

	/**
	 * Returns an array of possible mapping for a mono-alphabetic substitution
	 * cipher based on frequency analysis.
	 * 
	 * @param letterFrequencies A TreeMap ordered alphabetically containing the
	 *                          letters observed in the cipher text and their
	 *                          respective frequencies.
	 * @return An array of type Mapping describing the most likely letter
	 *         substitutions based on frequency analysis of the cipher text.
	 */
	public static Mapping[] probableSubstitutionGenerator(TreeMap<String, Float> letterFrequencies) {
		Mapping[] mappings = new Mapping[26]; // An array of Mappings, one for each letter of the alphabet.
		int pointer = 0;
		TreeMap<String, Float> freqs = letterFrequencies; // This temporarily handles all the frequencies to allow for
															// data removal without harming the input to the function.
		while (!freqs.isEmpty()) {// Tests to see if all the data has been assigned to a mapping.
			String target = maxKey(freqs); // Finds the character corresponding to the highest probability.
			mappings[pointer] = new Mapping(false, target.charAt(0), frequencyOrder[pointer]); // Creates a new mapping,
																								// in probability order
																								// with the particular
																								// character.
			freqs.remove(target); // Removes character to avoid repeats.
			pointer++;
		}
		for (char letter : alphabet) { // This corrects for any character that did not exist in the cipher text as
										// those mapping must still be accounted for in the array.
			if (pointer > 25) // Prevents index out of bounds error when iterating through the array.
				break;
			if (!letterFrequencies.containsKey(Character.toString(letter))) { // If the letter of the alphabet doesn't
																				// exist in the cipher text.
				mappings[pointer] = new Mapping(false, letter, frequencyOrder[pointer]); // Creates new mapping.
				pointer++;
			}
		}
		return mappings;
	}

	/**
	 * Returns the string corresponding to the maximum float value in a <String, Integer> Map
	 * 
	 * @param map The input <String, Integer> Map.
	 * @return The key corresponding to the maximum float value in the map.
	 */
	public static String maxKey(Map<String, Float> map) {
		return map.entrySet().stream().max((Entry<String, Float> entry1, Entry<String, Float> entry2) -> entry1
				.getValue().compareTo(entry2.getValue())).get().getKey(); // Looks through all the K, V pairs in the
																			// map, comparing all values. One is set to
																			// the max of the two and the next value in
																			// the map is compared to the current
																			// maximum. When the final maximum value is
																			// found, the corresponding key is returned.
	}
}
