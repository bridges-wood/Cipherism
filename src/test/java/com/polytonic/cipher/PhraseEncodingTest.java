package com.polytonic.cipher;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class PhraseEncodingTest {

	private String[] phrase;
	private Integer[][] encoding;
	private PredictWords tester = new PredictWords();

	public PhraseEncodingTest(String phrase, Integer[][] encoding) {
		this.phrase = phrase.split(" ");
		this.encoding = encoding;
	}

	@Parameters
	public static Collection<Object[]> data() {
		Object[][] data = { 
				{ "test case", new Integer[][] { { 0, 1, 2, 0 }, { 3, 4, 2, 1 } } } 
				};
		return Arrays.asList(data);
	}

	@Test
	public void testEncodePhrase() {
		assertArrayEquals(encoding, tester.encodePhrase(phrase));
	}

}
