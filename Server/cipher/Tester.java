package cipher;

public class Tester {

	Tester() {
		run();
	}

	public void run() {
		Utilities u = new Utilities();
		KasiskiExamination k = new KasiskiExamination();
		NGramAnalyser n = new NGramAnalyser();
		Vigenere v = new Vigenere();
		DetectEnglish d = new DetectEnglish();
		String toEncrypt = "this is meant to be an example of a simple english text of enough length to demonstrate the features of this";
		toEncrypt = u.cleanText(toEncrypt).replace(" ", "");
		//System.out.println(toEncrypt);
		System.out.println(d.graphicalRespace(toEncrypt, 20));
		/*String key = "akey";
		//System.out.println(k.computeFractionalMS(n.frequencyAnalysis(toEncrypt), toEncrypt.length()));
		String otherText = v.encrypt(toEncrypt, key);
		System.out.println(otherText + " " + v.decrypt(otherText, key));
		Manager m = new Manager(otherText);*/
		
	}

	public static void main(String[] args) {
		Tester a = new Tester();
	}

}
