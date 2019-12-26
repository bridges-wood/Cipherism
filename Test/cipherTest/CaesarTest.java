package cipherTest;

import static org.junit.Assert.*;

import org.junit.Test;

import cipher.Caesar;

public class CaesarTest {

	Caesar tester = new Caesar();
	String encrypted = "aolxbpjriyvdumveqbtwzvclyaolshgfkvn"; // "The quick brown fox jumps over the lazy dog" ROT 7.
	int shift = 19;

	@Test
	public void testCaesarShiftDecrypt() {
		assertEquals("the quick brown fox jumps over the lazy dog".replaceAll(" ", ""),
				tester.CaesarShiftDecrypt(encrypted, shift));
	}

}
