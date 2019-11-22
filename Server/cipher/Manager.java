package cipher;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * For all devices connected to the server, we will assign a Manager to it.
 */
public class Manager {

	private Utilities u;
	private IOC i;
	private NGramAnalyser n;
	private DetectEnglish d;
	private KasiskiExamination k;
	private Vigenere v;
	private BruteForce b;

	Manager(String text) {
		u = new Utilities();
		i = new IOC();
		n = new NGramAnalyser();
		d = new DetectEnglish();
		k = new KasiskiExamination();
		v = new Vigenere();
		b = new BruteForce();
		run(u.cleanText(text));
	}

	private String run(String text) {
		switch (detectCipher(text)) {
		case "Periodic":
			// Need to make sure key isn't null.
			String[] keys = k.run(text);
			return "";
		case "Substitution":
			// Start substitution breaking.
			// Return most likely substitution.
			break;
		case "Baconian":
			// ?
			break;
		case "Polybius":
			// ?
			break;
		case "Complex Square":
			// ?
			break;
		case "Transposition":
			// ?
			break;
		default:
			// Give all information known on the cipher.
			break;
		}
		return "";
	}

	private int charCount(String text) {
		char[] letters = text.toCharArray();
		ArrayList<Character> found = new ArrayList<Character>();
		for (int i = 0; i < letters.length; i++) {
			if (!found.contains(letters[i])) {
				found.add(letters[i]);
			}
		}
		return found.size();
	}

	public String detectCipher(String text) {
		int charCount = charCount(text);
		if (charCount <= 2) {
			return "Baconian";
		}
		if (text.length() > 30) {
			switch (charCount) {
			case 5 | 6:
				return "Polybius";
			case 23 | 24 | 25:
				if (u.cleanText(text).length() > 420) {
					return "Complex Square";
				} else {
					return refineGuess(text);
				}
			default:
				return refineGuess(text);
			}
		} else {
			return refineGuess(text);
		}
	}

	private String refineGuess(String text) {
		double chiSquared = d.chiSquaredTest(text);
		if (chiSquared < 70) {
			return "Transpostion";
		} else {
			float kappa = i.kappaText(n.NgramAnalysis(1, text, false), text.length());
			if ((0.686 - kappa) / kappa < 0.3) {
				return "Substitution";
			} else {
				double[] periodicIOCs = i.peroidicIndexOfCoincidence(text);
				for (int i = 0; i < periodicIOCs.length; i++) {
					periodicIOCs[i] = (float) Math.pow(1 - periodicIOCs[i], 2);
				}
				Arrays.sort(periodicIOCs);
				if (periodicIOCs[0] < 0.25 * periodicIOCs[periodicIOCs.length - 1]) {
					return "Periodic";
				} else {
					return "";
				}
			}
		}
	}
}
