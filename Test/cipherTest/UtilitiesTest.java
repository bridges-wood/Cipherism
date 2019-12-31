package cipherTest;

import static org.junit.Assert.*;

import java.util.Hashtable;

import org.junit.Test;

import cipher.Utilities;

public class UtilitiesTest {
	
	private Utilities tester = new Utilities();

	@Test
	public void testReadFile() {
		String[] expected = {"first", "second", "third", "fourth", "fifth", "sixth", "seventh", "eighth", "ninth", "tenth"};
		assertArrayEquals(tester.readFile("TestResources\\readTest.txt"), expected);
	}

	@Test
	public void testHash64() {
		assertEquals(tester.hash64("test"), -439409999022904539L);
	}

	@Test
	public void testGenerateHashTable() {
		tester.generateHashTable("TestResources\\readTest.txt", "TestResources\\hashTable.htb");
	}

	@Test
	public void testReadHashTable() {
		tester.generateHashTable("TestResources\\readTest.txt", "TestResources\\hashTable.htb");
		String[] expected = {"first", "second", "third", "fourth", "fifth", "sixth", "seventh", "eighth", "ninth", "tenth"};
		Hashtable<Long, String> table = new Hashtable<Long, String>();
		for(String line : expected) {
			table.put(tester.hash64(line), line);
		}
		assertEquals(tester.readHashTable("TestResources\\hashTable.htb").entrySet(), table.entrySet());
	}

	@Test
	public void testCleanText() {
		assertEquals(tester.cleanText("A򽚛ݚM둑aQ˛򖰭ݨ1⹦Mj󿅳ɿ뻜z晗񞠗紡򈁓Ӏm(J⃃!럑󣾜庴"), "amaqmjzmj");
	}
	
	@Test
	public void testDeSpace() {
		assertEquals("", tester.deSpace(" 	"));
	}

}
