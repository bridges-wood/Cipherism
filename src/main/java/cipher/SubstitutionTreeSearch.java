package cipher;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

/**
 * Class implementing the Monte-Carlo Tree Search for solving substitution
 * ciphers used by the University of Alberta.
 * 
 * @author Max Wood
 * @see <a href="https://www.aclweb.org/anthology/C14-1218.pdf">Paper</a>
 */
public class SubstitutionTreeSearch {

	private Substitution s;
	private PredictWords p;
	private DetectEnglish d;
	private FileIO u;
	private final SearchNode ORIGIN;
	private double cLambda1, cLambda2, cLambda3, wLambda1, wLambda2, wLambda3;
	private final TreeMap<String, Double> C1, C2, C3, W1, W2, W3;
	private final TreeMap<String, LinkedList<String>> words1, words2, words3;
	private HashMap<Long, Mapping[]> mappings = new HashMap<Long, Mapping[]>();
	private double Nc; // Number of letters in the corpus.
	private double Nw; // Number of words in the corpus.
	private final int POOL_SIZE = 8;
	private final double C = 1;
	private String TEXT;
	private LinkedList<SearchNode> path = new LinkedList<SearchNode>();

	public SubstitutionTreeSearch(Substitution s, Mapping[] initialKey, FileIO u, PredictWords p, DetectEnglish d) {
		this.s = s;
		this.u = u;
		this.p = p;
		this.d = d;
		this.ORIGIN = new SearchNode(0, initialKey, null);
		C1 = u.loadNgramMap(u.MONOGRAM_MAP_PATH);
		C2 = u.loadNgramMap(u.BIGRAM_MAP_PATH);
		C3 = u.loadNgramMap(u.TRIGRAM_MAP_PATH);
		W1 = u.loadNgramMap(u.MONOGRAM_COUNTS_MAP_PATH);
		W2 = u.loadNgramMap(u.BIGRAM_COUNTS_MAP_PATH);
		W3 = u.loadNgramMap(u.TRIGRAM_COUNTS_MAP_PATH);
		words1 = u.loadCharacterIndexForm(u.MONOGRAM_CIF_PATH);
		words2 = u.loadCharacterIndexForm(u.BIGRAM_CIF_PATH);
		words3 = u.loadCharacterIndexForm(u.TRIGRAM_CIF_PATH);
		Nc = 4.374127904E9;
		Nw = 5.80296995127E11;
		generateCLambdas();
		generateWLambdas();
	}

	/**
	 * Generates the optimal decryption of the cipher text based on word and
	 * character level n-grams.
	 * 
	 * @param text   The text to be decrypted.
	 * @param spaced Whether or not the spaces in the original text are to be
	 *               preserved.
	 * @return An array of Mappings corresponding to the decryption of the text.
	 */
	public Mapping[] monteCarloTreeSearch(String text, boolean spaced) {
		TEXT = text;
		String decrypted = s.decrypt(TEXT, ORIGIN.getKEY());
		while (u.deSpace(d.graphicalRespace(decrypted, 20)).length() < 0.5 * TEXT.length()) {
			path.clear();
			SearchNode leaf = findLeaf(ORIGIN);
			SearchNode bestChild = expand(leaf);
			path.add(bestChild);
			scorePath();
			decrypted = s.decrypt(TEXT, bestChild.getKEY());
		}
		return ORIGIN.getKEY();
	}

	/**
	 * Scores all nodes in the current path to the leaf node.
	 */
	private void scorePath() {
		double hiScore = Double.MIN_VALUE;
		for (SearchNode node : path) {
			double nodeScore = node.getScore();
			if (nodeScore > hiScore)
				hiScore = nodeScore;
		}
		for (SearchNode node : path) {
			node.setScore(hiScore);
		}
	}

	/**
	 * Generates the next best decryption of the cipher based on the current best.
	 * 
	 * @param leaf The current best node in the Monte-Carlo search tree.
	 * @return The best child of the leaf.
	 */
	private SearchNode expand(SearchNode leaf) {
		List<Mapping[]> leaves = generateKeyMutations(leaf.getKEY());
		SearchNode best = new SearchNode(Double.MIN_VALUE, null, null);
		for (Mapping[] key : leaves) {
			System.out.println(MappingArrayToString(key)); // TODO this does not appear to change in subsequent
															// iterations. Could be due to keyScore().
			if (!mappings.containsKey(u.hash64(MappingArrayToString(orderKey(key))))) {
				// If the key has not been seen before... This reduces overall processing time.
				double score = keyScore(key, true);
				SearchNode child = new SearchNode(score, key, leaf);
				leaf.addChild(child);
				addToHashMap(key);
				if (score > best.getScore()) {
					best = child;
				}
			}
			addToHashMap(orderKey(key));
		}
		return best;
	}

