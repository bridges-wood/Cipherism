package cipher;

import java.util.Map;

public class IOC {

	private Utilities u;
	private NGramAnalyser n;

	IOC() {
		u = new Utilities();
		n = new NGramAnalyser();
	}

	/**
	 * Returns the kappa value of the monogram analysed text.
	 * 
	 * @param letters A pre-created map of all monograms (ie. letters) in the text
	 *                and the respective fractions of the whole they make up.
	 * @param length  Integer length of the text being analysed.
	 * 
	 * @return The kappa value of the text.
	 */
	public float kappaText(Map<String, Float> letters, float length) {
		float kappa = 0f;
		for (float f : letters.values()) {
			kappa += f * ((f * (length - 1)) / (length - 1));
		}
		return kappa; // NOTE: For monoalphabetic encryptions, κp will be close to the nominal 0.0686
						// and above the random, κr: 0.038466. For polyalphabetic ciphers it is
						// somewhere in
						// between the two.
	}

	/**
	 * Returns the true index of coincidence of analysed text.
	 * 
	 * @param letters A map of letters and their integer occurrences within text.
	 * @return A float.
	 */
	public float indexOfCoincidence(Map<String, Integer> letters) {
		float IOC = 0f;
		float rTotal = 0f;
		for (int i : letters.values()) {
			rTotal += i;
			IOC += i * (i - 1);
		}
		return IOC / ((rTotal * (rTotal - 1) / 26)); // Note, this ought to be as far from 1 as possible to indicate the
														// presence of english. 1 is pure random text.
	}

	/**
	 * Returns an array of the indices of coincidence for nth letters in the text
	 * starting at 2.
	 * 
	 * @param text The text to be analysed.
	 * @return An array of nth letter IOC scores.
	 */
	public double[] peroidicIndexOfCoincidence(String text) {
		char[] letters = u.cleanText(text).toCharArray();
		double[] out = new double[(letters.length / 2) - 1]; // Creates an array thats the size of the number scores we
															// shall gather.
		for (int nth = 2; nth < letters.length / 2; nth++) {
			double score = 0; // The score for the key length.
			for (int start = 0; start < nth; start++) { // Offsets until we get to one less the length of the key as we
														// don't need to continue past there as we have already seen it.
				StringBuilder nths = new StringBuilder(); // Creates a StringBuilder to store our nth letters in.
				for (int i = 0; start + i < letters.length; i += nth) {
					nths.append(letters[start + i]); // Appends the letters for the given start to the StringBuilder.
				}
				score += indexOfCoincidence(n.frequencyAnalysis(nths.toString())); // The IOC of the StringBuilder is
																					// added to the score.
			}
			out[nth - 2] = score / nth; // The score added to the array is magnitude adjusted to allow for comparison.
		}
		return out;
	}
}
