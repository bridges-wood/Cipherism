import java.util.TreeMap;

public class ProbableSubstitutions {
	public char[] frequencyOrder = "ETAOINSRHDLUCMFYWGPBVKXQJZ".toLowerCase().toCharArray();
	public char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toLowerCase().toCharArray();
	public class Mapping {
		boolean isDefinite;
		char fromChar;
		char toChar;
	}
	public static Mapping[] probableSubstitutionGenerator(TreeMap<String, Float> letterFrequencies){
		//TODO get the letters from the treemap in order of probability to map to the letters in frequecny order.
		return null;
	}
}
