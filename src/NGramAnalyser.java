import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class NGramAnalyser {

	TreeMap<String, Float> analysis(int n, String text, boolean isSpaced) {
		TreeMap<String, Float> ngrams = new TreeMap<String, Float>();
		// Breakdown by words or not
		String[] words = null;
		if (isSpaced) {
			words = text.split(" ");
		}
		for (String word : words) {
			if (n < word.length()) {
				char[] chars = word.toCharArray();
				StringBuilder ngram = new StringBuilder();
				/*
				 * abcdef l:6 
				 * 2 5 
				 * 3 4 
				 * 4 3 
				 * 5 2 
				 * 6 1
				 */
				// Generate ngrams for each word.
				for (int i = 0; i < (chars.length - n) + 2; i++) { //NOTE: Deal with odd behaviour adding more and more to words and remove punctuation.
					if (i == 0) {
						ngram.append(' ');
					} else {
						ngram.append(chars[i - 1]);
					}
					for (int j = 1; j < n - 1; j++) {
						ngram.append(chars[i + j]);
					}
					if(i + n > chars.length) {
						ngram.append(' ');
					}
					// Add ngrams to tree.
					String finalNgram = ngram.toString();
					if(ngrams.containsKey(finalNgram)) {
						ngrams.put(finalNgram, ngrams.get(finalNgram) + 1); 
					} else {
						ngrams.put(finalNgram, (float) 1);
					}
				}
			} else {
				continue;
			}
		}
		// Set float to fraction of the total the n gram represents.
		// Test is to be on past cipher challenge answers to analyse how the author of
		// the puzzles writes.
		return null;
	}

}
