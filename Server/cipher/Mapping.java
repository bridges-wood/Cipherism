package cipher;
public class Mapping {
	boolean isDefinite;
	char cipherChar;
	char plainChar;

	/**
	 * @param isDefinite Whether or not the relationship is definite in the text.
	 * @param fromChar   The enciphered character.
	 * @param toChar     The deciphered character the enciphered character
	 *                   represents.
	 */
	public Mapping(char fromChar, char toChar) {
		this.isDefinite = false;
		this.cipherChar = fromChar;
		this.plainChar = toChar;
	}
}