package com.davidrm.dto;

import java.io.Serializable;
import java.time.LocalDate;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;


public class AlquilerDTO implements Serializable {
	private Long id;
	
	@NotNull(message="El campo Id Cliente no puede ser nulo")
	private Long idCliente;
	
	@NotNull(message="El campo Id Vehiculo no puede ser nulo")
	private Long idVehiculo;
	
	@NotNull(message="El campo fecha de inicio no puede ser nulo")
	@FutureOrPresent(message = "La fecha de inicio no puede ser anterior a la actual")
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private LocalDate fecha_inicio;
	
	@NotNull(message="El campo fecha de fin no puede ser nulo")
	@Future(message = "La fecha de fin debe ser posterior a la actual")
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private LocalDate fecha_fin;
	
	private Double pago;
	
	private String comentario;
	
	@NotNull(message="El campo estado no puede ser nulo")
	@NotEmpty(message="El campo estado no puede estar vacío")
	private String estado;

	public AlquilerDTO() {}

	public AlquilerDTO(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(Long idCliente) {
		this.idCliente = idCliente;
	}

	public Long getIdVehiculo() {
		return idVehiculo;
	}

	public void setIdVehiculo(Long idVehiculo) {
		this.idVehiculo = idVehiculo;
	}

	public LocalDate getFecha_inicio() {
		return fecha_inicio;
	}

	public void setFecha_inicio(LocalDate fecha_inicio) {
		this.fecha_inicio = fecha_inicio;
	}

	public LocalDate getFecha_fin() {
		return fecha_fin;
	}

	public void setFecha_fin(LocalDate fecha_fin) {
		this.fecha_fin = fecha_fin;
	}

	public Double getPago() {
		return pago;
	}

	public void setPago(Double pago) {
		this.pago = pago;
	}

	public String getComentario() {
		return comentario;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}
	
}
