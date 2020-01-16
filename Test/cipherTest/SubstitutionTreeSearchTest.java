package cipherTest;

import static org.junit.Assert.*;

import org.junit.Test;

import cipher.DetectEnglish;
import cipher.Mapping;
import cipher.NGramAnalyser;
import cipher.PredictWords;
import cipher.ProbableSubstitutions;
import cipher.Substitution;
import cipher.SubstitutionTreeSearch;
import cipher.Utilities;

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

	@Test
	public void testRun() {
		Mapping[] bestKey = tester.run(CIPHER_TEXT, true);
		System.out.println(s.decrypt(CIPHER_TEXT, bestKey));
	}

}
