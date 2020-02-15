package cipher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.TreeMap;

/**
 * A class that contains most of the methods to do with the detection of English
 * in decrypted text and the subsequent respacing.
 * 
 * @author Max Wood
 *
 */
public class DetectEnglish {

	private HashMap<Long, String> dictionaryTable, twoGramsTable;
	private final TreeMap<Character, Double> letterProbabilities;
	private FileIO u;
	private NGramAnalyser n;
	private WordGraph start = new WordGraph("", null);

	public DetectEnglish(FileIO u, NGramAnalyser n) {
		dictionaryTable = u.readHashTable(u.DICTIONARY_HASH_PATH);
		twoGramsTable = u.readHashTable(u.BIGRAM_WORD_HASH_PATH);
		letterProbabilities = u.readLetterFrequencies(u.LETTER_FREQUENCIES_MAP_PATH);
		this.u = u;
		this.n = n;
	}

	/**
	 * Returns what fraction of the text can be called English.
	 * 
	 * @param text The text to be analysed.
	 * @return Float representing the fraction of text that can be classified as
	 *         English. Scores above 0.75 for un-spaced text and 0.85 for spaced
	 *         text are good indications of English.
	 */
	public double detectEnglish(String text) {
		text = text.toLowerCase();
		boolean spaced = false;
		for (int i = 0; i < text.length(); i++) {
			if (text.charAt(i) == ' ') {
				spaced = true;
				break;
			}
		}
		if (spaced) {
			ArrayList<String> words = new ArrayList<String>(Arrays.asList(text.replaceAll(
					"[!\\\"\\�\\$\\%\\^\\&\\*\\(\\)\\_\\'\\+\\=\\{\\}\\[\\]\\;\\:\\@\\#\\~\\|\\<\\,\\.\\>\\/]", "")
					.split(" "))); // Removal of all unwanted characters, leaving only hyphens and alphabetical
									// characters and conversion to list.
			ArrayList<String> toRemove = new ArrayList<String>();
			for (String word : words) {
				char[] letters = word.toCharArray();
				for (int i = 0; i < word.length(); i++) {
					if (letters[i] == '-') {
						toRemove.add(word);
						/*
						 * If a word contains a '-' it needs to cleaned up because the dictionary does
						 * not deal well with compounded words.
						 */
					}
				}
			}
			for (String word : toRemove) { // For all the hyphenated words...
				words.remove(word);
				String[] split = word.split("-");
				for (String part : split) { // Split them by the hyphen and add the separate parts back in.
					words.add(part);
				}
			}
			return isEnglish(words.toArray(new String[0]));
		} else {
			return u.deSpace(graphicalRespace(text, 20)).length() / text.length();
			/*
			 * This is due to the fact that if there are any non-English words in the text,
			 * by nature they will not appear in the re-spaced text, so a different approach
			 * must be taken to account for this.
			 */
		}
	}

	/**
	 * Returns the fraction of words that are English in a given array.
	 * 
	 * @param words An array of words to be analysed for English text.
	 * @return Float representing the fraction of words in the array that are
	 *         English.
	 */
	public double isEnglish(String[] words) {
		if (words.length == 0) {
			return 0;
		}
		float englishWords = 0f;
		for (String word : words) {
			long hashedWord = u.hash64(word);
			if (dictionaryTable.containsKey(hashedWord)) {
				englishWords += 1;
			}
		}
		return englishWords / words.length;
	}

	/**
	 * Re-spaces text using a graphical method. Sub-methods:
	 *
	 * <p>
	 * {@link #traverse(WordGraph, String, int)}
	 * </p>
	 * <p>
	 * {@link #score(WordGraph)}
	 * </p>
	 * <p>
	 * {@link #prune(WordGraph)}
	 * </p>
	 * <p>
	 * {@link #reconstruct(WordGraph, StringBuilder)}
	 * </p>
	 *
	 * @param text          The text to be re-spaced.
	 * @param maxWordLength The maximum length of a word that could be possibly
	 *                      recognised.
	 * @return The input text, re-spaced.
	 */
	public String graphicalRespace(String text, int maxWordLength) {
		start.Clear();
		traverse(start, text, maxWordLength);
		score(start);
		prune(start);
		return reconstruct(start, new StringBuilder()).toString().trim();
	}

