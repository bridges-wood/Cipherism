package cipher;

public class InitialiseStaticResources {
	
static Utilities u = new Utilities();
	
	public static void uInit() {
		System.out.println("Initialising...");
		u.generateHashMap(u.DICTIONARY_TEXT_PATH, u.DICTIONARY_HASH_PATH);
		u.generateHashMap(u.BIGRAM_WORD_TEXT_PATH, u.BIGRAM_WORD_HASH_PATH);
		u.generateLetterFrequencies(u.LETTER_FREQUENCIES_TEXT_PATH, u.LETTER_FREQUENCIES_MAP_PATH);
		u.generateNGramMap(u.MONOGRAM_TEXT_PATH, u.MONOGRAM_MAP_PATH, true, true);
		u.generateNGramMap(u.BIGRAM_TEXT_PATH, u.BIGRAM_MAP_PATH, true, true);
		u.generateNGramMap(u.TRIGRAM_TEXT_PATH, u.TRIGRAM_MAP_PATH, true, true);
		u.generateNGramMap(u.QUADGRAM_TEXT_PATH, u.QUADGRAM_MAP_PATH, true, true);
		u.generateNGramMap(u.PENTAGRAM_TEXT_PATH, u.PENTAGRAM_MAP_PATH, true, true);
		
		u.generateNGramMap(u.MONOGRAM_COUNTS_PATH, u.MONOGRAM_COUNTS_MAP_PATH, false, false);
		u.generateNGramMap(u.BIGRAM_COUNTS_PATH, u.BIGRAM_COUNTS_MAP_PATH, false, false);
		u.generateNGramMap(u.TRIGRAM_COUNTS_PATH, u.TRIGRAM_COUNTS_MAP_PATH, false, false);
		u.generateNGramMap(u.QUADGRAM_COUNTS_PATH, u.QUADGRAM_COUNTS_MAP_PATH, false, false);
		u.generateNGramMap(u.PENTAGRAM_COUNTS_PATH, u.PENTAGRAM_COUNTS_MAP_PATH, false, false);
		//u.generateHashTable(filename, outputFilename);
	}

	public static void main(String[] args) {
		uInit();
	}

}
