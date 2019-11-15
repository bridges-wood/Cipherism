package cipher;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.TreeMap;

import org.apache.commons.math3.stat.regression.SimpleRegression;

public class KasiskiExamination {

	private char[] alphabet;
	private Map<Character, Double> expectedLetterFrequencies;
	private List<String> possKeys;
	private Utilities u;
	private NGramAnalyser n;
	private Vigenere v;
	private IOC i;

	KasiskiExamination() {
		alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toLowerCase().toCharArray();
		possKeys = new ArrayList<String>();
		setExpectedLetterFrequencies();
		u = new Utilities();
		n = new NGramAnalyser();
		v = new Vigenere();
		i = new IOC();
		new DetectEnglish();
	}

	private void setExpectedLetterFrequencies() {
		Map<Character, Double> letterFrequencies = new TreeMap<Character, Double>();
		letterFrequencies.put('e', 0.12702);
		letterFrequencies.put('t', 0.09056);
		letterFrequencies.put('a', 0.08167);
		letterFrequencies.put('o', 0.07507);
		letterFrequencies.put('i', 0.06966);
		letterFrequencies.put('n', 0.06749);
		letterFrequencies.put('s', 0.06327);
		letterFrequencies.put('h', 0.06094);
		letterFrequencies.put('r', 0.05987);
		letterFrequencies.put('d', 0.04253);
		letterFrequencies.put('l', 0.04025);
		letterFrequencies.put('c', 0.02782);
		letterFrequencies.put('u', 0.02758);
		letterFrequencies.put('m', 0.02406);
		letterFrequencies.put('w', 0.02360);
		letterFrequencies.put('f', 0.02228);
		letterFrequencies.put('g', 0.02015);
		letterFrequencies.put('y', 0.01974);
		letterFrequencies.put('p', 0.01929);
		letterFrequencies.put('b', 0.01492);
		letterFrequencies.put('v', 0.00978);
		letterFrequencies.put('k', 0.00772);
		letterFrequencies.put('j', 0.00153);
		letterFrequencies.put('x', 0.00150);
		letterFrequencies.put('q', 0.00095);
		letterFrequencies.put('z', 0.00074);
		this.expectedLetterFrequencies = letterFrequencies;
	}

	/**
	 * Gives the most likely key for the given text.
	 * 
	 * @param text The input text to be text.
	 * @return The most likely key used to encrypt the text.
	 */
	public String run(String text) {

		int[] likelyLengths = likelyKeyLengths(n.kasiskiBase(2, text), text);
		String a = keyGuesserVigenere(mostLikelyKeyLength(likelyLengths, text), text);
		return a;
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
			for (int i = 0; i < 26; i++) {
				String toAnalyse = v.decrypt(finalString, Character.toString(alphabet[i]));
				double valueToInsert = computeFractionalMS(n.frequencyAnalysis(toAnalyse), toAnalyse.length()); // Compute
																												// FMS
																												// of
																												// the
																												// composite.
				FMSarray[i] = valueToInsert;
			}
			List<String> possibleCharacters = new ArrayList<String>();
			for (int i = 0; i < 26; i++) {
				if (FMSarray[i] < 0.5) // This selects the top ~40% of letters.
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
			int minIndex = 0;
			for (int length = 0; length < possibleKeys.length; length++) {
				String toAnalyse = v.decrypt(text, possibleKeys[length]);
				double toInsert = computeFractionalMS(n.frequencyAnalysis(toAnalyse), toAnalyse.length());
				decryptionScores[length] = toInsert;
				if (toInsert < decryptionScores[minIndex]) {
					minIndex = length;
					System.out
							.println("The new best key is: " + possibleKeys[minIndex] + ". Its score is: " + toInsert);
				}
			}
			System.out.println("The score of the real key is: "
					+ computeFractionalMS(n.frequencyAnalysis(v.decrypt(text, "testkey")), text.length()));
			return possibleKeys[minIndex]; // Returns the most likely key.
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
			for (char c : mostLikely) { // If likely letters in English are similarly likely in the text increment the
										// FMS.
				if (mostPopular.contains(c)) {
					FMS++;
				}
			}
		}
		return FMS;
	}

