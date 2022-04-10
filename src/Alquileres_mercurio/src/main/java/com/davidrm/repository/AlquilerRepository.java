package com.davidrm.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.davidrm.model.Alquiler;

@Repository
public interface AlquilerRepository extends JpaRepository<Alquiler, Long> {

}
