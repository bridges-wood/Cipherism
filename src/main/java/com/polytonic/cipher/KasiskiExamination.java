package com.polytonic.cipher;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.commons.math3.distribution.GammaDistribution;
import org.apache.commons.math3.stat.regression.SimpleRegression;

public class KasiskiExamination {

	private final char[] ALPHABET;
	private final Map<Character, Double> expectedLetterFrequencies;
	private List<String> possKeys;
	private FileIO u;
	private NGramAnalyser n;
	private Vigenere v;
	private IOC i;
	private DetectEnglish d;

	public KasiskiExamination(FileIO u, NGramAnalyser n, Vigenere v, IOC i, DetectEnglish d) {
		ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toLowerCase().toCharArray();
		possKeys = new ArrayList<String>();
		expectedLetterFrequencies = u.readLetterFrequencies(u.LETTER_FREQUENCIES_MAP_PATH);
		this.u = u;
		this.n = n;
		this.v = v;
		this.i = i;
		this.d = d;
	}

	/**
	 * Gives the most likely keys for the given text.
	 * 
	 * @param text  The input text to be text.
	 * @param debug
	 * @return The most likely key used to encrypt the text.
	 */
	public String[] vigenereKeys(String text, boolean debug) {
		int[] likelyLengths = likelyKeyLengths(n.kasiskiBase(3, text), text, debug);
		String[] bestKeys = keyGuesserVigenere(mostLikelyKeyLength(likelyLengths, text, debug), text, debug);
		d.flush();
		return bestKeys;
	}

	/**
	 * Provides the most likely strings which could be the keys to a vigenere
	 * cipher.
	 * 
	 * @param keyLength The predicted length of the key.
	 * @param text      The encrypted text.
	 * @param debug
	 * @return The most likely key for the given text and key length.
	 */
	public String[] keyGuesserVigenere(int keyLength, String text, boolean debug) {
		int textLength = text.length();
		if (keyLength == 0) {
			return new String[0];
		}
		String[][] possibleLetters = new String[keyLength][];
		for (int b = 0; b < keyLength; b++) { // b is the balance, to offset each composite string in the text.
			StringBuilder composite = new StringBuilder(); // StringBuilder for each composite to increase speed.
			for (int i = 0; i < textLength - b; i += keyLength) {
				composite.append(text.charAt(i + b));
			}
			String finalString = composite.toString();
			SortedMap<Double, String> FMSmap = new TreeMap<Double, String>(); // Stores score and letter to allow
																				// fetching of lowest scores.
			for (int i = 0; i < 26; i++) {
				String keyLetter = Character.toString(ALPHABET[i]);
				String toAnalyse = v.decrypt(finalString, keyLetter);
				double valueToInsert = computeFractionalMS(n.frequencyAnalysis(toAnalyse), toAnalyse.length());
				// Compute FMS of the composite.
				if (debug) System.out.println(keyLetter + "," + valueToInsert);
				FMSmap.put(valueToInsert, keyLetter);
			}
			int k = 3; // The number of letters considered for each position in the key.
			String[] possibleCharacters = new String[k];
			for (int i = 0; i < k; i++) {
				Double key = FMSmap.firstKey();
				possibleCharacters[i] = FMSmap.get(key);
				FMSmap.remove(key);
			}
			possibleLetters[b] = possibleCharacters;
		}
		combinations(possibleLetters, new String[possibleLetters.length], 0); // Generates all possible combinations of
																				// likely letters in the key.
		String[] possibleKeys = new String[0];
		if (!possKeys.isEmpty()) { // Avoids Null Pointer Exceptions and the like.
			possibleKeys = possKeys.toArray(new String[0]);
			LinkedList<String> likelyKeys = new LinkedList<String>();
			for (int i = 0; i < possibleKeys.length; i++) {
				if (d.detectEnglish(possibleKeys[i]) == 1.0) {
					likelyKeys.push(possibleKeys[i]);
				}
			}
			LinkedList<String> toRemove = new LinkedList<String>();
			for (String s : likelyKeys) {
				if (debug)
					System.out.println("Possible key: " + s);
				String decryption = d.graphicalRespace(v.decrypt(text, s), 20);
				if (d.isEnglish(decryption.split(" ")) < 1
						|| u.deSpace(decryption).length() < 0.25 * u.deSpace(text).length()) {
					toRemove.add(s);
				}
			}
			for (String key : toRemove) {
				likelyKeys.remove(key);
			}
			if (debug) {
				for (String likelyKey : likelyKeys) {
					System.out.println("Likely key: " + likelyKey);
				}
			}

			return likelyKeys.toArray(new String[likelyKeys.size()]);
			// Returns the most likely key.
		}
		return null;
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
			for (char c : ALPHABET) {
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
			ELPs[i] = expectedLetterFrequencies.get(ALPHABET[i]);
			OLPs[i] = observedLetterFrequencies.get(ALPHABET[i]);

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
	 * @param debug
	 * @return The most likely key length.
	 */
	public int mostLikelyKeyLength(int[] likelyKeys, String text, boolean debug) {
		double[] averageIOCs = new double[likelyKeys.length]; // An array to store the indices of coincidence of the
																// different key lengths.
		if (likelyKeys.length == 0) {
			return 0;
		} else if (likelyKeys.length == 1) {
			return likelyKeys[0];
		} else {
			int counter = -1;
			for (int keyLength : likelyKeys) { // For each of the possible key lengths:
				counter++;
				double total = i.periodIndexOfCoincidence(keyLength, text);
				averageIOCs[counter] = total;
			}
			SimpleRegression reg = new SimpleRegression();
			GammaDistribution g = new GammaDistribution(2.3, 2); // This distribution roughly fits the probability of
																	// words of length x.

			for (int i = 0; i < likelyKeys.length; i++) {
				reg.addData(likelyKeys[i], averageIOCs[i]);
			}

			double slope = reg.getSlope();
			double intercept = reg.getIntercept();
			double maxDiff = Double.MIN_VALUE;
			int bestKeyLength = 1;
			for (int i = 0; i < likelyKeys.length; i++) {
				double diff = averageIOCs[i] - (slope * likelyKeys[i] + intercept)
						+ likelyKeys[i] * g.density(likelyKeys[i]);
				if (diff > maxDiff) {
					maxDiff = diff;
					bestKeyLength = likelyKeys[i];
				}
			}

			if (debug)
				System.out.println("Best key length: " + bestKeyLength);

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
	 * @param debug
	 * @return An array of the possible key lengths of the text and their respective
	 *         occurences.
	 */
	public int[] likelyKeyLengths(Map<String, Integer> repeated, String text, boolean debug) {
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
			if (factors.get(key) < cutoff)
				continue;

			if (key > 20)
				continue;

			output.add(key);
		} // Gives all factors that are at least half as likely as the most common one.
		if (debug) {
			for (int keyLength : output) {
				System.out.println("Likely key length: " + keyLength);
			}
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
	public Integer maxKeyInt(Map<Integer, Integer> map) {
		if (map.isEmpty())
			throw new IllegalArgumentException("Map size must be greater than 0.");
		return map.entrySet().stream().max((Entry<Integer, Integer> entry1, Entry<Integer, Integer> entry2) -> entry1
				.getValue().compareTo(entry2.getValue())).get().getKey();
		/*
		 * Looks through all the K, V pairs in the map, comparing all values. One is set
		 * to the max of the two and the next value in the map is compared to the
		 * current maximum. When the final maximum value is found, the corresponding key
		 * is returned.
		 */
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
	public Map<Integer, Integer> factorise(Map<Integer, Integer> foundFactors, int n) {
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
