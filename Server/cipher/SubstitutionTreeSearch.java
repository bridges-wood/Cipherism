package cipher;

import java.util.TreeMap;

public class SubstitutionTreeSearch {

	private Substitution s;
	private final SearchNode ORIGIN;
	private double cLambda1;
	private double cLambda2;
	private double cLambda3;
	private double wLambda1;
	private double wLambda2;
	private double wLambda3;
	private final TreeMap<String, Double> C1; // In character tables, we need frequency of letters with spaces.
	private final TreeMap<String, Double> C2;
	private final TreeMap<String, Double> C3;
	private final TreeMap<String, Double> W1;
	private final TreeMap<String, Double> W2;
	private final TreeMap<String, Double> W3;
	private double Nc; // Number of letters in the corpus.
	private double Nw; // Number of words in the corpus.

	SubstitutionTreeSearch(Substitution s, Mapping[] initialKey) {
		this.s = s;
		this.ORIGIN = new SearchNode(0, initialKey, null);
		// TODO load in all maps.
		generateCLambdas();
		generateWLambdas();
	}

	public Mapping[] run(Mapping[] initial, String text, boolean spaced) {
		// TODO add key mutation.
		// TODO add tree traversal.
		// TODO add node class.
		return initial;
	}

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
		}
		// Normalisation.
		double sum = cLambda1 + cLambda2 + cLambda3;
		cLambda1 = sum / cLambda1;
		cLambda2 = sum / cLambda2;
		cLambda3 = sum / cLambda3;
	}

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
}
