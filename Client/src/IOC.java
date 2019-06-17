package src;

import java.util.Map;

public class IOC {
	/**
	 * Returns the kappa value of the monogram analysed text.
	 * 
	 * @param letters A pre-created map of all monograms (ie. letters) in the text
	 *                and the respective fractions of the whole they make up.
	 * @param length  Integer length of the text being analysed.
	 * 
	 * @return The kappa value of the text.
	 */
	public static float kappaText(Map<String, Float> letters, float length) {
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
	 * Returns the true index of conincidence of analysed text.
	 * 
	 * @param letters A map of letters and their integer occurences within text.
	 * @return A floar
	 */
	public static float indexOfCoincidence(Map<String, Integer> letters) {
		float IOC = 0f;
		float rTotal = 0f;
		for (int i : letters.values()) {
			rTotal += i;
			IOC += i * (i - 1);
		}
		return IOC / ((rTotal * (rTotal - 1) / 26)); // Note, this ought to be as far from 1 as possible to indicate the
														// presence of english. 1 is pure random text.
	}
}
