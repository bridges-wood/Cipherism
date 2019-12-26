package cipher;

public class Caesar {

	Utilities u;
	
	public Caesar(Utilities u) {
		this.u = u;
	}

	/**
	 * @param text
	 *            The enciphered text to be decoded.
	 * @param shift
	 *            The shift that has been applied to the text.
	 * @return A string that has undergone the Caesar shift.
	 */
	public String CaesarShiftDecrypt(String text, int shift) {
		while (shift > 26) {
			shift -= 26;
		}
		while(shift < 0) {
			shift += 26;
		}
		String[] words = u.cleanText(text).split("\\s+");
		StringBuilder stringOut = new StringBuilder();
		for (String word : words) {
			char[] letters = word.toCharArray();
			for (int i = 0; i < letters.length; i++) {
				int out = letters[i] + shift;
				if (out > 122) {
					out -= 26;
				}
				letters[i] = (char) out;
			}
			stringOut.append(new String(letters) + " ");
		}
		String toReturn = stringOut.toString();
		return toReturn.strip();
	}
}
