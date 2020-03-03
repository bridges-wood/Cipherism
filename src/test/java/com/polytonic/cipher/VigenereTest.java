package com.polytonic.cipher;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class VigenereTest {

	private String text;
	private String key;
	private String encrypted;
	private Vigenere tester = new Vigenere();

	public VigenereTest(String text, String key, String encrypted) {
		this.text = text;
		this.key = key;
		this.encrypted = encrypted;
	}

	@Parameters
	public static Collection<Object[]> data() {
		Object[][] data = new Object[][] {
				{ "thequickbrownfoxjumpsoverthelazydog", "swordfish", "ldshxnkcijkkeitfbbelgfyjzlowhoqbiwy" },
				{ "mikesaysturnoffthelightswhenyouleavearoom", "bacon", "nimsfbyuhhsnqtsuhgzvhhvgjiepmbvlgoifatcbn" },
				{ "a", "nuggetinabiscuit", "n" }, 
				};
		return Arrays.asList(data);
	}

	@Test
	public void testEncrypt() {
		assertEquals(encrypted, tester.encrypt(text, key));
	}

	@Test
	public void testDecrypt() {
		assertEquals(text, tester.decrypt(encrypted, key));
	}

}
