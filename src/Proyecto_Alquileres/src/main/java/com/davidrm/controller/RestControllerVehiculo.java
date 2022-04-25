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

	@GetMapping("/vehiculos")
	public List<VehiculoDTO> vehiculoList(Model model){
		List<Vehiculo> vehiculos = vehiculoSer.getAllVehiculos();
		List<VehiculoDTO> vreturn = new ArrayList<>();
				
		vehiculos.forEach((Vehiculo v) -> {
			VehiculoDTO vc = new VehiculoDTO(v.getId(),v.getMatricula(),v.getModelo(),v.getTipo(),v.getConsumo(),v.getEstado());
			vreturn.add(vc);
		});
		
		return vreturn;	
	}
	
	@GetMapping("/vehiculo/{id}")
	public ResponseEntity<?> show(@PathVariable Long id) {	
		Vehiculo vehiculo = null;
		Map<String, Object> response = new HashMap<>();
		
		try {
			vehiculo = vehiculoSer.findVehiculoById(id);
		} catch(DataAccessException e) {
			response.put("mensaje", "Error al realizar la consulta en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		if(vehiculo == null) {
			response.put("mensaje", "El vehiculo ID: ".concat(id.toString().concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		VehiculoDTO vreturn = new VehiculoDTO(vehiculo.getId(),vehiculo.getMatricula(),vehiculo.getModelo(),
				vehiculo.getTipo(),vehiculo.getConsumo(),vehiculo.getEstado());
		
		return new ResponseEntity<VehiculoDTO>(vreturn, HttpStatus.OK);
	}
	
	@PostMapping("/vehiculo-add")
	public ResponseEntity<?> create(@RequestBody VehiculoDTO vehiculo) {
		Map<String, Object> response = new HashMap<>();
		
		if(vehiculo == null) {
			response.put("mensaje", "No se enviaron datos del vehiculo: ");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		VehiculoDTO vreturn = null;
		
		
		try {
			Vehiculo vNew = vehiculoSer.insertarVehiculo( new Vehiculo( vehiculo.getMatricula(), vehiculo.getModelo(),
					vehiculo.getTipo(),vehiculo.getConsumo(),vehiculo.getEstado()));
			
			vreturn = new VehiculoDTO(vNew.getId(), vNew.getMatricula(), vNew.getModelo(), vNew.getTipo(), 
					vNew.getConsumo(), vNew.getEstado());
		} catch(DataAccessException e) {
			response.put("mensaje", "Error al realizar el insert en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("mensaje", "El vehiculo ha sido creado con éxito!");
		response.put("vehiculo", vreturn);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	@PutMapping("/vehiculo-edit/{id}")
	public ResponseEntity<?> update(@RequestBody VehiculoDTO vehiculo, @PathVariable Long id) {
		Vehiculo vehiculoActual = vehiculoSer.findVehiculoById(id);

		VehiculoDTO vreturn = null;
		Map<String, Object> response = new HashMap<>();
		
		if (vehiculoActual == null) {
			response.put("mensaje", "Error: no se pudo editar, el vehiculo ID: "
					.concat(id.toString().concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		try {
			vehiculoActual.setEstado(vehiculo.getEstado());
		
			Vehiculo vUp = vehiculoSer.actualizarVehiculo(vehiculoActual);
			
			vreturn = new VehiculoDTO(vUp.getId(),vUp.getMatricula(),vUp.getModelo(),
					vUp.getTipo(),vUp.getConsumo(),vUp.getEstado());
			
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al actualizar el vehiculo en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("mensaje", "El vehiculo ha sido actualizado con éxito!");
		response.put("vehiculo", vreturn);

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
	
	@DeleteMapping("/vehiculo-delete/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {	
		Map<String, Object> response = new HashMap<>();
		
		Vehiculo vehiculo = vehiculoSer.findVehiculoById(id);
		
		if (vehiculo == null) {
			response.put("mensaje", "Error: no se pudo borrar, el vehiculo ID: "
					.concat(id.toString().concat(" no existe en la base de datos!")));
			
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		
		try {
		    vehiculoSer.borrarVehiculo(vehiculo);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al eliminar el vehiculo de la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("mensaje", "El vehiculo eliminado con éxito!");
		
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NO_CONTENT);
	}

}
