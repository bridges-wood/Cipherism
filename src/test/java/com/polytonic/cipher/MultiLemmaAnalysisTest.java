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
public class MultiLemmaAnalysisTest {

	private String lemata;
	private String encrypted;
	private FileIO u = new FileIO();
	private PredictWords p = new PredictWords();
	private MultiLemmaAnalysis tester = new MultiLemmaAnalysis(u, p);
	private Substitution s = new Substitution();
	private final Mapping[] MAPPINGS = SubstitutionTest.initialiseMappings("zyxwvutsrqponmlkjihgfedcba");

	public MultiLemmaAnalysisTest(String lemata) {
		this.lemata = lemata;
		this.encrypted = s.encrypt(lemata, MAPPINGS);
	}

	@Parameters
	public static Collection<Object[]> data() {
		Object[][] data = new Object[][] { 
			{ "that the" },
			{ "this is"},
			{ "could be" },
			{ "this is an" },
			{ "how could this" },
			{ "this is the result of" }
		};
		return Arrays.asList(data);
	}

	@Test
	public void testPossibleLemmata() {
			String[] possibleLemata = tester.possibleLemmata(encrypted);
			System.out.println(possibleLemata.length);
			List<String> list = Arrays.asList(possibleLemata);
			assertTrue(list.contains(lemata));
		}
	}