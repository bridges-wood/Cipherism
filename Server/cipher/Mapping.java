package cipher;

public class Mapping {

	private boolean isDefinite;
	private char cipherChar;
	private char plainChar;

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

	public boolean isDefinite() {
		return isDefinite;
	}

	public void setDefinite(boolean isDefinite) {
		this.isDefinite = isDefinite;
	}

	public char getCipherChar() {
		return cipherChar;
	}

	public void setCipherChar(char cipherChar) {
		this.cipherChar = cipherChar;
	}

	public char getPlainChar() {
		return plainChar;
	}

	public void setPlainChar(char plainChar) {
		this.plainChar = plainChar;
	}
}