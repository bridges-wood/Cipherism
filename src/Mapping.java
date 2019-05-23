public class Mapping {
	boolean isDefinite;
	char fromChar;
	char toChar;

	/**
	 * @param isDefinite Whether or not the relationship is definite in the text.
	 * @param fromChar   The enciphered character.
	 * @param toChar     The deciphered character the enciphered character
	 *                   represents.
	 */
	Mapping(boolean isDefinite, char fromChar, char toChar) {
		this.isDefinite = isDefinite;
		this.fromChar = fromChar;
		this.toChar = toChar;
	}
}