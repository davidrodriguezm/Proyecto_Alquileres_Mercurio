package com.davidrm.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class MainController {
	
	
	@GetMapping("/")
	public String inicio() {
		return "index";	
	}
	
	
	@GetMapping("/login-user")
	public String login() {
		return "login";	
	}
	
}
