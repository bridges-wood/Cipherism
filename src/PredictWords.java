import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PredictWords {
	/**
	 * Generate a series of possible words for an mono-alphabetically encoded
	 * string.
	 * 
	 * @param word     The encoded word from which possible words can be generated.
	 * @param probable Whether or not the list of most probable words should be
	 *                 checked or not.
	 * @return A String array containing all the possible words the encrypted string
	 *         could be.
	 */
	public static String[] predictedWords(String word, boolean probable) {
		int[] encodedForm = encodeWord(word);
		List<String> possibleWords = new ArrayList<String>();
		String[] lines;
		if (probable) {
			lines = Utilities.readFile("mostProbable.txt");
		} else {
			lines = Utilities.readFile("dictionary.txt");
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
	 * @param word The string to be encoded with the character index system.
	 * @return An integer array of the encoded string in character index form.
	 */
	public static int[] encodeWord(String word) {
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
}
