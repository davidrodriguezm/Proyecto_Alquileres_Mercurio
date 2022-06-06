package com.davidrm.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.davidrm.model.Vehiculo;

@Repository
public interface VehiculoRepository extends JpaRepository<Vehiculo, Long> {

	Optional<Vehiculo> findByMatricula(String matricula);
	
	Optional<List<Vehiculo>> findByTipo(String tipo);
	
	Optional<List<Vehiculo>> findByEstado(String estado);

}
