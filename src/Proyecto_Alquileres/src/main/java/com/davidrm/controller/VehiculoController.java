package com.davidrm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.davidrm.dto.VehiculoDTO;
import com.davidrm.model.Vehiculo;
import com.davidrm.services.VehiculoService;

@Controller
public class VehiculoController {

	@Autowired
	private VehiculoService vehiculoSer;
	
	
	@GetMapping("/vehiculos")
	public String vehiculoList(@RequestParam(required=false, name="tipo") String tipo, Model model) {		
		if (tipo != null) 
			model.addAttribute("vehiculos", vehiculoSer.findVehiculosByTipo(tipo));		
		else 
			model.addAttribute("vehiculos", vehiculoSer.getAllVehiculos());
		
		return "vehiculoList";	
	}
	
	
	@GetMapping("/vehiculo-edit")
	public String editVehiculoGet(@RequestParam(required=false, name="id") Long id, Model model) {
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
	public String deleteVehiculoGet(@RequestParam(required=false, name="id") Long id, Model model) {
		Vehiculo vehiculo = vehiculoSer.findVehiculoById(id);
		vehiculoSer.borrarVehiculo(vehiculo);
		return "redirect:/vehiculos";
	}
}
