package cipher;

import java.util.ArrayList;
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
	 * @param word     The encoded word from which possible words can be generated.
	 * @param probable Whether or not the list of most probable words should be
	 *                 checked or not.
	 * @return A String array containing all the possible words the encrypted string
	 *         could be.
	 */
	public String[] predictedWords(String word) {
		// TODO speed up this method by using a bucketed hashtable for each encoding.
		String encodedForm = encodeWord(word);
		List<String> possibleWords = new ArrayList<String>();
		String[] lines;
		lines = u.readFile(u.DICTIONARY_TEXT_PATH);
		for (String line : lines) {
			if (line.length() == word.length()) {// If the lengths match... (This is to thin the field examined and
													// speed up access times).
				if (encodedForm.equals(encodeWord(line))) { // If the encoded forms match...
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
	 * @return The encoded string in character index form.
	 */
	public String encodeWord(String word) {
		StringBuilder sb = new StringBuilder(); // Create output String.
		ArrayList<Character> seen = new ArrayList<Character>(); // Create a list to determine which characters have been
																// seen before to avoid mis-encoding.
		for (int i = 0; i < word.length(); i++) {
			char letter = word.charAt(i);
			if (!seen.contains(letter)) { // If the letter has been seen before...
				seen.add(letter);
			}
			sb.append(seen.indexOf(letter)); // Encode the letter in the output array.
		}
		return sb.toString();
	}

	/**
	 * Encode a phrase in character index form.
	 * 
	 * @param phrase The string to be encoded with the character index system.
	 * @return the encoded string in character index form.
	 */
	public String[] encodePhrase(String[] phrase) {
		String[] encodedPhrase = new String[phrase.length]; // Create output array.
		ArrayList<Character> seen = new ArrayList<Character>(); // Create a list to determine which characters have been
																// seen before to avoid mis-encoding.
		for(int i = 0; i < phrase.length; i++) {
			//For each word...
			String word = phrase[i];
			StringBuilder encoded = new StringBuilder();
			for(int j = 0; j < word.length(); j++) {
				char letter = word.charAt(j);
				if (!seen.contains(letter)) { // If the letter has been seen before...
					seen.add(letter);
				}
				encoded.append(seen.indexOf(letter));
			}
			encodedPhrase[i] = encoded.toString();
		}
		return encodedPhrase;
	}
}
