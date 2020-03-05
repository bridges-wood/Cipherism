package com.polytonic.cipher;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class WordEncodingTest {

	private String word;
	private Integer[] encoding;
	private PredictWords tester = new PredictWords();

	public WordEncodingTest(String word, Integer[] encoding) {
		this.word = word;
		this.encoding = encoding;
	}

	@Parameters
	public static Collection<Object[]> data(){
		Object[][] data = {
				{"scenario", new Integer[] {0, 1, 2, 3, 4, 5, 6, 7} },
				{"pride", new Integer[] {0, 1, 2, 3, 4} },
				{"act", new Integer[] {0, 1, 2} },
				{"lecture", new Integer[] {0, 1, 2, 3, 4, 5, 1} },
				{"corpse", new Integer[] {0, 1, 2, 3, 4, 5} },
				{"inside", new Integer[] {0, 1, 2, 0, 3, 4} },
				{"switch", new Integer[] {0, 1, 2, 3, 4, 5} },
				{"litigation", new Integer[] {0, 1, 2, 1, 3, 4, 2, 1, 5, 6} },
				{"picture", new Integer[] {0, 1, 2, 3, 4, 5, 6} }
		};
		return Arrays.asList(data);
	}

	@Test
	public void testEncodeWord() {
		assertArrayEquals(encoding, tester.encodeWord(word));
	}

}
