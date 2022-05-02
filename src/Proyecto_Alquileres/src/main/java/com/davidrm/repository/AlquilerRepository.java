package com.davidrm.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.davidrm.model.Alquiler;

@Repository
public interface AlquilerRepository extends JpaRepository<Alquiler, Long> {
	
	public List<Alquiler> findByPagoIsNull();
	
	public List<Alquiler> findByEstado(String estado);

}
