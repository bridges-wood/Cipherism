package cipher;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public class Utilities {

	private long FNV1_64_INIT;
	private long FNV1_PRIME_64;
	private Kryo kyro = new Kryo();

	public Utilities() {
		FNV1_64_INIT = 0xcbf29ce484222325L;
		FNV1_PRIME_64 = 1099511628211L;
		Hashtable<Long, String> init = new Hashtable<Long, String>();
		kyro.register(init.getClass());
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
				words.add(line);
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return words.parallelStream().toArray(String[]::new);
	}

	/**
	 * Gives the 64 bit FNV1a hash of an input string.
	 *
	 * @param text The text from which the hash is to be generated.
	 * @return The hash of the input data.
	 */
	public long hash64(String text) {
		byte[] data = text.getBytes();
		int length = data.length;
		long hash = FNV1_64_INIT;
		for (int i = 0; i < length; i++) { // For each byte, the initial prime is raised to the power of a masked int
											// version of the byte a the index before being multiplied by the main
											// prime.
			hash ^= (data[i] & 0xff);
			hash *= FNV1_PRIME_64;
		}

		return hash;
	}

	/**
	 * Creates a hashtable using fnv1 and each line of a given file of type <Long,
	 * String>.
	 * 
	 * @param filename       The name of the file which lines are to be read from.
	 * @param outputFilename The name of the file to which the hashtable is saved.
	 */
	public void generateHashTable(String filename, String outputFilename) {
		File fromFile = new File(filename);
		File toFile = new File(outputFilename);
		Hashtable<Long, String> hashTable = new Hashtable<Long, String>();
		try {
			Scanner sc = new Scanner(fromFile);
			while (sc.hasNextLine()) {
				String line = sc.nextLine();
				hashTable.put(hash64(line), line); // Puts each line into the hashtable.
			}
			sc.close();
			Output out = new Output(new FileOutputStream(toFile));
			kyro.writeClassAndObject(out, hashTable);
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Returns the hash-table that was stored in a given file. 10x Faster than
	 * previous method.
	 * 
	 * @param filename
	 * @return Loaded hash-table.
	 */
	public Hashtable<Long, String> readHashTable(String filename) {
		File fromFile = new File(filename);
		try {
			Input in = new Input(new FileInputStream(fromFile));
			return (Hashtable<Long, String>) kyro.readClassAndObject(in);
		} catch (IOException e) {
			e.printStackTrace();
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
}
