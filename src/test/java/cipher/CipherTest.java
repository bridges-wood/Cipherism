package cipher;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;
import org.junit.runner.notification.Failure;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ CaesarTest.class, CipherBreakersTest.class, DetectEnglishTest.class, IOCTest.class,
		KasiskiExaminationTest.class, ManagerTest.class, MultiLemmaAnalysisTest.class, NGramAnalyserTest.class,
		PredictWordsTest.class, ProbableSubstitutionsTest.class, SubstitutionTest.class, FileIOTest.class,
		VigenereTest.class })
public class CipherTest {

	public static void main(String[] args) {
		Result result = JUnitCore.runClasses(CipherTest.class);

		for (Failure failure : result.getFailures()) {
			System.out.println(failure.toString());
		}

		System.out.println(result.wasSuccessful());
	}
}
