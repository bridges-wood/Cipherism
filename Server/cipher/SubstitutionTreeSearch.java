package cipher;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

public class SubstitutionTreeSearch {

	private Substitution s;
	private PredictWords p;
	private DetectEnglish d;
	private Utilities u;
	private final SearchNode ORIGIN;
	private double cLambda1, cLambda2, cLambda3, wLambda1, wLambda2, wLambda3;
	private final TreeMap<String, Double> C1, C2, C3, W1, W2, W3;
	private final TreeMap<String, LinkedList<String>> words1, words2, words3;
	private HashMap<Long, Mapping[]> mappings = new HashMap<Long, Mapping[]>();
	private double Nc; // Number of letters in the corpus.
	private double Nw; // Number of words in the corpus.
	private final int POOL_SIZE = 8;
	private final double C = 1;
	private LinkedList<SearchNode> path = new LinkedList<SearchNode>();

	public SubstitutionTreeSearch(Substitution s, Mapping[] initialKey, Utilities u, PredictWords p, DetectEnglish d) {
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

	public Mapping[] run(String text, boolean spaced) {
		String decrypted = s.decrypt(text, ORIGIN.getKEY());
		while (u.deSpace(d.graphicalRespace(decrypted, 20)).length() < 0.5 * text.length()) {
			path.clear();
			SearchNode leaf = findLeaf(ORIGIN);
			SearchNode bestChild = expand(leaf, text);
			path.add(bestChild);
			scorePath();
			decrypted = s.decrypt(text, bestChild.getKEY());
		}
		return ORIGIN.getKEY();
	}

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

	private SearchNode expand(SearchNode leaf, String text) {
		List<Mapping[]> leaves = generateKeyMutations(text, leaf.getKEY());
		double hiScore = Double.MIN_VALUE;
		SearchNode best = null;
		for (Mapping[] key : leaves) {
			if (!mappings.containsKey(u.hash64(MappingArrayToString(orderKey(key))))) {
				double score = keyScore(text, key, true);
				SearchNode child = new SearchNode(score, key, leaf);
				leaf.addChild(child);
				addToHashMap(key);
				if (score > hiScore) {
					hiScore = score;
					best = child;
				}
			}
			addToHashMap(orderKey(key));
		}
		return best;
	}

	private String MappingArrayToString(Mapping[] key) {
		StringBuilder representation = new StringBuilder();
		for (Mapping m : key) {
			representation.append(m.toString());
		}
		return representation.toString();
	}

	private void addToHashMap(Mapping[] key) {
		mappings.put(u.hash64(MappingArrayToString(key)), key);
	}

	private Mapping[] orderKey(Mapping[] key) {
		Arrays.sort(key, new Comparator<Mapping>() {
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
			double hiScore = Double.MIN_VALUE;
			SearchNode best = null;
			for (SearchNode node : current.getChildren()) {
				double score = node.UCB(C);
				if (score > hiScore) {
					hiScore = score;
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
			double three = C3.get(key) - 1 / C2.get(letters[0] + letters[1]) - 1;
			double two = C2.get(letters[1] + letters[2]) - 1 / C1.get(letters[1]) - 1;
			double one = C1.get(letters[2]) - 1 / Nc - 1;
			double[] array = { one, two, three };
			switch (determineMaximum(array)) {
			case 0:
				cLambda3 += C3.get(key);
				break;
			case 1:
				cLambda2 += C3.get(key);
				break;
			case 2:
				cLambda1 += C3.get(key);
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
			double three = W3.get(key) - 1 / W2.get(words[0] + "," + words[1]) - 1;
			double two = W2.get(words[1] + "," + words[2]) - 1 / W1.get(words[1]) - 1;
			double one = W1.get(words[2]) - 1 / Nw - 1;
			double[] array = { one, two, three };
			switch (determineMaximum(array)) {
			case 0:
				wLambda1 += W3.get(key);
				break;
			case 1:
				wLambda2 += W3.get(key);
				break;
			case 2:
				wLambda3 += W3.get(key);
				break;
			}
		}
		// Normalisation.
		double sum = wLambda1 + wLambda2 + wLambda3;
		wLambda1 = sum / wLambda1;
		wLambda2 = sum / wLambda2;
		wLambda3 = sum / wLambda3;
	}

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
	 * @param text     The cipher-text to be decrypted.
	 * @param mappings The key to be analysed.
	 * @param spaced   Whether or not there are already spaces present in the text.
	 * @return A double representing the score of the key.
	 */
	public double keyScore(String text, Mapping[] mappings, boolean spaced) {
		String toAnalyse = s.decrypt(text, mappings);
		if (spaced) {
			double chi = 0.5;
			return chi * Math.log(characterScore(toAnalyse)) + (1 - chi) * Math.log(wordScore(toAnalyse));
		} else {
			// TODO implement greedy word search algorithm.
			return 0;
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
			if (W1.get(sb.toString()).equals(null)) {
				score += wLambda1 * (1 / Nw);
			} else {
				score += wLambda1 * W1.get(sb.toString());
			}
			if (i >= 1) {
				sb.insert(0, words[i - 1] + ",");
				if (W2.get(sb.toString()).equals(null)) {
					score += wLambda2 * (1 / Nw);
				} else {
					score += wLambda2 * W2.get(sb.toString());
				}
			}
			if (i >= 2) {
				sb.insert(0, words[i - 2] + ",");
				if (W3.get(sb.toString()).equals(null)) {
					score += wLambda3 * W3.get(sb.toString());
				} else {
					score += wLambda3 * W3.get(sb.toString());
				}
			}
		}
		return score;
	}

	private double characterScore(String toAnalyse) {
		char[] letters = toAnalyse.toCharArray();
		double score = 0;
		for (int i = 0; i < letters.length; i++) {
			StringBuilder sb = new StringBuilder();
			sb.append(letters[i]);
			score += cLambda1 * C1.get(sb.toString());
			if (i >= 1) {
				sb.insert(0, letters[i - 1]);
				score += cLambda2 * C2.get(sb.toString());
			}
			if (i >= 2) {
				sb.insert(0, letters[i - 2]);
				score += cLambda3 * C3.get(sb.toString());
			}
		}
		return score;
	}

	/**
	 * Generates a list of mutated keys based on the occurrence of probable
	 * character-index-form representations in the cipher text.
	 * 
	 * @param cipherText The text from which the mutations are to be derived from.
	 * @param parentKey  The key on which the mutations are based on. This provides
	 *                   some categorisation within the search tree,
	 * @return A list of mutated keys.
	 * @throws Exception
	 */
	private List<Mapping[]> generateKeyMutations(String cipherText, Mapping[] parentKey) {
		scoredNgram[] bestNgrams = new scoredNgram[POOL_SIZE];
		List<Mapping[]> childKeys = new LinkedList<Mapping[]>();
		for (int i = 1; i < 4; i++) {
			scoredNgram[] subGrams = bestPEquivalentNGrams(cipherText, i, POOL_SIZE);
			for (scoredNgram NGram : subGrams) {
				bestNgrams = replaceIfBetter(bestNgrams, NGram);
			}
		}
		for (int i = POOL_SIZE - 1; i >= 0; i--) {
			childKeys.add(mutateKey(parentKey, bestNgrams[i]));
		}
		return childKeys;
	}

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
		scoredNgram[] bestnGrams = new scoredNgram[k];
		for (int i = n - 1; i < words.length; i++) {
			StringBuilder nGram = new StringBuilder();
			for (int j = i - (n - 1); j < i; j++) {
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
	 * A class to contain an nGram and its score in the same place to facilitate
	 * easy comparison.
	 * 
	 * @author woodmb
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
