package cipher;

import static org.junit.Assert.*;

import org.junit.Test;

public class IOCTest {

	private Utilities u = new Utilities();
	private NGramAnalyser n = new NGramAnalyser(u);
	private IOC tester = new IOC(n);
	private final float κr = 0.038466f;
	private final float κp = 0.0686f;

	@Test
	public void testKappaText() {
		// Testing x random strings of length y.
		int x = 50;
		int y = 300;
		for (int i = 0; i < x; i++) {
			StringBuilder sb = new StringBuilder();
			for (int j = 0; j < y; j++) {
				sb.append((char) Math.round(Math.random() * 26 + 97));
			}
			assertTrue(Math.abs(κr - tester.kappaText(n.NgramAnalysis(1, sb.toString(), false), y)) < 0.01);
			// Test of sufficient closeness to κr as predicted.
		}
		// Testing known english text.
		String text = "I am by birth a Genevese, and my family is one of the most distinguished of that republic."
				+ " My ancestors had been for many years counsellors and syndics, and my father had filled several"
				+ " public situations with honour and reputation. He was respected by all who knew him for his"
				+ " integrity and indefatigable attention to public business. He passed his younger days perpetually"
				+ " occupied by the affairs of his country; a variety of circumstances had prevented his marrying"
				+ " early, nor was it until the decline of life that he became a husband and the father of a family.";
		// Extract from Chapter 1 of Mary Shelley's Frankenstein.
		text = u.cleanText(text);
		assertTrue(Math.abs(κp - tester.kappaText(n.NgramAnalysis(1, text, false), y)) < 0.01);
		// Test of sufficient closeness to κp as predicted.
	}

	@Test
	public void testIndexOfCoincidence() {
		// Testing x random strings of length y.
		int x = 50;
		int y = 300;
		for (int i = 0; i < x; i++) {
			StringBuilder sb = new StringBuilder();
			for (int j = 0; j < y; j++) {
				sb.append((char) Math.round(Math.random() * 26 + 97));
			}
			assertTrue(Math.abs(1 - tester.indexOfCoincidence(n.frequencyAnalysis(sb.toString()))) < 0.12);
			// Test of sufficient closeness to 1 as predicted.
		}
		// Testing known english text.
		String text = "Having had some time at my disposal when in London, I had visited the British Museum,"
				+ " and made search among the books and maps in the library regarding Transylvania; it had"
				+ " struck me that some foreknowledge of the country could hardly fail to have some importance"
				+ " in dealing with a nobleman of that country. I find that the district he named is in the"
				+ " extreme east of the country, just on the borders of three states, Transylvania, Moldavia"
				+ " and Bukovina, in the midst of the Carpathian mountains; one of the wildest and least known"
				+ " portions of Europe. I was not able to light on any map or work giving the exact locality of"
				+ " the Castle Dracula, as there are no maps of this country as yet to compare with our own"
				+ " Ordnance Survey maps; but I found that Bistritz, the post town named by Count Dracula, is a"
				+ " fairly well-known place. I shall enter here some of my notes, as they may refresh my memory"
				+ " when I talk over my travels with Mina.";
		// Extract from Chapter 1 of Bram Stoker's Dracula.
		text = u.cleanText(text);
		assertTrue(Math.abs(1 - tester.indexOfCoincidence(n.frequencyAnalysis(text))) > 0.5);
		// Test of sufficient distance from 1 as predicted.
	}

}
