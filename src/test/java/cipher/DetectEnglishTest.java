package cipher;

import static org.junit.Assert.*;

import org.junit.Test;

public class DetectEnglishTest {

	private FileIO u = new FileIO();
	private DetectEnglish tester = new DetectEnglish(u, new NGramAnalyser(u));

	@Test
	public void testDetectEnglish() {
		assertTrue(tester.detectEnglish("this is a test string") == 1); // Testing spaced text.
		assertTrue(tester.detectEnglish("thisisateststring") == 1); // Testing unspaced text.
		assertTrue(tester.detectEnglish("étoile") == 0); // Testing non-english text.
		assertTrue(tester.detectEnglish("test étoile") == 0.5); // Testing a combination of english and non-english.
	}

	@Test
	public void testGraphicalRespace() {
		String[] testCases = { "this is a test case", "check check one two" };
		for (String testCase : testCases) {
			assertEquals("Failure on test case " + testCase, testCase,
					tester.graphicalRespace(testCase.replace(" ", ""), 20));
		}
		/*
		 * Note: Non-english WILL cause the respacing to fail. As we cannot recognise it
		 * with the dictionary, it will never create a node in the tree. As there is no
		 * node for it in the tree, it will never appear in the output.
		 */
		/*
		 * Solution: Apply the function once, identify the character at which it fails.
		 * Keep trying to get the first english word out. Once it's found, append all
		 * previous characters to the output string, and apply the respace function to
		 * the remainder.
		 */
	}

	@Test
	public void testChiSquaredTest() {
		assertTrue(tester.chiSquaredTest("this is an english sentence") < 150);
		// 150 is the value recommended by practical cryptography.
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
		assertTrue(tester.isEnglish("the"));
		assertFalse(tester.isEnglish("rakata"));
		// Test cases chosen at random.
	}

}
