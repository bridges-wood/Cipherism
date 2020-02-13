package cipher;

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
		Mapping[] inititalKey = p.probableSubstitutionGenerator(n.NgramAnalysis(1, text, false));
		this.sts = new SubstitutionTreeSearch(s, inititalKey, u, pw, d);
		boolean spacing = false;
		if (text.contains(" "))
			spacing = true;
		return s.decrypt(text, sts.run(text, spacing));
	}

}
