package cipher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PredictWords {

	private Utilities u;

	public PredictWords(Utilities u) {
		this.u = u;
	}

	/**
	 * Generate a series of possible words for an mono-alphabetically encoded
	 * string.
	 * 
	 * @param word
	 *            The encoded word from which possible words can be generated.
	 * @param probable
	 *            Whether or not the list of most probable words should be checked
	 *            or not.
	 * @return A String array containing all the possible words the encrypted string
	 *         could be.
	 */
	public String[] predictedWords(String word, boolean probable) {
		int[] encodedForm = encodeWord(word);
		List<String> possibleWords = new ArrayList<String>();
		String[] lines;
		if (probable) {
			lines = u.readFile("src\\main\\resources\\mostProbable.txt");
		} else {
			lines = u.readFile("src\\main\\resources\\dictionary.txt");
		}
		for (String line : lines) {
			if (line.length() == word.length()) {// If the lengths match... (This is to thin the field examined and
													// speed up access times).
				if (Arrays.equals(encodedForm, encodeWord(line))) { // If the encoded forms match...
					possibleWords.add(line);
				}
			}
		}
		return possibleWords.toArray(new String[0]);
	}

	/**
	 * Encode a string in character index form.
	 * 
	 * @param word
	 *            The string to be encoded with the character index system.
	 * @return An integer array of the encoded string in character index form.
	 */
	public int[] encodeWord(String word) {
		int[] encodedText = new int[word.length()]; // Create output array.
		ArrayList<Character> seen = new ArrayList<Character>(); // Create a list to determine which characters have been
																// seen before to avoid mis-encoding.
		for (int i = 0; i < word.length(); i++) {
			char letter = word.charAt(i);
			if (!seen.contains(letter)) { // If the letter has been seen before...
				seen.add(letter);
			}
			encodedText[i] = seen.indexOf(letter); // Encode the letter in the output array.
		}
		return encodedText;
	}

	/**
	 * Encode a phrase in character index form.
	 * 
	 * @param phrase
	 *            The string to be encoded with the character index system.
	 * @return An integer array of the encoded string in character index form.
	 */
	public int[][] encodePhrase(String[] phrase) {
		int[][] encodedText = new int[phrase.length][]; // Create output array.
		ArrayList<Character> seen = new ArrayList<Character>(); // Create a list to determine which characters have been
																// seen before to avoid mis-encoding.
		for (int i = 0; i < phrase.length; i++) {
			encodedText[i] = new int[phrase[i].length()]; // Set the size of the jagged array.
			for (int j = 0; j < phrase[i].length(); j++) {
				char letter = phrase[i].charAt(j);
				if (!seen.contains(letter)) { // If the letter has been seen before...
					seen.add(letter);
				}
				encodedText[i][j] = seen.indexOf(letter); // Encode the letter in the output array.
			}
		}
		return encodedText;
	}
}
