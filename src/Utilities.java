import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Scanner;

public class Utilities {

	/**
	 * Returns all lines in a text file as separate words in a string array.
	 * 
	 * @param filename
	 *            The name of the file to be retrieved.
	 * @return A string array of each line in the file.
	 */
	public static String[] readFile(String filename) {
		List<String> words = new ArrayList<String>();
		File file = new File(filename);
		Scanner sc = null;
		try {
			sc = new Scanner(file);
		} catch (FileNotFoundException e) {
			System.err.println(e.toString());
		}
		while (sc.hasNextLine()) { // Ensures that the scanner can still access the next set of data.
			words.add(sc.nextLine());
		}
		sc.close();
		return words.toArray(new String[0]);
	}

	private static final long FNV1_64_INIT = 0xcbf29ce484222325L;
	private static final long FNV1_PRIME_64 = 1099511628211L;

	/**
	 * Gives the 64 bit FNV1a hash of an input string.
	 *
	 * @param text
	 *            The text from which the hash is to be generated.
	 * @return The hash of the input data.
	 */
	public static long hash64(String text) {
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
	 * @param filename
	 *            The name of the file which lines are to be read from.
	 * @param outputFilename
	 *            The name of the file to which the hashtable is saved.
	 */
	public static void generateHashTable(String filename, String outputFilename) {
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
			FileOutputStream outputStream = new FileOutputStream(toFile);
			ObjectOutputStream out = new ObjectOutputStream(outputStream);
			out.writeObject(hashTable);
			out.close(); // Writes the dictionary to the file.
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Creates a hashtable using fnv1 and each line of a given file of type <Long,
	 * GroupProbabilityPair>.
	 * 
	 * @param filename
	 *            The name of the file which lines are to be read from.
	 * @param outputFilename
	 *            The name of the file to which the hashtable is saved.
	 */
	public static void generateObjectHashTable(String filename, String outputFilename) {
		File fromFile = new File(filename);
		File toFile = new File(outputFilename);
		Hashtable<Long, GroupProbabilityPair> hashTable = new Hashtable<Long, GroupProbabilityPair>();
		try {
			Scanner sc = new Scanner(fromFile);
			int counter = 1;
			while (sc.hasNextLine()) {
				String line = sc.nextLine();
				System.out.println(line);
				hashTable.put(hash64(line), new GroupProbabilityPair(counter, line)); // Puts each line into the hashtable.
				counter++;
			}
			sc.close();
			FileOutputStream outputStream = new FileOutputStream(toFile);
			ObjectOutputStream out = new ObjectOutputStream(outputStream);
			out.writeObject(hashTable);
			out.close(); // Writes the dictionary to the file.
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Returns the hashtable that was stored in a given file.
	 * 
	 * @param filename
	 * @return
	 */
	public static Hashtable<?, ?> readHashTable(String filename) {
		File fromFile = new File(filename);
		try {
			FileInputStream fileIn = new FileInputStream(fromFile);
			ObjectInputStream objectIn = new ObjectInputStream(fileIn);
			Object obj = objectIn.readObject();
			objectIn.close();
			if (Hashtable.class.isInstance(obj)) {
				return (Hashtable<?, ?>) obj;
			} else {
				return null;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

}
