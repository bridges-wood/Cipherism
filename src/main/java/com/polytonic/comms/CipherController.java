package com.polytonic.comms;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import com.polytonic.cipher.Manager;

@RestController
public class CipherController {

	@GetMapping("/ciphertext/{cipherText}")
	public String decodeCipherText(@PathVariable String cipherText) {
		cipherText.replaceAll("%20", " ");
		Manager m = new Manager(cipherText, false);
		Gson g = new Gson();
		APIResponse responseObj = new APIResponse(m.getResult(), m.isFail());
		return g.toJson(responseObj);
	}

}
