package com.davidrm.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.davidrm.dto.PasswordDTO;
import com.davidrm.dto.UsuarioDTO;
import com.davidrm.model.Usuario;
import com.davidrm.services.UsuarioService;

@Controller
public class UsuarioController {

	@Autowired
	private UsuarioService usuarioSer;
	
	
	@GetMapping("/usuarios")
	public String usuarioList(Model model) {
		model.addAttribute("usuarios", usuarioSer.getAllUsuarios());
		return "usuarioList";	
	}
	
	
	@GetMapping("/usuario-add")
	public String addUsuarioGet(Model model) {
		model.addAttribute("errores", new HashMap<>());
		model.addAttribute("usuario", new UsuarioDTO());
		
		return "addUsuario";
	}
	
	
	@PostMapping("/usuario-add")
	public String addUsuarioPost(@Valid @ModelAttribute("usuDTO") UsuarioDTO usuDTO, BindingResult validacion,
			Model model) {
		
		String dirige = "redirect:/usuario-add";
		
		Map<String,List<String>> errores = new HashMap<>();		
		errores.put("nombre", new ArrayList<>());
		errores.put("telefono", new ArrayList<>());
		errores.put("email", new ArrayList<>());
		errores.put("dni", new ArrayList<>());
		errores.put("password", new ArrayList<>());
		
		boolean dniRepe = false;
		boolean emailRepe = false;
		
		if (usuDTO.getDni() != null && usuarioSer.findUsuarioByDni(usuDTO.getDni()) != null) {
			dniRepe = true;
			errores.get("dni").add("Ya existe un Usuario con ese DNI");
		}
		
		if (usuDTO.getEmail() != null && usuarioSer.findUsuarioByEmail(usuDTO.getEmail()) != null) {
			emailRepe = true;
			errores.get("email").add("Ya existe un Usuario con ese Email");
		}
		
		if (validacion.hasErrors() || dniRepe || emailRepe) {
			
			for (FieldError e : validacion.getFieldErrors()) errores.get(e.getField()).add(e.getDefaultMessage());
			
			model.addAttribute("errores", errores);		
			model.addAttribute("usuario", usuDTO);		
			dirige = "addUsuario";
			
		} else if (usuDTO != null) {
			Usuario usuario = new Usuario(usuDTO.getDni(), usuDTO.getEmail(), usuDTO.getNombre(),
					"ROLE_USER", usuDTO.getTelefono(), new BCryptPasswordEncoder(15).encode(usuDTO.getPassword()), true);
			
			usuarioSer.insertarUsuario(usuario);
			dirige = "redirect:/";
		}
		return dirige;
	}
	
	
	@GetMapping("/usuario-edit")
	public String editUsuarioGet(Model model) {
		String dirige = "redirect:/";
		Usuario usuario = null;	
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth.getName() != null) usuario = usuarioSer.findUsuarioByEmail(auth.getName());
		
		if (usuario != null ) {		
			model.addAttribute("errores", new HashMap<>());
			
			UsuarioDTO usuarioDTO = new UsuarioDTO();
			usuarioDTO.setEmail(usuario.getEmail());
			usuarioDTO.setTelefono(usuario.getTelefono());
			
			model.addAttribute("usuario", usuarioDTO);
			model.addAttribute("passwordDTO", new PasswordDTO());
			model.addAttribute("errores", new HashMap<>());		
						
			dirige = "editUsuario";
		}	
		return dirige;
	}
	
	
	@PostMapping("/usuario-edit")
	public String editUsuarioPost(@ModelAttribute UsuarioDTO usuDTO, Model model) {		
		String dirige = "redirect:/usuario-edit";
		Usuario usuario = null;
		boolean email_repe = false;
		Map<String,String> errores = new HashMap<>();
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth.getName() != null) usuario = usuarioSer.findUsuarioByEmail(auth.getName());
		
		if (usuDTO.getEmail() != null && !usuario.getEmail().equals(usuDTO.getEmail()) && 
				usuarioSer.findUsuarioByEmail(usuDTO.getEmail()) != null)  {
			
			email_repe = true;
			errores.put("email","Ya existe un Usuario con ese Email");
		}
		
		if (usuDTO != null && usuDTO.getTelefono() == null || usuDTO.getEmail() == null || email_repe) {
			
			if (usuDTO.getTelefono() == null) errores.put("telefono","Falta el telefono");
			
			if (usuDTO.getEmail() == null) errores.put("email","Falta el email");
			
			model.addAttribute("errores", errores);		
			model.addAttribute("usuario", usuDTO);
			model.addAttribute("passwordDTO", new PasswordDTO());
			
			dirige = "editUsuario";
			
		} else if(usuDTO != null && usuario != null) {
			usuario.setTelefono(usuDTO.getTelefono());
			usuario.setEmail(usuDTO.getEmail());
			
			usuarioSer.actualizarUsuario(usuario);
			dirige = "redirect:/";
		}
			
		return dirige;
	}
	
	
	@PostMapping("/usuario-edit-password")
	public String editPasswordPost(@Valid @ModelAttribute PasswordDTO passwordDTO, BindingResult validacion, Model model) {		
		String dirige = "redirect:/usuario-edit";
		Usuario usuario = null;
		
		boolean coincide = false;
		Map<String,List<String>> errores = new HashMap<>();		
		errores.put("password_nueva", new ArrayList<>());
		errores.put("password_repetida", new ArrayList<>());
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth.getName() != null) usuario = usuarioSer.findUsuarioByEmail(auth.getName());
		
		if (passwordDTO.getPassword_nueva() != null && passwordDTO.getPassword_repetida() != null) coincide = passwordDTO.coincide();
		
		if (validacion.hasErrors() || !coincide) {
			
			for (FieldError e : validacion.getFieldErrors()) errores.get(e.getField()).add(e.getDefaultMessage());
			
			if (!coincide) errores.get("password_repetida").add("La contraseña no coincide");
			
			model.addAttribute("errores", errores);		
			model.addAttribute("passwordDTO", new PasswordDTO());
			model.addAttribute("usuario", new UsuarioDTO());
			
			dirige = "editUsuario";
			
		} else if(passwordDTO != null && usuario != null && coincide) {
			
			usuario.setPassword(new BCryptPasswordEncoder(15).encode(passwordDTO.getPassword_nueva()));
			
			usuarioSer.actualizarUsuario(usuario);
			dirige = "redirect:/";
		}
			
		return dirige;
	}
	
	
	@GetMapping("/usuarios-activo")
	public String usuarioActivo(@RequestParam(required=false, name="id") Long id, Model model) {		
		Usuario usuario = null;
		
		if (id != null) usuario = usuarioSer.findUsuarioById(id);
		
		if (usuario != null) {
			usuario.setActivo(usuario.isActivo() ? false : true);
			usuarioSer.actualizarUsuario(usuario);
		}

		return "redirect:/usuarios";	
	}
}
