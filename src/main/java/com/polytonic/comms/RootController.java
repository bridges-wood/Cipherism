package com.polytonic.comms;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * End-point manager to test if the server is live.
 * 
 * @author Max Wood
 */
@Controller
public class RootController {

	@GetMapping("/")
	@ResponseBody
	public String getRoot() {
		return "Alive";
	}
}
