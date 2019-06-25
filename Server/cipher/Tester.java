package cipher;

import java.util.Map;

public class Tester {

	Tester() {
		run();
	}

	public void run() {
		String otherText = "IAEMPLDVXEVUPWWJTTSAJCMZFSHLBKAUHEPLSCAZFMSFSEHLBTWKIIEZFLX";
				// for (String word : PredictWords.predictedWords("complexity", true)) { // Test
		// single word prediction.
		// System.out.println(word);
		// }
		// for (String word : MultiLemmaAnalysis.possibleLemmata("this is a sentence"))
		// { // Test multi word prediction.
		// System.out.println(word);
		// }
		// for (Map.Entry<String, Float> entry : NGramAnalyser.NgramAnalysis(5,
		// otherText, false).entrySet()) {
		// System.out.println(entry.getKey() + ":" + entry.getValue());
		// }
		// int[] keys = KasiskiExamination.likelyKeyLengths(NGramAnalyser.kasiskiBase(3,
		// otherText), otherText);
		// System.out.println(KasiskiExamination.keyGuesserVigenere(KasiskiExamination.mostLikelyKeyLength(otherText,
		// keys), otherText));

		// Utilities.generateObjectHashTable("2grams.txt ", "2grams.ohtb");
		// DetectEnglish b = new DetectEnglish();
		// System.out.println(b.detectEnglish(otherText));
		// System.out.println(NGramAnalyser.computeScore(3, otherText, false));
		// System.out.println(b.respace(otherText, 20));
		// System.out.println(b.chiSquaredTest(otherText));
		IOC i = new IOC();
		float[] nums = i.peroidicIndexOfCoincidence(otherText);
		for (int x = 0; x < nums.length; x++) {
			System.out.println(x + 1 + " " + nums[x]);
		}
	}

	public static void main(String[] args) {
		Tester a = new Tester();
		try {
			a.finalize();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

}
