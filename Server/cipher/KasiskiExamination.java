package cipher;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import java.util.Optional;
import java.util.TreeMap;

public class KasiskiExamination {

	private char[] alphabet;
	private List<String> possKeys;
	private Utilities u;
	private NGramAnalyser n;
	private Vigenere v;
	private IOC i;
	private DetectEnglish d;

	KasiskiExamination() {
		alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toLowerCase().toCharArray();
		possKeys = new ArrayList<String>();
		u = new Utilities();
		n = new NGramAnalyser();
		v = new Vigenere();
		i = new IOC();
		d = new DetectEnglish();
	}

	/**
	 * Gives the most likely key for the given text.
	 * 
	 * @param text The input text to be text.
	 * @return The most likely key used to encrypt the text.
	 */
	public String run(String text) {

		int[] likelyLengths = likelyKeyLengths(n.kasiskiBase(2, text), text);
		for (int i : likelyLengths) {
			System.out.println(i + " is a likely key length");
		}
		String a = keyGuesserVigenere(mostLikelyKeyLength(likelyLengths, text), text);

		// for(int i : likelyKeyLengths)
		return a; // This
					// returns
					// an
					// empty
					// key.
	}

	/**
	 * Provides the most likely string which is the key to a vigenere cipher. There
	 * are issues when short sample texts with long keywords are analysed leading to
	 * keys that are close but not perfect.
	 * 
	 * @param keyLength The predicted length of the key.
	 * @param text      The encrypted text.
	 * @return The most likely key for the given text and key length.
	 */
	public String keyGuesserVigenere(int keyLength, String text) {
		int textLength = text.length();
		if (keyLength == 0) {
			return "";
		}
		String[][] possibleLetters = new String[keyLength][];
		for (int b = 0; b < keyLength; b++) { // b is the balance, to offset each composite string in the text.
			StringBuilder composite = new StringBuilder(); // StringBuilder for each composite to increase speed.
			for (int i = 0; i < textLength - b; i += keyLength) {
				composite.append(text.charAt(i + b));
			}
			String finalString = composite.toString();
			double[] FMSarray = new double[26]; // Create array for each letter to test how like English it decrypts its
			// respective section to.
			double maxValue = 0; // The most like English value in the array.
			for (int i = 0; i < 26; i++) {
				String toAnalyse = v.decrypt(finalString, Character.toString(alphabet[i]));
				double valueToInsert = computeFMS(n.frequencyAnalysis(toAnalyse));
				// Compute FMS of the composite.
				if (valueToInsert > maxValue) // If the current value is greater than the max, update it.
					maxValue = valueToInsert;
				FMSarray[i] = valueToInsert;
			}
			List<String> possibleCharacters = new ArrayList<String>();
			for (int i = 0; i < 26; i++) {
				if (FMSarray[i] > maxValue * 0.62) // If the character is the most likely to create English, it is
													// likely to
													// be in the key so add it to the array.
					possibleCharacters.add(Character.toString(alphabet[i]));
			}
			possibleLetters[b] = possibleCharacters.toArray(new String[0]);
		}
		combinations(possibleLetters, new String[possibleLetters.length], 0); // Generates all possible combinations of
																				// likely letters in the key.
		String[] possibleKeys = new String[0];
		if (!possKeys.isEmpty()) { // Avoids Null Exceptions and the like.
			possibleKeys = possKeys.toArray(new String[0]);
			for (String s : possibleKeys) {
				System.out.println(s);
			}
			double[] decryptionScores = new double[possibleKeys.length];
			int maxIndex = 0;
			for (int length = 0; length < possibleKeys.length; length++) {
				String toAnalyse = v.decrypt(text, possibleKeys[length]);
				double toInsert = i.indexOfCoincidence(n.frequencyAnalysis(toAnalyse));
				decryptionScores[length] = toInsert;
				if (toInsert > decryptionScores[maxIndex])
					maxIndex = length;
			}
			return possibleKeys[maxIndex]; // Returns the most likely key.
		}
		return null;
	}

