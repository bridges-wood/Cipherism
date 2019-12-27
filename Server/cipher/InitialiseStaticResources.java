package cipher;

public class InitialiseStaticResources {
	
static Utilities u = new Utilities();
	
	public static void uInit() {
		System.out.println("Initialising...");
		u.generateHashTable("src\\main\\resources\\dictionary.txt", "src\\main\\resources\\dictionary.htb");
		u.generateHashTable("src\\main\\resources\\2grams.txt", "src\\main\\resources\\2grams.htb");
		//u.generateHashTable(filename, outputFilename);
	}

	public static void main(String[] args) {
		uInit();
	}

}
