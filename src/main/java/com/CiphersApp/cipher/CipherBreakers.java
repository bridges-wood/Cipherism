package main.java.com.CiphersApp.cipher;

public class CipherBreakers {

	private Utilities u;
	private DetectEnglish d;
	private KasiskiExamination k;
	private Vigenere v;
	private ProbableSubstitutions p;
	private NGramAnalyser n;
	private Substitution s; 

	public CipherBreakers(Utilities u, KasiskiExamination k, DetectEnglish d, ProbableSubstitutions p,
			NGramAnalyser n) {
		this.d = d;
		this.u = u;
		this.k = k;
		this.n = n;
		this.p = new ProbableSubstitutions();
		this.v = new Vigenere();
		this.s = new Substitution();
	}

	public String vigenereBreaker(String text) {
		String[] keys = k.vigenereKeys(text);
		double maxScore = Double.NEGATIVE_INFINITY;
		String fittestKey = "";
		for (String key : keys) {
			double score = u.deSpace(d.graphicalRespace(v.decrypt(text, key), 20)).length() / text.length();
			if (score > maxScore) {
				maxScore = score;
				fittestKey = key;
			}
		}
		return d.graphicalRespace(v.decrypt(text, fittestKey), 20);
	}

	public String substitutionBreaker(String text) {
		/*
		 * TODO Plan: Do one pass based on FA, using letters of high confidence. Using the
		 * same graphical technique as previously, examine possible words using probable
		 * substitutions, locking in the letter from FA. From there, generate a tree
		 * using the possible encodings and use the path with the best score.
		 */
		return "";
	}

}
