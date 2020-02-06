package test.java.com.CiphersApp.cipher;

import static org.junit.Assert.*;

import org.junit.Test;

import main.java.com.CiphersApp.cipher.DetectEnglish;
import main.java.com.CiphersApp.cipher.Mapping;
import main.java.com.CiphersApp.cipher.NGramAnalyser;
import main.java.com.CiphersApp.cipher.PredictWords;
import main.java.com.CiphersApp.cipher.ProbableSubstitutions;
import main.java.com.CiphersApp.cipher.Substitution;
import main.java.com.CiphersApp.cipher.SubstitutionTreeSearch;
import main.java.com.CiphersApp.cipher.Utilities;

public class SubstitutionTreeSearchTest {

	private Substitution s = new Substitution();
	private ProbableSubstitutions p = new ProbableSubstitutions();
	private Utilities u = new Utilities();
	private PredictWords pW = new PredictWords();
	private NGramAnalyser n = new NGramAnalyser(u);
	private DetectEnglish d = new DetectEnglish(u, n);
	private final String PLAIN_TEXT = "in terms of speed, these bees are second to none";
	private final String CIPHER_TEXT = s.encrypt(PLAIN_TEXT,
			SubstitutionTest.initialiseMappings("qwertyuiopasdfghjklzxcvbnm"));
	SubstitutionTreeSearch tester = new SubstitutionTreeSearch(s,
			p.probableSubstitutionGenerator(n.NgramAnalysis(1, CIPHER_TEXT, true)), u, pW, d);

	// @Test
	public void testRun() {
		// TODO complete design of test.
		Mapping[] bestKey = tester.run(CIPHER_TEXT, true);
		assertEquals(PLAIN_TEXT, s.decrypt(CIPHER_TEXT, bestKey));
		System.out.println(s.decrypt(CIPHER_TEXT, bestKey));
	}

}