	/**
	 * Calculates the <a href =
	 * "https://en.wikipedia.org/wiki/Bhattacharyya_distance">Bhattacharyya Distance
	 * </a> between the letters in a given string and the expected letter
	 * distribution for English. Lower is better.
	 * 
	 * @param letterOccurences A map containing the integer occurrences of given
	 *                         letters in the text to be examined.
	 * @param length           The total length of the string from which the integer
	 *                         letter occurrences are drawn from.
	 * @return The Bhattacharyya Distance between the expected frequencies of
	 *         English letters and the observed distribution.
	 */
	public double computeFractionalMS(Map<String, Integer> letterOccurences, int length) {
		Map<Character, Double> observedLetterFrequencies = new TreeMap<Character, Double>();
		if (letterOccurences.keySet().size() < 26) { // If some letters of the alphabet do not occur in the text...
			List<Character> missingLetters = new ArrayList<Character>();
			for (char c : alphabet) {
				if (!letterOccurences.containsKey(Character.toString(c))) {
					missingLetters.add(c); // Adds to the list of characters that don't exist in the the text.
				}
			}
			for (char missing : missingLetters) {
				letterOccurences.put(String.valueOf(missing), 0);
			}
		}
		for (String letter : letterOccurences.keySet()) {
			observedLetterFrequencies.put(letter.charAt(0), (double) letterOccurences.get(letter) / (double) length);
		}
		double[] ELPs = new double[26];
		double[] OLPs = new double[26];
		for (int i = 0; i < 26; i++) {
			ELPs[i] = expectedLetterFrequencies.get(alphabet[i]);
			OLPs[i] = observedLetterFrequencies.get(alphabet[i]);

		}
		return bhattacharyyaDistance(ELPs, OLPs);
	}

	/**
	 * Calculates the <a href =
	 * "https://en.wikipedia.org/wiki/Bhattacharyya_distance">Bhattacharyya Distance
	 * </a> between two necessarily categorical distributions.,
	 * 
	 * @param p The first distribution.
	 * @param q The second distribution
	 * @return The Bhattacharyya Distance between both distributions.
	 */
	private double bhattacharyyaDistance(double[] p, double[] q) {
		double BC = 0;
		for (int i = 0; i < p.length; i++) {
			BC += Math.sqrt(p[i] * q[i]);
		}
		return -Math.log(BC);
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
		double[] averageIOCs = new double[likelyKeys.length]; // An array to store the indices of coincidence of the
																// different key lengths.
		if (likelyKeys.length == 0) {
			return 0;
		} else if (likelyKeys.length == 1) {
			return likelyKeys[0];
		} else if (likelyKeys.length == 2) {
			return likelyKeys[1];
		} else {
			int counter = -1;
			for (int keyLength : likelyKeys) { // For each of the possible key lengths:
				counter++;
				double total = i.periodIndexOfCoincidence(keyLength, text);
				averageIOCs[counter] = total;
			}
			SimpleRegression reg = new SimpleRegression();

			for (int i = 0; i < likelyKeys.length; i++) {
				reg.addData(likelyKeys[i], averageIOCs[i]);
			}

			double slope = reg.getSlope();
			double intercept = reg.getIntercept();
			double maxDiff = Double.MIN_VALUE;
			int bestKeyLength = 1;
			for (int i = 0; i < likelyKeys.length; i++) {
				double diff = averageIOCs[i] - (slope * likelyKeys[i] + intercept) + 0.05 * likelyKeys[i];
				if (diff > maxDiff) {
					maxDiff = diff;
					bestKeyLength = likelyKeys[i];
				}
			}

			return bestKeyLength;
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
