package cipher;

public class InitialiseStaticResources {
	
static Utilities u = new Utilities();
	
	public static void uInit() {
		System.out.println("Initialising...");
		u.generateHashTable(u.DICTIONARY_TEXT_PATH, u.DICTIONARY_HASH_PATH);
		u.generateHashTable(u.BIGRAM_WORD_TEXT_PATH, u.BIGRAM_WORD_HASH_PATH);
		u.generateTreeMap(u.LETTER_FREQUENCIES_TEXT_PATH, u.LETTER_FREQUENCIES_MAP_PATH);
		//u.generateHashTable(filename, outputFilename);
	}

	public static void main(String[] args) {
		uInit();
	}

}
