package cipherTest;

import static org.junit.Assert.*;

import java.util.TreeMap;

import org.junit.Test;

import cipher.NGramAnalyser;
import cipher.Utilities;

public class NGramAnalyserTest {

	Utilities u = new Utilities();
	NGramAnalyser tester = new NGramAnalyser(u);
	String text = "Mr. Utterson the lawyer was a man of a rugged countenance that was never lighted by a smile; cold,"
			+ " scanty and embarrassed in discourse; backward in sentiment; lean, long, dusty, dreary and yet somehow"
			+ " lovable. At friendly meetings, and when the wine was to his taste, something eminently human beaconed"
			+ " from his eye; something indeed which never found its way into his talk, but which spoke not only in"
			+ " these silent symbols of the after-dinner face, but more often and loudly in the acts of his life. He"
			+ " was austere with himself; drank gin when he was alone, to mortify a taste for vintages; and though he"
			+ " enjoyed the theatre, had not crossed the doors of one for twenty years. But he had an approved tolerance"
			+ " for others; sometimes wondering, almost with envy, at the high pressure of spirits involved in their"
			+ " misdeeds; and in any extremity inclined to help rather than to reprove. “I incline to Cain’s heresy,” he"
			+ " used to say quaintly: “I let my brother go to the devil in his own way.” In this character, it was"
			+ " frequently his fortune to be the last reputable acquaintance and the last good influence in the lives of"
			+ " downgoing men. And to such as these, so long as they came about his chambers, he never marked a shade of"
			+ " change in his demeanour.";

// Story of the Door from The Strange Case of Dr. Jekyll and Mr. Hyde.
	@Test
	public void testNgramAnalysis() {
		TreeMap<String, Float> trigrams = tester.NgramAnalysis(3, text, true);
		assertTrue(trigrams.get("he ") > 0);
		assertEquals(trigrams.get("xe "), null);
	}

	@Test
	public void testFrequencyAnalysis() {
		TreeMap<String, Integer> letters = tester.frequencyAnalysis(text);
		assertEquals(letters.get("e").intValue(), 131);
		assertEquals(letters.get("z").intValue(), 0);
	}

	@Test
	public void testKasiskiBase() {
		TreeMap<String, Integer> map = tester.kasiskiBase(1, text); // Length 1
		assertEquals(map.get("e").intValue(), 131);
		assertEquals(map.get("x").intValue(), 1);
		assertEquals(map.get("i").intValue(), 66);
		assertEquals(map.get("z"), null);
		map = tester.kasiskiBase(2, text); // Length 2
		assertEquals(map.get("of").intValue(), 8);
		assertEquals(map.get("in").intValue(), 31);
		assertEquals(map.get("at").intValue(), 5);
		map = tester.kasiskiBase(3, text); // Length 3
		assertEquals(map.get("the").intValue(), 19);
		assertEquals(map.get("who"), null);
		assertEquals(map.get("how").intValue(), 1);
	}

	@Test
	public void testComputeScore() {
		double score = tester.computeScore(3, "the", false);
		assertEquals(Math.log10(77534223d / 4274127909d), score, 0.1);
	}

	@Test
	public void testLoadNgramMap() {
		TreeMap<String, Double> probabilities = tester.loadNgramMap(3);
		assertTrue(probabilities.keySet().size() == 17556);
		assertEquals(Math.pow(10, probabilities.get("the")) * 4274127909d, 77534223d, 0.077534223);
		// Tests that it can resolve frequencies to order E-9 accuracy by
		// performing the inverse of the done operation.
		assertEquals(Math.pow(10, probabilities.get("gng")) * 4274127909d, 7610, 0.00000761);
	}

}
