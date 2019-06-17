package cipher;

public class Vigenere {

	public static String encrypt(String text, String key) {
		String output = "";
		text = text.toLowerCase();
		key = key.toLowerCase();
		for (int i = 0, j = 0; i < text.length(); i++) {
			char c = text.charAt(i);
			if (c < 'a' || c > 'z')
				continue;
			output += (char) ((c + key.charAt(j) - 2 * 'A') % 26 + 'A');
			j = ++j % key.length();
		}
		return output;
	}

	public static String decrypt(String text, String key) {
		String output = "";
		text = text.replaceAll("[^a-zA-Z ]", "").toLowerCase();
		key = key.replaceAll("[^a-zA-Z ]", "").toLowerCase();
		for (int i = 0, j = 0; i < text.length(); i++) {
			char c = text.charAt(i);
			if (c < 'a' || c > 'z')
				continue;
			output += (char) ((c - key.charAt(j) + 26) % 26 + 'a');
			j = ++j % key.length();
		}
		return output;
	}
}
