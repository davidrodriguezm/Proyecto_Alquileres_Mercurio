package com.davidrm.dto;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


public class VehiculoDTO implements Serializable {
	private Long id;
	
	@NotNull(message="El campo matricula no puede ser nulo")
	@NotEmpty(message="El campo matricula no puede estar vacío")
	private String matricula;
	
	@NotNull(message="El campo modelo no puede ser nulo")
	@NotEmpty(message="El campo modelo no puede estar vacío")
	private String modelo;
	
	@NotNull(message="El campo tipo no puede ser nulo")
	@NotEmpty(message="El campo tipo no puede estar vacío")
	private String tipo;
	
	@NotNull(message="El campo consumo no puede ser nulo")
	@NotEmpty(message="El campo consumo no puede estar vacío")
	private String consumo;
	
	@NotNull(message="El campo estado no puede ser nulo")
	@NotEmpty(message="El campo estado no puede estar vacío")
	private String estado;
	
	public VehiculoDTO() {}

	public VehiculoDTO(Long id) {
		this.id = id;
	}

	public VehiculoDTO(Long id, String matricula, String modelo, String tipo, String consumo, String estado) {
		this.id = id;
		this.matricula = matricula;
		this.modelo = modelo;
		this.tipo = tipo;
		this.consumo = consumo;
		this.estado = estado;
	}

	public VehiculoDTO(String matricula, String modelo, String tipo, String consumo, String estado) {
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

}
