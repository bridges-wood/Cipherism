import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

public class KasiskiExamination {
	
	public static int mostlikelyKeyLength(String text, int[] likelyKeys) {
		text = text.replaceAll("[^a-zA-Z ]", "").toLowerCase();
		int length = text.length();
		for(int keyLength : likelyKeys) {
			StringBuilder nthCharacters = new StringBuilder();
			for(int i = 0; i < length; i += keyLength) {
				//TODO get every nth character, do frequency analysis followed by index of conincidence to determine the best fit key.
			}
		}
		 
		return -1;
	}

	/**
	 * Gives numbers representing the 5 most likely key lengths for a
	 * polyalphabetically encypted text. If the key length is larger than it, it is
	 * likely to be a multiple.
	 * 
	 * @param repeated A map of the repeated sequences of a given length in the
	 *                 text.
	 * @param text     The polyalphabetically encrypted text.
	 * @return
	 */
	public static int[] likelyKeyLengths(Map<String, Integer> repeated, String text) {
		text = text.replaceAll("[^a-zA-Z ]", "").toLowerCase(); //Normalise text to only lower case letters for ease of work.
		Map<Integer, Integer> primeFactors = new TreeMap<Integer, Integer>();
		List<String> patterns = new ArrayList<String>();
		while (!repeated.isEmpty()) {
			String current = (String) repeated.keySet().toArray()[0];
			patterns.add(current);
			repeated.remove(current);
		}
		int length = patterns.get(0).length();
		for (String pattern : patterns) {
			List<Integer> startIndices = new ArrayList<Integer>();
			for (int i = 0; i < text.length() - (length - 1); i++) {
				String section = "";
				for (int j = 0; j < length; j++) {
					section += text.charAt(i + j);
				}
				if (section.equals(pattern)) {
					startIndices.add(i);
				}
			}
			Integer[] indices = startIndices.toArray(new Integer[0]);
			for (int s = 1; s < indices.length; s++) {
				primeFactors(primeFactors, indices[s] - indices[s - 1]);
			}
		}
		int[] output = new int[5];
		for(int i = 0; i < 5; i++) {
			int key = maxKeyInt(primeFactors);
			output[i] = key;
			primeFactors.remove(key);
		}
		return output;
	}

	/**
	 * Returns the key corresponding to the maximum value in a <Integer, Integer>
	 * Map
	 * 
	 * @param map The input <Integer, Integer> Map.
	 * @return The key corresponding to the maximum value in the map.
	 */
	public static Integer maxKeyInt(Map<Integer, Integer> map) {
		return map.entrySet().stream().max((Entry<Integer, Integer> entry1, Entry<Integer, Integer> entry2) -> entry1
				.getValue().compareTo(entry2.getValue())).get().getKey(); // Looks through all the K, V pairs in the
																			// map, comparing all values. One is set to
																			// the max of the two and the next value in
																			// the map is compared to the current
																			// maximum. When the final maximum value is
																			// found, the corresponding key is returned.
	}

	/**
	 * Create a map with all primefactors in the differences between repeated
	 * strings and their relative occurences.
	 * 
	 * @param foundFactors A map corresponding to the primefactors of the
	 *                     differences between repeated strings and their relative
	 *                     number of occurences.
	 * @param n            The number to be prime factorised.
	 * @return The map updated with n's prime factors.
	 */
	public static Map<Integer, Integer> primeFactors(Map<Integer, Integer> foundFactors, int n) {
		for (int i = 2; i < n; i++) {
			while (n % i == 0) { // If i is a factor of n:
				if (!foundFactors.containsKey(i)) { // If this factor hasn't been found before:
					foundFactors.put(i, 1); // Initialise it...
				} else {
					foundFactors.put(i, foundFactors.get(i) + 1); // Or update it.
				}
				n = n / i; // Update n to avoid repeat factorings.
			}
		}
		if (n > 2) { // If the remainder is greater than 2:
			if (!foundFactors.containsKey(n)) { // Same as above.
				foundFactors.put(n, 1);
			} else {
				foundFactors.put(n, foundFactors.get(n) + 1);
			}
		}

		return foundFactors;
	}
}
