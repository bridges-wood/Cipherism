package com.polytonic.cipher;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class IsEnglishStringTest {

	private String testCase;
	private FileIO u = new FileIO();
	private DetectEnglish tester = new DetectEnglish(u, new NGramAnalyser(u));

	public IsEnglishStringTest(String testCase) {
		this.testCase = testCase;
	}
	
	@Parameters
	public static Collection<Object[]> data(){
		Object[][] data = new Object[][] {
			{ "lammer" },
			{ "plenteously" },
			{ "jannock" },
			{ "bombproof" },
			{ "reburials" },
			{ "kursaals" },
			{ "flukeworm" },
			{ "requires" },
			{ "subsacral" },
			{ "the" }
		};
		return Arrays.asList(data);
	}

	@Test
	public void testIsEnglishString() {
		assertTrue(tester.isEnglish(testCase));
	}

}
