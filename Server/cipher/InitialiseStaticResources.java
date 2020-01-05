package cipher;

public class InitialiseStaticResources {
	
static Utilities u = new Utilities();
	
	public static void uInit() {
		System.out.println("Initialising...");
		u.generateHashMap(u.DICTIONARY_TEXT_PATH, u.DICTIONARY_HASH_PATH);
		u.generateHashMap(u.BIGRAM_WORD_TEXT_PATH, u.BIGRAM_WORD_HASH_PATH);
		u.generateLetterFrequencies(u.LETTER_FREQUENCIES_TEXT_PATH, u.LETTER_FREQUENCIES_MAP_PATH);
		u.generateNGramMap(1, u.MONOGRAM_MAP_PATH);
		u.generateNGramMap(2, u.BIGRAM_MAP_PATH);
		u.generateNGramMap(3, u.TRIGRAM_MAP_PATH);
		u.generateNGramMap(4, u.QUADGRAM_MAP_PATH);
		u.generateNGramMap(4, u.PENTAGRAM_MAP_PATH);
		//u.generateHashTable(filename, outputFilename);
	}

	public static void main(String[] args) {
		uInit();
	}

}
