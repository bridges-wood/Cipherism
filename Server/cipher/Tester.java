package cipher;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Tester {

	Tester() {
		run();
	}

	public void run() {
		String otherText = "thisisverysimilartoenglishtext";
		// String otherOtherText =
		// "King’sisaveryspecialplace,themomentyouwalkthroughthedoorsyoucansensethewarmth,purposeandenergyofallthepupilsandstaff.Thisisaschoolwhichvaluespositiverelationships,opportunityandgreatlearningaboveallelse,therebyallowingourpupilstomaketheverymostofthemselvesataschooltheylove.";
		// for (String word : PredictWords.predictedWords("complexity", true)) { // Test
		// single word prediction.
		// System.out.println(word);
		// }
		// for (String word : MultiLemmaAnalysis.possibleLemmata("this is a sentence"))
		// { // Test multi word prediction.
		// System.out.println(word);
		// }
		// for (Map.Entry<String, Integer> entry : NGramAnalyser.kasiskiBase(3,
		// otherText).entrySet()) {
		// System.out.println(entry.getKey() + ":" + entry.getValue());
		// }
		// int[] keys = KasiskiExamination.likelyKeyLengths(NGramAnalyser.kasiskiBase(3,
		// otherText), otherText);
		// System.out.println(KasiskiExamination.keyGuesserVigenere(KasiskiExamination.mostLikelyKeyLength(otherText,
		// keys), otherText));
		// System.out.println(DetectEnglish.detectEnglish(otherText));
		// Utilities.generateObjectHashTable("2grams.txt ", "2grams.ohtb");
		// System.out.println(Utilities.readHashTable("hashed_dictionary.htb").keySet().contains(Utilities.hash64("ghgashdfj")));
		DetectEnglish b = new DetectEnglish();
		// System.out.println(a.respace(otherOtherText));
		// System.out.println(NGramAnalyser.computeScore(3, otherText, false));
		System.out.println(b.viterbiRespace(otherText,20));
	}

	public static void main(String[] args) {
		Tester a = new Tester();
		try {
			a.finalize();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
