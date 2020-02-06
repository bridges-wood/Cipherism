package test.java.com.CiphersApp.cipher;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import main.java.com.CiphersApp.cipher.Manager;
import main.java.com.CiphersApp.cipher.Substitution;
import main.java.com.CiphersApp.cipher.Utilities;
import main.java.com.CiphersApp.cipher.Vigenere;

public class ManagerTest {

	private Vigenere v = new Vigenere();
	private Utilities u = new Utilities();
	private Substitution s = new Substitution();
	private final String PLAINTEXT = "The next thing I remember is, waking up with a feeling as if I had had a frightful nightmare,"
			+ " and seeing before me a terrible red glare, crossed with thick black bars. I heard voices, too,"
			+ " speaking with a hollow sound, and as if muffled by a rush of wind or water.";
	private final String KEY = "test";

	@Test
	public void testDetectCipher() {
		Manager tester = new Manager(v.encrypt(u.cleanText(PLAINTEXT), KEY), true);
		assertEquals(tester.detectCipher(tester.getText()), "Periodic");
		tester.setText(s.encrypt(PLAINTEXT, SubstitutionTest.initialiseMappings("qwertyuiopasdfghjklzxcvbnm")));
		assertEquals(tester.detectCipher(tester.getText()), "Substitution");
	}

}
