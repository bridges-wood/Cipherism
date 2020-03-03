package com.polytonic.cipher;

/**
 * Facilitates encryption and decryption implementing a Simple Substitution
 * Cipher.
 * 
 * @author Max Wood
 *
 */
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
		if (mappings.length != 26) {
			throw new IllegalArgumentException("Incomplete set of letter mappings.");
		}
		if (text.length() == 0) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		for (Character c : text.toCharArray()) {
			if (c.equals(' ')) {
				sb.append(' '); // Spaces are not encrypted.
				continue;
			}
			for (Mapping m : mappings) {
				/*
				 * Finds the character to be encrypted and outputs its corresponding encrypted
				 * version.
				 */
				if (m.getPlainChar() == c) {
					sb.append(m.getCipherChar());
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
		if (mappings.length != 26) {
			throw new IllegalArgumentException("Incomplete set of letter mappings.");
		}
		if (text.length() == 0) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		for (Character c : text.toCharArray()) {
			if (c.equals(' ')) {
				sb.append(' ');
				continue;
			}
			for (Mapping m : mappings) {
				if (m.getCipherChar() == c) {
					sb.append(m.getPlainChar());
				}
			}
		}
		return sb.toString();
	}
}
