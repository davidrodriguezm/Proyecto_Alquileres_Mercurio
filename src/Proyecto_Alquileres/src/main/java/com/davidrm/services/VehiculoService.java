package com.davidrm.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.davidrm.model.Vehiculo;
import com.davidrm.repository.VehiculoRepository;

@Service
public class VehiculoService {
	@Autowired
	VehiculoRepository vehiculoRepo;
	
	public VehiculoService() {}

	public Vehiculo findVehiculoById(Long id) {		
		return vehiculoRepo.findById(id).orElse(null);
	}
	
	public List<Vehiculo> getAllVehiculos() {		
		return vehiculoRepo.findAll(); 	
	}
	
	public Vehiculo insertarVehiculo(Vehiculo vehiculo) {
		if (vehiculo != null && findByMatricula(vehiculo.getMatricula()) == null)
			return vehiculoRepo.save(vehiculo);			
		else
			return null;
	}

	public Vehiculo actualizarVehiculo(Vehiculo vehiculo) {
		if (vehiculo == null || vehiculo.getId() == null)
			return null;
		else
			return vehiculoRepo.save(vehiculo); 	
	}
	
	public boolean borrarVehiculo(Vehiculo vehiculo) {
		if (vehiculo != null && vehiculo.getId() != null) {
			vehiculoRepo.delete(vehiculo);
			return true;
		} else
			return false;
	}
	
	public Vehiculo findByMatricula(String matricula) {		
		return vehiculoRepo.findByMatricula(matricula).orElse(null);
	}
	
	public List<Vehiculo> findByTipo(String tipo) {		
		return vehiculoRepo.findByTipo(tipo).orElse(null);
	}
	
	public List<Vehiculo> findByEstado(String estado) {		
		return vehiculoRepo.findByEstado(estado).orElse(null);
	}

}
