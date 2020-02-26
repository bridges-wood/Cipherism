package comms;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SpringController {

	//TODO read tutorial to understand how submission works.
	
	@GetMapping("/ciphers-app")
	public String submissionForm(Model model) {
		model.addAttribute("ciphertext", new Submission());
		return "decrypted";
	}
}
