package com.polytonic.cipher;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class VigenereExceptionTest {

	private String text;
	private String key;
	private String encrypted;
	private Vigenere tester = new Vigenere();

	public VigenereExceptionTest(String textP, String keyP, String encryptedP) {
		text = textP;
		key = keyP;
		encrypted = encryptedP;
	}

	@Parameters
	public static Collection<Object[]> data() {
		Object[][] data = new Object[][] {
				{ "", "", "" }, 
				{ "text", "", "" },
				{ "", "key", "" },
				{ "", "", "encrypted" }
				};
		return Arrays.asList(data);
	}

	@Test (expected = IllegalArgumentException.class)
	public void testEncrypt() {
		assertEquals(encrypted, tester.encrypt(text, key));
	}

	@Test (expected = IllegalArgumentException.class)
	public void testDecrypt() {
		assertEquals(text, tester.decrypt(encrypted, key));
	}

}
