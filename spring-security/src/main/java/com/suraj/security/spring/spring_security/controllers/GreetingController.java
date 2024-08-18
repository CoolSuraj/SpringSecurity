package com.suraj.security.spring.spring_security.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/")
public class GreetingController {
	
	@GetMapping("/hi")
	public String sayHi() {
		
		
		return "Hello World";
	}
	
	
	@GetMapping("/hiuser")
	public String sayHiuser() {
		
		
		return "Hello User!!";
	}

	@GetMapping("/hiBoss")
	public String sayHiToAdmin() {
		
		
		return "Welcome Admin!!";
	}
}
