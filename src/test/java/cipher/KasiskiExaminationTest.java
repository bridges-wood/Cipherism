package cipher;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class KasiskiExaminationTest {

	private FileIO u = new FileIO();
	private NGramAnalyser n = new NGramAnalyser(u);
	private Vigenere v = new Vigenere();
	private IOC i = new IOC(n);
	private DetectEnglish d = new DetectEnglish(u, n);
	private KasiskiExamination tester = new KasiskiExamination(u, n, v, i, d);
	private final String KEY = "test";
	private final String PLAINTEXT = "The next thing I remember is, waking up with a feeling as if I had had a frightful nightmare,"
			+ " and seeing before me a terrible red glare, crossed with thick black bars. I heard voices, too,"
			+ " speaking with a hollow sound, and as if muffled by a rush of wind or water.";
	private final String CIPHERTEXT = v.encrypt(u.deSpace(u.cleanText(PLAINTEXT)), KEY);

	@Test
	public void testKeyGuesserVigenere() {
		List<String> keys = Arrays.asList(tester.keyGuesserVigenere(KEY.length(), CIPHERTEXT));
		assertTrue(keys.contains(KEY));
	}

	@Test
	public void testMostLikelyKeyLength() {
		String[] keys = { "test", "other", "key", "lengths" };
		for (String key : keys) {
			String encrypted = v.encrypt(PLAINTEXT, key);
			int[] likelyLengths = tester.likelyKeyLengths(n.kasiskiBase(2, encrypted), encrypted);
			assertEquals(tester.mostLikelyKeyLength(likelyLengths, encrypted), key.length());
		}

	}

}
