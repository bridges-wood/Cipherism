import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class FrequencyAnalysis {
	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		String input = in.nextLine();
		in.close();
		int length = input.length();
		char[] inputArr = input.toCharArray();
		Map <Character, Integer> hits = new HashMap<Character, Integer>();
		for(char c : inputArr) {
			if(!hits.containsKey(c)) {
				hits.put(c, 1);
			} else {
				hits.put(c, hits.get(c) + 1);
			}
		}
		Set <Map.Entry<Character, Integer>> set = hits.entrySet();
		for(Map.Entry<Character, Integer> me : set) {
			System.out.println(me.getKey() + " : " + ((double) me.getValue()/ length)*100 + "%");
		}
	}
}
