package cipherTest;

import static org.junit.Assert.*;

import org.junit.Test;

import cipher.DetectEnglish;
import cipher.NGramAnalyser;
import cipher.Utilities;

public class DetectEnglishTest {

	Utilities u = new Utilities();
	DetectEnglish tester = new DetectEnglish(u, new NGramAnalyser(u));

	@Test
	public void testDetectEnglish() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsEnglishStringArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testGraphicalRespace() {
		String[] testCases = { "this is a test case", "let us invade" }; // TODO Assess why this loop gives comparison
																			// errors between iterations.
		for (String testCase : testCases) {
			assertEquals("Failure on test case " + testCase, testCase,
					tester.graphicalRespace(testCase.replace(" ", ""), 20));
		}

	}

	@Test
	public void testChiSquaredTest() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsEnglishString() {
		fail("Not yet implemented");
	}

}