	/**
	 * Gives the frequency match score for given text.
	 * 
	 * @param letterOccurences A map containing each letter and their integer
	 *                         occurences in the text.
	 * @return How many of the most and least likely six letters occur in the right
	 *         regions of the analysed text.
	 */
	private int computeFMS(Map<String, Integer> letterOccurences) {
		int FMS = 0;
		char[] leastLikely = new char[] { 'v', 'k', 'x', 'q', 'j', 'z' }; // The six least likely characters in English.
		char[] mostLikely = new char[] { 'e', 't', 'a', 'o', 'i', 'n' }; // The six most likely characters in English.
		if (letterOccurences.keySet().size() < 26) { // If some letters of the alphabet do not occur in the text...
			List<Character> missingLetters = new ArrayList<Character>();
			for (char c : alphabet) {
				if (!letterOccurences.containsKey(Character.toString(c))) {
					missingLetters.add(c); // Adds to the list of characters that don't exist in the the text.
				}
			}
			for (char l : leastLikely) { // If characters that are in the least likely are also missing, they contribute
											// to the FMS score.
				if (missingLetters.contains(l)) {
					FMS++;
				}
			}
		}
		if (FMS < 6) { // If all the least likely characters don't exist in the text, don't bother
						// computing the score of the least likely letters as that's just silly.
			Map<String, Integer> copy = letterOccurences;
			int missingLeastLikely = 6 - FMS; // Avoids looking at more characters than you need to in the map.
			List<Character> leastPopular = new ArrayList<Character>();
			for (int i = 0; i < missingLeastLikely; i++) {
				if (!copy.isEmpty()) { // Insures no errors trying to remove items from an empty map.
					String lowestKey = minKeyString(copy); // Finds the least likely letter in the array. Note - Not
															// perfect as under certain conditions, there may be
															// multiple least likely letters with the same number of
															// occurences, but this has been judged to be a sufficiently
															// unlikely edge case that it does not need to be accounted
															// for.
					leastPopular.add(lowestKey.charAt(0)); // Converts the letter to a char for storage in the list of
															// least popular letters.
					copy.remove(lowestKey); // Stops repeated access of the same letter.
				} else {
					break; // Insures no more iterations than absolutely necessary.
				}
			}
			for (char c : leastLikely) { // If the least likely letters are the least likely in the text, increment the
											// frequency match score.
				if (leastPopular.contains(c)) {
					FMS++;
				}
			}
		}
		Map<String, Integer> copy = letterOccurences;
		List<Character> mostPopular = new ArrayList<Character>(); // A similar operation to the above, we cannot account
																	// for the missing letters in this so we always have
																	// to check for the top six.
		for (int i = 0; i < 6; i++) {
			if (!copy.isEmpty()) {
				String highestKey = maxKeyString(copy); // Find the most likely letter. Note - it is possible for
														// similar issues to above to occur when looking for the most
														// likely letter.
				mostPopular.add(highestKey.charAt(0));
				copy.remove(highestKey);
			} else {
				break;
			}
			for (char c : mostLikely) { // If likely letters in english are similarly likely in the text increment the
										// FMS.
				if (mostPopular.contains(c)) {
					FMS++;
				}
			}
		}
		return FMS;
	}

	/**
	 * Determines the most likely key length for a given text.
	 * 
	 * @param text       The polyalphabetically enciphered text.
	 * @param likelyKeys An array of the lengths of possible keys used to encipher
	 *                   the text.
	 * @return The most likely key length.
	 */
	public int mostLikelyKeyLength(int[] likelyKeys, String text) {
		Double[] averageIOCs = new Double[likelyKeys.length]; // An array to store the indices of coincidence of the
																// different key lengths.
		int counter = -1;
		for (int keyLength : likelyKeys) { // For each of the possible key lengths:
			counter++;
			double total = i.periodIndexOfCoincidence(keyLength, text);
			System.out.println(likelyKeys[counter] + "," + total);
			averageIOCs[counter] = total;
		}
		if (likelyKeys.length > 0) {
			return likelyKeys[this.<Double>maxValueIndex(averageIOCs)];
		} else {
			return 0;
		}

	}

