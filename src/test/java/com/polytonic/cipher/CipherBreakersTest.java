package com.polytonic.cipher;

import static org.junit.Assert.*;

import org.junit.Test;

public class CipherBreakersTest {

	private FileIO u = new FileIO();
	private Substitution s = new Substitution();
	private Vigenere v = new Vigenere();
	private ProbableSubstitutions p = new ProbableSubstitutions();
	private NGramAnalyser n = new NGramAnalyser(u);
	private IOC i = new IOC(n);
	private DetectEnglish d = new DetectEnglish(u, n);
	private KasiskiExamination k = new KasiskiExamination(u, n, v, i, d);
	private CipherBreakers tester = new CipherBreakers(u, k, p);
	private final String PLAINTEXT = u.deSpace(
			u.cleanText("The next thing I remember is, waking up with a feeling as if I had had a frightful nightmare,"
					+ " and seeing before me a terrible red glare, crossed with thick black bars. I heard voices, too,"
					+ " speaking with a hollow sound, and as if muffled by a rush of wind or water."));
	private final String ADJUSDTED_PLAINTEXT = "The next thing I remember is waking up with a feeling as if I had had a frightful nightmare"
			+ " and seeing before me a terrible red glare crossed with thick black bars I heard voices too"
			+ " speaking with a hollow sound and as if muffled by a rush of wind or water";
	// Jane Eyre - Charlotte Bronte
	private final String KEY = "akey";
	private final String VIGENERE_ENCRYPTED = v.encrypt(PLAINTEXT, KEY);
	private final Mapping[] MAPPINGS = SubstitutionTest.initialiseMappings("qwertyuiopasdfghjklzxcvbnm");
	private final String SUBSTITUTION_ENCRYPTED = s.encrypt(ADJUSDTED_PLAINTEXT.toLowerCase(), MAPPINGS);

	@Test
	public void testVigenereBreaker() {
		assertEquals(d.graphicalRespace(PLAINTEXT, 20), tester.vigenereBreaker(VIGENERE_ENCRYPTED));
	}

	// @Test
	public void testSubstitutionBreaker() {
		assertEquals(d.graphicalRespace(PLAINTEXT, 20), tester.substitutionBreaker(SUBSTITUTION_ENCRYPTED));
	}

}
