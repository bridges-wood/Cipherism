import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.stream.Collectors;

public class DetectEnglish {

	public static final Hashtable<Long, String> dictionaryTable = Utilities.readHashTable("dictionary.htb");
	public static final Hashtable<Long, String> mostLikelyTable = Utilities.readHashTable("mostProbable.htb");

	/**
	 * Returns what fraction of the text can be called English.
	 * 
	 * @param text The text to be analysed.
	 * @return Float representing the fraction of text that can be classified as
	 *         English. Scores above 0.75 for unspaced text and 0.85 for spaced text
	 *         are good indications of English.
	 */
	// TODO Find every speed up I can for this, it's nowhere near fast enough.
	public static float detectEnglish(String text) {
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
					.split(" ")));
			ArrayList<String> toRemove = new ArrayList<String>();
			for (String word : words) {
				char[] letters = word.toCharArray();
				for (int i = 0; i < word.length(); i++) {
					if (letters[i] == '-') {
						toRemove.add(word);
					}
				}
			}
			for (String word : toRemove) {
				words.remove(word);
				String[] split = word.split("-");
				for (String part : split) {
					words.add(part);
				}
			}
			return isEnglish(words.toArray(new String[0]));
		} else {
			ArrayList<Character> letters = (ArrayList<Character>) text.replaceAll("[^a-zA-Z ]", "").chars()
					.mapToObj(e -> (char) e).collect(Collectors.toList());
			StringBuilder out = new StringBuilder();
			boolean changes = false;
			while (letters.size() > 0) {
				ArrayList<String>[] words = findWords(letters);
				ArrayList<String> foundWords = words[0];
				ArrayList<String> probableWords = words[1];
				ArrayList<GroupProbabilityPair> possibleGroups = new ArrayList<GroupProbabilityPair>();
				String[] lines = Utilities.readFile("2grams.txt");
				for (String word : probableWords) {
					StringBuilder succeeding = new StringBuilder();
					int length = word.length();
					switch (length) { // More cases can be added if I find more refined patterns in word lengths.
					case 1:
						for (int i = 0; i < 2 && i + length < letters.size(); i++) {
							succeeding.append(letters.get(length + i));
						}
						break;
					default:
						if (length < letters.size()) {
							succeeding.append(letters.get(length));
						}
						break;
					}
					for (int i = 0; i < lines.length; i++) {
						String line = lines[i];
						if (line.startsWith(word + "," + succeeding.toString())) {
							possibleGroups = rank(new GroupProbabilityPair(i, line.replaceAll(",", " ")),
									possibleGroups);
						}
					}
				}
				int[] toRemove = firstMatch(possibleGroups, letters); // Check if this equals 0, then examine words in
																		// foundWords.
				if (toRemove[0] > 0) {
					if (changes == false && out.length() > 0)
						out.append(" ");
					for (int i = 0; i < toRemove[0] && letters.size() > 0; i++) {
						out.append(letters.get(0));
						if (i == toRemove[1] - 1) {
							out.append(" ");
						}
						letters.remove(0);
					}
					out.append(" ");
					changes = true;
				} else {
					// Examine found words and select the most likely.
					lines = Utilities.readFile("mostProbable.txt");
					String wordToRemove = "";
					boolean found = false;
					for (String line : lines) {
						for (String word : foundWords) {
							if (line.equals(word)) {
								wordToRemove = word;
								found = true;
								break;
							}
						}
						if (found)
							break;
					}
					if (wordToRemove == "") {
						out.append(letters.get(0));
						letters.remove(0);
						changes = false;
					} else {
						for (int i = 0; i < wordToRemove.length(); i++) {
							out.append(letters.get(0));
							letters.remove(0);
						}
						out.append(" ");
						changes = true;
					}
				}
			}
			return isEnglish(out.toString().split(" "));
		}
	}

	/**
	 * Returns the fraction of words that are English in a given array.
	 * 
	 * @param words An array of words to be analysed for English text.
	 * @return Float representing the fraction of words in the array that are
	 *         English.
	 */
	public static float isEnglish(String[] words) {
		float englishWords = 0f;
		for (String word : words) {
			long hashedWord = Utilities.hash64(word);
			if (dictionaryTable.containsKey(hashedWord)) {
				englishWords += 1;
			}
		}
		return englishWords / words.length;
	}

	/**
	 * Find the first match with the text using the most likely groups of words.
	 * 
	 * @param possibleGroups A list of all the possible groups of words that could
	 *                       be in the text.
	 * @param letters
	 * @return An integer array of length 2, [0] being the length of the first word
	 *         to add and [1] being the position of the space.
	 */
	public static int[] firstMatch(ArrayList<GroupProbabilityPair> possibleGroups, ArrayList<Character> letters) {
		for (GroupProbabilityPair pair : possibleGroups) {
			char[] characters = pair.getGroup().replaceAll(" ", "").toCharArray();
			boolean match = true;
			for (int i = 0; i < characters.length && i < letters.size(); i++) {
				if (characters[i] != letters.get(i)) {
					match = false;
					break;
				}
			}
			if (match) {
				int a = pair.getGroup().length() - 1;
				int b = pair.getGroup().indexOf(" ");
				return new int[] { a, b };
			}
		}
		return new int[2];
	}

	/**
	 * Ranks all of the possible word pairs generated by the analysis of the text.
	 * 
	 * @param pair           The GroupProbabilityPair object to be inserted into the
	 *                       list into probability order.
	 * @param possibleGroups The list of currently sorted probability groups.
	 * @return The updated list of possible word groups.
	 */
	public static ArrayList<GroupProbabilityPair> rank(GroupProbabilityPair pair,
			ArrayList<GroupProbabilityPair> possibleGroups) {
		for (int i = 0; i < possibleGroups.size(); i++) { // Iterates through the list of possible word groups...
			if (pair.getRank() < possibleGroups.get(i).getRank()) {// When the the chance of getting the group being
																	// compared is higher than that in the position in
																	// the list...
				possibleGroups.add(i, pair); // Insert the group into that position.
				return possibleGroups;
			}
		}
		possibleGroups.add(pair); // If it's not more likely than any other existing group, add it to the end of
									// the list.
		return possibleGroups;
	}

	/**
	 * Finds the English words present in the first characters of a string.
	 * 
	 * @param letters An ArrayList of all letters in a given text.
	 * @return An array containing of size 2 with all words found at [0] and
	 *         probable words stored at [1].
	 */
	public static ArrayList<String>[] findWords(ArrayList<Character> letters) {
		ArrayList<String> foundWords = new ArrayList<String>();
		ArrayList<String> probableWords = new ArrayList<String>();
		StringBuilder word = new StringBuilder();
		int firstLength = 0;
		for (int i = 0; i < letters.size(); i++) { // Detecting the first word.
			word.append(letters.get(i));
			if (dictionaryTable.containsKey(Utilities.hash64(word.toString()))) {
				foundWords.add(word.toString()); // If the group of characters has a corresponding hash in the
													// dictionary, we've found a word.
				if (mostLikelyTable.containsKey(Utilities.hash64(word.toString()))) {
					probableWords.add(word.toString()); // If it also has a hash in the most likely words hashtable,
														// we've found a likely word.
				}
				firstLength = i; // This allows us to speed up our looking for words, as we can ignore letters
									// before this number, and we can avoid looking at words that are too long.
				break;
			}
		}
		for (int i = firstLength + 1; i < 45 && i < letters.size(); i++) { // Avoid ArrayIndexOutOfBoundsErrors and
																			// ignore
																			// words that are longer than the longest in
																			// English.
			word.append(letters.get(i));
			if (dictionaryTable.containsKey(Utilities.hash64(word.toString()))) {
				foundWords.add(word.toString());
				if (mostLikelyTable.containsKey(Utilities.hash64(word.toString()))) {
					probableWords.add(word.toString());
				}
			}
		}
		if (foundWords.size() > 0 && !probableWords.contains(foundWords.get(foundWords.size() - 1))) {
			// This introduces the longest found word into probable words, even if it isn't
			// particularly likely on its own. This is to try and mitigate over prediction
			// of short words.
			probableWords.add(foundWords.get(foundWords.size() - 1));
		}
		@SuppressWarnings("unchecked") // The cast from ArrayList to array has issues, this avoids unchecked type
										// exceptions.
		ArrayList<String>[] out = new ArrayList[2];
		out[0] = foundWords;
		out[1] = probableWords;
		return out;
	}
}
