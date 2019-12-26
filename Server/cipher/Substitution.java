package cipher;

public class Substitution {

	public Substitution() {

	}

	/**
	 * Encrypts a given text using a substitution cipher given a complete set of
	 * letter to letter mappings.
	 * 
	 * @param text     The text to be encrypted.
	 * @param mappings The letter-letter mappings that the text is to encrypted
	 *                 with.
	 * @return An encrypted string.
	 */
	public String encrypt(String text, Mapping[] mappings) {
		if (mappings.length < 26) {
			throw new IllegalArgumentException("Incomplete set of letter mappings.");
		}
		StringBuilder sb = new StringBuilder();
		for (Character c : text.toCharArray()) {
			for (Mapping m : mappings) {
				if (m.plainChar == c) {
					sb.append(m.cipherChar);
				}
			}
		}
		return sb.toString();
	}

	/**
	 * Decrypts a given text using a substitution cipher given a complete set of
	 * letter to letter mappings.
	 * 
	 * @param text     The text to be decrypted.
	 * @param mappings The letter-letter mappings that the text is to decrypted
	 *                 with.
	 * @return The decrypted string.
	 */
	public String decrypt(String text, Mapping[] mappings) {
		if (mappings.length < 26) {
			throw new IllegalArgumentException("Incomplete set of letter mappings.");
		}
		StringBuilder sb = new StringBuilder();
		for (Character c : text.toCharArray()) {
			for (Mapping m : mappings) {
				if (m.cipherChar == c) {
					sb.append(m.plainChar);
				}
			}
		}
		return sb.toString();
	}
}
