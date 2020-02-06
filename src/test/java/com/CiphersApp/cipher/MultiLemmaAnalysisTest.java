package test.java.com.CiphersApp.cipher;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import main.java.com.CiphersApp.cipher.Mapping;
import main.java.com.CiphersApp.cipher.MultiLemmaAnalysis;
import main.java.com.CiphersApp.cipher.PredictWords;
import main.java.com.CiphersApp.cipher.Substitution;
import main.java.com.CiphersApp.cipher.Utilities;

public class MultiLemmaAnalysisTest {

	private Utilities u = new Utilities();
	private PredictWords p = new PredictWords();
	private MultiLemmaAnalysis tester = new MultiLemmaAnalysis(u, p);
	private Substitution s = new Substitution();
	private final Mapping[] MAPPINGS = SubstitutionTest.initialiseMappings("zyxwvutsrqponmlkjihgfedcba");

	@Test
	public void testPossibleLemmata() {
		String[] cases = { "that the", "this is", "could be", "this is an", "how could this", "this is the result of" };
		// Test cases can be added as needed.
		for (String testCase : cases) {
			String[] possibleLemmata = tester.possibleLemmata(s.encrypt(testCase, MAPPINGS));
			List<String> list = Arrays.asList(possibleLemmata);
			assertTrue(list.contains(testCase));
		}
	}

}
