import java.util.ArrayList;
import java.util.List;

public class MultiLemmaAnalysis {
	//TODO predict using word suggestions, what strings exist in the text.
	/**
	 * Generate possible lemata that the fractions of the string in the text could correspond to.
	 * 
	 * @param text A section of encoded ciphertext from which possible lemata are to be generated.
	 * @return
	 */
	public static String[] possibleLemmata (String text) {
		String[] words = text.replaceAll("[^a-zA-Z ]", "").toLowerCase().split("\\s+");
		int numWords = words.length;
		if(numWords < 2 || numWords > 5) {
			return null;
		}
		List<String> possibleLemata = new ArrayList<String>();
		String[] lines = null;
		int[][] encodedGroup = new int[numWords][];
		for(int i = 0; i < numWords; i++) {
			encodedGroup[i] = PredictWords.encodeWord(words[i]);
		}
		switch(numWords) {
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
		for(int i = 0; i < lines.length; i++) {
			String[] lemataGroup = lines[i].replaceAll("-|'|'.'", "").toLowerCase().split(",");
			for(String word : lemataGroup) {
				System.out.println(word);
			}
			int[][] encodedLGroup = new int[numWords][];
			for(int j = 0; j < numWords; j++) {
				encodedLGroup[j] = PredictWords.encodeWord(lemataGroup[j]);
			}
			if(encodedLGroup.equals(encodedGroup)) {
				possibleLemata.add(lines[i].replaceAll(",", " "));
			}
		}
		return possibleLemata.toArray(new String[0]);
	}
}