	/**
	 * Traverses the input text, building a graph up of possible words in the text
	 * and possible words succeeding it.
	 * 
	 * @param parent        The node in the graph from which successive words are
	 *                      drawn from.
	 * @param text          The input text to be examined for words.
	 * @param maxWordLength The maximum length of a word that could be possibly
	 *                      recognised.
	 * @return A graph representing all possible English word combinations within
	 *         the text.
	 */
	private WordGraph traverse(WordGraph parent, String text, int maxWordLength) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < maxWordLength && i < text.length(); i++) {
			sb.append(text.charAt(i));
			if (dictionaryTable.containsKey(u.hash64(sb.toString()))) {
				parent.getChildren().add(new WordGraph(sb.toString(), parent));
			}
		}
		if (parent.getChildren().size() > 0) {
			for (WordGraph child : parent.getChildren()) {
				if (text.length() > child.getWord().length()) {
					traverse(child, text.substring(child.getWord().length()), maxWordLength);
				}
			}
		}

		return parent;
	}

	/**
	 * Moves through a graph of possible words in text, scoring each node based on
	 * whether or not its children are English words.
	 * 
	 * @param parent The node from which children are drawn from to calculate its
	 *               score.
	 * @return The word-graph injested, with scores for each node representing the
	 *         number of children per node that are English words.
	 */
	private WordGraph score(WordGraph parent) {
		if (parent.getChildren().isEmpty()) {
			parent.score = parent.getWord().length();
		} else {
			for (WordGraph child : parent.getChildren()) {
				score(child);
				parent.score = parent.score + child.score;
				if (twoGramsTable.containsKey(u.hash64(parent.getWord() + "," + child.getWord()))) {
					child.setScore(child.getScore() * 2 + child.getWord().length() * 2);
					/*
					 * Score adjustment based on whether the n-gram of the word and succeeding word
					 * is common.
					 */
				} else {
					child.setScore(child.getScore() / 2);
				}
			}
		}
		return parent;
	}

	/**
	 * For each level on the word-graph, the node with the highest score remains.
	 * 
	 * @param parent The node from which child nodes are drawn.
	 * @return A path graph of all the detectable English words that can be
	 *         predicted to exist in the target text.
	 */
	private WordGraph prune(WordGraph parent) {
		if (!parent.getChildren().isEmpty()) {
			WordGraph maxChild = parent.getChildren().get(0);
			for (WordGraph child : parent.getChildren()) {
				if (child.score > maxChild.score) {
					maxChild = child;
				}
			}
			parent.setChildren(new LinkedList<WordGraph>());
			parent.addChild(maxChild);
			prune(maxChild);
		}
		return parent;
	}

	/**
	 * Generates an English string from an input path graph.
	 * 
	 * @param parent The node from which child nodes are drawn.
	 * @param sb     A string-builder used during recursive calls of this function.
	 * @return A string-builder containing all the English words in the path graph,
	 *         concatenated.
	 */
	private StringBuilder reconstruct(WordGraph parent, StringBuilder sb) {
		sb.append(parent.getWord() + " ");
		if (!parent.getChildren().isEmpty()) {
			sb = reconstruct(parent.getChildren().get(0), sb);
		}
		return sb;
	}

	/**
	 * Gives the score of the given text as how close the text is to English.
	 * 
	 * @param text The text to be analysed.
	 * @return The Chi Squared value.
	 */
	public double chiSquaredTest(String text) {
		double length = text.length();
		double score = 0;
		TreeMap<String, Integer> letterFrequencies = n.frequencyAnalysis(text);
		for (Character letter : letterProbabilities.keySet()) {
			double expectedCount = letterProbabilities.get(letter) * length;
			score += Math.pow(letterFrequencies.get(letter.toString()) - expectedCount, 2) / expectedCount;
			// (Oi - Ei)^2 / Ei
		}
		return score;
	}

	/**
	 * Determines if a single word exists in the English language.
	 * 
	 * @param word The word to be examined
	 * @return Boolean whether word is English or not.
	 */
	public boolean isEnglish(String word) {
		return dictionaryTable.containsKey(u.hash64(word));
	}

	/**
	 * A recursive greedy word-search algorithm designed to operate quickly rather
	 * than accurately on a given piece of English text.
	 * 
	 * @param toAnalyse The text to be re-spaced.
	 * @param longest   The length of the longest possible word to be found.
	 * @return The re-spaced text.
	 */
	private String greedyRespace(String toAnalyse, int longest) {
		if (toAnalyse.length() == 0)
			return "";
		for (int wordLength = longest; wordLength >= 2; wordLength--) {
			for (int i = 0; i <= toAnalyse.length() - wordLength; i++) {
				String possWord = toAnalyse.substring(i, i + wordLength);
				if (isEnglish(possWord)) {
					return greedyRespace(toAnalyse.substring(0, i), longest) + " " + possWord + " "
							+ greedyRespace(toAnalyse.substring(wordLength + i), longest);
				}
			}
		}
		return toAnalyse;
	}

	/**
	 * A wrapper method to handle String formatting for {@link greedyRespace}.
	 * 
	 * @param toAnalyse The String to be re-spaced.
	 * @param longest   The length of the longest word that could be found.
	 * @return A properly formatted version of the re-spaced text.
	 */
	public String greedyWrapper(String toAnalyse, int longest) {
		return greedyRespace(toAnalyse, longest).replace("  ", " ").strip();
	}
}
