import java.util.Map;

public class IOC {
	/**
	 * Returns the index of coincidence of the monogram analysed text.
	 * 
	 * @param letters
	 *            A pre-created map of all monograms (ie. letters) in the text and
	 *            the respective fractions of the whole they make up.
	 * @param length
	 *            Integer length of the text being analysed.
	 * 
	 * @return Index of coincidence.
	 */
	float IndexOfCoincidence(Map<String, Float> letters, float length) {
		float IOC = 0f;
		for (float f : letters.values()) {
			IOC += f * ((f * (length - 1)) / (length - 1));
		}
		return IOC; // NOTE: For monoalphabetic encryptions, IOC will be close to the nominal 0.0686
					// and above the random: 0.038466. For polyalphabetic ciphers it is somewhere in
					// between the two.
	}
}
