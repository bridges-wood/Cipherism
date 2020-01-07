package cipher;

import java.util.TreeMap;

public class SubstitutionTreeSearch {
	private double cLambda1;
	private double cLambda2;
	private double cLambda3;
	private double wLambda1;
	private double wLambda2;
	private double wLambda3;
	private TreeMap<String, Double> c1;
	private TreeMap<String, Double> c2;
	private TreeMap<String, Double> c3;
	private TreeMap<String, Double> w1;
	private TreeMap<String, Double> w2;
	private TreeMap<String, Double> w3;
	private double Nc; // Number of letters in the corpus.
	private double Nw; // Number of words in the corpus.

	public Mapping[] run(Mapping[] initial) {
		// TODO load in all maps.
		generateCLambdas();
		generateWLambdas();
		// TODO add scoring method.
		// TODO add key mutation.
		// TODO add tree traversal.
		// TODO add node class.
		return initial;
	}

	public void generateCLambdas() {
		for (String key : c3.keySet()) {
			String[] letters = key.split("");
			double three = c3.get(key) - 1 / c2.get(letters[0] + letters[1]) - 1;
			double two = c2.get(letters[1] + letters[2]) - 1 / c1.get(letters[1]) - 1;
			double one = c1.get(letters[2]) - 1 / Nc - 1;
			double[] array = { one, two, three };
			switch (determineMaximum(array)) {
			case 0:
				cLambda3 += c3.get(key);
				break;
			case 1:
				cLambda2 += c3.get(key);
				break;
			case 2:
				cLambda1 += c3.get(key);
				break;
			}
		}
		// Normalisation.
		double sum = cLambda1 + cLambda2 + cLambda3;
		cLambda1 = sum / cLambda1;
		cLambda2 = sum / cLambda2;
		cLambda3 = sum / cLambda3;
	}

	public void generateWLambdas() {
		for (String key : w3.keySet()) {
			String[] words = key.split(",");
			double three = w3.get(key) - 1 / w2.get(words[0] + "," + words[1]) - 1;
			double two = w2.get(words[1] + "," + words[2]) - 1 / w1.get(words[1]) - 1;
			double one = w1.get(words[2]) - 1 / Nw - 1;
			double[] array = { one, two, three };
			switch (determineMaximum(array)) {
			case 0:
				wLambda1 += w3.get(key);
				break;
			case 1:
				wLambda2 += w3.get(key);
				break;
			case 2:
				wLambda3 += w3.get(key);
				break;
			}
		}
		// Normalisation.
		double sum = wLambda1 + wLambda2 + wLambda3;
		wLambda1 = sum / wLambda1;
		wLambda2 = sum / wLambda2;
		wLambda3 = sum / wLambda3;
	}

	public int determineMaximum(double[] values) {
		int maxIndex = 0;
		for (int i = 0; i < values.length; i++) {
			if (values[i] > values[maxIndex])
				maxIndex = i;
		}
		return maxIndex;
	}
}
