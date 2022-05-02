package com.davidrm.dto;

import java.io.Serializable;


public class VehiculoDTO implements Serializable {
	private Long id;
	private String matricula;
	private String modelo;
	private String tipo;
	private String consumo;
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
