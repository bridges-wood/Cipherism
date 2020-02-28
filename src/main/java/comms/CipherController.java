package comms;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import cipher.Manager;

@RestController
public class CipherController {

	@GetMapping("/ciphers-app/ciphertext/{cipherText}")
	public String decodeCipherText(@PathVariable String cipherText) {
		cipherText.replaceAll("%20", " ");
		Manager m = new Manager(cipherText, false);
		return m.getResult();
	}

}
