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
	
	private String result = "";
	
	Manager(String text, Utilities u, IOC i, NGramAnalyser n, DetectEnglish d, KasiskiExamination k, Vigenere v) {
		this.u = u;
		this.i = i;
		this.n = n;
		this.d = d;
		this.k = k;
		run(u.cleanText(text));
	}
	
	public Manager(String text){
		this.u = new Utilities();
		this.n = new NGramAnalyser(u);
		this.i = new IOC(u, n);
		this.d = new DetectEnglish(u, n);
		Vigenere v = new Vigenere();
		this.k = new KasiskiExamination(u, n, v, i, d);
		run(u.cleanText(text));
	}

	private String run(String text) {
		switch (detectCipher(text)) {
		case "Periodic":
			k.run(text);
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

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
}
