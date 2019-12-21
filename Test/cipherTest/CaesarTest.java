package cipherTest;

import static org.junit.Assert.*;

import org.junit.Test;

import cipher.Caesar;

public class CaesarTest {
	
	String encrypted = "aolxbpjriyvdumveqbtwzvclyaolshgfkvn"; // "The quick brown fox jumps over the lazy dog" ROT 7.
	int shift = 19;
	
	@Test
	public void testCaesarShiftDecrypt() {
		Caesar tester = new Caesar();
		
		assertEquals("the quick brown fox jumps over the lazy dog".replaceAll(" ", ""), tester.CaesarShiftDecrypt(encrypted, shift));
		fail("Failed Caesar decryption on: " + encrypted + " with offset 7.");
	}

}
