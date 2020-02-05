package test.java;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import main.java.cipher.Mapping;
import main.java.cipher.PredictWords;
import main.java.cipher.Substitution;

public class PredictWordsTest {

	private PredictWords tester = new PredictWords();
	private Substitution s = new Substitution();
	private final Mapping[] MAPPINGS = SubstitutionTest.initialiseMappings("zyxwvutsrqponmlkjihgfedcba");

	@Test
	public void testPredictedWords() {
		String[] testCases = { "this", "is", "a", "set", "of", "test", "words" };
		for (String test : testCases) {
			String encoding = s.encrypt(test, MAPPINGS);
			List<String> possibleEncodings = Arrays.asList(tester.predictedWords(encoding));
			assertTrue(possibleEncodings.contains(test));
		}
	}

	@Test
	public void testEncodeWord() {
		Integer[] encoding = { 0, 1, 2 };
		assertArrayEquals(encoding, tester.encodeWord("the"));
	}

	@Test
	public void testEncodePhrase() {
		Integer[][] encodedPhrase = { { 0, 1, 2, 0 }, { 3, 4, 2, 1 } };
		assertArrayEquals(encodedPhrase, tester.encodePhrase("test case".split(" ")));
	}

}
