package cipher;

import java.util.ArrayList;

/**
 * For all devices connected to the server, we will assign a Manager to it.
 */
public class Manager {

	private Utilities u;
	private IOC i;
	private NGramAnalyser n;
	private DetectEnglish d;

	Manager(String text) {
		u = new Utilities();
		i = new IOC();
		n = new NGramAnalyser();
		d = new DetectEnglish();
		run(u.cleanText(text));
	}

	private void run(String text) {
		switch (detectCipher(text)) {
		case "Vigenere":
			break;
		case "Substitution":
			break;
		default:
			break;
		}
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

	private String detectCipher(String text) {
		/*
		 * Set text to a char array. Analyse how many different characters there are.
		 * Use chi squared analysis to look at text. If it's not close, look at the
		 * letter frequencies. Check index of coincidence. If this is close to english,
		 * probably a substitution. Check periodic IOC. If spikes are identified, it is
		 * a periodic cipher. Look at cipher length and identify if its a square type
		 * cipher or not.
		 */
		switch (charCount(text)) {
		case 2:
			return "Baconian";
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
	}

	private String refineGuess(String text) {
		double chiSquared = d.chiSquaredTest(text) / text.length();
		if (chiSquared < 70) {
			return "Transpostion";
		} else {
			float kappa = i.indexOfCoincidence(n.frequencyAnalysis(text));
			if ((0.686 - kappa) / kappa < 0.3) {
				return "Monoalphabetic";
			} else {
				// For periodic analysis, need a reliable way to identify the 'peaks'. Take the
				// distance from 1 and square it then look at minima.
			}
		}
		return "";
	}
}
