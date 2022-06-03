package com.davidrm.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.davidrm.model.Usuario;
import com.davidrm.services.UsuarioService;


@Controller
public class MainController {
	
	@Autowired
	private UsuarioService usuarioSer;
	
	
	@GetMapping("/")
	public String inicio() {
		String dirige = "index";
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuario = usuarioSer.findUsuarioByEmail(auth.getName());
		
		if (usuario != null && usuario.getRole().equals("ROLE_ADMIN")) dirige = "inicioAdmin";
		
		return dirige;	
	}
	
	
	@GetMapping("/login-user")
	public String login() {
		return "login";	
	}
	
}
