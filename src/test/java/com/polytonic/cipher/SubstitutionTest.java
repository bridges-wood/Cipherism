package com.polytonic.cipher;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class SubstitutionTest {

	private String text;
	private Mapping[] mappings;
	private String encrypted;
	private Substitution tester = new Substitution();

	public SubstitutionTest(String textP, String mappingP, String encryptedP) {
		text = textP;
		mappings = initialiseMappings(mappingP);
		encrypted = encryptedP;
	}

	@Test
	public void testEncrypt() {
		assertEquals("gsvjfrxpyildmulcqfnkhlevigsvozabwlt",
				tester.encrypt("thequickbrownfoxjumpsoverthelazydog", MAPPINGS));
	}

	@Test
	public void testDecrypt() {
		assertEquals("thequickbrownfoxjumpsoverthelazydog",
				tester.decrypt("gsvjfrxpyildmulcqfnkhlevigsvozabwlt", MAPPINGS));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testThrow() {
		tester.decrypt("test", new Mapping[0]);
		tester.encrypt("test", new Mapping[0]);
	}

	/**
	 * Generates a complete letter-letter mapping for a substitution cipher.
	 * 
	 * @param cipherAlphabet The letters of the cipher-alphabet in the position of
	 *                       their plain English counterpart.
	 * @return A 26-long array of letter-letter mappings.
	 */
	public static Mapping[] initialiseMappings(String cipherAlphabet) {
		String plainAlphabet = "abcdefghijklmnopqrstuvwxyz";
		Mapping[] toReturn = new Mapping[26];
		for (int i = 0; i < 26; i++) {
			toReturn[i] = new Mapping(plainAlphabet.charAt(i), cipherAlphabet.charAt(i));
		}
		return toReturn;
	}

}
