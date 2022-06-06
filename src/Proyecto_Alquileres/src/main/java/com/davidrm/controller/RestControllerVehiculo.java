package com.davidrm.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.davidrm.dto.VehiculoDTO;
import com.davidrm.model.Vehiculo;
import com.davidrm.services.VehiculoService;

@CrossOrigin(origins={"http://localhost:4200/"})
@RestController
@RequestMapping("/rest")
public class RestControllerVehiculo {
	
	@Autowired
	private VehiculoService vehiculoSer;
	
	@GetMapping("/estado-vehiculos")
	public ResponseEntity<?> estadoVehiculo(Model model){			
		Map<String, Integer> respuesta = new HashMap<>();
		Integer disponible = 0;
		Integer alquilado = 0;
		Integer revision = 0;
		Integer averiado = 0;
		
		List<Vehiculo> vehiculos = vehiculoSer.getAllVehiculos();
		
		for ( Vehiculo v: vehiculos) {
			if (v.getEstado().equalsIgnoreCase("Disponible")) disponible++;
			
			if (v.getEstado().equalsIgnoreCase("Alquilado")) alquilado++;
			
			if (v.getEstado().equalsIgnoreCase("Revisión")) revision++;
			
			if (v.getEstado().equalsIgnoreCase("Averiado")) averiado++;
		}
		
		respuesta.put("disponible", disponible);
		respuesta.put("alquilado", alquilado);
		respuesta.put("revision", revision);
		respuesta.put("averiado", averiado);
				
		return new ResponseEntity<Object>(respuesta, HttpStatus.OK);	
	}
	
	@GetMapping("/tipos-vehiculo")
	public ResponseEntity<?> tiposVehiculo(Model model){			
		Map<String, Integer> respuesta = new HashMap<>();
		Integer mono = 0;
		Integer furgo = 0;
		Integer mini = 0;
		
		List<Vehiculo> vehiculos = vehiculoSer.getAllVehiculos();
		
		for ( Vehiculo v: vehiculos) {
			if (v.getTipo().equalsIgnoreCase("monovolumen")) mono++;
			
			if (v.getTipo().equalsIgnoreCase("Furgoneta")) furgo++;
			
			if (v.getTipo().equalsIgnoreCase("Mini Camión")) mini++;
		}
		
		respuesta.put("miniCamion", mini);
		respuesta.put("furgoneta", furgo);
		respuesta.put("monovolumen", mono);
				
		return new ResponseEntity<Object>(respuesta, HttpStatus.OK);	
	}

	@GetMapping("/vehiculos")
	public ResponseEntity<?> vehiculoList(Model model){
		List<Vehiculo> vehiculos = vehiculoSer.getAllVehiculos();
		List<VehiculoDTO> vreturn = new ArrayList<>();
				
		vehiculos.forEach((Vehiculo v) -> {
			VehiculoDTO vc = new VehiculoDTO(v.getId(),v.getMatricula(),v.getModelo(),v.getTipo(),v.getConsumo(),v.getEstado());
			vreturn.add(vc);
		});
		
		return new ResponseEntity<Object>(vreturn, HttpStatus.OK);	
	}
	
	@GetMapping("/vehiculo/{id}")
	public ResponseEntity<?> getVehiculo(@PathVariable Long id) {	
		Vehiculo vehiculo = null;
		Map<String, Object> response = new HashMap<>();
		
		try {
			vehiculo = vehiculoSer.findVehiculoById(id);
		} catch(DataAccessException e) {			
			return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		if(vehiculo == null) {
			return new ResponseEntity(response, HttpStatus.NOT_FOUND);
		}
		
		VehiculoDTO vreturn = new VehiculoDTO(vehiculo.getId(),vehiculo.getMatricula(),vehiculo.getModelo(),
				vehiculo.getTipo(),vehiculo.getConsumo(),vehiculo.getEstado());
		
		return new ResponseEntity(vreturn, HttpStatus.OK);
	}
	
	@PostMapping("/vehiculo-add")
	public ResponseEntity<?> createVehiculo(@RequestBody VehiculoDTO vehiculo) {
		Map<String, Object> response = new HashMap<>();
		
		if(vehiculo == null) {
			return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		VehiculoDTO vreturn = null;
		
		
		try {
			Vehiculo vNew = vehiculoSer.insertarVehiculo( new Vehiculo( vehiculo.getMatricula(), vehiculo.getModelo(),
					vehiculo.getTipo(),vehiculo.getConsumo(),vehiculo.getEstado()));
			
			vreturn = new VehiculoDTO(vNew.getId(), vNew.getMatricula(), vNew.getModelo(), vNew.getTipo(), 
					vNew.getConsumo(), vNew.getEstado());
		} catch(DataAccessException e) {
			
			return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity(response, HttpStatus.CREATED);
	}
	
	@PutMapping("/vehiculo-edit/{id}")
	public ResponseEntity<?> updateVehiculo(@RequestBody VehiculoDTO vehiculo, @PathVariable Long id) {
		Vehiculo vehiculoActual = vehiculoSer.findVehiculoById(id);

		VehiculoDTO vreturn = null;
		Map<String, Object> response = new HashMap<>();
		
		if (vehiculoActual == null) {
			return new ResponseEntity(response, HttpStatus.NOT_FOUND);
		}

		try {
			vehiculoActual.setEstado(vehiculo.getEstado());
		
			Vehiculo vUp = vehiculoSer.actualizarVehiculo(vehiculoActual);
			
			vreturn = new VehiculoDTO(vUp.getId(),vUp.getMatricula(),vUp.getModelo(),
					vUp.getTipo(),vUp.getConsumo(),vUp.getEstado());
			
		} catch (DataAccessException e) {
			return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity(response, HttpStatus.OK);
	}
	
	@DeleteMapping("/vehiculo-delete/{id}")
	public ResponseEntity<?> deleteVehiculo(@PathVariable Long id) {	
		Map<String, Object> response = new HashMap<>();
		
		Vehiculo vehiculo = vehiculoSer.findVehiculoById(id);
		
		if (vehiculo == null) {			
			return new ResponseEntity(response, HttpStatus.NOT_FOUND);
		}		
		try {
		    vehiculoSer.borrarVehiculo(vehiculo);
		} catch (DataAccessException e) {
			return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity(response, HttpStatus.NO_CONTENT);
	}

}
