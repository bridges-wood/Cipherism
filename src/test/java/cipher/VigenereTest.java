package cipher;

import static org.junit.Assert.*;

import org.junit.Test;

public class VigenereTest {

	private Vigenere tester = new Vigenere();
	private final String TEXT = "thequickbrownfoxjumpsoverthelazydog"; // "The quick brown fox jumps over the lazy dog"
	private final String KEY = "swordfish"; // It's always swordfish.
	private final String ENCRYPTED = "ldshxnkcijkkeitfbbelgfyjzlowhoqbiwy"; // Using an already available encryptor.

	@Test
	public void testEncrypt() {
		assertEquals(ENCRYPTED, tester.encrypt(TEXT, KEY));
	}

	@Test
	public void testDecrypt() {
		assertEquals(TEXT, tester.decrypt(ENCRYPTED, KEY));
	}

}
