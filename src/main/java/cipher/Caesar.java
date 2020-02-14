package cipher;

/**
 * Class that facilitates the encryption and decryption of a Caesar shift
 * cipher.
 * 
 * @author Max Wood
 *
 */
public class Caesar {

	public Caesar() {
	}

	/**
	 * @param text  The enciphered text to be decoded.
	 * @param shift The shift that has been applied to the text.
	 * @return A string that has undergone the Caesar shift.
	 */
	public String CaesarShift(String text, int shift) {
		shift %= 26; // This avoids any issues with keys that aren't from 0 to 26.
		String[] words = text.split("\\s+"); // Automatically handles spaces.
		StringBuilder stringOut = new StringBuilder();
		for (String word : words) {
			char[] letters = word.toCharArray(); // Builds a new char array for each word, so all encrypting is done in
													// place.
			for (int i = 0; i < letters.length; i++) {
				int out = ((letters[i] - 97) + shift) % 26; // Normalises to zero so modulo arithmetic can be used.
				letters[i] = (char) (out + 97);
			}
			stringOut.append(new String(letters) + " ");
		}
		String toReturn = stringOut.toString();
		return toReturn.strip();
	}
}
