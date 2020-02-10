package cipher;

public class CipherBreakers {

	private Utilities u;
	private DetectEnglish d;
	private KasiskiExamination k;
	private Vigenere v;
	private ProbableSubstitutions p;
	private PredictWords pw;
	private Substitution s; 
	private SubstitutionTreeSearch sts;

	public CipherBreakers(Utilities u, KasiskiExamination k, DetectEnglish d, ProbableSubstitutions p) {
		this.d = d;
		this.u = u;
		this.k = k;
		this.p = new ProbableSubstitutions();
		this.v = new Vigenere();
		this.s = new Substitution();
		this.pw = new PredictWords();
		//TODO generate initial
		this.sts = new SubstitutionTreeSearch(s, null, u, pw, d);
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
		
		return "";
	}

}
