package cipher;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public class Utilities {

	public final String DICTIONARY_HASH_PATH = "src/main/resources/dictionary.htb";
	public final String DICTIONARY_TEXT_PATH = "src/main/resources/dictionary.txt";
	public final String BIGRAM_WORD_HASH_PATH = "src/main/resources/2grams.htb";
	public final String BIGRAM_WORD_TEXT_PATH = "src/main/resources/2grams.txt";
	public final String TRIGRAM_WORD_TEXT_PATH = "src/main/resources/3grams.txt";
	public final String QUADGRAM_WORD_TEXT_PATH = "src/main/resources/4grams.txt";
	public final String PENTAGRAM_WORD_TEXT_PATH = "src/main/resources/5grams.txt";
	public final String MONOGRAM_TEXT_PATH = "src/main/resources/1l.txt";
	public final String BIGRAM_TEXT_PATH = "src/main/resources/2l.txt";
	public final String TRIGRAM_TEXT_PATH = "src/main/resources/3l.txt";
	public final String QUADGRAM_TEXT_PATH = "src/main/resources/4l.txt";
	public final String PENTAGRAM_TEXT_PATH = "src/main/resources/5l.txt";
	public final String MONOGRAM_MAP_PATH = "src/main/resources/1l.tmp";
	public final String BIGRAM_MAP_PATH = "src/main/resources/2l.tmp";
	public final String TRIGRAM_MAP_PATH = "src/main/resources/3l.tmp";
	public final String QUADGRAM_MAP_PATH = "src/main/resources/4l.tmp";
	public final String PENTAGRAM_MAP_PATH = "src/main/resources/5l.tmp";
	public final String MOST_PROBABLE_TEXT_PATH = "src/main/resources/mostProbable.txt";
	public final String LETTER_FREQUENCIES_TEXT_PATH = "src/main/resources/letterFrequencies.txt";
	public final String LETTER_FREQUENCIES_MAP_PATH = "src/main/resources/letterFrequencies.tmp";
	private final long FNV1_64_INIT = 0xcbf29ce484222325L;
	private final long FNV1_PRIME_64 = 1099511628211L;
	private final Kryo kyro = new Kryo();

	public Utilities() {
		kyro.register(new HashMap<Long, String>().getClass()); // Registration is required for proper serialisation.
		kyro.register(new TreeMap<Character, Double>().getClass());
		kyro.register(new TreeMap<String, Double>().getClass());
	}

	/**
	 * Returns all lines in a text file as separate words in a string array.
	 * 
	 * @param filename The name of the file to be retrieved.
	 * @return A string array of each line in the file.
	 */
	public String[] readFile(String filename) {
		List<String> words = new LinkedList<String>();
		File file = new File(filename);
		try {
			BufferedReader br = new BufferedReader(
					new InputStreamReader(new FileInputStream(file), StandardCharsets.US_ASCII));
			String line;
			while ((line = br.readLine()) != null) {
				/*
				 * Avoids having a NullPointerException as we automatically detect once the end
				 * of the file has been reached.
				 */
				words.add(line);
			}
			br.close();
		} catch (FileNotFoundException f) {
			System.err.println("Text file to be read not found.");
		} catch (IOException e) {
			System.err.println("Buffered reader failed to read file correctly.");
		}
		return words.parallelStream().toArray(String[]::new); // Fastest possible encoding to array.
	}

	/**
	 * Gives the 64 bit FNV-1a hash of an input string.
	 *
	 * @param text The text from which the hash is to be generated.
	 * @return The hash of the input data.
	 */
	public long hash64(String text) {
		byte[] data = text.getBytes();
		int length = data.length;
		long hash = FNV1_64_INIT;
		/*
		 * FNV-1a is used instead of FNV-1 as it has better avalanche characteristics
		 * for short strings.
		 */
		for (int i = 0; i < length; i++) {
			hash ^= (data[i] & 0xff); // XOR
			hash *= FNV1_PRIME_64; // Multiply
		}

		return hash;
	}

	/**
	 * Creates a hash-map using FNV1-a and each line of a given file of type <Long,
	 * String>.
	 * 
	 * @param filename       The name of the file which lines are to be read from.
	 * @param outputFilename The name of the file to which the hashtable is saved.
	 */
	public void generateHashMap(String filename, String outputFilename) {
		File fromFile = new File(filename);
		File toFile = new File(outputFilename);
		HashMap<Long, String> hashTable = new HashMap<Long, String>();
		try {
			BufferedReader br = new BufferedReader(
					new InputStreamReader(new FileInputStream(fromFile), StandardCharsets.US_ASCII));
			String line;
			while ((line = br.readLine()) != null) {
				hashTable.put(hash64(line), line); // Puts each line into the hashtable.
			}
			br.close();
			Output out = new Output(new FileOutputStream(toFile));
			kyro.writeClassAndObject(out, hashTable);
			/*
			 * Writing both the class and the object means that we can avoid unchecked
			 * casting on loading back into memory.
			 */
			out.close();
		} catch (FileNotFoundException f) {
			System.err.println("Text file to be hashed not found.");
		} catch (IOException i) {
			System.err.println("Buffered reader failed to read file correctly.");
		}
	}

	/**
	 * Returns the has-hmap that was stored in a given file. 10x Faster than
	 * previous method.
	 * 
	 * @param filename
	 * @return Loaded hash-map.
	 */
	public HashMap<Long, String> readHashTable(String filename) {
		File fromFile = new File(filename);
		try {
			Input in = new Input(new FileInputStream(fromFile));
			return (HashMap<Long, String>) kyro.readClassAndObject(in);
		} catch (FileNotFoundException f) {
			System.err.println("Hashtable to be read not found.");
		}
		return null;
	}

	/**
	 * Cleans a given piece of text.
	 * 
	 * @param text The text to be cleaned.
	 * @return The same text with only alphabetic characters, in lower case.
	 */
	public String cleanText(String text) {
		return text.replaceAll("[^a-zA-Z ]", "").toLowerCase();
	}

	/**
	 * Removes spaces from text.
	 * 
	 * @param text Text to have spaces removed from.
	 * @return Un-spaced text.
	 */
	public String deSpace(String text) {
		return text.replaceAll("\\s+", "");
	}

	/**
	 * Returns the tree-map of letter frequencies that was stored in a specific
	 * file.
	 * 
	 * @param filename
	 * @return Loaded map of letter frequencies.
	 */
	public TreeMap<Character, Double> readLetterFrequencies(String filename) {
		File fromFile = new File(filename);
		try {
			Input in = new Input(new FileInputStream(fromFile));
			return (TreeMap<Character, Double>) kyro.readClassAndObject(in);
		} catch (FileNotFoundException e) {
			System.err.println("Map to be read not found.");
		}
		return null;
	}

	/**
	 * Generates a tree-map of letter frequencies based on a text file.
	 * 
	 * @param filename The file name of the text file to be read.
	 */
	public void generateLetterFrequencies(String filename, String outputFilename) {
		File fromFile = new File(filename);
		File toFile = new File(outputFilename);
		Map<Character, Double> treeMap = new TreeMap<Character, Double>();
		try {
			BufferedReader br = new BufferedReader(
					new InputStreamReader(new FileInputStream(fromFile), StandardCharsets.US_ASCII));
			String line;
			while ((line = br.readLine()) != null) {
				String[] parts = line.split(",");
				treeMap.put(parts[0].charAt(0), Double.valueOf(parts[1])); // Puts each line into the map.
			}
			br.close();
			Output out = new Output(new FileOutputStream(toFile));
			kyro.writeClassAndObject(out, treeMap);
			out.close();
		} catch (FileNotFoundException f) {
			System.err.println("Text file to be hashed not found.");
		} catch (IOException i) {
			System.err.println("Buffered reader failed to read file correctly.");
		}
	}

	/**
	 * Generates and saves a TreeMap from a file containing ngrams in English and
	 * their relative appearances in Google's trillion word corpus.
	 * 
	 * @param size The number of letters to be examined for.
	 */
	public void generateNGramMap(String filename, String outputFilename, boolean characters) {
		File toFile = new File(outputFilename);
		TreeMap<String, Double> chances = new TreeMap<String, Double>();
		String[] lines = this.readFile(filename);
		double total = 0d;
		for (String line : lines) {
			String[] splitLine = line.split(",");
			chances.put(splitLine[0], Double.valueOf(splitLine[1]));
			total += Double.valueOf(splitLine[1]);
		}
		System.out.println(total);
		for (String key : chances.keySet()) {
			Double toInsert = 0d;
			if (characters) {
				toInsert = chances.get(key) / total;
			} else {
				toInsert = Math.log10(chances.get(key) / total); // For every key, the log is taken to avoid
																		// numerical underflow when operating
																		// with such small probabilities.
			}
			chances.put(key, toInsert);
		}
		try {
			Output out = new Output(new FileOutputStream(toFile));
			kyro.writeClassAndObject(out, chances);
			out.close();
		} catch (FileNotFoundException f) {
			System.err.println("Save location not found.");
		}

	}

	/**
	 * Loads a TreeMap from a file containing NGrams in English and their respective
	 * log probabilities.
	 * 
	 * @param size The number of letters to be examined for.
	 */
	public TreeMap<String, Double> loadNgramMap(String filename) {
		File location = new File(filename);
		try {
			Input in = new Input(new FileInputStream(location));
			return (TreeMap<String, Double>) kyro.readClassAndObject(in);
		} catch (FileNotFoundException e) {
			System.err.println("Treemap to be read not found.");
		}
		return null;
	}

	public TreeMap<String, LinkedList<String>> loadCharacterIndexForm() {
		// TODO create saver and loader for character index form
		return null;
	}
}
