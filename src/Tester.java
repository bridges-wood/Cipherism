import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class Tester {

	public static void main(String[] args) {
		int n = 1;
		TreeMap<String, Float> ngrams = new TreeMap<String, Float>();
		// TODO Auto-generated method stub

		String text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Duis faucibus metus et dui faucibus imperdiet. Vivamus accumsan tortor eget pharetra pharetra. Phasellus urna metus, iaculis eu suscipit a, rhoncus sit amet arcu. Proin et nulla diam. Ut ut tellus viverra, fringilla enim vel, porta diam. Donec in dolor ut metus mattis malesuada. Sed ultrices ultrices sollicitudin. Etiam pulvinar condimentum accumsan.\r\n" + 
				"\r\n" + 
				"Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Etiam non erat mollis, vulputate est sit amet, scelerisque risus. Quisque mollis massa quis tortor fermentum, at dignissim lorem porta. Donec aliquam lorem velit, in interdum metus imperdiet ut. Mauris auctor pretium nibh at bibendum. Nullam velit risus, blandit ut mi at, blandit blandit mauris. Aliquam eu quam sit amet est porta tempor. Proin convallis lorem vel ornare interdum. Morbi imperdiet, dolor at sodales sagittis, velit ipsum consectetur leo, quis pharetra massa libero ac velit. In in.";
		String[] words = text.replaceAll("[^a-zA-Z ]", "").toLowerCase().split("\\s+");
		for (String word : words) {
			String temp = word;
			word = " " + temp + " ";
			if (n <= word.length()) {
				char[] chars = word.toCharArray();
				// Generate ngrams for each word.
				for (int i = 0; i < (chars.length - n) + 1; i++) {
					StringBuilder ngram = new StringBuilder();
					for (int j = 0; j < n; j++) {
						ngram.append(chars[i + j]);
					}

					// Add ngrams to tree.
					String finalNgram = ngram.toString();
					if (ngrams.containsKey(finalNgram)) {
						ngrams.put(finalNgram, ngrams.get(finalNgram) + 1);
					} else {
						ngrams.put(finalNgram, (float) 1);
					}
				}
			} else {
				continue;
			}
		}
		Set<Map.Entry<String, Float>> set = ngrams.entrySet();
		for (Map.Entry<String, Float> me : set) {
			System.out.println(me.getKey() + ":" + me.getValue());
		}
	}
}
