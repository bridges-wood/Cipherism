import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class NGramAnalyser {

	TreeMap<String, Float> analysis(int n, String text, boolean isSpaced){
		//Breakdown by words or not
		if(isSpaced) {
			String[] words = text.split(" ");
		}
		String[] words = null;
		for(String word : words) {
			if(n < word.length()) {
				//Iterate through word and add ngrams.
			} else {
				continue;
			}
		}
		//Generate ngrams for each word.
		//Add ngrams to tree.
		//Set float to fraction of the total the n gram represents.
		//Test is to be on past cipher challenge answers to analyse how the author of the puzzles writes.
		return null;
	}
	
}
