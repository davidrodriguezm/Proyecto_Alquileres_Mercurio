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
		return vehiculoRepo.findById(id).get();
	}
	
	public List<Vehiculo> getAllVehiculos() {		
		return vehiculoRepo.findAll(); 	
	}
	
	public Vehiculo findVehiculoByMatricula(String matricula) {		
		return vehiculoRepo.findByMatricula(matricula).get();
	}
	
	public Vehiculo insertarVehiculo(Vehiculo vehiculo) {
//		if (vehiculo != null && findVehiculoByMatricula(vehiculo.getMatricula()) == null)
		if (vehiculo != null)
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
}