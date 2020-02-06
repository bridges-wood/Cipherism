package test.java.com.CiphersApp.cipher;

import static org.junit.Assert.*;

import java.util.TreeMap;

import org.junit.Test;

import main.java.com.CiphersApp.cipher.NGramAnalyser;
import main.java.com.CiphersApp.cipher.Utilities;

public class NGramAnalyserTest {

	private Utilities u = new Utilities();
	private NGramAnalyser tester = new NGramAnalyser(u);
	private final String PLAINTEXT = u.cleanText(
			"Mr. Utterson the lawyer was a man of a rugged countenance that was never lighted by a smile; cold,"
					+ " scanty and embarrassed in discourse; backward in sentiment; lean, long, dusty, dreary and yet somehow"
					+ " lovable. At friendly meetings, and when the wine was to his taste, something eminently human beaconed"
					+ " from his eye; something indeed which never found its way into his talk, but which spoke not only in"
					+ " these silent symbols of the after-dinner face, but more often and loudly in the acts of his life. He"
					+ " was austere with himself; drank gin when he was alone, to mortify a taste for vintages; and though he"
					+ " enjoyed the theatre, had not crossed the doors of one for twenty years. But he had an approved tolerance"
					+ " for others; sometimes wondering, almost with envy, at the high pressure of spirits involved in their"
					+ " misdeeds; and in any extremity inclined to help rather than to reprove. I incline to Cain's heresy, the"
					+ " used to say quaintly: I let my brother go to the devil in his own way. In this character, it was"
					+ " frequently his fortune to be the last reputable acquaintance and the last good influence in the lives of"
					+ " downgoing men. And to such as these, so long as they came about his chambers, he never marked a shade of"
					+ " change in his demeanour.");

// Story of the Door from The Strange Case of Dr. Jekyll and Mr. Hyde.
	@Test
	public void testNgramAnalysis() {
		TreeMap<String, Double> trigrams = tester.NgramAnalysis(3, PLAINTEXT, true);
		assertTrue(trigrams.get("he ") > 0);
		assertEquals(trigrams.get("xe "), null);
	}

	@Test
	public void testFrequencyAnalysis() {
		TreeMap<String, Integer> letters = tester.frequencyAnalysis(PLAINTEXT);
		assertEquals(letters.get("e").intValue(), 131);
		assertEquals(letters.get("z").intValue(), 0);
	}

	@Test
	public void testKasiskiBase() {
		TreeMap<String, Integer> map = tester.kasiskiBase(1, PLAINTEXT); // Length 1
		assertEquals(map.get("e").intValue(), 131);
		assertEquals(map.get("x").intValue(), 1);
		assertEquals(map.get("i").intValue(), 66);
		assertEquals(map.get("z"), null);
		map = tester.kasiskiBase(2, PLAINTEXT); // Length 2
		assertEquals(map.get("of").intValue(), 8);
		assertEquals(map.get("in").intValue(), 31);
		assertEquals(map.get("at").intValue(), 5);
		map = tester.kasiskiBase(3, PLAINTEXT); // Length 3
		assertEquals(map.get("the").intValue(), 20);
		assertEquals(map.get("who"), null);
		assertEquals(map.get("how").intValue(), 1);
	}

	@Test
	public void testComputeScore() {
		double score = tester.computeScore(3, "the", false);
		assertEquals(Math.log10(77534223d / 4274127909d), score, 0.0000000001);
	}

}
