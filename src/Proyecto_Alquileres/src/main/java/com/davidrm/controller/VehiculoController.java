package com.davidrm.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
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
			model.addAttribute("vehiculos", vehiculoSer.findByTipo(tipo));		
		else 
			model.addAttribute("vehiculos", vehiculoSer.getAllVehiculos());
		
		return "vehiculoList";	
	}
	
	
	@GetMapping("/vehiculo-add")
	public String addVehiculoGet(Model model) {
		model.addAttribute("errores", new HashMap<>());
		model.addAttribute("vehiculo", new VehiculoDTO());
		
		return "addVehiculo";
	}
	
	
	@PostMapping("/vehiculo-add")
	public String addVehiculoPost(@Valid @ModelAttribute("vehiDTO") VehiculoDTO vehiDTO, BindingResult validacion,
			Model model) {
		
		String dirige = "redirect:/vehiculo-add";
		
		Map<String,List<String>> errores = new HashMap<>();		
		errores.put("matricula", new ArrayList<>());
		errores.put("modelo", new ArrayList<>());
		errores.put("tipo", new ArrayList<>());
		errores.put("consumo", new ArrayList<>());
		errores.put("estado", new ArrayList<>());
		
		boolean matriculaRepe = false;
		
		if (vehiDTO.getMatricula() != null && vehiculoSer.findByMatricula(vehiDTO.getMatricula()) != null) {
			matriculaRepe = true;
			errores.get("matricula").add("Ya existe un Vehículo con esa matricula");
		}
		
		if (validacion.hasErrors() || matriculaRepe) {
			
			for (FieldError e : validacion.getFieldErrors()) errores.get(e.getField()).add(e.getDefaultMessage());
			
			model.addAttribute("errores", errores);
			model.addAttribute("vehiculo", vehiDTO);		
			dirige = "addVehiculo";
			
		} else if (vehiDTO != null) {
			if (vehiDTO.getTipo().equalsIgnoreCase("MiniCamion")) vehiDTO.setTipo("Mini Camión");
				
			Vehiculo vehiculo = new Vehiculo(vehiDTO.getMatricula(), vehiDTO.getModelo(),
							vehiDTO.getTipo(), vehiDTO.getConsumo(), vehiDTO.getEstado());
			
			vehiculoSer.insertarVehiculo(vehiculo);
			dirige = "redirect:/vehiculos";
		}
		return dirige;
	}
	
	
	@GetMapping("/vehiculo-edit")
	public String editVehiculoGet(@RequestParam(required=false, name="id") Long id, Model model) {
		String dirige = "redirect:/vehiculos";
		
		if (id != null && vehiculoSer.findVehiculoById(id) != null) {			
			model.addAttribute("errores", new HashMap<>());
			model.addAttribute("vehiculo", new VehiculoDTO(id));
			
			dirige = "editVehiculo";
		}	
		return dirige;
	}
	
	
	@PostMapping("/vehiculo-edit")
	public String editVehiculoPost(@ModelAttribute VehiculoDTO vehiDTO, Model model) {		
		String dirige = "redirect:/vehiculo-edit";		
		
		if (vehiDTO != null && vehiDTO.getId() == null || vehiDTO.getEstado() == null) {
			Map<String,String> errores = new HashMap<>();
			
			if (vehiDTO.getId() == null) errores.put("id","Falta el ID");
			
			if (vehiDTO.getEstado() == null) errores.put("estado","Falta el estado");
			
			model.addAttribute("errores", errores);		
			model.addAttribute("vehiculo", vehiDTO);
			
			dirige = "editVehiculo";
			
		} else if(vehiDTO != null) {
			Vehiculo vehiculo = vehiculoSer.findVehiculoById(vehiDTO.getId());
			
			vehiculo.setEstado(vehiDTO.getEstado());
			vehiculoSer.actualizarVehiculo(vehiculo);
			
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
