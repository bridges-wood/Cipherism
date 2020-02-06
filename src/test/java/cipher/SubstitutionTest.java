package cipher;

import static org.junit.Assert.*;

import org.junit.Test;

public class SubstitutionTest {

	private Substitution tester = new Substitution();
	private final Mapping[] MAPPINGS = initialiseMappings("zyxwvutsrqponmlkjihgfedcba"); // Substitution alphabet to be tested.
	
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

	@Test
	public void testEncrypt() {
		assertEquals(tester.encrypt("thequickbrownfoxjumpsoverthelazydog", MAPPINGS), "gsvjfrxpyildmulcqfnkhlevigsvozabwlt");
	}

	@Test
	public void testDecrypt() {
		assertEquals(tester.decrypt("gsvjfrxpyildmulcqfnkhlevigsvozabwlt", MAPPINGS), "thequickbrownfoxjumpsoverthelazydog");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testThrow() {
		tester.decrypt("test", new Mapping[0]);
		tester.encrypt("test", new Mapping[0]);
	}

}