	/**
	 * Converts a given array of Mappings to a string.
	 * 
	 * @param key A key for decryption of the cipher.
	 * @return The string representation of the key.
	 */
	private String MappingArrayToString(Mapping[] key) {
		StringBuilder representation = new StringBuilder();
		for (Mapping m : key) {
			representation.append(m.toString());
		}
		return representation.toString();
	}

	/**
	 * Method to aid in abstraction of putting Mapping arrays into a hash map so
	 * they can be compared.
	 * 
	 * @param key The mapping array to be inserted into the map.
	 */
	private void addToHashMap(Mapping[] key) {
		mappings.put(u.hash64(MappingArrayToString(key)), key);
	}

	/**
	 * Sorts a given key into alphabetical order based on the cipher character for
	 * each Mapping.
	 * 
	 * @param key The Mapping array to be sorted.
	 * @return The sorted array.
	 */
	private Mapping[] orderKey(Mapping[] key) {
		Arrays.sort(key, new Comparator<Mapping>() {
			// Custom comparator to order the key alphabetically.
			public int compare(Mapping o1, Mapping o2) {
				Character c1 = Character.valueOf(o1.getCipherChar());
				Character c2 = Character.valueOf(o2.getCipherChar());
				return c1.compareTo(c2);
			}
		});
		return key;
	}

	/**
	 * Recursively selects the optimal path from the root node to the best leaf of
	 * the search-tree.
	 * 
	 * @param current The node currently being evaluated.
	 * @return The leaf node.
	 */
	private SearchNode findLeaf(SearchNode current) {
		current.incrementVisited();
		path.add(current);
		if (current.getChildren().isEmpty()) {
			return current;
		} else {
			SearchNode best = new SearchNode(Double.MIN_VALUE, null, null);
			for (SearchNode node : current.getChildren()) {
				double score = node.UCB(C);
				if (score > best.getScore()) {
					best = node;
				}
			}
			return findLeaf(best);
		}
	}

	/**
	 * Generates the character lambda coefficients for determining the score of a
	 * decrypted text.
	 */
	private void generateCLambdas() {
		for (String key : C3.keySet()) {
			String[] letters = key.split("");
			double three = fetch(key) - 1 / fetch(letters[0] + letters[1]) - 1;
			double two = fetch(letters[1] + letters[2]) - 1 / fetch(letters[1]) - 1;
			double one = fetch(letters[2]) - 1 / Nc - 1;
			double[] array = { one, two, three };
			switch (determineMaximum(array)) {
			case 0:
				cLambda3 += fetch(key);
				break;
			case 1:
				cLambda2 += fetch(key);
				break;
			case 2:
				cLambda1 += fetch(key);
				break;
			}
			/*
			 * Each of the lambdas is changed based on the most likely occurrence of either
			 * the character, the character and the previous character, or the character and
			 * its 2 previous characters.
			 */
		}
		// Normalisation.
		double sum = cLambda1 + cLambda2 + cLambda3;
		cLambda1 = sum / cLambda1;
		cLambda2 = sum / cLambda2;
		cLambda3 = sum / cLambda3;
	}

	/**
	 * Generates the word lambda coefficients for determining the score of a
	 * decrypted cipher text.
	 */
	private void generateWLambdas() {
		for (String key : W3.keySet()) {
			String[] words = key.split(",");
			double three = fetch(key) - 1 / fetch(words[0] + "," + words[1]) - 1;
			double two = fetch(words[1] + "," + words[2]) - 1 / fetch(words[1]) - 1;
			double one = fetch(words[2]) - 1 / Nw - 1;
			double[] array = { one, two, three };
			switch (determineMaximum(array)) {
			case 0:
				wLambda1 += fetch(key);
				break;
			case 1:
				wLambda2 += fetch(key);
				break;
			case 2:
				wLambda3 += fetch(key);
				break;
			}
		}
		// Normalisation.
		double sum = wLambda1 + wLambda2 + wLambda3;
		wLambda1 = sum / wLambda1;
		wLambda2 = sum / wLambda2;
		wLambda3 = sum / wLambda3;
	}

	/**
	 * Determines the index of the maximum value in an array of doubles.
	 * 
	 * @param values The array of doubles from which the maximum is to be found.
	 * @return The index of the maximum value in the array.
	 */
	private int determineMaximum(double[] values) {
		int maxIndex = 0;
		for (int i = 0; i < values.length; i++) {
			if (values[i] > values[maxIndex])
				maxIndex = i;
		}
		return maxIndex;
	}

