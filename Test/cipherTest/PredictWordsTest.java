package cipherTest;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import cipher.Mapping;
import cipher.PredictWords;
import cipher.Substitution;
import cipher.Utilities;

public class PredictWordsTest {

	private Utilities u = new Utilities();
	private PredictWords tester = new PredictWords(u);
	private Substitution s = new Substitution();
	private final Mapping[] MAPPINGS = SubstitutionTest.initialiseMappings("zyxwvutsrqponmlkjihgfedcba");

	@Test
	public void testPredictedWords() {
		String[] testCases = { "this", "is", "a", "set", "of", "test", "words" };
		for (String test : testCases) {
			String encoding = s.encrypt(test, MAPPINGS);
			List<String> possibleEncodings = Arrays.asList(tester.predictedWords(encoding, false));
			assertTrue(possibleEncodings.contains(test));
		}
	}

	@Test
	public void testEncodeWord() {
		assertArrayEquals(new int[] {0,1,2}, tester.encodeWord("the"));
	}

	@Test
	public void testEncodePhrase() {
		assertArrayEquals(new int[][] {{0,1,2,0}, {3,4,2,1}}, tester.encodePhrase("test case".split(" ")));
	}

}
