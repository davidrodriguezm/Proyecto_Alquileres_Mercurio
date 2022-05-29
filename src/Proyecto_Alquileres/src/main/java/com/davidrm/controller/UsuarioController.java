package com.davidrm.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
			dirige = "redirect:/usuarios";
		}
		return dirige;
	}
	
	
	@GetMapping("/usuario-edit")
	public String editUsuarioGet(@RequestParam(required=false, name="id") Long id, Model model) {
		String dirige = "redirect:/usuarios";	
		if (id != null && usuarioSer.findUsuarioById(id) != null) {
			model.addAttribute("usuario", new UsuarioDTO(id));
			dirige = "editUsuario";
		}	
		return dirige;
	}
	
	
	@PostMapping("/usuario-edit")
	public String editUsuarioPost(@ModelAttribute UsuarioDTO usuDTO, Model model) {		
		String dirige = "redirect:/usuario-edit";
		
		if (usuDTO != null) {
			Usuario usuario = usuarioSer.findUsuarioById(usuDTO.getId());
			usuario.setPassword(new BCryptPasswordEncoder(15).encode(usuDTO.getPassword()));
			usuario.setTelefono(usuDTO.getTelefono());
			
			usuarioSer.actualizarUsuario(usuario);
			dirige = "redirect:/usuarios";
		}
		return dirige;
	}
}
