package cipher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class BruteForce {

	private IOC i;
	private KasiskiExamination k;
	private Vigenere v;
	private DetectEnglish d;

	BruteForce() {
		i = new IOC();
		k = new KasiskiExamination();
		v = new Vigenere();
		d = new DetectEnglish();
	}

	public String bruteForcePeriodic(String text) {
		// Need a way to work out the best key.
		TreeMap<Double, Integer> keyLengths = new TreeMap<Double, Integer>();
		double[] periodicIOCs = i.peroidicIndexOfCoincidence(text);
		for (int i = 0; i < periodicIOCs.length; i++) {
			keyLengths.put(periodicIOCs[i], i + 2);
		}
		ArrayList<Integer> probableKeyLengths = new ArrayList<Integer>();
		for (int i = 0; !keyLengths.isEmpty() && i < keyLengths.size() * (1f / 3f); i++) {
			double highestKey = keyLengths.lastKey();
			probableKeyLengths.add(keyLengths.get(highestKey));
			keyLengths.remove(highestKey);
		}
		Map<Integer, Integer> factors = new HashMap<Integer, Integer>();
		for (int i : probableKeyLengths) {
			int[] factored = factorise(i);
			for (int j = 0; j < factored.length; j++) {
				int factor = factored[j];
				if (factors.containsKey(factor)) {
					factors.put(factor, factors.get(factor) + 1);
				} else {
					factors.put(factor, 1);
				}
			}
		}
		ArrayList<Integer> moreProbableKeyLengths = new ArrayList<Integer>();
		for (int key : factors.keySet()) {
			if (factors.get(key) > 2) {
				// System.out.println(key);
				moreProbableKeyLengths.add(key);
			}
		}
		int[] moreProbableKeyLengthsArr = moreProbableKeyLengths.stream().mapToInt(i -> i).toArray();
		TreeMap<String, String> vigenerePairs = new TreeMap<String, String>();
		for (int c = 0; c < moreProbableKeyLengthsArr.length; c++) {
			int keyLength = moreProbableKeyLengthsArr[c];
			System.out.println(keyLength);
			String predictedKey = k.keyGuesserVigenere(keyLength, text); // This has a weird behaviour where it reports
																			// the result of the previous call.
			vigenerePairs.put(predictedKey, v.decrypt(text, predictedKey));
			System.out.println(predictedKey + ":" + d.chiSquaredTest(v.decrypt(text, predictedKey)));
		}

		return "";
	}

	public int[] factorise(int n) {
		ArrayList<Integer> factors = new ArrayList<Integer>();
		for (int i = 2; i <= n; i++) {
			if (n % i == 0) {
				factors.add(i);
			}
		}
		return factors.stream().mapToInt(i -> i).toArray();
	}
}
