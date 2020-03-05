package com.polytonic.comms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.polytonic.cipher.Manager;

@RestController
@SpringBootApplication
public class Server {

	public static void main(String[] args) {
		SpringApplication.run(Server.class, args);
	}
	
	@GetMapping("/ciphertext/{cipherText}")
	public String decodeCipherText(@PathVariable String cipherText) {
		cipherText.replaceAll("%20", " ");
		Manager m = new Manager(cipherText, false);
		Gson g = new Gson();
		APIResponse responseObj = new APIResponse(m.getResult(), m.isFail());
		return g.toJson(responseObj);
	}
}
