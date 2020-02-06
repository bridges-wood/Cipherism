package main.java.com.CiphersApp.cipher;

public class Vigenere {

	public Vigenere() {
	}

	/**
	 * Encrypts the given text using the key.
	 * 
	 * @param text The text to be encrypted.
	 * @param key  The key used to encrypt the given text.
	 * @return The encrypted text.
	 */
	public String encrypt(String text, String key) {
		StringBuilder result = new StringBuilder();
		key = key.toLowerCase();
		text = text.toLowerCase();
		for (int i = 0, j = 0; i < text.length(); i++) {
			char c = text.charAt(i);
			if (c < 'a' || c > 'z')
				continue; // If the character to be encrypted cannot be, don't.
			result.append((char) ((c + key.charAt(j) - 2 * 'a') % 26 + 'a')); // Normalisation and encryption.
			j = ++j % key.length();
		}
		return result.toString();
	}

	/**
	 * Decrypts the given text using the key.
	 * 
	 * @param text The text to be decrypted.
	 * @param key  The key used to decrypt the text.
	 * @return The decrypted text.
	 */
	public String decrypt(String text, String key) {
		StringBuilder result = new StringBuilder();
		key = key.toLowerCase();
		text = text.toLowerCase();
		for (int i = 0, j = 0; i < text.length(); i++) {
			char c = text.charAt(i);
			if (c < 'a' || c > 'z')
				continue;
			result.append((char) ((c - key.charAt(j) + 26) % 26 + 'a'));
			j = ++j % key.length();
		}
		return result.toString();
	}

}
