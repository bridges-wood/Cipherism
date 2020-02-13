package cipher;

import java.util.TreeMap;

/**
 * A class that is capable of counting the number of given strings of length n
 * that occur in a given string.
 * 
 * @author Max Wood
 *
 */
public class NGramAnalyser {

	private double floor;
	private final String[] LETTERS;
	private final TreeMap<String, Double> MONOGRAMS;
	private final TreeMap<String, Double> BIGRAMS;
	private final TreeMap<String, Double> TRIGRAMS;
	private final TreeMap<String, Double> QUADGRAMS;
	private final TreeMap<String, Double> PENTAGRAMS;

	public NGramAnalyser(FileIO u) {
		LETTERS = "qwertyuiopasdfghjklzxcvbnm".split("");
		this.MONOGRAMS = u.loadNgramMap(u.MONOGRAM_LOG_MAP_PATH);
		this.BIGRAMS = u.loadNgramMap(u.BIGRAM_LOG_MAP_PATH);
		this.TRIGRAMS = u.loadNgramMap(u.TRIGRAM_LOG_MAP_PATH);
		this.QUADGRAMS = u.loadNgramMap(u.QUADGRAM_LOG_MAP_PATH);
		this.PENTAGRAMS = u.loadNgramMap(u.PENTAGRAM_LOG_MAP_PATH);
	}

	/**
	 * Creates a map of ngrams in target text.
	 * 
	 * @param n        Integer length of the ngrams to be found in the text.
	 * @param text     String to be analysed.
	 * @param isSpaced Boolean whether or not the text contains spaces already.
	 * @return TreeMap of all ngrams and fraction of text they represent.
	 */
	public TreeMap<String, Double> NgramAnalysis(int n, String text, boolean isSpaced) {
		TreeMap<String, Double> ngrams = new TreeMap<String, Double>();
		String[] words = text.split("\\s+");
		/*
		 * Takes every word in text and removes all punctuation before adding it to the
		 * array.
		 */
		for (String word : words) {
			String temp = word;
			word = " " + temp + " ";
			/*
			 * Adds spaces to the end of every word (Allows for word end and start
			 * analysis).
			 */
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
						ngrams.put(finalNgram, 1d);
					}
				}
			}
		}
		if (!isSpaced) {
			ngrams.remove(" " + text.substring(0, n - 1));
			ngrams.remove(text.substring(text.length() - (n - 1)) + " ");
			/*
			 * This removes the nGram that was found at the start and end containing the
			 * spaces added in before.
			 */
		}
		if (n == 1) {
			ngrams.remove(" ");
			// Insures that all letters are included in the map even if they appear 0 times.
			for (String letter : LETTERS) {
				if (!ngrams.containsKey(letter)) {
					ngrams.put(letter, 0d);
				}
			}
		}
		// Set double to fraction of the total the n gram represents.
		double total = 0d;
		for (double d : ngrams.values()) {
			total += d;
		} // Collect total number of ngrams in text.
		for (String key : ngrams.keySet()) {
			ngrams.put(key, ngrams.get(key) / total);
		}
		return ngrams;
	}

	/**
	 * Creates a map of letter frequencies in target text.
	 * 
	 * @param text String to be analysed.
	 * @return TreeMap of all letters and their integer occurrences in text.
	 */
	public TreeMap<String, Integer> frequencyAnalysis(String text) {
		TreeMap<String, Integer> ngrams = new TreeMap<String, Integer>();
		String[] characters = text.split(""); /*
												 * Takes every word in text and removes all punctuation before adding it
												 * to the array.
												 */
		for (String letter : characters) {
			// Generate ngrams for each word.
			if (ngrams.containsKey(letter)) {
				ngrams.put(letter, ngrams.get(letter) + 1);
			} else {
				ngrams.put(letter, 1);
			}
		}
		for (String letter : LETTERS) {
			if (!ngrams.containsKey(letter)) {
				ngrams.put(letter, 0);
			}
		}
		return ngrams;
	}

	/**
	 * Creates a map of ngrams and frequencies in unspaced target text.
	 * 
	 * @param n    Integer length of the ngrams to be found in the text.
	 * @param text String to be analysed.
	 * @return TreeMap of all length n strings in the text, in order to look for
	 *         repeats.
	 */
	public TreeMap<String, Integer> kasiskiBase(int n, String text) {
		TreeMap<String, Integer> ngrams = new TreeMap<String, Integer>();
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
		return ngrams;
	}

	/**
	 * Computes the log score for the ngrams present in the analysed text. Higher is
	 * better.
	 * 
	 * @param n       The length of the nGrams we wish to test for.
	 * @param text    The input text to be analysed.
	 * @param perGram Whether or not we want an adjusted score based on the length
	 *                of text, in order to compare texts of varying lengths.
	 * @return A double representing the total score of the text. Higher is better.
	 */
	public double computeScore(int n, String text, boolean perGram) {
		double score = 0;
		TreeMap<String, Double> ngrams = new TreeMap<String, Double>();
		switch (n) {
		case 1:
			ngrams = this.getMONOGRAMS();
			break;
		case 2:
			ngrams = this.getBIGRAMS();
			break;
		case 3:
			ngrams = this.getTRIGRAMS();
			break;
		case 4:
			ngrams = this.getQUADGRAMS();
			break;
		case 5:
			ngrams = this.getPENTAGRAMS();
			break;
		default:
			throw new IllegalArgumentException("Improper value of n.");
		}
		char[] letters = text.toCharArray();
		for (int i = 0; i <= letters.length
				- n; i++) {/* Assures we can get the most number of ngrams without overflow. */
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

	public TreeMap<String, Double> getMONOGRAMS() {
		return MONOGRAMS;
	}

	public TreeMap<String, Double> getBIGRAMS() {
		return BIGRAMS;
	}

	public TreeMap<String, Double> getTRIGRAMS() {
		return TRIGRAMS;
	}

	public TreeMap<String, Double> getQUADGRAMS() {
		return QUADGRAMS;
	}

	public TreeMap<String, Double> getPENTAGRAMS() {
		return PENTAGRAMS;
	}
}
