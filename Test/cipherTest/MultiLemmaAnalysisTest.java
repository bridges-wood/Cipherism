package cipherTest;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import cipher.Mapping;
import cipher.MultiLemmaAnalysis;
import cipher.PredictWords;
import cipher.Substitution;
import cipher.Utilities;

public class MultiLemmaAnalysisTest {

	Utilities u = new Utilities();
	PredictWords p = new PredictWords(u);
	MultiLemmaAnalysis tester = new MultiLemmaAnalysis(u, p);
	Substitution s = new Substitution();
	Mapping[] mappings = SubstitutionTest.initialiseMappings("zyxwvutsrqponmlkjihgfedcba");

	@Test
	public void testPossibleLemmata() {
		String[] cases = {"that the", "this is", "could be"};
		for(String testCase : cases) {
			String[] possibleLemmata = tester.possibleLemmata(s.encrypt(testCase, mappings));
			List<String> list = Arrays.asList(possibleLemmata);
			assertTrue(list.contains(testCase));
		}
	}

}
