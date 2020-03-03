package com.polytonic.cipher;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class SubstitutionExceptionTest {

	private String text;
	private Mapping[] mappings;
	private String encrypted;
	private Substitution tester = new Substitution();

	public SubstitutionExceptionTest(String text, Mapping[] mappings, String encrypted) {
		this.text = text;
		this.mappings = mappings;
		this.encrypted = encrypted;
	}

	@Parameters
	public static Collection<Object[]> data() {
		Object[][] data = new Object[][] { { "thequickbrownfoxjumpsoverthelazydog", new Mapping[0],
				"gsvjfrxpyildmulcqfnkhlevigsvozabwlt" }, { "", new Mapping[27], "" } };
		return Arrays.asList(data);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testEncrypt() {
		assertEquals(encrypted, tester.encrypt(text, mappings));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testDecrypt() {
		assertEquals(text, tester.decrypt(encrypted, mappings));
	}

}
