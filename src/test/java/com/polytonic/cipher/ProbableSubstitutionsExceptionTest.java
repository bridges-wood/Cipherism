package com.polytonic.cipher;

import static org.junit.Assert.*;

import java.util.TreeMap;

import org.junit.Test;

public class ProbableSubstitutionsExceptionTest {

	private ProbableSubstitutions tester = new ProbableSubstitutions();

	@Test(expected = IllegalArgumentException.class)
	public void testMaxKey() {
		TreeMap<String, Double> newMap = new TreeMap<String, Double>();
		assertEquals("2", tester.maxKey(newMap));
	}

}
