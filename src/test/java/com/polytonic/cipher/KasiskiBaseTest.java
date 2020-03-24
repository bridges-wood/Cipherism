package com.polytonic.cipher;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.TreeMap;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class KasiskiBaseTest {

	private FileIO u = new FileIO();
	private NGramAnalyser tester = new NGramAnalyser(u);
	private final String PLAINTEXT = u.cleanText(
			"Mr. Utterson the lawyer was a man of a rugged countenance that was never lighted by a smile; cold,"
					+ " scanty and embarrassed in discourse; backward in sentiment; lean, long, dusty, dreary and yet somehow"
					+ " lovable. At friendly meetings, and when the wine was to his taste, something eminently human beaconed"
					+ " from his eye; something indeed which never found its way into his talk, but which spoke not only in"
					+ " these silent symbols of the after-dinner face, but more often and loudly in the acts of his life. He"
					+ " was austere with himself; drank gin when he was alone, to mortify a taste for vintages; and though he"
					+ " enjoyed the theatre, had not crossed the doors of one for twenty years. But he had an approved tolerance"
					+ " for others; sometimes wondering, almost with envy, at the high pressure of spirits involved in their"
					+ " misdeeds; and in any extremity inclined to help rather than to reprove. I incline to Cain's heresy, the"
					+ " used to say quaintly: I let my brother go to the devil in his own way. In this character, it was"
					+ " frequently his fortune to be the last reputable acquaintance and the last good influence in the lives of"
					+ " downgoing men. And to such as these, so long as they came about his chambers, he never marked a shade of"
					+ " change in his demeanour.");
	private int length;
	private String target;
	private int count;
	private TreeMap<String, Integer> map1 = tester.kasiskiBase(1, PLAINTEXT);
	private TreeMap<String, Integer> map2 = tester.kasiskiBase(2, PLAINTEXT);
	private TreeMap<String, Integer> map3 = tester.kasiskiBase(3, PLAINTEXT);

	public KasiskiBaseTest(String target, int count) {
		this.length = target.length();
		this.target = target;
		this.count = count;
	}

	@Parameters
	public static Collection<Object[]> data() {
		Object[][] data = new Object[][] { 
			{ "e", 131 }, 
			{ "x", 1 }, 
			{ "i", 66 }, 
			{ "of", 8 },
			{ "in", 31 },
			{ "at", 5 },
			{ "the", 20 },
			{ "how", 1 }
			};
		return Arrays.asList(data);
	}

	@Test
	public void testKasiskiBase() {
		switch(length) {
		case 1:
			assertEquals(count, map1.get(target).intValue());
			break;
		case 2:
			assertEquals(count, map2.get(target).intValue());
			break;
		case 3:
			assertEquals(count, map3.get(target).intValue());
			break;
		}
		
	}

}
