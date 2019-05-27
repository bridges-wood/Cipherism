import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class KasiskiExamination {

	/**
	 * Gives an array of possible key lengths for a polyalphabetically encypted
	 * text.
	 * 
	 * @param repeated A map of the repeated sequences of a given length in the
	 *                 text.
	 * @param text     The polyalphabetically encrypted text.
	 * @return
	 */
	public static int[] kasiskiExam(Map<String, Integer> repeated, String text) {
		text = text.replaceAll("[^a-zA-Z ]", "").toLowerCase();
		List<Integer> possibleKeyLengths = new ArrayList<Integer>();
		List<String> patterns = new ArrayList<String>();
		while (!repeated.isEmpty()) {
			String currentMax = maxKey(repeated);
			patterns.add(currentMax);
			repeated.remove(currentMax);
		}
		int length = patterns.get(0).length();
		for (String pattern : patterns) {
			System.out.println(pattern);
			List<Integer> startIndices = new ArrayList<Integer>();
			for (int i = 0; i < text.length() - (length - 1); i++) {
				String section = "";
				for (int j = 0; j < length; j++) {
					section += text.charAt(i + j);
				}
				if (section.equals(pattern)) {
					startIndices.add(i);
				}
			}
			Integer[] indices = startIndices.toArray(new Integer[0]);
			List<Integer> differences = new ArrayList<Integer>();
			for (int s = 1; s < indices.length; s++) {
				System.out.println("Difference: " + (indices[s] - indices[s - 1]));
				if (!isPrime((indices[s] + (length - 1)) - indices[s - 1])) {
					differences.add((indices[s] + (length - 1)) - indices[s - 1]);
				}
			}
			int[] arr = differences.stream().mapToInt(a -> a).toArray();
			if (arr.length > 0) {
				possibleKeyLengths.add(findGCD(arr, differences.size()));
			}
		}
		int[] poss = possibleKeyLengths.stream().mapToInt(a -> a).toArray(); // TODO Clean up the above code.
		System.out.println("Most likely key length: " + findGCD(poss, poss.length)); // Need to find a way to calculate
																						// gcd without a single number
																						// 'imposing it's will'.
		return null;
	}

	/**
	 * Returns the maximum string value in a <String, Integer> Map
	 * 
	 * @param map The input <String, Integer> Map.
	 * @return The key corresponding to the maximum value in the map.
	 */
	public static String maxKey(Map<String, Integer> map) {
		return map.entrySet().stream().max((Entry<String, Integer> entry1, Entry<String, Integer> entry2) -> entry1
				.getValue().compareTo(entry2.getValue())).get().getKey(); // Looks through all the K, V pairs in the
																			// map, comparing all values. One is set to
																			// the max of the two and the next value in
																			// the map is compared to the current
																			// maximum. When the final maximum value is
																			// found, the corresponding key is returned.
	}

	public static int gcd(int a, int b) {
		if (a == 0)
			return b;
		return gcd(b % a, a);
	}

	public static int findGCD(int arr[], int n) {
		int result = arr[0];
		for (int i = 1; i < n; i++)
			result = gcd(arr[i], result);

		return result;
	}

	public static boolean isPrime(int a) {
		for (int i = 2; i < Math.sqrt(a); i++) {
			if (a % i == 0) {
				return false;
			}
		}
		return true;
	}
}
