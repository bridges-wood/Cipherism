package com.polytonic.cipher;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class SubstitutionTest {

	private String text;
	private Mapping[] mappings;
	private String encrypted;
	private Substitution tester = new Substitution();

	public SubstitutionTest(String text, String mapping, String encrypted) {
		this.text = text;
		this.mappings = initialiseMappings(mapping);
		this.encrypted = encrypted;
	}

	@Parameters
	public static Collection<Object[]> data() {
		Object[][] data = new Object[][] {
				{ "thepenetrationofcivilengineering", "orwqaihykltcdpsuvfejnmgxbz", "jyauapajfojkspsiwkmkcaphkpaafkph" },
				{ "thequickbrownfoxjumpsoverthelazydog", "zyxwvutsrqponmlkjihgfedcba", "gsvjfrxpyildmulcqfnkhlevigsvozabwlt" },
				{ "loremipsumdolorsitamet", "zqvouragcjlhsywtnemfbkdxpi", "hweusctmbsowhwemcfzsuf" } };
		return Arrays.asList(data);
	}

	@Test
	public void testEncrypt() {
		assertEquals(encrypted, tester.encrypt(text, mappings));
	}

	@Test
	public void testDecrypt() {
		assertEquals(text, tester.decrypt(encrypted, mappings));
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
			toReturn[i] = new Mapping(cipherAlphabet.charAt(i), plainAlphabet.charAt(i));
		}
		return toReturn;
	}

}
