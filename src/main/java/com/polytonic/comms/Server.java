package com.polytonic.comms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.polytonic.cipher.Manager;

@RestController
@SpringBootApplication
public class Server {

	public static void main(String[] args) {
		SpringApplication.run(Server.class, args);
	}

	@GetMapping("/")
	public String decodeCipherText(@RequestParam String cipher) {
		cipher.replaceAll("%20", " ");
		System.out.println("Manager started on text: " + cipher);
		Manager m = new Manager(cipher, false, false);
		Gson g = new Gson();
		APIResponse responseObj = new APIResponse(m.getResult(), m.isFail());
		return g.toJson(responseObj);
	}
}
