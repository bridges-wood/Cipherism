import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Utilities {

	/**
	 * Returns all lines in a text file as separate words in a string array.
	 * 
	 * @param filename The name of the file to be retrieved.
	 * @return A string array of each line in the file.
	 */
	public static String[] readFile(String filename){
		List<String> words = new ArrayList<String>();
		File file = new File(filename);
		Scanner sc = null;
		try {
			sc = new Scanner(file);
		} catch (FileNotFoundException e) {
			System.err.println(e.toString());
		}
		while (sc.hasNextLine()) { //Ensures that the scanner can still access the next set of data. 
			words.add(sc.nextLine());
		}
		sc.close();
		return words.toArray(new String[0]);
	}
}
