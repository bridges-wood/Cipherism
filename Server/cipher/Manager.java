package cipher;

/**
 * For all devices connected to the server, we will assign a Manager to it.
 */
public class Manager {

	Manager(String text) {
		run(text);
	}

	private void run(String text) {
		String cipherType = detectCipher(text);
		switch (cipherType) {
		case "Vigenere":
			break;
		case "Substitution":
			break;
		default:
			break;
		}
	}

	private String detectCipher(String text) {
		/* Set text to a char array.
		 * Analyse how many different characters there are.
		 * Use chi squared analysis to look at text.
		 * If it's not close,  look at the letter frequencies.
		 * Check index of coincidence.
		 * If this is close to english, probably a substitution.
		 * Check periodic IOC.
		 * If spikes are identified, it is a periodic cipher.
		 * Look at cipher length and identify if its a square type cipher or not.
		 */
		
		return null;
	}
}
