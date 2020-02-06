package main.java.com.CiphersApp.cipher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.TreeMap;

import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;

/**
 * For all devices connected to the server, we will assign a Manager to it.
 */
public class Manager {

	private Utilities u;
	private ProbableSubstitutions p;
	private IOC i;
	private NGramAnalyser n;
	private KasiskiExamination k;
	private CipherBreakers c;
	private DetectEnglish d;

	private String result = "";
	private String text = "";

	public Manager(String text, boolean test) {
		this.text = text;
		this.u = new Utilities();
		this.p = new ProbableSubstitutions();
		this.n = new NGramAnalyser(u);
		this.i = new IOC(n);
		this.d = new DetectEnglish(u, n);
		this.c = new CipherBreakers(u, k, d, p, n);
		if (!test)
			run(u.cleanText(text));
	}

	private String run(String text) {
		switch (detectCipher(text)) {
		case "Periodic":
			return c.vigenereBreaker(text);
		case "Substitution":
			return c.substitutionBreaker(text);
		}
		return "";
	}

	/**
	 * Detects whether a given piece of text is encoded with a periodic cipher, by
	 * identifying peaks in the indices of coincidence.
	 * 
	 * @param text The text to be analysed.
	 * @return A boolean representing whether the text is encoded periodically.
	 */
	public boolean detectPeriodic(String text) {
		double[] IOCs = new double[19];
		for (int n = 2; n <= 20; n++) {
			IOCs[n - 2] = i.periodIndexOfCoincidence(n, text);
		}
		double sd = new StandardDeviation().evaluate(IOCs);
		double mean = new Mean().evaluate(IOCs);
		int counter = 0;
		for (double index : IOCs) {
			if (index >= mean + sd)
				counter++;
			/*
			 * If the text is encrypted with a periodic cipher, peaks should be identified
			 * in the indices of coincidence.
			 */
		}
		if (counter >= 3) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Detects whether a given piece of text has been encoded with a substitution
	 * cipher by calculating the Bhattacharya coefficient of the letter distribution
	 * of the text and normal English.
	 * 
	 * @param text The text to be analysed.
	 * @return A boolean representing whether the text is encoded using a
	 *         substitution cipher.
	 */
	public boolean detectSubstitution(String text) {
		TreeMap<Character, Double> expectedLetterFrequencies = u.readLetterFrequencies(u.LETTER_FREQUENCIES_MAP_PATH);
		ArrayList<Double> expected = new ArrayList<Double>(expectedLetterFrequencies.values());
		Collections.sort(expected);
		TreeMap<String, Double> observeredLetterFrequencies = n.NgramAnalysis(1, text, false);
		ArrayList<Double> observed = new ArrayList<Double>(observeredLetterFrequencies.values());
		Collections.sort(observed);
		double bc = 0;
		for (int i = 0; i < 25; i++) {
			bc += Math.sqrt(expected.get(i) * observed.get(i));
		}
		bc = -Math.log(bc);
		if (Math.abs(bc) < 0.25) {
			return true;
		} else {
			return false;
		}
	}

	public String detectCipher(String text) {
		if (detectPeriodic(text)) {
			return "Periodic";
		} else {
			return "Substitution";
		}
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}
