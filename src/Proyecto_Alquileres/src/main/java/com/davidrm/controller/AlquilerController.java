package com.davidrm.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.davidrm.dto.AlquilerDTO;
import com.davidrm.model.Alquiler;
import com.davidrm.model.Usuario;
import com.davidrm.model.Vehiculo;
import com.davidrm.services.AlquilerService;
import com.davidrm.services.UsuarioService;
import com.davidrm.services.VehiculoService;

@Controller
public class AlquilerController {
	
	@Autowired
	private AlquilerService alquilerSer;
	
	@Autowired
	private UsuarioService usuarioSer;
	
	@Autowired
	private VehiculoService vehiculoSer;
	
	
	@GetMapping("/alquileres")
	public String alquilerList(Model model) {
		String dirige = "alquilerClienteList";
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuario = usuarioSer.findUsuarioByEmail(auth.getName());
		
		if (usuario.getRole().equals("ROLE_ADMIN")) {		
			dirige = "alquilerList";
			model.addAttribute("alquileres", alquilerSer.getAllAlquileres());
			
		} else model.addAttribute("alquileres", usuario.getAlquileres());
				
		return dirige;	
	}
	
	
	@GetMapping("/alquiler-add")
	public String addAlquilerGet(Model model) {
		model.addAttribute("alquiler", new AlquilerDTO());
		model.addAttribute("vehiculos", vehiculoSer.getAllVehiculos());
		model.addAttribute("usuarios", usuarioSer.getAllUsuarios());
		model.addAttribute("errores", new HashMap<>());
		
		return "addAlquiler";
	}
	
	
	@PostMapping("/alquiler-add")
	public String addAlquilerPost(@Valid @ModelAttribute("alqDTO") AlquilerDTO alqDTO, BindingResult validacion, 
			Model model) {
		
		String dirige = "redirect:/alquiler-add";
		
		Map<String,List<String>> errores = new HashMap<>();		
		errores.put("idVehiculo", new ArrayList<>());
		errores.put("idCliente", new ArrayList<>());
		errores.put("fecha_inicio", new ArrayList<>());
		errores.put("fecha_fin", new ArrayList<>());
		errores.put("estado", new ArrayList<>());
		
		boolean errorFecha = false;
		
		if (alqDTO.getFecha_inicio() != null && alqDTO.getFecha_fin() != null && 
				!alqDTO.getFecha_fin().isAfter(alqDTO.getFecha_inicio())) {
			errores.get("fecha_fin").add("La fecha de fin debe ser posterior a la de inicio");
			errorFecha = true;
		}
		
		if (validacion.hasErrors() || errorFecha) {
						
			for (FieldError e : validacion.getFieldErrors()) errores.get(e.getField()).add(e.getDefaultMessage());
			
			model.addAttribute("errores", errores);
			model.addAttribute("vehiculos", vehiculoSer.getAllVehiculos());
			model.addAttribute("usuarios", usuarioSer.getAllUsuarios());
			model.addAttribute("alquiler", alqDTO);		
			dirige = "addAlquiler";
			
		} else if (alqDTO != null) {
			Vehiculo vehiculo = vehiculoSer.findVehiculoById(alqDTO.getIdVehiculo());
			Usuario cliente = usuarioSer.findUsuarioById(alqDTO.getIdCliente());
			
			Alquiler alquiler = new Alquiler(cliente, vehiculo, alqDTO.getFecha_inicio(), alqDTO.getFecha_fin(), 
					alqDTO.getPago(), alqDTO.getComentario(), alqDTO.getEstado());
				
			vehiculo.addAlquiler(alquiler);
			cliente.addAlquiler(alquiler);
			
			alquilerSer.insertarAlquiler(alquiler);
			dirige = "redirect:/alquileres";
		}
		return dirige;
	}
	
	
	@GetMapping("/alquiler-edit")
	public String editAlquilerGet(Model model,@RequestParam(required=false,name="id") Long id) {
		String dirige = "redirect:/alquileres";
		Alquiler alquiler = alquilerSer.findAlquilerById(id);
		
		if (id != null && alquiler != null) {
			AlquilerDTO alquilerDTO = new AlquilerDTO(alquiler.getId());
			
			if (alquiler.getEstado() != null) alquilerDTO.setEstado(alquiler.getEstado());
			
			if (alquiler.getFecha_inicio() != null) alquilerDTO.setFecha_inicio(alquiler.getFecha_inicio());
			
			if (alquiler.getFecha_fin() != null) alquilerDTO.setFecha_fin(alquiler.getFecha_fin());
			
			if (alquiler.getPago() != null) alquilerDTO.setPago(alquiler.getPago());
			
			if (alquiler.getComentario() != null) alquilerDTO.setComentario(alquiler.getComentario());
			
			model.addAttribute("errores", new HashMap<>());
			model.addAttribute("alquiler", alquilerDTO);
			
			dirige = "editAlquiler";
		}
		return dirige;
	}
	
	
	@PostMapping("/alquiler-edit")
	public String editAlquilerPost(@ModelAttribute AlquilerDTO alqDTO, Model model) {		
		String dirige = "redirect:/alquiler-edit";
		
		Map<String,String> errores = new HashMap<>();		
		boolean errorFecha = false;
		
		if (alqDTO.getFecha_inicio() != null && alqDTO.getFecha_fin() != null && 
				!alqDTO.getFecha_fin().isAfter(alqDTO.getFecha_inicio())) {
			errores.put("fecha_fin","La fecha de fin debe ser posterior a la de inicio");
			errorFecha = true;
		}
		
		if (alqDTO != null && alqDTO.getId() == null || alqDTO.getEstado() == null || alqDTO.getFecha_inicio() == null 
				|| errorFecha) {		
			
			if (alqDTO.getId() == null) errores.put("id","Falta el ID");
			
			if (alqDTO.getFecha_inicio() == null) errores.put("estado","Falta la fecha de inicio");
			
			if (alqDTO.getEstado() == null) errores.put("estado","Falta el estado");
			
			model.addAttribute("errores", errores);		
			model.addAttribute("alquiler", alqDTO);
			
			dirige = "editAlquiler";
			
		} else if (alqDTO != null) {
			Alquiler alquiler = alquilerSer.findAlquilerById(alqDTO.getId());
			
			alquiler.setComentario(alqDTO.getComentario());
			alquiler.setPago(alqDTO.getPago());
			alquiler.setEstado(alqDTO.getEstado());
			
			if (alqDTO.getFecha_fin() != null) alquiler.setFecha_fin(alqDTO.getFecha_fin());			
			
			alquilerSer.actualizarAlquiler(alquiler);
			dirige = "redirect:/alquileres";
		}
		return dirige;
	}
	
	
	@GetMapping("/alquiler-cliente-add")
	public String addAlquilerClienteGet(Model model,@RequestParam(required=false,name="id") Long idVehiculo) {
		String dirige = "redirect:/vehiculos";	
		Vehiculo vehiculo = vehiculoSer.findVehiculoById(idVehiculo);

		
		if (idVehiculo != null && vehiculo != null) {
			AlquilerDTO alquilerDTO = new AlquilerDTO();
			alquilerDTO.setIdVehiculo(vehiculo.getId());
			
			model.addAttribute("modelvehiculo", vehiculo.getModelo());	
			model.addAttribute("errores", new HashMap<>());
			model.addAttribute("alquiler", alquilerDTO);
			dirige = "addAlquilerCliente";
		} else 
			dirige = "redirect:/vehiculos";
		
		return dirige;
	}
	
	
	@PostMapping("/alquiler-cliente-add")
	public String addAlquilerClientePost(@ModelAttribute("alqDTO") AlquilerDTO alqDTO, Model model) {		
		String dirige = "redirect:/alquiler-cliente-add";
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuario = usuarioSer.findUsuarioByEmail(auth.getName());
		
		if (alqDTO != null) {
			alqDTO.setEstado("Reserva");
			alqDTO.setIdCliente(usuario.getId());
		}
				
		Map<String,List<String>> errores = new HashMap<>();		
		errores.put("fecha_inicio", new ArrayList<>());
		errores.put("fecha_fin", new ArrayList<>());
		
		boolean errorFecha = false;
		LocalDate actual = LocalDate.now();
		
		if (alqDTO.getFecha_inicio() != null && alqDTO.getFecha_fin() != null && 
				!alqDTO.getFecha_fin().isAfter(alqDTO.getFecha_inicio())) {
			errores.get("fecha_fin").add("La fecha de fin debe ser posterior a la de inicio");
			errorFecha = true;
		}
		
		if (alqDTO.getFecha_fin() != null && !alqDTO.getFecha_fin().isAfter(actual)) {
			errores.get("fecha_fin").add("La fecha de fin debe ser posterior a la actual");
			errorFecha = true;
		}
		
		if (alqDTO.getFecha_inicio() != null && alqDTO.getFecha_inicio().isBefore(actual)) {
			errores.get("fecha_inicio").add("La fecha de inicio no puede ser anterior a la actual");
			errorFecha = true;
		}
		
		if (alqDTO.getFecha_inicio() == null || alqDTO.getFecha_fin() == null || alqDTO.getIdVehiculo() == null || errorFecha) {
			
			if (alqDTO.getFecha_inicio() == null) errores.get("fecha_inicio").add("El campo fecha de inicio no puede ser nulo");
			
			if (alqDTO.getFecha_fin() == null) errores.get("fecha_fin").add("El campo fecha de fin no puede ser nulo");
			
			if (alqDTO.getIdVehiculo() != null) {
				model.addAttribute("errores", errores);
				model.addAttribute("vehiculos", vehiculoSer.getAllVehiculos());
				model.addAttribute("usuarios", usuarioSer.getAllUsuarios());
				model.addAttribute("alquiler", alqDTO);
				
				dirige = "addAlquilerCliente";
				
			} else dirige = "redirect:/vehiculos";
					
		} else if (alqDTO != null) {
			Vehiculo vehiculo = vehiculoSer.findVehiculoById(alqDTO.getIdVehiculo());
			Usuario cliente = usuarioSer.findUsuarioById(alqDTO.getIdCliente());
			
			Alquiler alquiler = new Alquiler(cliente, vehiculo, alqDTO.getFecha_inicio(),alqDTO.getFecha_fin(),
					alqDTO.getEstado());
				
			vehiculo.addAlquiler(alquiler);
			cliente.addAlquiler(alquiler);
			
			alquilerSer.insertarAlquiler(alquiler);
			dirige = "redirect:/alquileres";
		}
		return dirige;
	}

}
