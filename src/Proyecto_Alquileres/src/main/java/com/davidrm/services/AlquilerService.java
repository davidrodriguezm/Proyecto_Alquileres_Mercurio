package com.davidrm.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.davidrm.model.Alquiler;
import com.davidrm.repository.AlquilerRepository;

@Service
public class AlquilerService {
	@Autowired
	AlquilerRepository alquilerRepo;
	
	public AlquilerService() {}

	public Alquiler findAlquilerById(Long id) {		
		return alquilerRepo.findById(id).orElse(null);
	}
	
	public List<Alquiler> getAllAlquileres() {		
		return alquilerRepo.findAll(); 	
	}
	
	public Alquiler insertarAlquiler(Alquiler alquiler) {		
		if (alquiler != null)
			return alquilerRepo.save(alquiler);			
		else
			return null;
	}

	public Alquiler actualizarAlquiler(Alquiler alquiler) {
		if (alquiler == null || alquiler.getId() == null)
			return null;
		else
			return alquilerRepo.save(alquiler); 	
	}
	
	public boolean borrarAlquiler(Alquiler alquiler) {
		if (alquiler != null && alquiler.getId() != null) {
			alquilerRepo.delete(alquiler);
			return true;
		} else
			return false;
	}
	
	public List<Alquiler> getAlquilersIncomplete() {		
		return alquilerRepo.findByPagoIsNull(); 	
	}
	
	public List<Alquiler> getEstado(String estado) {		
		return alquilerRepo.findByEstado(estado); 	
	}
}
