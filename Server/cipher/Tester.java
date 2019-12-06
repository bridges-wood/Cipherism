package cipher;

public class Tester {

	Tester() {
		run();
	}

	public void run() {
		Utilities u = new Utilities();
		//u.generateHashTable("Server\\StaticResources\\UKACD17.TXT", "Server\\StaticResources\\dictionary.htb");
		//u.generateHashTable("Server\\StaticResources\\2grams.txt", "Server\\StaticResources\\2grams.htb");
		KasiskiExamination k = new KasiskiExamination();
		NGramAnalyser n = new NGramAnalyser();
		Vigenere v = new Vigenere();
		DetectEnglish d = new DetectEnglish();
		String toEncrypt = "woman lives after her heart stops for six hours";
		toEncrypt = u.cleanText(toEncrypt).replace(" ", "");
		//System.out.println(toEncrypt);
		long start = System.nanoTime();
		System.out.println(d.graphicalRespace(toEncrypt, 20));
		System.out.print("For length of text: " + toEncrypt.length() + " that took " + (System.nanoTime() - start)*1E-9 + "seconds");
		
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
