package cipher;

/**
 * Class that contains all complete cipher decryption stacks.
 * 
 * @author Max Wood
 *
 */
public class CipherBreakers {

	private FileIO u;
	private DetectEnglish d;
	private KasiskiExamination k;
	private NGramAnalyser n;
	private Vigenere v;
	private ProbableSubstitutions p;
	private PredictWords pw;
	private Substitution s;
	private SubstitutionTreeSearch sts;

	public CipherBreakers(FileIO u, KasiskiExamination k, ProbableSubstitutions p) {
		this.n = new NGramAnalyser(u);
		this.d = new DetectEnglish(u, n);
		this.u = u;
		this.k = k;
		this.p = new ProbableSubstitutions();
		this.v = new Vigenere();
		this.s = new Substitution();
		this.pw = new PredictWords();
	}

	/**
	 * Takes in a Vigenere-encrypted string and completely decrypts and respaces it.
	 * 
	 * @param text The text to be decrypted.
	 * @return The original plain-text.
	 */
	public String vigenereBreaker(String text) {
		String[] keys = k.vigenereKeys(text);

		if (keys.length == 0)
			return "Error 1: Decryption failed. No keys found.";

		double maxScore = Double.NEGATIVE_INFINITY;
		String fittestKey = "";
		/*
		 * As the Vignere decryptor generates multiple keys, we need to detect the
		 * fittest one.
		 */
		for (String key : keys) {
			double score = u.deSpace(d.graphicalRespace(v.decrypt(text, key), 20)).length() / text.length();
			if (score > maxScore) {
				maxScore = score;
				fittestKey = key;
			}
		}
		return d.graphicalRespace(v.decrypt(text, fittestKey), 20);
	}

	/**
	 * Takes in a string encrypted with the simple substitution cipher and decrypts
	 * it and returns a respaced version.
	 * 
	 * @param text The text to be decrypted.
	 * @return The original plain-text.
	 */
	public String substitutionBreaker(String text) {
		Mapping[] inititalKey = p.probableSubstitutionGenerator(n.NgramAnalysis(1, text, false));
		this.sts = new SubstitutionTreeSearch(s, inititalKey, u, pw, d);
		boolean spaced = false;
		if (text.contains(" "))
			spaced = true;
		if (spaced) {
			return s.decrypt(text, sts.monteCarloTreeSearch(text, spaced));
		} else {
			return d.graphicalRespace(s.decrypt(text, sts.monteCarloTreeSearch(text, spaced)), 20);
		}
	}

}
