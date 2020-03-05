package com.polytonic.cipher;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class PredictWordsTest {

	private PredictWords tester = new PredictWords();
	private Substitution s = new Substitution();
	private String testCase;
	private String expected;

	public PredictWordsTest(String word) {
		Mapping[] mappings = SubstitutionTest.initialiseMappings("zyxwvutsrqponmlkjihgfedcba");
		this.expected = word;
		this.testCase = s.encrypt(word, mappings);
	}

	@Parameters
	public static Collection<Object[]> data() {
		Object[][] data = { 
				{ "show" }, 
				{ "determine" }, 
				{ "suffer" }, 
				{ "west" }, 
				{ "angel" }, 
				{ "allocation" },
				{ "artist" }, 
				{ "current" }, 
				{ "factor" } 
				};
		return Arrays.asList(data);
	}

	@Test
	public void testPredictedWords() {
		List<String> possibleEncodings = Arrays.asList(tester.predictedWords(testCase));
		assertTrue(possibleEncodings.contains(expected));
	}

}
