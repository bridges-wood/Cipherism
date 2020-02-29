package com.polytonic.cipher;

import static org.junit.Assert.*;

import org.junit.Test;

public class DetectEnglishTest {

	private FileIO u = new FileIO();
	private DetectEnglish tester = new DetectEnglish(u, new NGramAnalyser(u));

	@Test
	public void testDetectEnglish() {
		assertEquals(1, tester.detectEnglish("this is a test string"), 0); // Testing spaced text.
		assertEquals(1, tester.detectEnglish("thisisateststring"), 0); // Testing un-spaced text.
		assertEquals(0, tester.detectEnglish("étoile"), 0); // Testing non-English text.
		assertEquals(0.5, tester.detectEnglish("test étoile"), 0); // Testing a combination of English and non-English.
	}

	@Test
	public void testGraphicalRespace() {
		String[] testCases = { "this is a test case", "check check one two" };
		for (String testCase : testCases) {
			assertEquals("Failure on test case " + testCase, testCase,
					tester.graphicalRespace(testCase.replace(" ", ""), 20));
		}
		/*
		 * Note: Non-English WILL cause the respacing to fail. As we cannot recognise it
		 * with the dictionary, it will never create a node in the tree. As there is no
		 * node for it in the tree, it will never appear in the output.
		 */
		/*
		 * Solution: Apply the function once, identify the character at which it fails.
		 * Keep trying to get the first English word out. Once it's found, append all
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

	@Test
	public void testGreeedyRespace() {
		assertEquals("a test sentence", tester.greedyWrapper("atestsentence", 20));
	}

}
