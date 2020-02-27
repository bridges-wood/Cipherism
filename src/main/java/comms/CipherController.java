package comms;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import cipher.Manager;

@RestController
public class CipherController {

	@GetMapping("/ciphers-app/plaintext/{plainText}")
	public String decodeCipherText(@PathVariable String plainText) {
		plainText.replaceAll("%20", " ");
		Manager m = new Manager(plainText, false);
		return m.getResult();
	}

}
