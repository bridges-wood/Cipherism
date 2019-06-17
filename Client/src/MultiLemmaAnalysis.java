package src;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cipher.Utilities;

public class MultiLemmaAnalysis {
	/**
	 * Generate possible lemmata that the fractions of the string in the text could
	 * correspond to.
	 * 
	 * @param text A section of encoded ciphertext from which possible lemmata are to
	 *             be generated. This can only be 2 to 5 words in length.
	 * @return
	 */
	public static String[] possibleLemmata(String text) {
		String[] words = text.replaceAll("[^a-zA-Z ]", "").toLowerCase().split("\\s+"); // Removes all non-letter
																						// characters and splits by
																						// whitespace.
		int numWords = words.length;
		if (numWords < 2 || numWords > 5) { // Since we only have files with word combinations of up to size five...
			return null;
		}
		List<String> possibleLemata = new ArrayList<String>(); // Creates a dynamic list as we do not know how many
																// possible lemata will be drawn.
		String[] lines = null;
		int[][] encodedGroup = PredictWords.encodePhrase(words); // Encode the text in character index form.
		switch (numWords) { // Locates the file corresponding to the length of the input.
		case 2:
			lines = Utilities.readFile("2grams.txt");
			break;
		case 3:
			lines = Utilities.readFile("3grams.txt");
			break;
		case 4:
			lines = Utilities.readFile("4grams.txt");
			break;
		case 5:
			lines = Utilities.readFile("5grams.txt");
			break;
		}
		for (int i = 0; i < lines.length; i++) { // Iterates through the file, encoding each line in character index
													// form.
			int[][] encodedLGroup = PredictWords.encodePhrase(lines[i].toLowerCase().split(","));
			if (Arrays.deepEquals(encodedLGroup, encodedGroup)) { // If the two encodings are the same, a match is
																	// returned.
				possibleLemata.add(lines[i].replaceAll(",", " "));
			}
		}
		return possibleLemata.toArray(new String[0]); // Outputs in a fixed size array form for easy iteration.
	}
}