	/**
	 * Gives numbers representing the possible key lengths for a polyalphabetically
	 * encrypted text. If the key length is larger than any given value, it is
	 * likely to be a multiple. The cutoff value for recommending keys, is 50% the
	 * occurences of the most common factor. This seems to always locate the key
	 * somewhere in the array though I don't yet understand why.
	 * 
	 * @param repeated A map of the repeated sequences of a given length in the
	 *                 text.
	 * @param text     The polyalphabetically encrypted text.
	 * @return An array of the possible key lengths of the text and their respective
	 *         occurences.
	 */
	private int[] likelyKeyLengths(Map<String, Integer> repeated, String text) {
		text = u.cleanText(text); // Normalise text to only lower case letters for ease of
									// work.
		Map<Integer, Integer> factors = new TreeMap<Integer, Integer>();
		List<String> patterns = new ArrayList<String>();
		while (!repeated.isEmpty()) {
			String current = (String) repeated.keySet().toArray()[0];
			patterns.add(current); // Adds all the repeated letter groups into a list of patterns.
			repeated.remove(current);
		}
		int length = 1;
		if (!patterns.isEmpty()) { // If there are patterns, set the length to look for to the length of those
									// patters, otherwise, set it to one.
			length = patterns.get(0).length();
		}
		for (String pattern : patterns) { // For each pattern found find all the indices at which they start.
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
				for (int key : factors.keySet()) {
					System.out.println(key + " " + factors.get(key));
				}
			} // Factorise the distances between each occurrence of a particular pattern and
				// append these to a list of factors and their respective occurrences.
		}
		List<Integer> output = new ArrayList<Integer>();
		float cutoff = Float.MAX_VALUE;
		if (!factors.isEmpty()) {
			cutoff = (float) (factors.get(maxKeyInt(factors)) * 0.5); // Cut off is equal to one fifth the count of the
																		// most
																		// likely factor.
		}
		for (int key : factors.keySet()) {
			if (factors.get(key) < cutoff) {
				continue;
			}
			output.add(key);
		} // Gives all factors that are at least half as likely as the most common one.
		return output.stream().mapToInt(Integer::intValue).toArray();
	}

	/**
	 * Returns the key corresponding to the maximum value in a <Integer, Integer>
	 * Map
	 * 
	 * @param map The input <Integer, Integer> Map.
	 * @return The key corresponding to the maximum value in the map.
	 */
	private Integer maxKeyInt(Map<Integer, Integer> map) {
		return map.entrySet().stream().max((Entry<Integer, Integer> entry1, Entry<Integer, Integer> entry2) -> entry1
				.getValue().compareTo(entry2.getValue())).get().getKey(); // Looks through all the K, V pairs in the
																			// map, comparing all values. One is set to
																			// the max of the two and the next value in
																			// the map is compared to the current
																			// maximum. When the final maximum value is
																			// found, the corresponding key is returned.
	}

	/**
	 * Returns the key corresponding to the maximum value in a <String, Integer> Map
	 * 
	 * @param map The input <String, Integer> Map.
	 * @return The key corresponding to the maximum value in the map.
	 */
	private String maxKeyString(Map<String, Integer> map) {
		return map.entrySet().stream().max((Entry<String, Integer> entry1, Entry<String, Integer> entry2) -> entry1
				.getValue().compareTo(entry2.getValue())).get().getKey(); // Looks through all the K, V pairs in the
																			// map, comparing all values. One is set to
																			// the max of the two and the next value in
																			// the map is compared to the current
																			// maximum. When the final maximum value is
																			// found, the corresponding key is returned.
	}

	/**
	 * Returns the key corresponding to the minimum value in a <String, Integer> Map
	 * 
	 * @param map The input <String, Integer> Map.
	 * @return The key corresponding to the minimum value in the map.
	 */
	private String minKeyString(Map<String, Integer> map) {
		Comparator<? super Entry<String, Integer>> valueComparator = (entry1, entry2) -> entry1.getValue()
				.compareTo(entry2.getValue());

		Optional<Entry<String, Integer>> minValue = map.entrySet().stream().min(valueComparator);
		return minValue.get().getKey();
	}

	/**
	 * Create a map with all prime factors in the differences between repeated
	 * strings and their relative occurrences.
	 * 
	 * @param foundFactors A map corresponding to the factors of the differences
	 *                     between repeated strings and their relative number of
	 *                     occurrences.
	 * @param n            The number to be factorized.
	 * @return The map updated with n's factors.
	 */
	private Map<Integer, Integer> factorise(Map<Integer, Integer> foundFactors, int n) {
		for (int i = 2; i <= n; i++) {
			if (n % i == 0) {
				if (!foundFactors.containsKey(i)) { // If this factor hasn't been found before:
					foundFactors.put(i, 1); // Initialise it...
				} else {
					foundFactors.put(i, foundFactors.get(i) + 1); // Or update it.
				}
			}
		}

		return foundFactors;
	}

	/**
	 * Provides the index of the highest value in a generic array.
	 * 
	 * @param array An array of floats.
	 * @return The index of the highest value in the array.
	 */
	private <E extends Comparable<E>> int maxValueIndex(E[] array) {
		int maxIndex = 0;
		for (int i = 0; i < array.length; i++) {
			if (array[i].compareTo(array[maxIndex]) > 0) {
				maxIndex = i;
			}
		}
		return maxIndex;
	}

	/**
	 * Takes a 2D array of strings and outputs all the possible combinations of that
	 * array.
	 * 
	 * @param input      The 2D (Likely truncated) String[] containing all the
	 *                   possible letters at each point in the key.
	 * @param currentKey The current combination of letters being produced by the
	 *                   function.
	 * @param counter    A variable to keep track how deep in the 2D array the
	 *                   function is.
	 * @return A String[] of possible keys.
	 */
	private void combinations(String[][] input, String[] currentKey, int counter) {

		if (counter == input.length) { // If current is a word containing one string from each level of the array...
			StringBuilder out = new StringBuilder();
			for (int i = 0; i < counter; i++) {
				out.append(currentKey[i]); // Combine all the strings.
			}
			possKeys.add(out.toString()); // Add to the public list of possible passwords.
		} else {
			for (int j = 0; j < input[counter].length; j++) { // For every string in each level...
				currentKey[counter] = input[counter][j]; // Append the each string to a currentKey.
				combinations(input, currentKey, counter + 1); // Start permutations for the slightly more complete
																// key...
			}
			// Endfor - this ensures that all combinations are found.
		}
	}
}
