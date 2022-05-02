package com.davidrm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
		return "index";	
	}
	
	@GetMapping("/usuarios")
	public String usuarioList(Model model){
		model.addAttribute("usuarios", usuarioSer.getAllUsuarios());
		return "usuarioList";	
	}
	
	@GetMapping("/usuario-add")
	public String addUsuarioGet(Model model) {
		model.addAttribute("usuario", new UsuarioDTO());
		return "addUsuario";
	}
	
	@PostMapping("/usuario-add")
	public String addUsuarioPost(@ModelAttribute UsuarioDTO usuDTO, Model model) {		
		String dirige = "redirect:/usuario-add";
		if (usuDTO != null) {
			Usuario usuario = new Usuario(usuDTO.getDni(), usuDTO.getEmail(), usuDTO.getNombre(),
										"ROLE_USER", usuDTO.getTelefono(), 
										new BCryptPasswordEncoder(15).encode(usuDTO.getPassword()), true);
			
			usuarioSer.insertarUsuario(usuario);
			dirige = "redirect:/usuarios";
		}
		return dirige;
	}
	
	@GetMapping("/usuario-edit")
	public String editUsuarioGet(@RequestParam(required=false,name="id") Long id, Model model) {
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
	
	@GetMapping("/vehiculos")
	public String vehiculoList(Model model){
		model.addAttribute("vehiculos", vehiculoSer.getAllVehiculos());
		return "vehiculoList";	
	}
	
	@GetMapping("/vehiculo-edit")
	public String editVehiculoGet(@RequestParam(required=false,name="id") Long id, Model model) {
		String dirige = "redirect:/vehiculos";
		if (id != null && vehiculoSer.findVehiculoById(id) != null) {
			model.addAttribute("vehiculo", new VehiculoDTO(id));
			dirige = "editVehiculo";
		}	
		return dirige;
	}
	
	@PostMapping("/vehiculo-edit")
	public String editVehiculoPost(@ModelAttribute VehiculoDTO vehiDTO, Model model) {		
		String dirige = "redirect:/vehiculo-edit";
		if (vehiDTO != null) {
			Vehiculo vehiculo = vehiculoSer.findVehiculoById(vehiDTO.getId());
			vehiculo.setEstado(vehiDTO.getEstado());
			vehiculoSer.actualizarVehiculo(vehiculo);
			dirige = "redirect:/vehiculos";
		}
		return dirige;
	}
	
	@GetMapping("/vehiculo-add")
	public String addVehiculoGet(Model model) {
		model.addAttribute("vehiculo", new VehiculoDTO());
		return "addVehiculo";
	}
	
	@PostMapping("/vehiculo-add")
	public String addVehiculoPost(@ModelAttribute VehiculoDTO vehiDTO, Model model) {		
		String dirige = "redirect:/vehiculo-add";
		if (vehiDTO != null) {
			Vehiculo vehiculo = new Vehiculo(vehiDTO.getMatricula(), vehiDTO.getModelo(),
							vehiDTO.getTipo(), vehiDTO.getConsumo(), vehiDTO.getEstado());
			
			vehiculoSer.insertarVehiculo(vehiculo);
			dirige = "redirect:/vehiculos";
		}
		return dirige;
	}
	
	@GetMapping("/vehiculo-delete")
	public String deleteVehiculoGet(@RequestParam(required=false,name="id") Long id, Model model) {
		Vehiculo vehiculo = vehiculoSer.findVehiculoById(id);
		vehiculoSer.borrarVehiculo(vehiculo);
		return "redirect:/vehiculos";
	}
	
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
		if (idVehiculo != null && alquilerSer.findAlquilerById(idVehiculo) != null) {
			AlquilerDTO alquilerDTO = new AlquilerDTO();
			alquilerDTO.setIdVehiculo(idVehiculo);
			
			model.addAttribute("alquilerDTO", alquilerDTO);
			dirige = "addAlquilerCliente";
		}
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
