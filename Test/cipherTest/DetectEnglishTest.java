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
		assertTrue(tester.detectEnglish("this is a test string") == 1);
		assertTrue(tester.detectEnglish("thisisateststring") == 1);
		assertTrue(tester.detectEnglish("étoile") == 0);
		assertTrue(tester.detectEnglish("test étoile") == 0.5);
	}

	@Test
	public void testGraphicalRespace() {
		String[] testCases = {"this is a test case", "check check one two"}; //Non-English text causes a failure as it cannot be recognised in the middle of the string.
		for (String testCase : testCases) {
			assertEquals("Failure on test case " + testCase, testCase,
					tester.graphicalRespace(testCase.replace(" ", ""), 20));
		}

	}

	@Test
	public void testChiSquaredTest() {
		assertTrue(tester.chiSquaredTest("this is an english sentence") < 150);
	}

	@Test
	public void testIsEnglishString() {
		assertTrue(tester.isEnglish("lammer"));
		assertTrue(tester.isEnglish("plenteously"));
		assertTrue(tester.isEnglish("jannock"));
		assertTrue(tester.isEnglish("bombproof"));
		assertTrue(tester.isEnglish("reburials"));
		assertTrue(tester.isEnglish("kursaals"));
		assertTrue(tester.isEnglish("flukeworm"));
		assertTrue(tester.isEnglish("requires"));
		assertTrue(tester.isEnglish("subsacral"));
		assertTrue(tester.isEnglish("saleable"));
	}

}
