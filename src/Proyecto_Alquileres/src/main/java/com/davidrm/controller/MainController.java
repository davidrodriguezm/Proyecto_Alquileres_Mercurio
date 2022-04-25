package com.davidrm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.davidrm.dto.VehiculoDTO;
import com.davidrm.dto.UsuarioDTO;
import com.davidrm.dto.AlquilerDTO;
import com.davidrm.model.Vehiculo;
import com.davidrm.model.Usuario;
import com.davidrm.model.Alquiler;
import com.davidrm.services.VehiculoService;
import com.davidrm.services.UsuarioService;
import com.davidrm.services.AlquilerService;


@Controller
public class MainController {
	
	@Autowired
	private UsuarioService usuarioSer;
	
	@Autowired
	private VehiculoService vehiculoSer;
	
	@Autowired
	private AlquilerService alquilerSer;
	
	@GetMapping("/")
	public String inicio(){
		return "inicio";	
	}
	
	@GetMapping("/usuarios")
	public String usuarioList(Model model){
		model.addAttribute("usuarios", usuarioSer.getAllUsuarios());
		return "usuarioList";	
	}
	
	@GetMapping("/addUsuario")
	public String addUsuarioGet(Model model) {
		model.addAttribute("usuario", new UsuarioDTO());
		return "addUsuario";
	}
	
	@PostMapping("/addUsuario")
	public String addUsuarioPost(@ModelAttribute UsuarioDTO usuDTO, Model model) {		
		String dirige = "redirect:/addusuario";
		if (usuDTO != null) {
			Usuario usuario = new Usuario(usuDTO.getDni(), usuDTO.getEmail(), usuDTO.getNombre(),
										"USER", usuDTO.getTelefono(), usuDTO.getPassword());
			
			usuarioSer.insertarUsuario(usuario);
			dirige = "redirect:/usuarios";
		}
		return dirige;
	}
	
	@GetMapping("/vehiculos")
	public String vehiculoList(Model model){
		model.addAttribute("vehiculos", vehiculoSer.getAllVehiculos());
		return "vehiculoList";	
	}
	
	@GetMapping("/editVehiculo")
	public String editVehiculoGet(@RequestParam(required=false,name="id") Long id, Model model) {
		String dirige = "redirect:/vehiculos";
		if (id != null && vehiculoSer.findVehiculoById(id) != null) {
			model.addAttribute("id", id);
			model.addAttribute("vehiculo", new VehiculoDTO());
			dirige = "editVehiculo";
		}
		
		return dirige;
	}
	
	@PostMapping("/editVehiculo")
	public String editVehiculoPost(@ModelAttribute VehiculoDTO vehiDTO, Model model) {		
		String dirige = "redirect:/editVehiculo";
		if (vehiDTO != null) {
			Vehiculo vehiculo = vehiculoSer.findVehiculoById(vehiDTO.getId());
			vehiculo.setEstado(vehiDTO.getEstado());
			vehiculoSer.actualizarVehiculo(vehiculo);
			dirige = "redirect:/vehiculos";
		}
		return dirige;
	}
	
	@GetMapping("/addVehiculo")
	public String addVehiculoGet(Model model) {
		model.addAttribute("vehiculo", new VehiculoDTO());
		return "addVehiculo";
	}
	
	@PostMapping("/addVehiculo")
	public String addVehiculoPost(@ModelAttribute VehiculoDTO vehiDTO, Model model) {		
		String dirige = "redirect:/addVehiculo";
		if (vehiDTO != null) {
			Vehiculo vehiculo = new Vehiculo(vehiDTO.getMatricula(), vehiDTO.getModelo(),
							vehiDTO.getTipo(), vehiDTO.getConsumo(), vehiDTO.getEstado());
			
			vehiculoSer.insertarVehiculo(vehiculo);
			dirige = "redirect:/vehiculos";
		}
		return dirige;
	}
	
	@GetMapping("/deleteVehiculo")
	public String deleteVehiculoGet(@RequestParam(required=false,name="id") Long id, Model model) {
		Vehiculo vehiculo = vehiculoSer.findVehiculoById(id);
		vehiculoSer.borrarVehiculo(vehiculo);
		return "redirect:/vehiculos";
	}
	
