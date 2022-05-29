package com.davidrm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
	public String alquilerList(Model model){
		model.addAttribute("alquileres", alquilerSer.getAllAlquileres());
		return "alquilerList";	
	}
	
	
	@GetMapping("/alquiler-add")
	public String addAlquilerGet(Model model) {
		model.addAttribute("alquiler", new AlquilerDTO());
		model.addAttribute("vehiculos", vehiculoSer.getAllVehiculos());
		model.addAttribute("usuarios", usuarioSer.getAllUsuarios());
		return "addAlquiler";
	}
	
	
	@PostMapping("/alquiler-add")
	public String addAlquilerPost(@ModelAttribute AlquilerDTO alqDTO, Model model) {		
		String dirige = "redirect:/alquiler-add";
		
		if (alqDTO != null) {
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
		
		if (id != null && alquilerSer.findAlquilerById(id) != null) {
			model.addAttribute("alquilerDTO", new AlquilerDTO(id));
			dirige = "editAlquiler";
		}
		return dirige;
	}
	
	
	@PostMapping("/alquiler-edit")
	public String editAlquilerPost(@ModelAttribute AlquilerDTO alqDTO, Model model) {		
		String dirige = "redirect:/alquiler-editr";
		
		if (alqDTO != null) {			
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
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuario = usuarioSer.findUsuarioByEmail(auth.getName());
		
		if (usuario != null) {
			if (idVehiculo != null && vehiculoSer.findVehiculoById(idVehiculo) != null) {
				AlquilerDTO alquilerDTO = new AlquilerDTO();
				alquilerDTO.setIdVehiculo(idVehiculo);
				alquilerDTO.setIdCliente(usuario.getId());
				
				model.addAttribute("alquilerDTO", alquilerDTO);
				dirige = "addAlquilerCliente";
			}
		} else dirige = "redirect:/";
		
		return dirige;
	}
	
	
	@PostMapping("/alquiler-cliente-add")
	public String addAlquilerClientePost(@ModelAttribute AlquilerDTO alqDTO, Model model) {		
		String dirige = "redirect:/alquiler-cliente-add";
		
		if (alqDTO != null) {
			Vehiculo vehiculo = vehiculoSer.findVehiculoById(alqDTO.getIdVehiculo());
			Usuario cliente = usuarioSer.findUsuarioById(alqDTO.getIdCliente());
			
			Alquiler alquiler = new Alquiler(cliente, vehiculo, alqDTO.getFecha_inicio(),alqDTO.getFecha_fin(),
					"Reserva");
				
			vehiculo.addAlquiler(alquiler);
			cliente.addAlquiler(alquiler);
			
			alquilerSer.insertarAlquiler(alquiler);
			dirige = "redirect:/alquileres";
		}
		return dirige;
	}

}
