package cipherTest;

import static org.junit.Assert.*;

import org.junit.Test;

import cipher.Vigenere;

public class VigenereTest {
	
	Vigenere tester = new Vigenere();
	String text = "thequickbrownfoxjumpsoverthelazydog"; // "The quick brown fox jumps over the lazy dog"
	String key = "swordfish"; // It's always swordfish.
	String encrypted = "ldshxnkcijkkeitfbbelgfyjzlowhoqbiwy"; //Using an already available encryptor.
	
	@Test
	public void testEncrypt() {
		assertEquals(tester.encrypt(text, key), encrypted);
	}

	@Test
	public void testDecrypt() {
		assertEquals(tester.decrypt(encrypted, key), text);
	}

}