	/**
	 * Scores a given simple-substitution cipher key.
	 * 
	 * @param mappings The key to be analysed.
	 * @param spaced   Whether or not there are already spaces present in the text.
	 * @return A double representing the score of the key.
	 */
	public double keyScore(Mapping[] mappings, boolean spaced) {
		String toAnalyse = s.decrypt(TEXT, mappings);
		double chi = 0.5;
		if (spaced) {
			return chi * Math.log(characterScore(toAnalyse)) + (1 - chi) * Math.log(wordScore(toAnalyse));
		} else {
			toAnalyse = d.greedyRespace(toAnalyse, 20);
			return chi * Math.log(characterScore(toAnalyse)) + (1 - chi) * Math.log(wordScore(toAnalyse));
		}
	}

	/**
	 * Scores a given nGram based on the probability of its existence.
	 * 
	 * @param toAnalyse The nGram to be scored.
	 * @return The score of the sum of the probabilities of its constituent parts.
	 */
	private double wordScore(String toAnalyse) {
		String[] words = toAnalyse.split(" ");
		double score = 0;
		for (int i = 0; i < words.length; i++) {
			StringBuilder sb = new StringBuilder();
			sb.append(words[i]);
			score += wLambda1 * fetch(sb.toString());
			if (i >= 1) {
				sb.insert(0, words[i - 1] + ",");
				score += wLambda2 * fetch(sb.toString());
			}
			if (i >= 2) {
				sb.insert(0, words[i - 2] + ",");
				score += wLambda3 * fetch(sb.toString());
			}
		}
		return score;
	}

	/**
	 * Scores a given nGram based on the probability of its existence.
	 * 
	 * @param toAnalyse The nGram to be scored.
	 * @return The score of the sum of the probabilities of its constituent parts.
	 */
	private double characterScore(String toAnalyse) {
		char[] letters = toAnalyse.toCharArray();
		double score = 0;
		for (int i = 0; i < letters.length; i++) {
			StringBuilder sb = new StringBuilder();
			sb.append(letters[i]);
			score += cLambda1 * fetch(sb.toString());
			if (i >= 1) {
				sb.insert(0, letters[i - 1]);
				score += cLambda2 * fetch(sb.toString());
			}
			if (i >= 2) {
				sb.insert(0, letters[i - 2]);
				score += cLambda3 * fetch(sb.toString());
			}
		}
		return score;
	}

	/**
	 * Generates a list of mutated keys based on the occurrence of probable
	 * character-index-form representations in the cipher text.
	 * 
	 * @param parentKey The key on which the mutations are based on. This provides
	 *                  some categorisation within the search tree,
	 * @return A list of mutated keys.
	 * @throws Exception
	 */
	private List<Mapping[]> generateKeyMutations(Mapping[] parentKey) {
		scoredNgram[] bestNgrams = generateArray(POOL_SIZE);
		List<Mapping[]> childKeys = new LinkedList<Mapping[]>();
		for (int i = 1; i < 4; i++) {
			scoredNgram[] subGrams = bestPEquivalentNGrams(TEXT, i, POOL_SIZE);
			for (scoredNgram NGram : subGrams) {
				bestNgrams = replaceIfBetter(bestNgrams, NGram);
			}
		}
		for (int i = POOL_SIZE - 1; i >= 0; i--) {
			childKeys.add(mutateKey(parentKey, bestNgrams[i]));
		}
		return childKeys;
	}

	/**
	 * Generates a mutation of a given key based on a likely n-gram in the text.
	 * 
	 * @param parentKey   The key to be mutated.
	 * @param scoredNgram The n-gram on which the mutation is to be based.
	 * @return The mutated key.
	 */
	private Mapping[] mutateKey(Mapping[] parentKey, scoredNgram scoredNgram) {
		LinkedList<Character> seen = new LinkedList<Character>();
		Mapping[] mutatedKey = parentKey.clone();
		for (int i = 0; i < scoredNgram.getnGram().length(); i++) {
			char from = scoredNgram.getTextSource().charAt(i);
			char to = scoredNgram.getnGram().charAt(i);
			if (seen.contains(from)) {
				continue;
			} else
				seen.add(from);
			for (Mapping mapping : parentKey) {
				if (mapping.getCipherChar() == from) {
					mapping.setPlainChar(to);
					break;
				}
			}
		}
		return mutatedKey;
	}

