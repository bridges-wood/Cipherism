package com.polytonic.cipher;

import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class KasiskiExaminationTest {

	private String key;
	private final String PLAINTEXT = "The next thing I remember is, waking up with a feeling as if I had had a frightful nightmare,"
			+ " and seeing before me a terrible red glare, crossed with thick black bars. I heard voices, too,"
			+ " speaking with a hollow sound, and as if muffled by a rush of wind or water.";
	private String ciphertext;
	private FileIO u = new FileIO();
	private NGramAnalyser n = new NGramAnalyser(u);
	private Vigenere v = new Vigenere();
	private IOC i = new IOC(n);
	private DetectEnglish d = new DetectEnglish(u, n);
	private KasiskiExamination tester = new KasiskiExamination(u, n, v, i, d);

	public KasiskiExaminationTest(String key) {
		this.key = key;
		this.ciphertext = v.encrypt(u.deSpace(u.cleanText(PLAINTEXT)), key);
	}

	@Parameters
	public static Collection<Object[]> data(){
		Object[][] data = new Object[][] {
			{ "test" },
			{ "other" },
			{ "key" },
			{ "lengths" }
		};
		return Arrays.asList(data);
	}
	
	@Test
	public void testKeyGuesserVigenere() {
		List<String> keys = Arrays.asList(tester.keyGuesserVigenere(key.length(), ciphertext));
		assertTrue(keys.contains(key));
	}

	@Test
	public void testMostLikelyKeyLength() {
			String encrypted = v.encrypt(PLAINTEXT, key);
			int[] likelyLengths = tester.likelyKeyLengths(n.kasiskiBase(2, encrypted), encrypted);
			assertEquals(key.length(), tester.mostLikelyKeyLength(likelyLengths, encrypted));
	}

}
