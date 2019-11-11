package cipher;

public class Tester {

	Tester() {
		run();
	}

	public void run() {
		Utilities u = new Utilities();
		// u.generateHashTable("Server//StaticResources//mostProbable.txt",
		// "Server//StaticResources//mostProbable.htb");
		// u.generateHashTable("Server//StaticResources//dictionary.txt",
		// "Server//StaticResources//dictionary.htb");
		Vigenere v = new Vigenere();
		String toEncrypt = "this is meant to be an example of a simple english text of enough length to demonstrate the features of this";
		String otherText = v.encrypt(u.cleanText(toEncrypt), "two"); // a test string to be encrypted Key: tester
		//System.out.println(otherText + " " + v.decrypt(u.cleanText(toEncrypt), "ankey"));
		Manager m = new Manager(otherText);
		// DetectEnglish d = new DetectEnglish();
		// System.out.println(d.graphicalRespace("howmanywordsdoyouactuallyknow", 20));
	}

	public static void main(String[] args) {
		Tester a = new Tester();
	}

}
