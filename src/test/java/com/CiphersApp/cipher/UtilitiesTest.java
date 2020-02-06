package test.java.com.CiphersApp.cipher;

import static org.junit.Assert.*;

import java.util.Hashtable;
import java.util.TreeMap;

import org.junit.Test;

import main.java.com.CiphersApp.cipher.Utilities;

public class UtilitiesTest {

	private Utilities tester = new Utilities();

	@Test
	public void testReadFile() {
		String[] expected = { "first", "second", "third", "fourth", "fifth", "sixth", "seventh", "eighth", "ninth",
				"tenth" };
		assertArrayEquals(tester.readFile("src/test/resources/readTest.txt"), expected);
	}

	@Test
	public void testHash64() {
		assertEquals(tester.hash64("test"), -439409999022904539L);
	}

	@Test
	public void testGenerateHashTable() {
		tester.generateHashMap("src/test/resources/readTest.txt", "src/test/resources/hashTable.htb");
	}

	@Test
	public void testReadHashTable() {
		tester.generateHashMap("src/test/resources/readTest.txt", "src/test/resources/hashTable.htb");
		String[] expected = { "first", "second", "third", "fourth", "fifth", "sixth", "seventh", "eighth", "ninth",
				"tenth" };
		Hashtable<Long, String> table = new Hashtable<Long, String>();
		for (String line : expected) {
			table.put(tester.hash64(line), line);
		}
		assertEquals(tester.readHashTable("src/test/resources/hashTable.htb").entrySet(), table.entrySet());
	}

	@Test
	public void testCleanText() {
		assertEquals(tester.cleanText("A򽚛ݚM둑aQ˛򖰭ݨ1⹦Mj󿅳ɿ뻜z晗񞠗紡�?�Ӏm(J⃃!럑󣾜庴"), "amaqmjzmj");
	}

	@Test
	public void testDeSpace() {
		assertEquals("", tester.deSpace(" 	"));
	}

	@Test
	public void testGenerateLetterFrequencies() {
		tester.generateLetterFrequencies("src/test/resources/letterFrequencies.txt", "src/test/resources/letterFrequencies.tmp");
	}

	@Test
	public void testReadLetterFrequencies() {
		TreeMap<Character, Double> frequencies = new TreeMap<Character, Double>();
		frequencies.put('e', 0.227d);
		assertEquals(tester.readLetterFrequencies("src/test/resources/letterFrequencies.tmp").entrySet(),
				frequencies.entrySet());
	}
	
	@Test
	public void testLoadNgramMap() {
		TreeMap<String, Double> probabilities = tester.loadNgramMap(tester.TRIGRAM_LOG_MAP_PATH);
		assertTrue(probabilities.keySet().size() == 17556);
		assertEquals(Math.pow(10, probabilities.get("the")) * 4274127909d, 77534223d, 0.077534223);
		// Tests that it can resolve frequencies to order E-9 accuracy by
		// performing the inverse of the done operation.
		assertEquals(Math.pow(10, probabilities.get("gng")) * 4274127909d, 7610, 0.00000761);
	}

}
