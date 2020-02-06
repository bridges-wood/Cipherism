package cipher;

import static org.junit.Assert.*;

import org.junit.Test;

public class CaesarTest {

	private Caesar tester = new Caesar();
	private final String ENCRYPTED = "aolxbpjriyvdumveqbtwzvclyaolshgfkvn"; // "The quick brown fox jumps over the lazy dog" ROT 7.
	private final int SHIFT = 19;

	@Test
	public void testCaesarShiftDecrypt() {
		assertEquals("the quick brown fox jumps over the lazy dog".replaceAll(" ", ""),
				tester.CaesarShiftDecrypt(ENCRYPTED, SHIFT));
	}

}
