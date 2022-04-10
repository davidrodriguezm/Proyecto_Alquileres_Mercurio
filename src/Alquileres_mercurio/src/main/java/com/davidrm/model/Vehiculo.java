package com.davidrm.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;


@Entity
@Table(name="vehiculos")
public class Vehiculo implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="matricula",unique=true,nullable=true)
	private String matricula;
	
	@Column(name="modelo",nullable=false)
	private String modelo;
	
	@Column(name="tipo",nullable=false)
	private String tipo;
	
	@Column(name="consumo",nullable=false)
	private String consumo;
	
	@Column(name="estado",nullable=false)
	private String estado;
	
	@OneToMany(mappedBy="vehiculo",cascade=CascadeType.ALL,orphanRemoval = true)
	private Set<Alquiler> alquileres = new HashSet<>();

	public Vehiculo() {}

	public Vehiculo(String matricula, String modelo, String tipo, String consumo, String estado) {
		super();
		this.matricula = matricula;
		this.modelo = modelo;
		this.tipo = tipo;
		this.consumo = consumo;
		this.estado = estado;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMatricula() {
		return matricula;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}

	public String getModelo() {
		return modelo;
	}

	public void setModelo(String modelo) {
		this.modelo = modelo;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getConsumo() {
		return consumo;
	}

	public void setConsumo(String consumo) {
		this.consumo = consumo;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public Set<Alquiler> getAlquileres() {
		return alquileres;
	}

	public void setAlquileres(Set<Alquiler> alquileres) {
		this.alquileres = alquileres;
	}
	
	public void addAlquiler(Alquiler alquiler) {
		this.alquileres.add(alquiler);
		alquiler.setVehiculo(this);
	}
	
	public void removeAlquiler(Alquiler alquiler) {
		this.alquileres.remove(alquiler);
		alquiler.setVehiculo(null);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, matricula);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Vehiculo other = (Vehiculo) obj;
		return Objects.equals(id, other.id) || Objects.equals(matricula, other.matricula);
	}	

}
