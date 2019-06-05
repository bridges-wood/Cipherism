import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

public class DetectEnglish {

	public static float detectEnglish(String text) {
		boolean spaced = false;
		for (int i = 0; i < text.length(); i++) {
			if (text.charAt(i) == ' ') {
				spaced = true;
				break;
			}
		}
		if (spaced) {
			// Iterate through and see what fraction of words are english.
			ArrayList<String> words = new ArrayList<String>(Arrays.asList(text.toLowerCase()
					.replaceAll(
							"[!\\\"\\�\\$\\%\\^\\&\\*\\(\\)\\_\\'\\+\\=\\{\\}\\[\\]\\;\\:\\@\\#\\~\\|\\<\\,\\.\\>\\/]",
							"")
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
			for(String word : toRemove) {
				System.out.println(word);
				words.remove(word);
				String[] split = word.split("-");
				for (String part : split) {
					words.add(part);
				}
			}
			Hashtable<Long, String> dictionaryTable = Utilities.readHashTable("hashed_dictionary.htb");
			float englishWords = 0f;
			for(String word : words) {
				long hashedWord = Utilities.hash64(word);
				if(dictionaryTable.containsKey(hashedWord)) {
					englishWords += 1;
				}
			}
			return(englishWords / words.size());
		} else {
			// Iterate through letter by letter, testing if it's a word at each stage. If a
			// word is found continue until all words of all lengths from that possible
			// start have been found. Use 2grams to find the most likely words to follow it,
			// and check succeeding characters for those words. If a sufficiently likely
			// pair is found, check for triples and so on. If a sufficiently high multi
			// lemata is found, generate the spaced text, delete the analysed text and move
			// on. If a successive string of characters does not exist as a word, remove the
			// first letter and check the succeeding characters. If this carries on for more
			// than 2 characters, output it to another function to search for and check if
			// the word is english but does not exist in a dictionary. (Note - dictionary
			// does contain a lot of names)
		}
		return 0;
	}
}
