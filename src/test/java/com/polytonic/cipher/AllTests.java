package com.polytonic.cipher;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ CaesarTest.class, CipherBreakersTest.class, DetectEnglishTest.class, FileIOTest.class, IOCTest.class,
		IsEnglishStringTest.class, KasiskiBaseTest.class, KasiskiExaminationTest.class, ManagerTest.class,
		MultiLemmaAnalysisTest.class, NGramAnalyserTest.class, PhraseEncodingTest.class, PredictWordsTest.class,
		ProbableSubstitutionsExceptionTest.class, ProbableSubstitutionsTest.class, SubstitutionExceptionTest.class,
		SubstitutionTest.class, SubstitutionTreeSearchTest.class, VigenereExceptionTest.class, VigenereTest.class,
		WordEncodingTest.class })
public class AllTests {

}
