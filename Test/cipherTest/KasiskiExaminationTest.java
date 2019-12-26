package cipherTest;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import cipher.DetectEnglish;
import cipher.IOC;
import cipher.KasiskiExamination;
import cipher.NGramAnalyser;
import cipher.Utilities;
import cipher.Vigenere;

public class KasiskiExaminationTest {

	Utilities u = new Utilities();
	NGramAnalyser n = new NGramAnalyser(u);
	Vigenere v = new Vigenere();
	IOC i = new IOC(u, n);
	DetectEnglish d = new DetectEnglish(u, n);
	KasiskiExamination tester = new KasiskiExamination(u, n, v, i, d);
	String key = "test";
	String plain = "The next thing I remember is, waking up with a feeling as if I had had a frightful nightmare,"
			+ " and seeing before me a terrible red glare, crossed with thick black bars. I heard voices, too,"
			+ " speaking with a hollow sound, and as if muffled by a rush of wind or water.";
	String encrypted = v.encrypt(u.deSpace(u.cleanText(plain)), key);

	@Test
	public void testKeyGuesserVigenere() {
		List<String> keys = Arrays.asList(tester.keyGuesserVigenere(key.length(), encrypted));
		assertTrue(keys.contains(key));
	}

	@Test
	public void testMostLikelyKeyLength() {
		String[] keys = { "test", "other", "key", "lengths" };
		for (String key : keys) {
			String encrypted = v.encrypt(plain, key);
			int[] likelyLengths = tester.likelyKeyLengths(n.kasiskiBase(2, encrypted), encrypted);
			assertEquals(tester.mostLikelyKeyLength(likelyLengths, encrypted), key.length());
		}

	}

}