	/**
	 * Generates the best k p-equivalent nGrams of size n from the target text.
	 * 
	 * @param cipherText The text from which the nGrams are to be derived.
	 * @param n          The length of the nGrams to be searched for.
	 * @param k          The number of nGrams to be examined.
	 * @return An array of the best k scored nGrams.
	 */
	private scoredNgram[] bestPEquivalentNGrams(String cipherText, int n, int k) {
		String[] words = cipherText.split(" ");
		scoredNgram[] bestnGrams = generateArray(k);
		for (int i = n - 1; i < words.length; i++) {
			StringBuilder nGram = new StringBuilder();
			for (int j = i - (n - 1); j <= i; j++) {
				nGram.append(words[j] + " ");
			}
			String toSearch = nGram.toString().substring(0, nGram.length() - 1);
			String searchable = p.toString(p.encodeWord(toSearch));
			LinkedList<String> pEquivalents = new LinkedList<String>();
			switch (n) {
			case 1:
				if (words1.containsKey(searchable)) {
					pEquivalents = words1.get(searchable);
				}
				break;
			case 2:
				if (words2.containsKey(searchable)) {
					pEquivalents = words2.get(searchable);
				}
				break;
			case 3:
				if (words3.containsKey(searchable)) {
					pEquivalents = words3.get(searchable);
				}
				break;
			}
			for (String pEquivalent : pEquivalents) {
				replaceIfBetter(bestnGrams, new scoredNgram(toSearch, pEquivalent, wordScore(pEquivalent)));
			}
		}
		return bestnGrams;
	}

	/**
	 * Searches an array of scoredNGrams and replaces the first nGram whose score
	 * does not exceed that of the nGram to be entered.
	 * 
	 * @param bestNgrams The array to searched.
	 * @param toTest     The scored nGram to be inserted if it has a sufficiently
	 *                   high score.
	 * @return The array of the best scored nGrams, with or without replacement.
	 */
	private scoredNgram[] replaceIfBetter(scoredNgram[] bestNgrams, scoredNgram toTest) {
		for (int i = 0; i < bestNgrams.length; i++) {
			if (toTest.score < bestNgrams[i].getScore() && i > 0) {
				bestNgrams[i - 1] = toTest;
				break;
			} else if (toTest.score == bestNgrams[i].getScore()) {
				if (toTest.HammingDistance() < bestNgrams[i].HammingDistance()) {
					bestNgrams[i] = toTest;
					break;
				}
			} else if (toTest.score > bestNgrams[i].getScore()) {
				continue;
			} else {
				break;
			}
		}
		return bestNgrams;
	}

	/**
	 * An interface between the gets to the maps of n-grams and their occurrences to
	 * avoid null returns when the value is not found. If the combination is not
	 * found it is assumed to be the minimum possible likelihood of occurring.
	 * 
	 * @param nGram The nGram to have its score fetched from its respective map.
	 * @return The score of the nGram.
	 */
	private double fetch(String nGram) {
		// Determining all the characteristics of the nGram.
		boolean word = false;
		String[] parts = nGram.split(",");
		if (parts[0].length() > 1)
			word = true;
		int size = parts.length;
		Double toReturn = 0d;
		switch (size) {
		case 1:
			if (word) {
				toReturn = W1.get(nGram);
			} else {
				toReturn = C1.get(nGram);
			}
			break;
		case 2:
			if (word) {
				toReturn = W2.get(nGram);
			} else {
				toReturn = C2.get(nGram);
			}
			break;
		case 3:
			if (word) {
				toReturn = W3.get(nGram);
			} else {
				toReturn = C3.get(nGram);
			}
			break;
		}
		if (toReturn == null) {
			if (word) {
				toReturn = Math.pow(1 / Nw, size);
			} else {
				toReturn = Math.pow(1 / Nw, size);
			}
		}
		return toReturn;
	}

	/**
	 * Generates an array of scoredNgrams to avoid null-pointer exceptions when
	 * comparing scores.
	 * 
	 * @param length The length of the array to be generated.
	 * @return The array of minimum scored scoredNgrams.
	 */
	public scoredNgram[] generateArray(int length) {
		scoredNgram[] out = new scoredNgram[length];
		for (int i = 0; i < length; i++) {
			out[i] = new scoredNgram("", "", Double.MIN_VALUE);
		}
		return out;
	}

	/**
	 * A class to contain an nGram and its score in the same place to facilitate
	 * easy comparison.
	 * 
	 * @author Max Wood
	 */
	private class scoredNgram {
		private final String textSource;
		private final String nGram;
		private final Double score;

		public scoredNgram(String textSource, String nGram, double score) {
			this.textSource = textSource;
			this.nGram = nGram;
			this.score = score;
		}

		public String getTextSource() {
			return textSource;
		}

		public String getnGram() {
			return nGram;
		}

		public Double getScore() {
			return score;
		}

		/**
		 * Calculates the Hamming Distance between a p equivalent nGram and its current
		 * decryption.
		 * 
		 * @return An int representing the Hamming distance between the 2 strings.
		 */
		public int HammingDistance() {
			int distance = 0;
			for (int i = 0; i < this.getnGram().length(); i++) {
				if (this.getnGram().charAt(i) != this.getTextSource().charAt(i))
					distance++;
			}
			return distance;
		}
	}
}
