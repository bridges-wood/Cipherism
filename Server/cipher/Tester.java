package cipher;

public class Tester {

	Tester() {
		run();
	}

	public void run() {
		Utilities u = new Utilities();
		//u.generateHashTable("Server//StaticResources//mostProbable.txt", "Server//StaticResources//mostProbable.htb");
		u.generateHashTable("Server//StaticResources//dictionary.txt", "Server//StaticResources//dictionary.htb");
		/*Vigenere v = new Vigenere();
		String otherText = v.encrypt("thisisateststring", "key"); //a test string to be encrypted Key: tester
		Manager m = new Manager(otherText);*/
		DetectEnglish d = new DetectEnglish();
		System.out.println(d.graphicalRespace("thisisateststring", 20));
	}

	public static void main(String[] args) {
		Tester a = new Tester();
	}

}
