package cipher;

public class InitialiseStaticResources {

	static Utilities u = new Utilities();

	public static void uInit(boolean misc, boolean logs, boolean lettersDirect, boolean wordsDirect,
			boolean characterIndexForm) {
		System.out.println("Initialising...");
		if (misc) {
			System.out.println("Misc");
			u.generateHashMap(u.DICTIONARY_TEXT_PATH, u.DICTIONARY_HASH_PATH);
			u.generateHashMap(u.BIGRAM_WORD_TEXT_PATH, u.BIGRAM_WORD_HASH_PATH);
			u.generateLetterFrequencies(u.LETTER_FREQUENCIES_TEXT_PATH, u.LETTER_FREQUENCIES_MAP_PATH);
		}

		if (logs) {
			// Generates all letter maps with log probabilities.
			System.out.println("Logs");
			u.generateNGramMap(u.MONOGRAM_TEXT_PATH, u.MONOGRAM_LOG_MAP_PATH, true, true);
			u.generateNGramMap(u.BIGRAM_TEXT_PATH, u.BIGRAM_LOG_MAP_PATH, true, true);
			u.generateNGramMap(u.TRIGRAM_TEXT_PATH, u.TRIGRAM_LOG_MAP_PATH, true, true);
			u.generateNGramMap(u.QUADGRAM_TEXT_PATH, u.QUADGRAM_LOG_MAP_PATH, true, true);
			u.generateNGramMap(u.PENTAGRAM_TEXT_PATH, u.PENTAGRAM_LOG_MAP_PATH, true, true);
		}

		if (lettersDirect) {
			// Generates all letter maps with direct probabilities.
			System.out.println("Letters Direct");
			u.generateNGramMap(u.MONOGRAM_TEXT_PATH, u.MONOGRAM_MAP_PATH, true, false);
			u.generateNGramMap(u.BIGRAM_TEXT_PATH, u.BIGRAM_MAP_PATH, true, false);
			u.generateNGramMap(u.TRIGRAM_TEXT_PATH, u.TRIGRAM_MAP_PATH, true, false);
			u.generateNGramMap(u.QUADGRAM_TEXT_PATH, u.QUADGRAM_MAP_PATH, true, false);
			u.generateNGramMap(u.PENTAGRAM_TEXT_PATH, u.PENTAGRAM_MAP_PATH, true, false);
		}

		if (wordsDirect) {
			// Generates all word maps with direct probabilities.
			System.out.println("Words Direct");
			u.generateNGramMap(u.MONOGRAM_COUNTS_PATH, u.MONOGRAM_COUNTS_MAP_PATH, false, false);
			u.generateNGramMap(u.BIGRAM_COUNTS_PATH, u.BIGRAM_COUNTS_MAP_PATH, false, false);
			u.generateNGramMap(u.TRIGRAM_COUNTS_PATH, u.TRIGRAM_COUNTS_MAP_PATH, false, false);
			u.generateNGramMap(u.QUADGRAM_COUNTS_PATH, u.QUADGRAM_COUNTS_MAP_PATH, false, false);
			u.generateNGramMap(u.PENTAGRAM_COUNTS_PATH, u.PENTAGRAM_COUNTS_MAP_PATH, false, false);
		}

		if (characterIndexForm) {
			// Generates all character-index-form maps.
			System.out.println("Character-Index-Form");
			u.generateCharacterIndexFormMap(u.DICTIONARY_TEXT_PATH, u.MONOGRAM_CIF_PATH);
			u.generateCharacterIndexFormMap(u.BIGRAM_TEXT_PATH, u.BIGRAM_CIF_PATH);
			u.generateCharacterIndexFormMap(u.TRIGRAM_TEXT_PATH, u.TRIGRAM_CIF_PATH);
			u.generateCharacterIndexFormMap(u.QUADGRAM_TEXT_PATH, u.QUADGRAM_CIF_PATH);
			u.generateCharacterIndexFormMap(u.PENTAGRAM_TEXT_PATH, u.PENTAGRAM_CIF_PATH);
		}

		System.out.println("Done");
	}

	public static void main(String[] args) {
		uInit(true, true, true, true, true);
	}

}
