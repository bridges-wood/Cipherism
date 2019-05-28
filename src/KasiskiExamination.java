import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

public class KasiskiExamination {
	// TODO use frequency match score to guess at elements of the key.
	// https://inventwithpython.com/hacking/chapter20.html

	/**
	 * Determines the most likely key length for a given text.
	 * 
	 * @param text       The polyalphabetically enciphered text.
	 * @param likelyKeys An array of the lengths of possible keys used to encipher
	 *                   the text.
	 * @return The most likely key length.
	 */
	public static int mostLikelyKeyLength(String text, int[] likelyKeys) {
		int textLength = text.length(); // Creates a variable to store the length of the text to avoid recalculation.
		float[] averageIOCs = new float[likelyKeys.length]; // An array to store the indices of coincidence of the
															// different key lengths.
		int counter = -1;
		for (int keyLength : likelyKeys) { // For each of the possible key lengths:
			counter++;
			float total = 0;
			for (int b = 0; b < keyLength; b++) { // b is the balance, to offset each composite string in the text.
				StringBuilder composite = new StringBuilder(); // StringBuilder for each composite to increase speed.
				for (int i = 0; i < textLength - b; i += keyLength) {
					composite.append(text.charAt(i + b));
				}
				String finalString = composite.toString();
				total += IOC.indexOfCoincidence(NGramAnalyser.frequencyAnalysis(finalString)); // Total is the sum of
																								// the indices of
																								// conincidence.
			}
			averageIOCs[counter] = total / keyLength; // Geometric mean to offset for the different numbers of indices
														// of coincidence that will be developed for different key
														// lengths.
		}

		return likelyKeys[maxValueIndex(averageIOCs)]; // Returns the key length corresponding to the greatest average
														// index of coincidence.
	}

	/**
	 * Gives numbers representing the possible key lengths for a polyalphabetically
	 * encypted text. If the key length is larger than any given value, it is likely
	 * to be a multiple. The cutoff value for recommending keys, is 50% the
	 * occurences of the most common factor. This seems to always locate the key
	 * somewhere in the array though I don't yet understand why.
	 * 
	 * @param repeated A map of the repeated sequences of a given length in the
	 *                 text.
	 * @param text     The polyalphabetically encrypted text.
	 * @return An array of the possible key lengths of the text and their repsective
	 *         occurences.
	 */
	public static int[] likelyKeyLengths(Map<String, Integer> repeated, String text) {
		text = text.replaceAll("[^a-zA-Z ]", "").toLowerCase(); // Normalise text to only lower case letters for ease of
																// work.
		Map<Integer, Integer> factors = new TreeMap<Integer, Integer>();
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
				factorise(factors, indices[s] - indices[s - 1]);
			}
		}
		List<Integer> output = new ArrayList<Integer>();
		float cutoff = (float) (factors.get(maxKeyInt(factors)) * 0.5);
		System.out.println(cutoff);
		for (int i = 0; i < factors.keySet().size(); i++) {
			int key = maxKeyInt(factors);
			if (factors.get(key) < cutoff) {
				continue;
			}
			System.out.println(key + ":" + factors.get(key));
			output.add(key);
			factors.remove(key);
		}
		return output.stream().mapToInt(Integer::intValue).toArray();
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
	 * @param foundFactors A map corresponding to the factors of the differences
	 *                     between repeated strings and their relative number of
	 *                     occurences.
	 * @param n            The number to be factorised.
	 * @return The map updated with n's factors.
	 */
	public static Map<Integer, Integer> factorise(Map<Integer, Integer> foundFactors, int n) {
		for (int i = 2; i < Math.sqrt(n); i++) {
			if (n % i == 0) {
				if (!foundFactors.containsKey(i)) { // If this factor hasn't been found before:
					foundFactors.put(i, 1); // Initialise it...
				} else {
					foundFactors.put(i, foundFactors.get(i) + 1); // Or update it.
				}
				if (!foundFactors.containsKey(n / i)) { // If this factor hasn't been found before:
					foundFactors.put(n / i, 1); // Initialise it...
				} else {
					foundFactors.put(n / i, foundFactors.get(n / i) + 1); // Or update it.
				}
			}
		}

		return foundFactors;
	}

	/**
	 * Provides the index of the highest value in a float array.
	 * 
	 * @param array An array of floats.
	 * @return The index of the highest value in the array.
	 */
	public static int maxValueIndex(float[] array) {
		int maxIndex = 0;
		for (int i = 0; i < array.length; i++) {
			if (array[i] > array[maxIndex]) {
				maxIndex = i;
			}
		}
		return maxIndex;
	}
}
