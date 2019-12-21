package cipher;

import java.util.Map.Entry;
import java.util.TreeMap;

public class NGramAnalyser {

	private double floor;
	private String[] letters;
	private Utilities u;

	NGramAnalyser() {
		letters = "qwertyuiopasdfghjklzxcvbnm".split("");
		u = new Utilities();
	}

	/**
	 * Creates a map of ngrams in target text.
	 * 
	 * @param n
	 *            Integer length of the ngrams to be found in the text.
	 * @param text
	 *            String to be analysed.
	 * @param isSpaced
	 *            Boolean whether or not the text contains spaces already.
	 * @return TreeMap of all ngrams and fraction of text they represent.
	 */
	public TreeMap<String, Float> NgramAnalysis(int n, String text, boolean isSpaced) {
		TreeMap<String, Float> ngrams = new TreeMap<String, Float>();
		// Breakdown by words or not (this is not necessary as the code works on
		// unspaced inputs equally well)
		String[] words = u.cleanText(text).split("\\s+"); // Takes every word in text and
															// removes all punctuation
															// before adding it to the
															// array.
		for (String word : words) {
			String temp = word;
			word = " " + temp + " "; // Adds spaces to the end of every word (Allows for word end and start
										// analysis).

			if (n <= word.length()) {
				char[] chars = word.toCharArray();
				// Generate ngrams for each word.
				for (int i = 0; i < (chars.length - n) + 1; i++) {
					StringBuilder ngram = new StringBuilder();
					for (int j = 0; j < n; j++) {
						ngram.append(chars[i + j]);
					}
					// Add ngrams to tree.
					String finalNgram = ngram.toString();
					if (ngrams.containsKey(finalNgram)) {
						ngrams.put(finalNgram, ngrams.get(finalNgram) + 1);
					} else {
						ngrams.put(finalNgram, 1f);
					}
				}
			}
		}
		if (!isSpaced) {
			ngrams.remove(" " + text.substring(0, n - 1));
			ngrams.remove(text.substring(text.length() - (n - 1)) + " ");
		}
		if (n == 1) {
			ngrams.remove(" ");
		}
		// Set float to fraction of the total the n gram represents.
		float total = 0f;
		for (float f : ngrams.values()) {
			total += f;
		} // Collect total number of ngrams in text.
		for (String key : ngrams.keySet()) {
			ngrams.put(key, ngrams.get(key) / total);
		}
		// Test is to be on past cipher challenge answers to analyse how the author of
		// the puzzles writes.
		return ngrams;
	}

	/**
	 * Creates a map of letter frequencies in target text.
	 * 
	 * @param text
	 *            String to be analysed.
	 * @return TreeMap of all letters and their integer occurrences in text.
	 */
	public TreeMap<String, Integer> frequencyAnalysis(String text) {
		TreeMap<String, Integer> ngrams = new TreeMap<String, Integer>();
		// Breakdown by words or not (this is not necessary as the code works on
		// unspaced inputs equally well)
		String[] characters = u.cleanText(text).split(""); // Takes every word in text and
															// removes all punctuation
															// before adding it to the
															// array.
		for (String letter : characters) {
			// Generate ngrams for each word.
			if (ngrams.containsKey(letter)) {
				ngrams.put(letter, ngrams.get(letter) + 1);
			} else {
				ngrams.put(letter, 1);
			}
		}
		for (String letter : letters) {
			if (!ngrams.containsKey(letter)) {
				ngrams.put(letter, 0);
			}
		}
		return ngrams;
	}

	/**
	 * Creates a map of ngrams and frequencies in unspaced target text.
	 * 
	 * @param n
	 *            Integer length of the ngrams to be found in the text.
	 * @param text
	 *            String to be analysed.
	 * @return TreeMap of all length n strings in the text, in order to look for
	 *         repeats.
	 */
	public TreeMap<String, Integer> kasiskiBase(int n, String text) {
		TreeMap<String, Integer> ngrams = new TreeMap<String, Integer>();
		text = u.cleanText(text);
		char[] chars = text.toCharArray();
		// Generate ngrams for each word.
		for (int i = 0; i < (chars.length - n) + 1; i++) {
			StringBuilder ngram = new StringBuilder();
			for (int j = 0; j < n; j++) {
				ngram.append(chars[i + j]);
			}
			// Add ngrams to tree.
			String finalNgram = ngram.toString();
			if (ngrams.containsKey(finalNgram)) {
				ngrams.put(finalNgram, ngrams.get(finalNgram) + 1);
			} else {
				ngrams.put(finalNgram, 1);
			}
		}
		TreeMap<String, Integer> ngramsCopy = new TreeMap<String, Integer>();
		for (Entry<String, Integer> entry : ngrams.entrySet()) {
			if (entry.getValue() != 1)
				ngramsCopy.put(entry.getKey(), entry.getValue());
		}

		return ngramsCopy;
	}

	/**
	 * Computes the log score for the ngrams present in the analysed text. Higher is
	 * better.
	 * 
	 * @param n
	 *            The length of the nGrams we wish to test for.
	 * @param text
	 *            The input text to be analysed.
	 * @param perGram
	 *            Whether or not we want an adjusted score based on the length of
	 *            text, in order to compare texts of varying lengths.
	 * @return A double representing the total score of the text. Higher is better.
	 */
	public double computeScore(int n, String text, boolean perGram) {
		double score = 0;
		TreeMap<String, Double> ngrams = loadNgramMap(n);
		char[] letters = u.cleanText(text).toCharArray(); // Clean up input.
		for (int i = 0; i < letters.length - n; i += n) {// Assures we can get the most number of ngrams without
															// overflow.
			StringBuilder graph = new StringBuilder();
			for (int j = 0; j < n; j++) {
				graph.append(letters[i + j]);
			} // Creates length n groups of letters from the text.
			String group = graph.toString();
			if (ngrams.containsKey(group)) {
				score += ngrams.get(group);
			} else {
				score += floor;
			}
		}
		if (perGram) {
			return score / ngrams.keySet().size();
		} else {
			return score;
		}
	}

	/**
	 * Loads a TreeMap from a file containing ngrams in English and their relative
	 * appearances in Google's trillion word corpus.
	 * 
	 * @param size
	 *            The number of letters to be examined for.
	 * @return A TreeMap containing ngrams and their logProbability of occurring.
	 */
	public TreeMap<String, Double> loadNgramMap(int size) {
		TreeMap<String, Double> chances = new TreeMap<String, Double>();
		String[] lines = null;
		switch (size) { // Load the file that we're interested in.
		case 1:
			lines = u.readFile("1l.txt");
			break;
		case 2:
			lines = u.readFile("2l.txt");
			break;
		case 3:
			lines = u.readFile("3l.txt");
			break;
		case 4:
			lines = u.readFile("4l.txt");
			break;
		case 5:
			lines = u.readFile("5l.txt");
			break;
		}
		for (String line : lines) {
			String[] splitLine = line.split(",");
			chances.put(splitLine[0], Double.valueOf(splitLine[1]));
		}
		Double total = 0d;
		Double[] values = chances.values().toArray(new Double[0]);
		for (int i = 0; i < values.length; i++) {
			total += values[i]; // Loads the total number of occurrences of all ngrams into one total.
		}
		for (String key : chances.keySet()) {
			Double toInsert = Math.log10((double) chances.get(key) / total); // For every key, the log is taken to avoid
																				// numerical underflow when operating
																				// with such small probabilities.
			chances.put(key, toInsert);
		}
		floor = Math.log10(0.01 / total); // A floor is devised to stop -infinity probabilities.
		return chances;
	}
}
