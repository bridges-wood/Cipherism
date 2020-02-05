package test.java;

import static org.junit.Assert.*;

import org.junit.Test;

import main.java.cipher.CipherBreakers;
import main.java.cipher.DetectEnglish;
import main.java.cipher.IOC;
import main.java.cipher.KasiskiExamination;
import main.java.cipher.Mapping;
import main.java.cipher.NGramAnalyser;
import main.java.cipher.ProbableSubstitutions;
import main.java.cipher.Substitution;
import main.java.cipher.Utilities;
import main.java.cipher.Vigenere;

public class CipherBreakersTest {

	private Utilities u = new Utilities();
	private Substitution s = new Substitution();
	private Vigenere v = new Vigenere();
	private ProbableSubstitutions p = new ProbableSubstitutions();
	private NGramAnalyser n = new NGramAnalyser(u);
	private IOC i = new IOC(n);
	private DetectEnglish d = new DetectEnglish(u, n);
	private KasiskiExamination k = new KasiskiExamination(u, n, v, i, d);
	private CipherBreakers tester = new CipherBreakers(u, k, d, p, n);
	private final String PLAINTEXT = u.deSpace(
			u.cleanText("The next thing I remember is, waking up with a feeling as if I had had a frightful nightmare,"
					+ " and seeing before me a terrible red glare, crossed with thick black bars. I heard voices, too,"
					+ " speaking with a hollow sound, and as if muffled by a rush of wind or water."));
	private final String KEY = "akey";
	private final String VIGENERE_ENCRYPTED = v.encrypt(PLAINTEXT, KEY);
	private final Mapping[] MAPPINGS = SubstitutionTest.initialiseMappings("qwertyuiopasdfghjklzxcvbnm");
	private final String SUBSTITUTION_ENCRYPTED = s.encrypt(PLAINTEXT, MAPPINGS);

	@Test
	public void testVigenereBreaker() {
		assertEquals(tester.vigenereBreaker(VIGENERE_ENCRYPTED), d.graphicalRespace(PLAINTEXT, 20));
	}

	@Test
	public void testSubstitutionBreaker() {
		assertEquals(tester.substitutionBreaker(SUBSTITUTION_ENCRYPTED), d.graphicalRespace(PLAINTEXT, 20));
	}

}
