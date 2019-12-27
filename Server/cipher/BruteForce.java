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
	private PredictWords p;
	private Utilities u;
	private NGramAnalyser n;

	BruteForce(IOC i, KasiskiExamination k, Vigenere v, DetectEnglish d, PredictWords p, Utilities u, NGramAnalyser n) {
		this.i = i;
		this.k = k;
		this.v = v;
		this.d = d;
		this.p = p;
		this.u = u;
		this.n = n;
	}

	public String bruteForcePeriodic(String text) {
		TreeMap<Double, Integer> keyLengths = new TreeMap<Double, Integer>();
		double[] periodicIOCs = i.peroidicIndexOfCoincidence(text);
		for (int i = 0; i < periodicIOCs.length; i++) {
			keyLengths.put(periodicIOCs[i], i + 2);
		}
		ArrayList<Integer> probableKeyLengths = new ArrayList<Integer>();
		for (int i = 0; i < keyLengths.size() * (1f / 3f); i++) {
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
				moreProbableKeyLengths.add(key);
			}
		}
		int[] moreProbableKeyLengthsArr = moreProbableKeyLengths.stream().mapToInt(i -> i).toArray();
		double min = Double.MAX_VALUE;
		String minKey = "key";
		for (int c = 0; c < moreProbableKeyLengthsArr.length; c++) {
			int keyLength = moreProbableKeyLengthsArr[c];
			System.out.println(keyLength);
			String[] predictedKey = k.keyGuesserVigenere(keyLength, text);
			double keyScore = d.chiSquaredTest(v.decrypt(text, predictedKey[0]));
			System.out.println(predictedKey + ":" + keyScore);
			if (keyScore < min) {
				minKey = predictedKey[0];
				min = keyScore;
			}
		}
		return editKey(text, minKey);
	}

	public String bruteForceAlt(String text) {
		String[] lines = u.readFile("Server//StaticResources//mostProbable.txt");
		String key = "";
		float max = Float.MIN_VALUE;
		for(String line : lines) {
			long start = System.nanoTime();
			float score = d.detectEnglish(v.decrypt(text, line));
			System.out.println((System.nanoTime() - start) / 10e9 + "seconds");
			if( score > max) {
				max = score;
				key = line;
			}
		}
		return key;
	}
	
	private int[] factorise(int n) {
		ArrayList<Integer> factors = new ArrayList<Integer>();
		for (int i = 2; i <= n; i++) {
			if (n % i == 0) {
				factors.add(i);
			}
		}
		return factors.stream().mapToInt(i -> i).toArray();
	}

	private String editKey(String text, String key) {
		String[] alternativeKeys = p.predictedWords(key, true);
		String bestKey = "";
		double lowestScore = Double.MAX_VALUE;
		for (int i = 0; i < alternativeKeys.length; i++) {
			String currentKey = alternativeKeys[i];
			System.out.println(currentKey);
			double currentScore = d.chiSquaredTest(v.decrypt(text, currentKey));
			int distance = distance(key, currentKey);
			currentScore = currentScore * distance;
			if (currentScore < lowestScore) {
				bestKey = currentKey;
				lowestScore = currentScore;
			}
		}
		System.out.println("Best Key: " + bestKey);
		return bestKey;
	}

	private int distance(String start, String finish) {
		int distance = 0;
		for (int i = 0; i < start.length(); i++) {
			if (start.charAt(i) != finish.charAt(i)) {
				distance++;
			}
		}
		return distance;
	}

	public String vigenereBruteforce(String text) {
		int[] likelyLengths = k.likelyKeyLengths(n.kasiskiBase(2, text), text);
		int length = k.mostLikelyKeyLength(likelyLengths, text);
		//TODO generate all permutations for the key length
		return "";
	}
}