	@GetMapping("/alquileres")
	public String alquilerList(Model model){
		model.addAttribute("alquileres", alquilerSer.getAlquilersIncomplete());
		return "alquilerList";	
	}
	
	@GetMapping("/addAlquiler")
	public String addAlquilerGet(Model model) {
		model.addAttribute("alquiler", new AlquilerDTO());
		model.addAttribute("vehiculos", vehiculoSer.getAllVehiculos());
		model.addAttribute("usuarios", usuarioSer.getAllUsuarios());
		return "addAlquiler";
	}
	
	@PostMapping("/addAlquiler")
	public String addAlquilerPost(@ModelAttribute AlquilerDTO alqDTO, Model model) {		
		String dirige = "redirect:/addAlquiler";
		if (alqDTO != null) {
			Vehiculo vehiculo = vehiculoSer.findVehiculoById(alqDTO.getIdVehiculo());
			Usuario cliente = usuarioSer.findUsuarioById(alqDTO.getIdCliente());
			
			Alquiler alquiler = new Alquiler(cliente, vehiculo, alqDTO.getFecha_inicio(),
								alqDTO.getFecha_fin(), alqDTO.getPago(), alqDTO.getComentario());
				
			vehiculo.addAlquiler(alquiler);
			cliente.addAlquiler(alquiler);
			
			alquilerSer.insertarAlquiler(alquiler);
			dirige = "redirect:/alquileres";
		}
		return dirige;
	}
	
	@GetMapping("/editAlquiler")
	public String editAlquilerGet(Model model,@RequestParam(required=false,name="id") Long id) {
		String dirige = "redirect:/alquileres";
		if (id != null && alquilerSer.findAlquilerById(id) != null) {
			model.addAttribute("alquilerDTO", new AlquilerDTO(id));
			dirige = "editAlquiler";
		}
		return dirige;
	}
	
	@PostMapping("/editAlquiler")
	public String editAlquilerPost(@ModelAttribute AlquilerDTO alqDTO, Model model) {		
		String dirige = "redirect:/editAlquiler";
		if (alqDTO != null) {			
			Alquiler alquiler = alquilerSer.findAlquilerById(alqDTO.getId());
			
			alquiler.setComentario(alqDTO.getComentario());
			alquiler.setPago(alqDTO.getPago());
			
			if (alqDTO.getFecha_fin() != null) alquiler.setFecha_fin(alqDTO.getFecha_fin());			
			
			alquilerSer.actualizarAlquiler(alquiler);
			dirige = "redirect:/alquileres";
		}
		return dirige;
	}
	
	@GetMapping("/addAlquilerCliente")
	public String addAlquilerClienteGet(Model model,@RequestParam(required=false,name="id") Long idVehiculo) {
		String dirige = "redirect:/alquileres";
		if (idVehiculo != null && alquilerSer.findAlquilerById(idVehiculo) != null) {
			model.addAttribute("alquilerDTO", new AlquilerDTO(idVehiculo));
			dirige = "addAlquilerCliente";
		}
		return dirige;
	}
	
	@PostMapping("/addAlquilerCliente")
	public String addAlquilerClientePost(@ModelAttribute AlquilerDTO alqDTO, Model model) {		
		String dirige = "redirect:/addAlquilerCliente";
		if (alqDTO != null) {
			Vehiculo vehiculo = vehiculoSer.findVehiculoById(alqDTO.getIdVehiculo());
			Usuario cliente = usuarioSer.findUsuarioById(alqDTO.getIdCliente());
			
			Alquiler alquiler = new Alquiler(cliente, vehiculo, alqDTO.getFecha_inicio(),
								alqDTO.getFecha_fin(), alqDTO.getPago(), alqDTO.getComentario());
				
			vehiculo.addAlquiler(alquiler);
			cliente.addAlquiler(alquiler);
			
			alquilerSer.insertarAlquiler(alquiler);
			dirige = "redirect:/alquileres";
		}
		return dirige;
	}
}
